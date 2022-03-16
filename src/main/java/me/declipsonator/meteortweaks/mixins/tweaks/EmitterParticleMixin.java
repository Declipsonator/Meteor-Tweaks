package me.declipsonator.meteortweaks.mixins.tweaks;

import me.declipsonator.meteortweaks.modules.Confetti;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.particle.EmitterParticle;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EmitterParticle.class)
public class EmitterParticleMixin extends NoRenderParticle {
    protected EmitterParticleMixin(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    @Final
    @Shadow
    private Entity entity;
    @Final
    @Shadow
    private ParticleEffect parameters;
    @Final
    @Shadow
    private int maxEmitterAge;
    @Shadow
    private int emitterAge;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void theTick(CallbackInfo ci) {
        Confetti confetti = Modules.get().get(Confetti.class);
        if(confetti == null) return;
        if(confetti.isActive() && this.parameters.getType() == ParticleTypes.TOTEM_OF_UNDYING) {
            for (int i = 0; i < confetti.amountOfParticles.get() * 16; ++i) {
                double d = this.random.nextFloat() * 2.0F - 1.0F;
                double e = this.random.nextFloat() * 2.0F - 1.0F;
                double f = this.random.nextFloat() * 2.0F - 1.0F;
                if (!(d * d + e * e + f * f > 1.0D)) {
                    double g = this.entity.offsetX(d / 4.0D);
                    double h = this.entity.getBodyY(0.5D + e / 4.0D);
                    double j = this.entity.offsetZ(f / 4.0D);
                    this.world.addParticle(this.parameters, false, g, h, j, d, e + 0.2D, f);
                }
            }

            ++this.emitterAge;
            if (this.emitterAge >= this.maxEmitterAge) {
                this.markDead();
            }
            ci.cancel();
        }

    }
}
