package me.declipsonator.meteortweaks.utils;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;

public class TweaksUtil {
    public static boolean firstTimeTitleScreen = true;
    public static Version version() {
        return FabricLoader.getInstance().getModContainer("meteor-tweaks").get().getMetadata().getVersion();
    }
}
