package me.declipsonator.meteortweaks.mixins.tweaks;

import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(RenderLayers.class)
public interface RenderLayersMixin {
	@Accessor("BLOCKS")
	static Map<Block, RenderLayer> getBlocks() {
		throw new IllegalStateException("This should never happen");
	}
}