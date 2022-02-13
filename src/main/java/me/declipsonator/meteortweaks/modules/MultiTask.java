package me.declipsonator.meteortweaks.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class MultiTask extends Module {
    public MultiTask() {
        super(Categories.Player, "multi-task", "Lets you use 2 items at once.");
    }
}
