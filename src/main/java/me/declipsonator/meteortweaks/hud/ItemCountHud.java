/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client/).
 * Copyright (c) 2021 Meteor Development.
 */

package me.declipsonator.meteortweaks.hud;

import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.ItemListSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.hud.HUD;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.systems.hud.modules.HudElement;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.List;


public class ItemCountHud extends HudElement {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<List<Item>> items = sgGeneral.add(new ItemListSetting.Builder()
        .name("items")
        .description("Which items to display")
        .defaultValue(Items.ENCHANTED_GOLDEN_APPLE, Items.END_CRYSTAL, Items.OBSIDIAN)
        .build()
    );

    private final Setting<SettingColor> itemColor = sgGeneral.add(new ColorSetting.Builder()
        .name("count-color")
        .description("The item count color.")
        .defaultValue(new SettingColor(25, 225, 25))
        .build()
    );

    public ItemCountHud(HUD hud) {
        super(hud, "item-count", "Displays the amount of given items in your inventory..", false);
    }

    @Override
    public void update(HudRenderer renderer) {
        double width = 0;
        double height = 0;

        int i = 0;
        for (Item item : items.get()) {
            width = Math.max(width, getItemWidth(renderer, item));
            height += renderer.textHeight();
            if (i > 0) height += 2;

            i++;
        }

        box.setSize(width, height);
    }

    @Override
    public void render(HudRenderer renderer) {
        double xScreenPos = box.getX();
        double yScreenPos = box.getY();

        if (items.get().isEmpty()) {
            renderer.text("Item Count", xScreenPos, yScreenPos, hud.primaryColor.get());
            return;
        }

        for (Item item : items.get()) {
            renderItem(renderer, item, xScreenPos + box.alignX(getItemWidth(renderer, item)), yScreenPos);

            yScreenPos += 2 + renderer.textHeight();
        }
    }

    private void renderItem(HudRenderer renderer, Item item, double x, double y) {
        renderer.text(item.getName().getString() + ":", x, y, hud.primaryColor.get());
        String count;
        if(mc.player == null) count = String.valueOf(0);
        else count = String.valueOf(InvUtils.find(item).count());
        renderer.text(count, x + renderer.textWidth(item.getName().getString()) + renderer.textWidth(": "), y, itemColor.get());
    }

    private double getItemWidth(HudRenderer renderer, Item item) {
        double width = renderer.textWidth(item.getName().getString() + ": ");
        double count;
        if(mc.player == null) count = renderer.textWidth(String.valueOf(0));
        else count = renderer.textWidth(String.valueOf(InvUtils.find(item).count()));
        return width + count;
    }

}