package me.declipsonator.meteortweaks.mixins.tweaks;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import me.declipsonator.meteortweaks.modules.GameTweaks;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerListEntry.class)
public class PlayerListEntryMixin {
    @Inject(method = "method_2956", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void ignoreMigratorCape(MinecraftProfileTexture.Type type, Identifier identifier, MinecraftProfileTexture minecraftProfileTexture, CallbackInfo ci) {
        GameTweaks tweaks = Modules.get().get(GameTweaks.class);
        if(!tweaks.isActive() || !tweaks.migCapes.get()) return;
        if (type == MinecraftProfileTexture.Type.CAPE || type == MinecraftProfileTexture.Type.ELYTRA) {
            if (minecraftProfileTexture.getUrl().equals("http://textures.minecraft.net/texture/2340c0e03dd24a11b15a8b33c2a7e9e32abb2051b2481d0ba7defd635ca7a933")) {
                ci.cancel();
            }
        }
    }
}