package me.declipsonator.meteortweaks.modules;

import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.KeybindSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.lwjgl.glfw.GLFW;

public class AntiGhostBlocks extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();


    private final Setting<Integer> range = sgGeneral.add(new IntSetting.Builder()
            .name("range")
            .description("How far to remove the ghost blocks")
            .defaultValue(4)
            .min(1)
            .sliderMax(8)
            .build()
    );


    private final Setting<Keybind> keybind = sgGeneral.add(new KeybindSetting.Builder()
            .name("keybind")
            .description("The keybind to erase ghost blocks.")
            .action(this::removeGhostBlocks)
            .defaultValue(Keybind.fromKey(GLFW.GLFW_KEY_B))
            .build()
    );


    public AntiGhostBlocks() {
        super(Categories.World, "anti-ghost-blocks", "Attempts to delete ghost blocks near you when a hotkey is pressed.");
    }

    private void removeGhostBlocks() {
        if (mc.getNetworkHandler() == null || mc.currentScreen == null) return;


        assert mc.player != null;
        BlockPos blockPos = mc.player.getBlockPos();
        for (int x = -range.get(); x <= range.get(); x++) {
            for (int y = -range.get(); y <= range.get(); y++) {
                for (int z = -range.get(); z <= range.get(); z++) {

                    PlayerActionC2SPacket packet = new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, new BlockPos(blockPos.getX() + x, blockPos.getY() + y, blockPos.getZ() + z), Direction.UP);
                    mc.getNetworkHandler().sendPacket(packet);

                }
            }
        }
        ChatUtils.info("Ghost Blocks Removed");

    }

}