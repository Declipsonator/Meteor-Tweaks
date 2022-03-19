package me.declipsonator.meteortweaks.mixins.meteor;

import me.declipsonator.meteortweaks.utils.MixinReferences;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.movement.elytrafly.ElytraFly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ElytraFly.class, remap = false)
public class ElytraFlyMixin extends Module {
    @Final
    @Shadow
    private SettingGroup sgGeneral;

    private boolean first = true;

    public ElytraFlyMixin(Category category, String name, String description) {
        super(category, name, description);
    }

    // Janky way to inject settings
    @Override
    public String getInfoString() {
        if(first) {
            MixinReferences.gradualAcceleration = sgGeneral.add(new BoolSetting.Builder()
                    .name("gradual-acceleration")
                    .description("Takes time to speed up to the full speed instead of speeding up instantly.")
                    .defaultValue(false)
                    .build()
            );

            MixinReferences.gradualAccelerationTime = sgGeneral.add(new IntSetting.Builder()
                    .name("acceleration-time")
                    .description("The time to take to fully accelerate.")
                    .defaultValue(60)
                    .min(0)
                    .sliderMax(100)
                    .visible(MixinReferences.gradualAcceleration::get)
                    .build()
            );

            MixinReferences.whenChangingDirections = sgGeneral.add(new BoolSetting.Builder()
                    .name("changing-directions")
                    .description("Gradually accelerate again when moving different ways.")
                    .defaultValue(true)
                    .visible(MixinReferences.gradualAcceleration::get)
                    .build()
            );

            MixinReferences.vertically = sgGeneral.add(new BoolSetting.Builder()
                    .name("vertically")
                    .description("Gradually accelerate vertically as well.")
                    .defaultValue(false)
                    .visible(MixinReferences.gradualAcceleration::get)
                    .build()
            );
            first = false;
        }
        return null;
    }

}

