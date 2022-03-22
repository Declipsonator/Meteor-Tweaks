/*
 *  This file is part of the Meteor Tweaks distribution (https://github.com/Declipsonator/Meteor-Tweaks/).
 *  Copyright (c) 2022 Meteor Tweaks.
 *  Licensed Under the GNU Lesser General Public License v3.0
 */

package me.declipsonator.meteortweaks.mixins.tweaks;

import me.declipsonator.meteortweaks.events.BoatMovementEvent;
import meteordevelopment.meteorclient.MeteorClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Shadow
    public EntityType<?> getType() {
        return null;
    }


    @Inject(method = "move", at = @At(value = "HEAD"))
    private void onTickInvokeMove(MovementType movementType, Vec3d movement, CallbackInfo ci) {
        if(this.getType() != EntityType.BOAT) return;
        MeteorClient.EVENT_BUS.post(BoatMovementEvent.get((BoatEntity) (Object) this, movement));
    }
}