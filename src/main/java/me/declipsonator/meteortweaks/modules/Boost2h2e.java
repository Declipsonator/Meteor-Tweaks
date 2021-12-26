/*
 * A portion of this file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client/).
 * Copyright (c) 2021 Meteor Development.
 */

package me.declipsonator.meteortweaks.modules;

import meteordevelopment.meteorclient.events.entity.player.InteractItemEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class Boost2h2e extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();



    private final Setting<Boolean> dontConsumeFirework = sgGeneral.add(new BoolSetting.Builder()
        .name("anti-consume")
        .description("Prevents fireworks from being consumed when using Elytra Boost.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Double> bps = sgGeneral.add(new DoubleSetting.Builder()
        .name("blocks-per-second")
        .min(0)
        .defaultValue(20)
        .sliderMax(100)
        .build()
    );

    private final Setting<Boolean> playSound = sgGeneral.add(new BoolSetting.Builder()
        .name("play-sound")
        .description("Plays the firework sound when the module is triggered.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Keybind> keybind = sgGeneral.add(new KeybindSetting.Builder()
        .name("keybind")
        .description("The keybind to boost.")
        .action(this::boost)
        .build()
    );

    public static final List<FireworkRocketEntity> fireworks = new ArrayList<>();

    public Boost2h2e() {
        super(Categories.Movement, "elytra-boost+", "Boosts you when flying with velocity.");
    }

    @EventHandler
    private void onInteractItem(InteractItemEvent event) {
        assert mc.player != null;
        ItemStack itemStack = mc.player.getStackInHand(event.hand);

        if (itemStack.getItem() instanceof FireworkRocketItem && dontConsumeFirework.get()) {
            event.toReturn = ActionResult.PASS;

            boost();
        }
    }

    private void boost() {
        assert mc.player != null;
        if (mc.player.isFallFlying() && mc.currentScreen == null) {
            if(playSound.get()) {
                mc.player.playSound(SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 1.0f);
            }

            assert mc.player != null;
            float yaw = mc.player.getYaw();
            Vec3d forward = Vec3d.fromPolar(0, yaw);
            double x = forward.x / 20 * bps.get();
            double z = forward.z / 20 * bps.get();
            mc.player.addVelocity(x, 0, z);
        }
    }


}