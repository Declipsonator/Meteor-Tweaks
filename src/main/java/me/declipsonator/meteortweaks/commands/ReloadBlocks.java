/*
 *  This file is part of the Meteor Tweaks distribution (https://github.com/Declipsonator/Meteor-Tweaks/).
 *  Copyright (c) 2022 Meteor Tweaks.
 *  Licensed Under the GNU Lesser General Public License v3.0
 */

package me.declipsonator.meteortweaks.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.systems.commands.Command;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class ReloadBlocks extends Command {
    public ReloadBlocks() {
        super("reload-blocks", "Removes ghost blocks in a range around the player.", "remove-ghosts");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("range", IntegerArgumentType.integer(1, 7)).executes(context -> {
            int range = IntegerArgumentType.getInteger(context, "range");
            BlockPos blockPos = mc.player.getBlockPos();
            for (int x = -range; x <= range; x++) {
                for (int y = -range; y <= range; y++) {
                    for (int z = -range; z <= range; z++) {

                        PlayerActionC2SPacket packet = new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, new BlockPos(blockPos.getX() + x, blockPos.getY() + y, blockPos.getZ() + z), Direction.UP);
                        mc.getNetworkHandler().sendPacket(packet);

                    }
                }
            }
            ChatUtils.info("Ghost Blocks Removed");

            return SINGLE_SUCCESS;
        }));
    }
}
