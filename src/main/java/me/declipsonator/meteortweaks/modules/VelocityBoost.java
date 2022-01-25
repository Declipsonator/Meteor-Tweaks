/*
 * A portion of this file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client/).
 * Copyright (c) 2021 Meteor Development.
 */

package me.declipsonator.meteortweaks.modules;

import meteordevelopment.meteorclient.events.entity.player.InteractItemEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;

public class VelocityBoost extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();


    private final Setting<Type> type = sgGeneral.add(new EnumSetting.Builder<Type>()
         .name("type")
         .description("What type of velocity you want.")
         .defaultValue(Type.Once)
         .build()
    );

    private final Setting<Boolean> doNotConsumeFirework = sgGeneral.add(new BoolSetting.Builder()
        .name("anti-consume")
        .description("Prevents fireworks from being consumed when using Elytra Boost.")
        .defaultValue(true)
        .visible(() -> type.get() == Type.Once)
        .build()
    );

    private final Setting<Double> onceBPS = sgGeneral.add(new DoubleSetting.Builder()
        .name("blocks-per-second")
        .min(0)
        .defaultValue(20)
        .sliderMax(100)
        .visible(() -> type.get() == Type.Once)
        .build()
    );

    private final Setting<Double> continuousBPS = sgGeneral.add(new DoubleSetting.Builder()
        .name("blocks-per-second")
        .min(0)
        .defaultValue(40)
        .sliderMax(100)
        .visible(() -> type.get() == Type.Continuous)
        .build()
    );

    private final Setting<Boolean> playSound = sgGeneral.add(new BoolSetting.Builder()
        .name("play-sound")
        .description("Plays the firework sound when the module is triggered.")
        .defaultValue(true)
        .visible(() -> type.get() == Type.Once)
        .build()
    );

    private final Setting<Boolean> changeY = sgGeneral.add(new BoolSetting.Builder()
            .name("vertical-increase")
            .description("Adds to your vertical velocity as well.")
            .defaultValue(true)
            .visible(() -> type.get() == Type.Once)
            .build()
    );

    private final Setting<Keybind> keybind = sgGeneral.add(new KeybindSetting.Builder()
        .name("keybind")
        .description("The keybind to boost.")
        .action(this::boostOnce)
        .visible(() -> type.get() == Type.Once)
        .build()
    );


    public VelocityBoost() {
        super(Categories.Movement, "velocity-boost", "Boosts you when elytra flying with velocity.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if(type.get() == Type.Continuous) {
            assert mc.player != null;
            if (mc.player.isFallFlying() && mc.currentScreen == null) {
                Vec3d velocity = getHorizontalVelocity(continuousBPS.get(), mc.player.getPitch(), mc.player.getYaw());
                assert mc.player != null;
                mc.player.setVelocity(velocity);
            }
        }
    }



    private void onInteractItem(InteractItemEvent event) {
        assert mc.player != null;
        ItemStack itemStack = mc.player.getStackInHand(event.hand);

        if (itemStack.getItem() instanceof FireworkRocketItem && doNotConsumeFirework.get() && type.get() == Type.Once) {
            event.toReturn = ActionResult.PASS;

            boostOnce();
        }
    }

    private void boostOnce() {
        assert mc.player != null;
        if (mc.player.isFallFlying() && mc.currentScreen == null) {
            if(type.get() == Type.Once) {
                if (playSound.get()) {
                    mc.player.playSound(SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 1.0f);
                }

                Vec3d velocity = getHorizontalVelocity(onceBPS.get(), mc.player.getPitch(), mc.player.getYaw());
                if(changeY.get()) mc.player.addVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
                else mc.player.addVelocity(velocity.getX(), 0, velocity.getZ());
            }
        }
    }

    private Vec3d getHorizontalVelocity(double bps, float pitch, float yaw) {
        assert mc.player != null;
        Vec3d forward = Vec3d.fromPolar(pitch, yaw);
        double x = forward.x / 20 * bps;
        double y = forward.y / 20 * bps;
        double z = forward.z / 20 * bps;
        return new Vec3d(x, y, z);
    }


    public enum Type {
        Once,
        Continuous
    }
}
