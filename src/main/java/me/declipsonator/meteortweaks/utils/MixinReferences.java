package me.declipsonator.meteortweaks.utils;

import meteordevelopment.meteorclient.settings.Setting;

public class MixinReferences {
    // For ElytraFlyMixin/ElytraFlightModeMixin
    public static Setting<Boolean> gradualAcceleration;
    public static Setting<Integer> gradualAccelerationTime;
    public static Setting<Boolean> whenChangingDirections;
    public static Setting<Boolean> vertically;

    // For PotionTimerMixin
    public enum whiteBlackNoRacismBecauseImBetterThanYou {
        Whitelist,
        Blacklist
    }


}
