package me.declipsonator.meteortweaks.mixins.tweaks;

import me.declipsonator.meteortweaks.modules.GameTweaks;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

@Mixin(BatEntity.class)
public abstract class BatEntityMixin extends AmbientEntity {

    @Shadow public abstract float getSoundPitch();

    protected BatEntityMixin(EntityType<? extends AmbientEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public float getSoundVolume() {
        GameTweaks tweaks = Modules.get().get(GameTweaks.class);
        if(tweaks.isActive() && tweaks.unobtrusiveBats.get()) return 0;
        return 0.1f;
    }

    @Override
    public boolean collides() {
        GameTweaks tweaks = Modules.get().get(GameTweaks.class);
        if(tweaks.isActive() && tweaks.unobtrusiveBats.get()) return false;
        return !this.isRemoved();
    }

    private static boolean isTodayAroundHalloween() {
        GameTweaks tweaks = Modules.get().get(GameTweaks.class);
        if(tweaks.isActive() && tweaks.unobtrusiveBats.get()) return false;
        LocalDate localDate = LocalDate.now();
        int i = localDate.get(ChronoField.DAY_OF_MONTH);
        int j = localDate.get(ChronoField.MONTH_OF_YEAR);
        return j == 10 && i >= 20 || j == 11 && i <= 3;
    }
}
