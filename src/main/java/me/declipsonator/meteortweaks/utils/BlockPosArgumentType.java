/*
    Mojang was fucking with me so I skidded them
 */

package me.declipsonator.meteortweaks.utils;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.CommandSource.RelativePosition;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class BlockPosArgumentType implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Arrays.asList("0 0 0", "~ ~ ~", "^ ^ ^", "^1 ^ ^-5", "~0.5 ~1 ~-5");
    public static final SimpleCommandExceptionType OUT_OF_BOUNDS_EXCEPTION = new SimpleCommandExceptionType(new TranslatableText("argument.pos.outofbounds"));

    public BlockPosArgumentType() {
    }

    public static BlockPosArgumentType blockPos() {
        return new BlockPosArgumentType();
    }

    public static BlockPos getBlockPos(CommandContext<CommandSource> context, String name) throws CommandSyntaxException {
        String[] nums = context.getArgument(name, String.class).split(" ");
        BlockPos blockPos = new BlockPos(Integer.parseInt(nums[1]), Integer.parseInt(nums[2]), Integer.parseInt(nums[3]));
        if (!World.isValid(blockPos)) {
            throw OUT_OF_BOUNDS_EXCEPTION.create();
        } else {
            return blockPos;
        }
    }

    public String parse(StringReader stringReader) {
        return stringReader.getRead();
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (!(context.getSource() instanceof CommandSource)) {
            return Suggestions.empty();
        } else {
            String string = builder.getRemaining();
            Object collection;
            if (!string.isEmpty() && string.charAt(0) == '^') {
                collection = Collections.singleton(RelativePosition.ZERO_LOCAL);
            } else {
                collection = ((CommandSource)context.getSource()).getBlockPositionSuggestions();
            }

            return CommandSource.suggestPositions(string, (Collection)collection, builder, CommandManager.getCommandValidator(this::parse));
        }
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
