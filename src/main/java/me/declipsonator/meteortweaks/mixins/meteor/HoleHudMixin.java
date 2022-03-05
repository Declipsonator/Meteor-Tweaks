/*
    I understand that I probably did this the absolutely worst way possible, but it works
 */


package me.declipsonator.meteortweaks.mixins.meteor;

import meteordevelopment.meteorclient.gui.screens.HudEditorScreen;
import meteordevelopment.meteorclient.gui.screens.HudElementScreen;
import meteordevelopment.meteorclient.mixin.WorldRendererAccessor;
import meteordevelopment.meteorclient.renderer.Renderer2D;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.systems.hud.modules.HoleHud;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.render.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Mixin(value = HoleHud.class, remap = false)
public class HoleHudMixin {

    @Shadow
    @Final
    private SettingGroup sgGeneral;

    @Final
    @Shadow
    private Setting<Double> scale;

    @Final
    @Shadow
    public Setting<List<Block>> safe;

    private double posX = 0;
    private double posY = 0;
    private boolean first = true;
    private Setting<Boolean> burrowBlock;
    private Setting<Boolean> burrowSafe;

    @Inject(method = "render", at = @At("TAIL"))
    public void renderMiddle(HudRenderer renderer, CallbackInfo ci) {
        Renderer2D.COLOR.begin();
        drawMiddleBlock(posX, posY - 16 * scale.get());
        Renderer2D.COLOR.render(null);
    }

    @Inject(method = "drawBlock", at = @At("HEAD"))
    private void getXY(Direction dir, double x, double y, CallbackInfo ci) {
        posX = x;
        posY = y;
    }

    private void drawMiddleBlock(double x, double y) {
        if(first) {
            burrowBlock = sgGeneral.add(new BoolSetting.Builder()
                    .name("burrow-block")
                    .description("Displays the block you're burrowed in.")
                    .defaultValue(true)
                    .build()
            );

            burrowSafe = sgGeneral.add(new BoolSetting.Builder()
                    .name("only-safe-burrow")
                    .description("Only displays the burrowed block if its in the safe list.")
                    .defaultValue(false)
                    .build()
            );
            first = false;
        }

        if(!burrowBlock.get()) return;
        boolean couldNull = !Utils.canUpdate() || isInEditor();
        Block block = (couldNull)? Blocks.OBSIDIAN : mc.world.getBlockState(mc.player.getBlockPos()).getBlock();
        if (!safe.get().contains(block) && burrowSafe.get()) return;

        RenderUtils.drawItem(block.asItem().getDefaultStack(), (int) x, (int) y, scale.get(), false);

        if(couldNull) return;

        ((WorldRendererAccessor) mc.worldRenderer).getBlockBreakingInfos().values().forEach(info -> {
            if (info.getPos().equals(mc.player.getBlockPos())) {
                renderBreaking(x, y, info.getStage() / 9f);
            }
        });
    }

    @Shadow
    private void renderBreaking(double x, double y, double percent) {}

    private boolean isInEditor() {
        return (mc.currentScreen instanceof HudEditorScreen || mc.currentScreen instanceof HudElementScreen || !Utils.canUpdate());
    }
}