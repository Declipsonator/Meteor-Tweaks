/*
 *  This file is part of the Meteor Tweaks distribution (https://github.com/Declipsonator/Meteor-Tweaks/).
 *  Copyright (c) 2022 Meteor Tweaks.
 *  Licensed Under the GNU Lesser General Public License v3.0
 */

package me.declipsonator.meteortweaks.utils;

import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;

public class MixinReferences {
    // For ElytraFlyMixin/ElytraFlightModeMixin
    public static Setting<Boolean> gradualAcceleration;
    public static Setting<Integer> gradualAccelerationTime;
    public static Setting<Boolean> whenChangingDirections;
    public static Setting<Boolean> vertically;

    // For SpeedMineMixin
    public static Setting<Boolean> confirmBreak;

    // For NoRenderMixin
    public static Setting<Boolean> toasts;

    // For GuiMoveMixin
    public static Setting<Boolean> antiClose;
    public static Setting<Boolean> ghostMove;

    // For PotionTimerMixin
    public enum whiteBlackNoRacismBecauseImBetterThanYou {
        Whitelist,
        Blacklist
    }

    // For Crafting Recipe Book Stuff
    public static boolean clicked = false;

    // For NotifierMixin
    public static SettingGroup sgJoinLeave;
    public static Setting<Boolean> joinLeaveMessages;
    public static Setting<joinLeave> joinOrLeave;
    public static Setting<Boolean> friendsJoinLeave;
    public enum joinLeave {
        Join,
        Leave,
        Both
    }


}
