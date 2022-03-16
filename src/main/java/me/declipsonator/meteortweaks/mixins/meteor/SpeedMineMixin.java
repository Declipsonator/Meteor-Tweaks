package me.declipsonator.meteortweaks.mixins.meteor;

import me.declipsonator.meteortweaks.utils.MixinReferences;
import meteordevelopment.meteorclient.events.entity.player.BreakBlockEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.player.SpeedMine;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = SpeedMine.class, remap = false)
public class SpeedMineMixin extends Module {

    @Shadow
    @Final
    private SettingGroup sgGeneral;

    private boolean first = true;

    public SpeedMineMixin(Category category, String name, String description) {
        super(category, name, description);
    }

    @EventHandler
    private void onBlockBreak(BreakBlockEvent event) {
        if (MixinReferences.confirmBreak.get()) {
            if (event.blockPos != null) {
                mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, event.blockPos, Direction.UP));
            }
        }
    }

    @Override
    public String getInfoString() {
        if(first) {
            MixinReferences.confirmBreak = sgGeneral.add(new BoolSetting.Builder()
                    .name("confirm-break")
                    .description("Comfirms MainMixin block broken with the server (increasing break chance).")
                    .defaultValue(true)
                    .build()
            );

            first = false;
        }

        return null;
    }
}
