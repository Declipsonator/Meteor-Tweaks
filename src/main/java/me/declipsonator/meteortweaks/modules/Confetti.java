/*
 *  This file is part of the Meteor Tweaks distribution (https://github.com/Declipsonator/Meteor-Tweaks/).
 *  Copyright (c) 2022 Meteor Tweaks.
 *  Licensed Under the GNU Lesser General Public License v3.0
 */

package me.declipsonator.meteortweaks.modules;

import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;

public class Confetti extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<SettingColor> particleColorOne = sgGeneral.add(new ColorSetting.Builder()
            .name("first-color")
            .description("The first color to change.")
            .defaultValue(new SettingColor(39, 144, 17))
            .build()
    );

    public final Setting<SettingColor> particleColorTwo = sgGeneral.add(new ColorSetting.Builder()
            .name("second-color")
            .description("The second color to change.")
            .defaultValue(new SettingColor(185, 170, 24))
            .build()
    );

    public final Setting<Double> amountOfParticles = sgGeneral.add(new DoubleSetting.Builder()
            .name("particle-amount")
            .description("How many particles there should be.")
            .defaultValue(1.0)
            .min(0.1)
            .sliderMax(2)
            .decimalPlaces(2)
            .build()
    );

    public final Setting<Double> sizeOfParticles = sgGeneral.add(new DoubleSetting.Builder()
            .name("particle-size")
            .description("How many particles there should be.")
            .defaultValue(1.0)
            .min(0.1)
            .sliderMax(2)
            .decimalPlaces(2)
            .build()
    );

    public final Setting<Integer> timeLasting = sgGeneral.add(new IntSetting.Builder()
            .name("emit-time")
            .description("How long the particle emitter should last. (Ticks)")
            .defaultValue(30)
            .min(1)
            .sliderMax(100)
            .sliderMin(1)
            .build()
    );

    public final Setting<Integer> particleTimeLasting = sgGeneral.add(new IntSetting.Builder()
            .name("particle-time")
            .description("How long the particles should last. (Ticks)")
            .defaultValue(60)
            .min(1)
            .sliderMax(100)
            .sliderMin(1)
            .build()
    );


    public Confetti() {
        super(Categories.Render, "confetti", "Changes totem particle characteristics.");
    }

}
