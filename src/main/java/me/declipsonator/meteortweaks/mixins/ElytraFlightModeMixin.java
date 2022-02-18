package me.declipsonator.meteortweaks.mixins;

import me.declipsonator.meteortweaks.utils.MixinReferences;
import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.systems.modules.movement.elytrafly.ElytraFlightMode;
import meteordevelopment.meteorclient.systems.modules.movement.elytrafly.ElytraFly;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Mixin(value = ElytraFlightMode.class, remap = false)
public class ElytraFlightModeMixin {
    @Shadow
    @Final
    protected ElytraFly elytraFly;
    @Shadow
    protected double velY, velX, velZ;
    @Shadow
    protected Vec3d forward, right;


    private double gradualUp = 0;
    private double gradualDown = 0;
    private double gradualFoward = 0;
    private double gradualBack = 0;
    private double gradualRight = 0;
    private double gradualLeft = 0;
    private double gradualTogether = 0;

    @Inject(method = "onTick", at = @At("HEAD"))
    private void resetAccelerates(CallbackInfo ci) {
        int notPressed = 0;
        if (!mc.options.keyJump.isPressed()) {
            notPressed++;
            gradualUp = 0;
        }
        if(!mc.options.keySneak.isPressed()) {
            notPressed++;
            gradualDown = 0;
        }
        if(!mc.options.keyForward.isPressed()) {
            notPressed++;
            gradualFoward = 0;
        }
        if(!mc.options.keyBack.isPressed()) {
            notPressed++;
            gradualBack = 0;
        }
        if(!mc.options.keyRight.isPressed()) {
            notPressed++;
            gradualRight = 0;
        }
        if(!mc.options.keyLeft.isPressed()) {
            notPressed++;
            gradualLeft = 0;
        }

        if(notPressed == 6) {
            gradualTogether = 0;
        }
    }

    @Inject(method = "handleHorizontalSpeed", at = @At("HEAD"), cancellable = true)
    private void gradualHorizontalIncrease(PlayerMoveEvent event, CallbackInfo ci) {
        boolean a = false;
        boolean b = false;
        gradualTogether += elytraFly.horizontalSpeed.get() / MixinReferences.gradualAccelerationTime.get();

        if (mc.options.keyForward.isPressed()) {
            gradualFoward += elytraFly.horizontalSpeed.get() / MixinReferences.gradualAccelerationTime.get();

            if(gradualTogether < elytraFly.horizontalSpeed.get() && !MixinReferences.whenChangingDirections.get()) {
                velX += forward.x * gradualTogether * 10;
                velZ += forward.z * gradualTogether * 10;
            } else if (gradualFoward < elytraFly.horizontalSpeed.get()) {
                velX += forward.x * gradualFoward * 10;
                velZ += forward.z * gradualFoward * 10;
            } else {
                velX += forward.x * elytraFly.horizontalSpeed.get() * 10;
                velZ += forward.z * elytraFly.horizontalSpeed.get() * 10;
            }
            a = true;
        } else if (mc.options.keyBack.isPressed()) {
            gradualBack += elytraFly.horizontalSpeed.get() / MixinReferences.gradualAccelerationTime.get();

            if(gradualTogether < elytraFly.horizontalSpeed.get() && !MixinReferences.whenChangingDirections.get()) {
                velX -= forward.x * gradualTogether * 10;
                velZ -= forward.z * gradualTogether * 10;
            } else if (gradualBack < elytraFly.horizontalSpeed.get()) {
                velX -= forward.x * gradualBack * 10;
                velZ -= forward.z * gradualBack * 10;
            } else {
                velX -= forward.x * elytraFly.horizontalSpeed.get() * 10;
                velZ -= forward.z * elytraFly.horizontalSpeed.get() * 10;
            }
            a = true;
        }

        if (mc.options.keyRight.isPressed()) {
            gradualRight += elytraFly.horizontalSpeed.get() / MixinReferences.gradualAccelerationTime.get();

            if(gradualTogether < elytraFly.horizontalSpeed.get() && !MixinReferences.whenChangingDirections.get()) {
                velX += right.x * gradualTogether * 10;
                velZ += right.z * gradualTogether * 10;
            } else if (gradualRight < elytraFly.horizontalSpeed.get()) {
                velX += right.x * gradualRight * 10;
                velZ += right.z * gradualRight * 10;
            } else {
                velX += right.x * elytraFly.horizontalSpeed.get() * 10;
                velZ += right.z * elytraFly.horizontalSpeed.get() * 10;
            }
            b = true;

        } else if (mc.options.keyLeft.isPressed()) {
            gradualLeft += elytraFly.horizontalSpeed.get() / MixinReferences.gradualAccelerationTime.get();

            if(gradualTogether < elytraFly.horizontalSpeed.get() && !MixinReferences.whenChangingDirections.get()) {
                velX -= right.x * gradualTogether * 10;
                velZ -= right.z * gradualTogether * 10;
            } else if (gradualLeft < elytraFly.horizontalSpeed.get()) {
                velX -= right.x * gradualLeft * 10;
                velZ -= right.z * gradualLeft * 10;
            } else {
                velX -= right.x * elytraFly.horizontalSpeed.get() * 10;
                velZ -= right.z * elytraFly.horizontalSpeed.get() * 10;
            }
            b = true;
        }

        if (a && b) {
            double diagonal = 1 / Math.sqrt(2);
            velX *= diagonal;
            velZ *= diagonal;
        }
        ci.cancel();
    }

    @Inject(method = "handleVerticalSpeed", at = @At("TAIL"))
    private void gradualVerticalIncrease(PlayerMoveEvent event, CallbackInfo ci) {
        if(!MixinReferences.vertically.get() || !MixinReferences.gradualAcceleration.get()) return;
        if (mc.options.keyJump.isPressed()) {
            gradualUp += elytraFly.verticalSpeed.get() / MixinReferences.gradualAccelerationTime.get();

            if(gradualTogether < elytraFly.horizontalSpeed.get() && !MixinReferences.whenChangingDirections.get()) {
                velY -= 0.5 * elytraFly.verticalSpeed.get();
                velY += 0.5 * gradualTogether * 10;
            } else if (!MixinReferences.gradualAcceleration.get() || gradualUp >= elytraFly.verticalSpeed.get()) {
                velY -= 0.5 * elytraFly.verticalSpeed.get();
                velY += 0.5 * gradualUp;
            }



        } else if (mc.options.keySneak.isPressed()) {
            gradualDown += elytraFly.verticalSpeed.get() / MixinReferences.gradualAccelerationTime.get();

            if(gradualTogether < elytraFly.horizontalSpeed.get() && !MixinReferences.whenChangingDirections.get()) {
                velY += 0.5 * elytraFly.verticalSpeed.get();
                velY -= 0.5 * gradualTogether * 10;
            } else if (!MixinReferences.gradualAcceleration.get() || gradualDown >= elytraFly.verticalSpeed.get()) {
                velY += 0.5 * elytraFly.verticalSpeed.get();
                velY -= 0.5 * gradualDown;
            }

        }
    }

}



