package me.declipsonator.meteortweaks.mixins.tweaks;

import me.declipsonator.meteortweaks.modules.Confetti;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.particle.TotemParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TotemParticle.class)
public class TotemParticleMixin extends AnimatedParticle {
    protected TotemParticleMixin(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider, float upwardsAcceleration) {
        super(world, x, y, z, spriteProvider, upwardsAcceleration);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void theTick(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider, CallbackInfo ci) {
        Confetti confetti = Modules.get().get(Confetti.class);
        if(!confetti.isActive()) return;
        Vec3d firstColor = confetti.particleColorOne.get().getVec3d();
        Vec3d secondColor = confetti.particleColorTwo.get().getVec3d();

        if (this.random.nextInt(4) == 0) {
            this.setColor((float) firstColor.x,(float) firstColor.y,(float) firstColor.z);
        } else {
            this.setColor((float)secondColor.x,(float) secondColor.y,(float) firstColor.z);
        }
        this.scale *= confetti.sizeOfParticles.get();
        this.maxAge = confetti.particleTimeLasting.get();
    }
}
