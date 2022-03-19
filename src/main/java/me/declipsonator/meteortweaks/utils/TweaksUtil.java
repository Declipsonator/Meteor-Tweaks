package me.declipsonator.meteortweaks.utils;

import com.google.gson.Gson;
import meteordevelopment.meteorclient.utils.network.Http;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;

import java.awt.image.BufferedImage;
import java.util.UUID;

public class TweaksUtil {
    public static boolean firstTimeTitleScreen = true;
    public static Version version() {
        return FabricLoader.getInstance().getModContainer("meteor-tweaks").get().getMetadata().getVersion();
    }

    public static Account uuidToAccount(UUID uuid) {
        String accountString = Http.get("https://api.mojang.com/user/profile/" + uuid.toString()).sendString();
        return new Gson().fromJson(accountString, Account.class);
    }

    public static Account uuidToAccount(String uuid) {
        String accountString = Http.get("https://api.mojang.com/user/profile/" + uuid).sendString();
        return new Gson().fromJson(accountString, Account.class);
    }

}

