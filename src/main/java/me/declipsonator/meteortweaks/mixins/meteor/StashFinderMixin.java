/*
 *  This file is part of the Meteor Tweaks distribution (https://github.com/Declipsonator/Meteor-Tweaks/).
 *  Copyright (c) 2022 Meteor Tweaks.
 *  Licensed Under the GNU Lesser General Public License v3.0
 */

package me.declipsonator.meteortweaks.mixins.meteor;

import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.world.StashFinder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(value = StashFinder.class, remap = false)
public class StashFinderMixin extends Module {

    @Shadow
    public List<StashFinder.Chunk> chunks;

    public StashFinderMixin(Category category, String name, String description) {
        super(category, name, description);
    }

    @Override
    public String getInfoString() {
        return Integer.toString(chunks.size());
    }

}
