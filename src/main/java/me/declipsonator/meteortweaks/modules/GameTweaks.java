/*
 *  This file is part of the Meteor Tweaks distribution (https://github.com/Declipsonator/Meteor-Tweaks/).
 *  Copyright (c) 2022 Meteor Tweaks.
 *  Licensed Under the GNU Lesser General Public License v3.0
 */

package me.declipsonator.meteortweaks.modules;

import me.declipsonator.meteortweaks.mixins.tweaks.RenderLayersMixin;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;

public class GameTweaks extends Module {
    private final SettingGroup sgGamePlay = settings.createGroup("Game Play");
    private final SettingGroup sgRender = settings.createGroup("Render");
    private final SettingGroup sgMisc = settings.createGroup("Misc");


    public final Setting<Boolean> fullRecipes = sgGamePlay.add(new BoolSetting.Builder()
            .name("all-recipes")
            .description("Makes all recipes visible in the crafting screen.")
            .defaultValue(true)
            .build()
    );

    public final Setting<Boolean> throughPartial = sgGamePlay.add(new BoolSetting.Builder()
            .name("mobs-through-partial")
            .description("Has the game hit entities through partial blocks (like grass).")
            .defaultValue(false)
            .build()
    );

    public final Setting<Boolean> unobtrusiveBats = sgGamePlay.add(new BoolSetting.Builder()
            .name("unobtrusive-bats")
            .description("Makes bats less annoying and obtrusive.")
            .defaultValue(true)
            .build()
    );


    public final Setting<Boolean> migCapes = sgRender.add(new BoolSetting.Builder()
            .name("no-migration")
            .description("Doesn't render migration capes on players.")
            .defaultValue(false)
            .build()
    );

    public final Setting<Boolean> transWater = sgRender.add(new BoolSetting.Builder()
            .name("trans-cauldron-water")
            .description("Makes cauldron water translucent.")
            .defaultValue(true)
            .onChanged(this::cauldronSettingChanged)
            .build()
    );

    public final Setting<Boolean> copyScreenshots = sgMisc.add(new BoolSetting.Builder()
            .name("copy-screenshots")
            .description("Copies screenshots to the clipboard after taking them.")
            .defaultValue(true)
            .build()
    );

    public final Setting<Boolean> noScore = sgMisc.add(new BoolSetting.Builder()
            .name("no-score")
            .description("Removes the score from the death screen.")
            .defaultValue(false)
            .build()
    );

    public GameTweaks() {
        super(Categories.Misc, "game-tweaks", "Various tweaks and additions to the vanilla game.");
    }

    @Override
    public void onActivate() {
        RenderLayersMixin.getBlocks().put(Blocks.CAULDRON, transWater.get() ? RenderLayer.getTranslucent() : RenderLayer.getSolid());
    }


    @Override
    public void onDeactivate() {
        RenderLayersMixin.getBlocks().remove(Blocks.CAULDRON, RenderLayer.getSolid());
    }

    private void cauldronSettingChanged(Boolean aBoolean) {
        RenderLayersMixin.getBlocks().put(Blocks.CAULDRON, transWater.get() ? RenderLayer.getTranslucent() : RenderLayer.getSolid());
    }



}
