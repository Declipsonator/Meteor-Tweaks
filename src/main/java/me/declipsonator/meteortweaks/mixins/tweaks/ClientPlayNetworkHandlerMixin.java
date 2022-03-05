package me.declipsonator.meteortweaks.mixins.tweaks;

import me.declipsonator.meteortweaks.modules.GameTweaks;
import me.declipsonator.meteortweaks.utils.RecipeBookRecipes;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.network.packet.s2c.play.SynchronizeRecipesS2CPacket;
import net.minecraft.network.packet.s2c.play.UnlockRecipesS2CPacket;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

	@Shadow
	@Final
	private RecipeManager recipeManager;

	@Inject(method = "onSynchronizeRecipes", at = @At("HEAD"))
	private void greatMethodName(SynchronizeRecipesS2CPacket packet, CallbackInfo cir) {
		if (!Modules.get().get(GameTweaks.class).isActive() || !Modules.get().get(GameTweaks.class).fullRecipes.get() || mc.player == null) return;
		ClientRecipeBook recipeBook = mc.player.getRecipeBook();
		packet.getRecipes().forEach(recipeBook::add);
		RecipeBookRecipes.setRecipes(packet.getRecipes());
	}

	@Inject(method = "onUnlockRecipes", at = @At("HEAD"))
	private void notherOne(UnlockRecipesS2CPacket packet, CallbackInfo cir) {
		if (!Modules.get().get(GameTweaks.class).isActive() || !Modules.get().get(GameTweaks.class).fullRecipes.get()) return;
		switch (packet.getAction()) {
			case ADD, INIT -> {
				for (Identifier identifier : packet.getRecipeIdsToChange()) {
					this.recipeManager.get(identifier).ifPresent(RecipeBookRecipes::removeRecipeFromCache);
				}
			}
			case REMOVE -> {
				for (Identifier identifier : packet.getRecipeIdsToChange()) {
					this.recipeManager.get(identifier).ifPresent(RecipeBookRecipes::addRecipe);
				}
			}
		}
	}
}