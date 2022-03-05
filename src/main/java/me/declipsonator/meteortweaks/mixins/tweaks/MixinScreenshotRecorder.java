package me.declipsonator.meteortweaks.mixins.tweaks;


import me.declipsonator.meteortweaks.MeteorTweaks;
import me.declipsonator.meteortweaks.modules.GameTweaks;
import me.declipsonator.meteortweaks.utils.ClipboardImage;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Mixin(ScreenshotRecorder.class)
public abstract class MixinScreenshotRecorder {
    @Inject(method = "method_1661", at = @At("TAIL"))
    private static void onScreenshot(NativeImage image, File file, Consumer<Text> messageReceiver, CallbackInfo ci) {
        GameTweaks tweaks = Modules.get().get(GameTweaks.class);
        if(!(tweaks.isActive() && tweaks.copyScreenshots.get())) return;
        try {
            File path = new File(mc.runDirectory.getAbsolutePath() + "\\screenshots\\");
            Optional<Path> lastFilePath = Files.list(path.toPath())
                    .filter(f -> !Files.isDirectory(f))
                    .max(Comparator.comparingLong(f -> f.toFile().lastModified()));
            Image lastScreen = new ImageIcon(lastFilePath.get().toString()).getImage();
            ClipboardImage newImage = new ClipboardImage(lastScreen);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(newImage, null);
            ChatUtils.info("GameTweaks", "Copied Screenshot to Clipboard.");
        } catch(Exception e) {
            MeteorTweaks.LOG.error(e.getMessage());
        }
    }
}



