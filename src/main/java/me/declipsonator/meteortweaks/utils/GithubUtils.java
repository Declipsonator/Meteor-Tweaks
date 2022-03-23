/*
 *  This file is part of the Meteor Tweaks distribution (https://github.com/Declipsonator/Meteor-Tweaks/).
 *  Copyright (c) 2022 Meteor Tweaks.
 *  Licensed Under the GNU Lesser General Public License v3.0
 */

package me.declipsonator.meteortweaks.utils;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import meteordevelopment.meteorclient.utils.network.Http;
import net.fabricmc.loader.api.Version;

public class GithubUtils {
    public static String getLastCommit() {
        String response = Http.get("https://api.github.com/repos/Declipsonator/Meteor-Tweaks/commits").sendString();
        JsonArray ar = JsonParser.parseString(response).getAsJsonArray();

        return ar.get(0).getAsJsonObject().get("sha").getAsString();
    }

    public static String getUpdateCommit() {
        return getLastReleaseCommit("v" + TweaksUtil.version().getFriendlyString());
    }

    public static boolean isOutdated() {
        return TweaksUtil.version().compareTo(getLastUpdate()) < 0;
    }

    public static Version getLastUpdate() {
        String response = Http.get("https://api.github.com/repos/Declipsonator/Meteor-Tweaks/releases/latest").sendString();
        JsonObject jo = JsonParser.parseString(response).getAsJsonObject();

        try {
            return Version.parse(jo.get("tag_name").getAsString().replace("v", ""));
        } catch(Exception e) {
            return null;
        }
    }

    public static String getLastReleaseCommit(String release) {
        String response = Http.get("https://api.github.com/repos/Declipsonator/Meteor-Tweaks/tags").sendString();
        JsonArray ar = JsonParser.parseString(response).getAsJsonArray();

        for(JsonElement element: ar) {
            if(element.getAsJsonObject().get("name").getAsString().equals(release)) {
                return element.getAsJsonObject().get("commit").getAsJsonObject().get("sha").getAsString();
            }
        }
        return null;
    }
}
