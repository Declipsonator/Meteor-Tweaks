package me.declipsonator.meteortweaks.mixins.tweaks;

import me.declipsonator.meteortweaks.modules.Confetti;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPacketListenerMixin {
    @ModifyArg(method = "onEntityStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleManager;addEmitter(Lnet/minecraft/entity/Entity;Lnet/minecraft/particle/ParticleEffect;I)V"), index = 2)
    private int modifyTheArg(int maxAge) {
        Confetti confetti = Modules.get().get(Confetti.class);
        if(!confetti.isActive()) return maxAge;
        return  Modules.get().get(Confetti.class).timeLasting.get();
    }
}
