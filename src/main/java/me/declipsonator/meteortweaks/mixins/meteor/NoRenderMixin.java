/*
 *  This file is part of the Meteor Tweaks distribution (https://github.com/Declipsonator/Meteor-Tweaks/).
 *  Copyright (c) 2022 Meteor Tweaks.
 *  Licensed Under the GNU Lesser General Public License v3.0
 */

package me.declipsonator.meteortweaks.mixins.meteor;

import me.declipsonator.meteortweaks.utils.MixinReferences;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.render.NoRender;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = NoRender.class, remap = false)
public class NoRenderMixin extends Module {

    @Final
    @Shadow
    private SettingGroup sgHUD;

    private boolean first = true;

    public NoRenderMixin(Category category, String name, String description) {
        super(category, name, description);
    }

    @Override
    public String getInfoString() {
        if(first) {
            MixinReferences.toasts = sgHUD.add(new BoolSetting.Builder()
                    .name("toasts")
                    .description("Disables rendering of toasts (How Achievements Show Up).")
                    .defaultValue(false)
                    .build()
            );
            first = false;
        }

        return null;
    }

}
