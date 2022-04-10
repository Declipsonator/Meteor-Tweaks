/*
 *  This file is part of the Meteor Tweaks distribution (https://github.com/Declipsonator/Meteor-Tweaks/).
 *  Copyright (c) 2022 Meteor Tweaks.
 *  Licensed Under the GNU Lesser General Public License v3.0
 */

package me.declipsonator.meteortweaks.mixins.tweaks;

import me.declipsonator.meteortweaks.modules.GameTweaks;
import me.declipsonator.meteortweaks.modules.MultiTask;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Mixin(value = MinecraftClient.class, priority = 999)
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
    @Shadow
    public ClientPlayerInteractionManager interactionManager;
    @Shadow
    public HitResult crosshairTarget;

    @Redirect(method = "handleBlockBreaking", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"), require = 0)
    public boolean breakBlock(ClientPlayerEntity cpim) {
        if(Modules.get().get(MultiTask.class).isActive()) {
            return false;
        }
        return cpim.isUsingItem();
    }

    @Redirect(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isBreakingBlock()Z"), require = 0)
    public boolean itemBreak(ClientPlayerInteractionManager cpim) {
        if(Modules.get().get(MultiTask.class).isActive()) {
            return false;
        }
        return cpim.isBreakingBlock();
    }




    @Redirect(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"), require = 0)
    public boolean attackCheck(ClientPlayerEntity instance) {
        MultiTask multi = Modules.get().get(MultiTask.class);
        if(multi == null) return player.isUsingItem();
        if(multi.isActive()) {
            while(this.options.attackKey.wasPressed()) {
                this.doAttack();
            }

            while(this.options.useKey.wasPressed()) {
                this.doItemUse();
            }
        }
        return player.isUsingItem();
    }


    @Redirect(method = "doAttack", at = @At(value = "INVOKE", target = "net/minecraft/client/network/ClientPlayerInteractionManager.attackBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z"))
    private boolean checkForEntity(ClientPlayerInteractionManager manager, BlockPos pos, Direction direction) {
        GameTweaks tweaks = Modules.get().get(GameTweaks.class);
        if(!tweaks.isActive() || !tweaks.throughPartial.get()) {
            BlockHitResult blockHitResult = (BlockHitResult)this.crosshairTarget;
            BlockPos blockPos = blockHitResult.getBlockPos();
            return this.interactionManager.attackBlock(blockPos, blockHitResult.getSide());
        }

        BlockState clicked = world.getBlockState(pos);
        if (!clicked.getCollisionShape(world, pos).isEmpty()) {
            return manager.attackBlock(pos, direction);
        }
        Vec3d camera = player.getCameraPosVec(1.0F);
        Vec3d rotation = player.getRotationVec(1.0F);
        float reach = manager.getReachDistance();
        Vec3d end = camera.add(rotation.x * reach, rotation.y * reach, rotation.z * reach);
        EntityHitResult result = ProjectileUtil.getEntityCollision(world, player, camera, end, new Box(camera, end), EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.and(e -> e != null && e.collides() && e instanceof LivingEntity));
        if (result != null) {
            manager.attackEntity(player, result.getEntity());
            return true;
        }
        return manager.attackBlock(pos, direction);
    }
}
