/*
 *  This file is part of the Meteor Tweaks distribution (https://github.com/Declipsonator/Meteor-Tweaks/).
 *  Copyright (c) 2022 Meteor Tweaks.
 *  Licensed Under the GNU Lesser General Public License v3.0
 */

package me.declipsonator.meteortweaks.mixins.tweaks;

import me.declipsonator.meteortweaks.modules.GameTweaks;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({DeathScreen.class})
public abstract class MixinDeathScreen {
   @Shadow
   private Text scoreText;

   @Inject(method = "init", at = @At("TAIL"))
   private void doNotSetScoreText(CallbackInfo ci) {
      GameTweaks tweaks = Modules.get().get(GameTweaks.class);
      if(!tweaks.isActive() || !tweaks.noScore.get()) return;
      scoreText = Text.of("");
   }
}