/*
 *  This file is part of the Meteor Tweaks distribution (https://github.com/Declipsonator/Meteor-Tweaks/).
 *  Copyright (c) 2022 Meteor Tweaks.
 *  Licensed Under the GNU Lesser General Public License v3.0
 */

package me.declipsonator.meteortweaks.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.systems.commands.Command;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.BlockPos;

import java.io.*;
import java.util.ArrayList;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class Export extends Command {
    public Export() {
        super("export", "Export various things to files.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("terrain-exploit-data")
                .then(argument("X1", IntegerArgumentType.integer())
                        .then(argument("Y1", IntegerArgumentType.integer())
                                .then(argument("Z1", IntegerArgumentType.integer())
                                        .then(argument("X2", IntegerArgumentType.integer())
                                                .then(argument("Y2", IntegerArgumentType.integer())
                                                        .then(argument("Z2", IntegerArgumentType.integer())
                                                                .then(argument("File Name", StringArgumentType.string()).executes(context -> {
                                                                    BlockPos corner1 = new BlockPos(
                                                                            IntegerArgumentType.getInteger(context, "X1"),
                                                                            IntegerArgumentType.getInteger(context, "Y1"),
                                                                            IntegerArgumentType.getInteger(context, "Z1")
                                                                    );

                                                                    BlockPos corner2 = new BlockPos(
                                                                            IntegerArgumentType.getInteger(context, "X2"),
                                                                            IntegerArgumentType.getInteger(context, "Y2"),
                                                                            IntegerArgumentType.getInteger(context, "Z2")
                                                                    );

                                                                    BlockPos first = new BlockPos(
                                                                            Math.min(corner1.getX(), corner2.getX()),
                                                                            Math.min(corner1.getY(), corner2.getY()),
                                                                            Math.min(corner1.getZ(), corner2.getZ())
                                                                    );
                                                                    BlockPos
                                                                            second = new BlockPos(
                                                                            Math.max(corner1.getX(), corner2.getX()),
                                                                            Math.max(corner1.getY(), corner2.getY()),
                                                                            Math.max(corner1.getZ(), corner2.getZ())
                                                                    );

                                                                    context.getSource().getPositionSuggestions().size();

                                                                    ArrayList<String> lines = new ArrayList<>();
                                                                    for(int x = 0; x <= second.getX() - first.getX(); x++) {
                                                                        for(int y = 0; y <= second.getY() - first.getY(); y++) {
                                                                            for(int z = 0; z <= second.getZ() - first.getZ(); z++) {
                                                                                BlockState state = mc.world.getBlockState(first.add(x, y, z));
                                                                                if(state.getMaterial().isSolid()) {
                                                                                    lines.add(x + "," + y + "," + z);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    ChatUtils.info("Exploit Data", lines.toString());

                                                                    try{
                                                                        String givenName =  String.valueOf(StringArgumentType.getString(context, "File Name"));
                                                                        String filePath = MeteorClient.FOLDER.getPath() + "\\";
                                                                        filePath += (givenName.endsWith(".txt") ?  givenName : givenName + ".txt");
                                                                        File file = new File(filePath);
                                                                        FileOutputStream fos = new FileOutputStream(file);

                                                                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

                                                                        for (String text : lines) {
                                                                            bw.write(text);
                                                                            if(!lines.get(lines.size() - 1).equals(text)) {
                                                                                bw.newLine();
                                                                            }
                                                                        }
                                                                        bw.close();

                                                                    }catch (IOException e){
                                                                        e.printStackTrace();
                                                                    }
                                                                    return SINGLE_SUCCESS;
                                                                }))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );



    }
}
