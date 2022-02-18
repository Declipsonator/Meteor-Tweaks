package me.declipsonator.meteortweaks.mixins;

import me.declipsonator.meteortweaks.MeteorTweaks;
import me.declipsonator.meteortweaks.utils.MixinReferences;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StatusEffectListSetting;
import meteordevelopment.meteorclient.systems.hud.HUD;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.systems.hud.modules.HudElement;
import meteordevelopment.meteorclient.systems.hud.modules.PotionTimersHud;
import meteordevelopment.meteorclient.utils.misc.Names;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(PotionTimersHud.class)
public class PotionTimerMixin extends HudElement {
    private final Color color = new Color();

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<MixinReferences.whiteBlackNoRacismBecauseImBetterThanYou> whiteBlackNoRacismBecauseImBetterThanYouSetting = sgGeneral.add(new EnumSetting.Builder<MixinReferences.whiteBlackNoRacismBecauseImBetterThanYou>()
            .name("whitelist-blacklist")
            .description("Whether or not to use a whitelist or a blacklist.")
            .defaultValue(MixinReferences.whiteBlackNoRacismBecauseImBetterThanYou.Blacklist)
            .build()
    );

    private final Setting<List<StatusEffect>> potions = sgGeneral.add(new StatusEffectListSetting.Builder()
            .name("potion-effects")
            .description("What effects to whitelist/blacklist.")
            .build()
    );

    public PotionTimerMixin(HUD hud) {
        super(hud, "potion-timers", "Displays active potion effects with timers.");
    }


    @Override
    public void update(HudRenderer renderer) {
        if (isInEditor()) {
            box.setSize(renderer.textWidth("Potion Timers 0:00"), renderer.textHeight());
            return;
        }

        double width = 0;
        double height = 0;

        int i = 0;
        for (StatusEffectInstance statusEffectInstance : mc.player.getStatusEffects()) {
            if(potions.get().contains(statusEffectInstance.getEffectType()) && whiteBlackNoRacismBecauseImBetterThanYouSetting.get() == MixinReferences.whiteBlackNoRacismBecauseImBetterThanYou.Blacklist) continue;
            else if(!potions.get().contains(statusEffectInstance.getEffectType()) && whiteBlackNoRacismBecauseImBetterThanYouSetting.get() == MixinReferences.whiteBlackNoRacismBecauseImBetterThanYou.Whitelist) continue;
            width = Math.max(width, renderer.textWidth(getString(statusEffectInstance)));
            height += renderer.textHeight();

            if (i > 0) height += 2;
            i++;
        }

        box.setSize(width, height);
    }

    @Override
    public void render(HudRenderer renderer) {
        double x = box.getX();
        double y = box.getY();

        if (isInEditor()) {
            renderer.text("Potion Timers 0:00", x, y, color);
            return;
        }

        int i = 0;
        for (StatusEffectInstance statusEffectInstance : mc.player.getStatusEffects()) {
            StatusEffect statusEffect = statusEffectInstance.getEffectType();
            if(potions.get().contains(statusEffect) && whiteBlackNoRacismBecauseImBetterThanYouSetting.get() == MixinReferences.whiteBlackNoRacismBecauseImBetterThanYou.Blacklist) continue;
            else if(!potions.get().contains(statusEffectInstance.getEffectType()) && whiteBlackNoRacismBecauseImBetterThanYouSetting.get() == MixinReferences.whiteBlackNoRacismBecauseImBetterThanYou.Whitelist) continue;

            int c = statusEffect.getColor();
            color.r = Color.toRGBAR(c);
            color.g = Color.toRGBAG(c);
            color.b = Color.toRGBAB(c);

            String text = getString(statusEffectInstance);
            renderer.text(text, x + box.alignX(renderer.textWidth(text)), y, color);

            color.r = color.g = color.b = 255;
            y += renderer.textHeight();
            if (i > 0) y += 2;
            i++;
        }
    }

    private String getString(StatusEffectInstance statusEffectInstance) {
        return String.format("%s %d (%s)", Names.get(statusEffectInstance.getEffectType()), statusEffectInstance.getAmplifier() + 1, StatusEffectUtil.durationToString(statusEffectInstance, 1));
    }
}
