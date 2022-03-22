/*
 *  This file is part of the Meteor Tweaks distribution (https://github.com/Declipsonator/Meteor-Tweaks/).
 *  Copyright (c) 2022 Meteor Tweaks.
 *  Licensed Under the GNU Lesser General Public License v3.0
 */

package me.declipsonator.meteortweaks.events;

import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.Vec3d;

public class BoatMovementEvent {
    private static final BoatMovementEvent INSTANCE = new BoatMovementEvent();

    public BoatEntity boat;
    public Vec3d movement;

    public static BoatMovementEvent get(BoatEntity entity, Vec3d movement) {
        INSTANCE.boat = entity;
        INSTANCE.movement = movement;
        return INSTANCE;
    }
}
