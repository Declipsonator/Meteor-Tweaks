package me.declipsonator.meteortweaks.mixins.tweaks;

import com.google.gson.Gson;
import me.declipsonator.meteortweaks.utils.GithubReleaseJson;
import me.declipsonator.meteortweaks.utils.TweaksUtil;
import meteordevelopment.meteorclient.utils.network.Http;
import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
import meteordevelopment.meteorclient.utils.render.prompts.YesNoPrompt;
import net.fabricmc.loader.api.Version;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = TitleScreen.class, priority = 900)
public class TitleScreenMixin {


    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawStringWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void onRenderShit(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo info) {
        if (TweaksUtil.firstTimeTitleScreen) {
            TweaksUtil.firstTimeTitleScreen = false;

            MeteorExecutor.execute(() -> {
                Http.Request latestRelease = Http.get("https://api.github.com/repos/Declipsonator/Meteor-Tweaks/releases/latest");
                GithubReleaseJson githubReleaseJson = new Gson().fromJson(latestRelease.sendString(), GithubReleaseJson.class);

                try {
                    if (TweaksUtil.version().compareTo(Version.parse(githubReleaseJson.getTagName().replace("v", ""))) < 0) {

                        YesNoPrompt.create()
                                .id("meteortweaks-update")
                                .title("Outdated")
                                .message("A new version of Meteor GameTweaks has been released.")
                                .message("Current version: " + TweaksUtil.version().getFriendlyString())
                                .message("Latest version: " + Version.parse(githubReleaseJson.getTagName().replace("v", "")).getFriendlyString())
                                .message("Update?")
                                .onYes(() -> Util.getOperatingSystem().open("https://github.com/Declipsonator/Meteor-Tweaks/releases"))
                                .show();


                    }

                } catch (Exception ignored) {
                }
            });

        }
    }


}