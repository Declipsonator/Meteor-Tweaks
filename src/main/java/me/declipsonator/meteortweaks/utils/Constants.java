package me.declipsonator.meteortweaks.utils;

import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

import java.lang.reflect.Field;
import java.util.Arrays;

public class Constants {
    public static final Direction[] directions = Direction.values();
    public static ThreadLocal<Object2ByteLinkedOpenHashMap<Block.NeighborGroup>> FACE_CULL_MAP;
    static {
        try {
            Field field = Arrays.stream(Block.class.getDeclaredFields())
                    .filter(it -> it.getType() == ThreadLocal.class)
                    .findFirst().orElseThrow(NoSuchFieldException::new);
            field.setAccessible(true);
            //noinspection unchecked
            Constants.FACE_CULL_MAP = (ThreadLocal<Object2ByteLinkedOpenHashMap<Block.NeighborGroup>>) field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    public static boolean shouldDrawSide(BlockPos pos, Direction dir, World world) {
        BlockState state = (BlockState) world.getBlockState(pos);
        BlockPos otherPos = pos.offset(dir);
        BlockState otherState = (BlockState) world.getBlockState(otherPos);
        //noinspection ConstantConditions
        if (/*state.isSideInvisible(otherState, dir)*/ false) { // client-only method
            return false;
        } else if (otherState.isOpaque()) {
            Block.NeighborGroup neighborGroup = new Block.NeighborGroup(state, otherState, dir);
            Object2ByteLinkedOpenHashMap<Block.NeighborGroup> faceCullMap = Constants.FACE_CULL_MAP.get();
            byte flags = faceCullMap.getAndMoveToFirst(neighborGroup);
            if (flags != 127) {
                return flags != 0;
            } else {
                VoxelShape cullShape = state.getCullingFace(world, pos, dir);
                VoxelShape otherCullShape = otherState.getCullingFace(world, otherPos, dir.getOpposite());
                boolean shouldDraw = VoxelShapes.matchesAnywhere(cullShape, otherCullShape, BooleanBiFunction.ONLY_FIRST);
                if (faceCullMap.size() == 200) {
                    faceCullMap.removeLastByte();
                }

                faceCullMap.putAndMoveToFirst(neighborGroup, (byte)(shouldDraw ? 1 : 0));
                return shouldDraw;
            }
        } else {
            return true;
        }
    }
}