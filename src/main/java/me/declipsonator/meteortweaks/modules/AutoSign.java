package me.declipsonator.meteortweaks.modules;

import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.mixin.SignEditScreenAccessor;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class AutoSign extends Module {
    SettingGroup sgGeneral = settings.getDefaultGroup();
    SettingGroup sgExtra = settings.createGroup("Visible");

    private final Setting<String> lineOne = sgGeneral.add(new StringSetting.Builder()
            .name("line-one")
            .description("What to put on the first line of the sign.")
            .defaultValue("Meteor")
            .build()
    );

    private final Setting<String> lineTwo = sgGeneral.add(new StringSetting.Builder()
            .name("line-two")
            .description("What to put on the second line of the sign.")
            .defaultValue("Tweaks")
            .build()
    );

    private final Setting<String> lineThree = sgGeneral.add(new StringSetting.Builder()
            .name("line-three")
            .description("What to put on the third line of the sign.")
            .defaultValue("On Top")
            .build()
    );

    private final Setting<String> lineFour = sgGeneral.add(new StringSetting.Builder()
            .name("line-four")
            .description("What to put on the Fourth line of the sign.")
            .defaultValue("ez")
            .build()
    );
    
    private final Setting<Boolean> autoDye = sgExtra.add(new BoolSetting.Builder()
            .name("auto-dye")
            .description("Dye signs that you place")
            .defaultValue(false)
            .build()
    );

    private final Setting<List<Item>> dyeColors = sgExtra.add(new ItemListSetting.Builder()
            .name("dye-colors")
            .description("What color dyes to dye the sign with.")
            .filter(this::filter)
            .build()
    );

    private final Setting<Boolean> autoGlow = sgExtra.add(new BoolSetting.Builder()
            .name("auto-glow")
            .description("Dye signs that you place")
            .defaultValue(false)
            .build()
    );


    public AutoSign() {
        super(Categories.World, "auto-sign", "Automatically writes signs and can dye them as well.");
    }



    @EventHandler
    private void onOpenScreen(OpenScreenEvent event) {
        if(!(event.screen instanceof SignEditScreen)) return;

        SignBlockEntity sign = ((SignEditScreenAccessor) event.screen).getSign();
        mc.player.networkHandler.sendPacket(new UpdateSignC2SPacket(sign.getPos(),
                lineOne.get(),
                lineTwo.get(),
                lineThree.get(),
                lineFour.get()
        ));

        event.cancel();


        if(autoDye.get()) {
            int slot = -1;
            for (int i = 0; i < 36; i++) {
                if (dyeColors.get().contains(mc.player.getInventory().getStack(i).getItem())) {
                    slot = i;
                    break;
                }
            }

            if (slot == -1 && dyeColors.get().contains(mc.player.getOffHandStack().getItem())) slot = 45;
            if (slot != -1) {
                InvUtils.move().from(slot).to(mc.player.getInventory().selectedSlot);

                mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(
                        new Vec3d(sign.getPos().getX(), sign.getPos().getY(), sign.getPos().getZ()),
                        Direction.UP,
                        sign.getPos(),
                        true
                )));

                InvUtils.move().from(mc.player.getInventory().selectedSlot).toHotbar(slot);

            }
        }
        if(autoGlow.get()) {

            int slot = -1;
            for (int i = 0; i < 36; i++) {
                if (mc.player.getInventory().getStack(i).getItem() == Items.GLOW_INK_SAC) {
                    slot = i;
                    break;
                }
            }

            if (slot == -1 && mc.player.getOffHandStack().getItem() == Items.GLOW_INK_SAC) slot = 45;
            if (slot != -1) {
                InvUtils.move().from(slot).to(mc.player.getInventory().selectedSlot);

                mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(
                        new Vec3d(sign.getPos().getX(), sign.getPos().getY(), sign.getPos().getZ()),
                        Direction.UP,
                        sign.getPos(),
                        true
                )));

                InvUtils.move().from(mc.player.getInventory().selectedSlot).toHotbar(slot);

            }
        }
    }

    private boolean filter(Item item) {
        return Items.WHITE_DYE.equals(item)
                || Items.BLACK_DYE.equals(item)
                || Items.BLUE_DYE.equals(item)
                || Items.BROWN_DYE.equals(item)
                || Items.CYAN_DYE.equals(item)
                || Items.GRAY_DYE.equals(item)
                || Items.YELLOW_DYE.equals(item)
                || Items.RED_DYE.equals(item)
                || Items.GREEN_DYE.equals(item)
                || Items.LIGHT_BLUE_DYE.equals(item)
                || Items.ORANGE_DYE.equals(item)
                || Items.LIME_DYE.equals(item)
                || Items.PURPLE_DYE.equals(item)
                || Items.PINK_DYE.equals(item)
                || Items.MAGENTA_DYE.equals(item)
                || Items.LIGHT_GRAY_DYE.equals(item);
    }

}
