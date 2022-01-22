/*
    I believe a lot of the code in this originated in ion
    Usually if I take inspiration from code I rewrite it
    In this case, it's so short that there's not really much I can do
 */

package me.declipsonator.meteortweaks.modules;

import meteordevelopment.meteorclient.events.entity.player.BreakBlockEvent;
import meteordevelopment.meteorclient.events.entity.player.InteractBlockEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.Direction;

public class TpsSync extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> breakBlockDeSync = sgGeneral.add(new BoolSetting.Builder()
            .name("break")
            .description("Stop de-syncing for block breaking.")
            .defaultValue(true)
            .build()
    );


    private final Setting<Boolean> placeBlockDeSync = sgGeneral.add(new BoolSetting.Builder()
            .name("place")
            .description("Stop de-syncing for block placing.")
            .defaultValue(true)
            .build()
    );

    public TpsSync() {
        super(Categories.Misc, "tps-sync", "Prevent ghost blocks from forming.");
    }


    @EventHandler
    private void onBlockPlace(InteractBlockEvent event) {
        if (placeBlockDeSync.get()) {
            if (event.result.getBlockPos() != null) {
                assert mc.player != null;
                mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, event.result.getBlockPos(), Direction.UP));
            }
        }
    }

    @EventHandler
    private void onBlockBreak(BreakBlockEvent event) {
        if (breakBlockDeSync.get()) {
            if (event.blockPos != null) {
                assert mc.player != null;
                mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, event.blockPos, Direction.UP));
            }
        }
    }
}