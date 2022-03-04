package me.declipsonator.meteortweaks.mixins;

import me.declipsonator.meteortweaks.modules.MultiTask;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    public @Nullable ClientPlayerEntity player;
    @Shadow
    @Final
    public GameOptions options;
    @Shadow
    private boolean doAttack() {
        return false;
    }
    @Shadow
    private void doItemUse() {}
    @Shadow
    @Nullable
    public ClientWorld world;

    @Redirect(method = "handleBlockBreaking", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
    public boolean breakBlock(ClientPlayerEntity cpim) {
        if(Modules.get().get(MultiTask.class).isActive()) {
            return false;
        }
        return cpim.isUsingItem();
    }

    @Redirect(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isBreakingBlock()Z"))
    public boolean itemBreak(ClientPlayerInteractionManager cpim) {
        if(Modules.get().get(MultiTask.class).isActive()) {
            return false;
        }
        return cpim.isBreakingBlock();
    }




    @Redirect(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
    public boolean attackCheck(ClientPlayerEntity instance) {
        if(Modules.get().get(MultiTask.class).isActive()) {
            while(this.options.attackKey.wasPressed()) {
                this.doAttack();
            }

            while(this.options.useKey.wasPressed()) {
                this.doItemUse();
            }
        }
        return player.isUsingItem();
    }
}
