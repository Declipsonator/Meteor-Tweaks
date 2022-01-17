/*
 * Most of this file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client/).
 * Copyright (c) 2021 Meteor Development.
 */

package me.declipsonator.meteortweaks.modules;

import meteordevelopment.meteorclient.events.entity.player.StartBreakingBlockEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.world.InfinityMiner;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.ToolItem;

import java.util.function.Predicate;

public class AutoTool extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> inventory = sgGeneral.add(new BoolSetting.Builder()
        .name("inventory")
        .description("Whether to use tools from you inventory.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> hotbarFirst = sgGeneral.add(new BoolSetting.Builder()
        .name("hotbar-first")
        .description("Whether or not to prefer using tools from the hotbar over tools from your inventory.")
        .defaultValue(true)
        .visible(() -> inventory.get())
        .build()
    );

    private final Setting<EnchantPreference> prefer = sgGeneral.add(new EnumSetting.Builder<EnchantPreference>()
        .name("prefer")
        .description("Either to prefer Silk Touch, Fortune, or none.")
        .defaultValue(EnchantPreference.Fortune)
        .build()
    );

    private final Setting<Boolean> silkTouchForEnderChest = sgGeneral.add(new BoolSetting.Builder()
        .name("silk-touch-for-ender-chest")
        .description("Mines Ender Chests only with the Silk Touch enchantment.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> antiBreak = sgGeneral.add(new BoolSetting.Builder()
        .name("anti-break")
        .description("Stops you from breaking your tool.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Integer> breakDurability = sgGeneral.add(new IntSetting.Builder()
        .name("anti-break-percentage")
        .description("The durability percentage to stop using a tool.")
        .defaultValue(10)
        .range(1, 100)
        .sliderRange(1, 100)
        .visible(antiBreak::get)
        .build()
    );

    private final Setting<Boolean> switchBack = sgGeneral.add(new BoolSetting.Builder()
        .name("switch-back")
        .description("Switches your hand to whatever was selected when releasing your attack key.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Integer> switchDelay = sgGeneral.add((new IntSetting.Builder()
        .name("switch-delay")
        .description("Delay in ticks before switching tools.")
        .defaultValue(0)
        .build()
    ));

    private boolean wasPressed;
    private boolean shouldSwitch;
    private int ticks;
    private int bestSlot;
    private int previousToolSlot;
    private int previousSelectedSlot;
    private int newToolSlot;

    public AutoTool() {
        super(Categories.Player, "auto-tool", "Automatically switches to the most effective tool when performing an action.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (Modules.get().isActive(InfinityMiner.class)) return;

        if (switchBack.get() && !mc.options.keyAttack.isPressed() && wasPressed && previousToolSlot > 8) {
            assert mc.player != null;
            InvUtils.move().fromHotbar(newToolSlot).to(previousToolSlot);
            if(previousSelectedSlot != -1) mc.player.getInventory().selectedSlot = previousSelectedSlot;
            previousToolSlot = -1;
            previousSelectedSlot = -1;
            wasPressed = false;
            return;
        } else if(switchBack.get() && !mc.options.keyAttack.isPressed() && wasPressed && InvUtils.previousSlot != -1) {
            InvUtils.swapBack();
            previousToolSlot = -1;
            previousSelectedSlot = -1;
            wasPressed = false;
            return;
        } else if (!switchBack.get() && previousToolSlot > 8) {
            previousSelectedSlot = -1;
            previousToolSlot = -1;
        }

        if (ticks <= 0 && shouldSwitch && bestSlot != -1) {
            InvUtils.swap(bestSlot, switchBack.get());
            shouldSwitch = false;
        } else {
            ticks--;
        }

        wasPressed = mc.options.keyAttack.isPressed();
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onStartBreakingBlock(StartBreakingBlockEvent event) {
        if (Modules.get().isActive(InfinityMiner.class)) return;

        // Get blockState
        BlockState blockState = mc.world.getBlockState(event.blockPos);
        if (!BlockUtils.canBreak(event.blockPos, blockState)) return;

        // Check if we should switch to a better tool
        ItemStack currentStack = mc.player.getMainHandStack();

        double bestScore = -1;
        bestSlot = -1;

        boolean inHotbar = false;

        for (int i = 0; i < 36; i++) {
            double score = getScore(mc.player.getInventory().getStack(i), blockState, silkTouchForEnderChest.get(), prefer.get(), itemStack -> !shouldStopUsing(itemStack));
            if (score < 0) continue;

            if (score > bestScore) {
                if(!inHotbar || i < 9 || mc.player.getInventory().getStack(i).getItem() != mc.player.getInventory().getStack(bestSlot).getItem()) {
                    bestScore = score;
                    bestSlot = i;
                    if (i < 9) {
                        inHotbar = true;
                    }
                } else if(!hotbarFirst.get()) {
                    bestScore = score;
                    bestSlot = i;
                }
            }
        }

        if ((bestSlot != -1 && (bestScore > getScore(currentStack, blockState, silkTouchForEnderChest.get(), prefer.get(), itemStack -> !shouldStopUsing(itemStack))) || shouldStopUsing(currentStack) || !isTool(currentStack))) {
            ticks = switchDelay.get();
            if(inventory.get() && bestSlot > 8) {
                if(InvUtils.findEmpty().isHotbar()) {
                    previousSelectedSlot = mc.player.getInventory().selectedSlot;
                    mc.player.getInventory().selectedSlot = mc.player.getInventory().getSwappableHotbarSlot();
                    InvUtils.move().from(bestSlot).to(mc.player.getInventory().getSwappableHotbarSlot());
                } else {
                    InvUtils.move().from(bestSlot).toHotbar(mc.player.getInventory().selectedSlot);
                }
                previousToolSlot = bestSlot;
                newToolSlot = mc.player.getInventory().selectedSlot;

            } else {
                if (ticks == 0) InvUtils.swap(bestSlot, true);
                else shouldSwitch = true;
            }
        }

        // Anti break
        currentStack = mc.player.getMainHandStack();

        if (shouldStopUsing(currentStack) && isTool(currentStack)) {
            mc.options.keyAttack.setPressed(false);
            event.setCancelled(true);
        }
    }

    private boolean shouldStopUsing(ItemStack itemStack) {
        return antiBreak.get() && (itemStack.getMaxDamage() - itemStack.getDamage()) < (itemStack.getMaxDamage() * breakDurability.get() / 100);
    }

    public static double getScore(ItemStack itemStack, BlockState state, boolean silkTouchEnderChest, EnchantPreference enchantPreference, Predicate<ItemStack> good) {
        if (!good.test(itemStack) || !isTool(itemStack)) return -1;

        if (silkTouchEnderChest
            && state.getBlock() == Blocks.ENDER_CHEST
            && EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, itemStack) == 0) {
            return -1;
        }

        double score = 0;

        score += itemStack.getMiningSpeedMultiplier(state) * 1000;
        score += EnchantmentHelper.getLevel(Enchantments.UNBREAKING, itemStack);
        score += EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, itemStack);
        score += EnchantmentHelper.getLevel(Enchantments.MENDING, itemStack);

        if (enchantPreference == EnchantPreference.Fortune) score += EnchantmentHelper.getLevel(Enchantments.FORTUNE, itemStack);
        if (enchantPreference == EnchantPreference.SilkTouch) score += EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, itemStack);

        return score;
    }

    public static boolean isTool(ItemStack itemStack) {
        return itemStack.getItem() instanceof ToolItem || itemStack.getItem() instanceof ShearsItem;
    }

    public enum EnchantPreference {
        None,
        Fortune,
        SilkTouch
    }
}