package me.declipsonator.meteortweaks.mixins.tweaks;

import me.declipsonator.meteortweaks.utils.GhostRecipeBookWidget;
import me.declipsonator.meteortweaks.utils.MixinReferences;
import net.minecraft.client.gui.screen.recipebook.RecipeBookGhostSlots;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RecipeBookWidget.class)
public class RecipeBookWidgetMixin implements GhostRecipeBookWidget {

	@Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;clickRecipe(ILnet/minecraft/recipe/Recipe;Z)V"))
	public void onMouseClick(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
		MixinReferences.clicked = true;
	}

	@Shadow
	@Final
	protected RecipeBookGhostSlots ghostSlots;

	@Override
	public void clearGhostSlots() {
		this.ghostSlots.reset();
	}
}