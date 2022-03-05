package me.declipsonator.meteortweaks.mixins.tweaks;

import me.declipsonator.meteortweaks.modules.EntitySpeed;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoatEntity.class)
public class BoatEntityMixin {

    @Shadow
    private boolean pressingLeft;

    @Shadow
    private boolean pressingRight;

    @Shadow
    private boolean pressingForward;

    @Shadow
    private boolean pressingBack;

    @Inject(method = "updatePaddles", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/vehicle/BoatEntity;pressingLeft:Z"))
    private void onUpdatePaddlesPressingLeft(CallbackInfo ci) {
        if (Modules.get().isActive(EntitySpeed.class)) EntitySpeed.pressingLeft = pressingLeft;
    }

    @Inject(method = "updatePaddles", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/vehicle/BoatEntity;pressingRight:Z"))
    private void onUpdatePaddlesPressingRight(CallbackInfo ci) {
        if (Modules.get().isActive(EntitySpeed.class)) EntitySpeed.pressingRight = pressingRight;
    }

    @Inject(method = "updatePaddles", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/vehicle/BoatEntity;pressingBack:Z"))
    private void onUpdatePaddlesPressingBack(CallbackInfo ci) {
        if (Modules.get().isActive(EntitySpeed.class)) EntitySpeed.pressingBack = pressingBack;
    }

    @Inject(method = "updatePaddles", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/vehicle/BoatEntity;pressingForward:Z"))
    private void onUpdatePaddlesPressingForward(CallbackInfo ci) {
        if (Modules.get().isActive(EntitySpeed.class)) EntitySpeed.pressingForward = pressingForward;
    }


}
