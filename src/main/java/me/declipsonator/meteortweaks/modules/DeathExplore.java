/*
 *  This file is part of the Meteor Tweaks distribution (https://github.com/Declipsonator/Meteor-Tweaks/).
 *  Copyright (c) 2022 Meteor Tweaks.
 *  Licensed Under the GNU Lesser General Public License v3.0
 */

package me.declipsonator.meteortweaks.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.DeathScreen;

public class DeathExplore extends Module {

    public DeathExplore() {
        super(Categories.Player, "death-explore", "Lets you walk around after you die.");
    }

    private boolean isDead = false;

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if(mc.player.isDead()) {
            mc.player.setHealth(20);
            mc.setScreen(null);
            isDead = true;
        }
    }


    @Override
    public void onDeactivate() {
        if (isDead) {
            mc.player.setHealth(0);
            mc.setScreen(new DeathScreen(null, mc.world.getLevelProperties().isHardcore()));
            isDead = false;
        }
    }
}
