/*
 *  This file is part of the Meteor Tweaks distribution (https://github.com/Declipsonator/Meteor-Tweaks/).
 *  Copyright (c) 2022 Meteor Tweaks.
 *  Licensed Under the GNU Lesser General Public License v3.0
 */

package me.declipsonator.meteortweaks.mixins.tweaks;

import me.declipsonator.meteortweaks.utils.MixinReferences;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.render.NoRender;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Deque;

@Mixin(ToastManager.class)
public class ToastManagerMixin {
    @Final
    @Shadow
    private Deque<Toast> toastQueue;


    @Inject(method = "draw", at = @At("HEAD"), cancellable = true)
    private void stopToast(MatrixStack matrices, CallbackInfo ci) {
        if(Modules.get().get(NoRender.class).isActive() && MixinReferences.toasts.get()) {
            toastQueue.clear();
            ci.cancel();
        }
    }
}
