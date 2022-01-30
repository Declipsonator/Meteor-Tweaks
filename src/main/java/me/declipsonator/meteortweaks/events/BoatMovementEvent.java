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
