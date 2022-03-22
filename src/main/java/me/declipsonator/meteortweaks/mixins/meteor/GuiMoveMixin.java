/*
 *  This file is part of the Meteor Tweaks distribution (https://github.com/Declipsonator/Meteor-Tweaks/).
 *  Copyright (c) 2022 Meteor Tweaks.
 *  Licensed Under the GNU Lesser General Public License v3.0
 */

package me.declipsonator.meteortweaks.mixins.meteor;

import me.declipsonator.meteortweaks.utils.MixinReferences;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.movement.GUIMove;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = GUIMove.class, remap = false)
public class GuiMoveMixin extends Module {
    @Shadow
    @Final
    private SettingGroup sgGeneral;
    private boolean first = true;

    public GuiMoveMixin(Category category, String name, String description) {
        super(category, name, description);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    private void onReceivePacket(PacketEvent.Receive event) {
        if (event.packet instanceof CloseScreenS2CPacket && MixinReferences.antiClose.get()) event.cancel();
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if (event.packet instanceof PlayerInteractItemC2SPacket && MixinReferences.ghostMove.get() && mc.currentScreen != null) event.cancel();
    }

    @Override
    public String getInfoString() {
        if(first) {
            MixinReferences.antiClose = sgGeneral.add(new BoolSetting.Builder()
                    .name("anti-close")
                    .description("Prevents the server from closing your gui.")
                    .defaultValue(true)
                    .build()
            );

            MixinReferences.ghostMove = sgGeneral.add(new BoolSetting.Builder()
                    .name("ghost-move")
                    .description("Only tells the server when you're done moving items.")
                    .defaultValue(true)
                    .build()
            );
            first = false;
        }
        return null;
    }
}
