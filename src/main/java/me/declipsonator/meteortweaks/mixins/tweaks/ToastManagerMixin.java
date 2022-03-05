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
