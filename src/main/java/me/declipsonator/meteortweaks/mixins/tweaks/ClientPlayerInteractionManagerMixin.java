/*
 *  This file is part of the Meteor Tweaks distribution (https://github.com/Declipsonator/Meteor-Tweaks/).
 *  Copyright (c) 2022 Meteor Tweaks.
 *  Licensed Under the GNU Lesser General Public License v3.0
 */

package me.declipsonator.meteortweaks.mixins.tweaks;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import me.declipsonator.meteortweaks.modules.GameTweaks;
import me.declipsonator.meteortweaks.utils.GhostRecipeBookWidget;
import me.declipsonator.meteortweaks.utils.MixinReferences;
import me.declipsonator.meteortweaks.utils.RecipeBookRecipes;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
	

	@Inject(method = "clickRecipe", at = @At("HEAD"), cancellable = true)
	public void clickedRecipe(int syncId, Recipe<?> recipe, boolean craftAll, CallbackInfo cir) {
		GameTweaks tweaks = Modules.get().get(GameTweaks.class);

		if (!(tweaks.isActive() && tweaks.fullRecipes.get() && MixinReferences.clicked && RecipeBookRecipes.isCached(recipe) && mc.currentScreen instanceof HandledScreen<?> handledScreen)) return;

		RecipeBookWidget widget = null;
		if (handledScreen instanceof InventoryScreen playerScreen) {
			widget = playerScreen.getRecipeBookWidget();
		} else if (handledScreen instanceof CraftingScreen craftingScreen) {
			widget = craftingScreen.getRecipeBookWidget();
		}

		((GhostRecipeBookWidget) widget).clearGhostSlots();

		boolean canCraft = true;
		for(Ingredient ingredient: recipe.getIngredients()) {
			boolean ingredientGood = false;
			for(ItemStack itemStack: ingredient.getMatchingStacks()) {
				int numOfIngredients = 0;
				for(int i = 0; i < 36; i++) {
					if (mc.player.getInventory().getStack(i) == itemStack) numOfIngredients += mc.player.getInventory().getStack(i).getCount();
				}
				if(ingredient.getMatchingStacks()[0].getCount() <= numOfIngredients) {
					ingredientGood = true;
					break;
				}
			}

			if(!ingredientGood) {
				canCraft = false;
				break;
			}
		}


		if(!canCraft) widget.showGhostRecipe(recipe, mc.player.currentScreenHandler.slots);
		else {
			fillInputSlots(recipe, Screen.hasShiftDown());
		}

		cir.cancel();
	}

	/*
		The code below was pretty much skidded from the server side
		I couldn't interface with it by just referencing it or smth so I just pasted
	 */
	private void fillInputSlots(final Recipe<?> recipe, final boolean craftAll) {
		 RecipeMatcher matcher = new RecipeMatcher();
		AbstractRecipeScreenHandler<?> handler = mc.player.playerScreenHandler;
		int i = matcher.countCrafts(recipe, (IntList)null);
		int j;
		for(j = 0; j < handler.getCraftingHeight() * handler.getCraftingWidth() + 1; ++j) {
			if (j != handler.getCraftingResultSlotIndex()) {
				ItemStack itemStack = handler.getSlot(j).getStack();
				if (!itemStack.isEmpty() && Math.min(i, itemStack.getMaxCount()) < itemStack.getCount() + 1) {
					return;
				}
			}
		}

		j = getAmountToFill(craftAll, i, true);
		IntList itemStack = new IntArrayList();
		if (matcher.match(recipe, itemStack, j)) {
			int k = j;

			for (int l : itemStack) {
				int m = RecipeMatcher.getStackFromId(l).getMaxCount();
				if (m < k) {
					k = m;
				}
			}

			if (matcher.match(recipe, itemStack, k)) {
				returnInputs(false);
				alignRecipeToGrid(handler.getCraftingWidth(), handler.getCraftingHeight(), handler.getCraftingResultSlotIndex(), recipe, itemStack.iterator(), k);
			}
		}
	}

	protected void returnInputs(boolean bl) {
		AbstractRecipeScreenHandler<?> handler = mc.player.playerScreenHandler;
		for(int i = 0; i < handler.getCraftingSlotCount(); ++i) {
			if (handler.canInsertIntoSlot(i)) {
				ItemStack itemStack = handler.getSlot(i).getStack().copy();
				mc.player.getInventory().offer(itemStack, false);
				handler.getSlot(i).setStack(itemStack);
			}
		}

		handler.clearCraftingSlots();
	}
	
	protected int getAmountToFill(boolean craftAll, int limit, boolean recipeInCraftingSlots) {
		AbstractRecipeScreenHandler<?> handler = mc.player.playerScreenHandler;
		int i = 1;
		if (craftAll) {
			i = limit;
		} else if (recipeInCraftingSlots) {
			i = 64;

			for(int j = 0; j < handler.getCraftingWidth() * handler.getCraftingHeight() + 1; ++j) {
				if (j != handler.getCraftingResultSlotIndex()) {
					ItemStack itemStack = handler.getSlot(j).getStack();
					if (!itemStack.isEmpty() && i > itemStack.getCount()) {
						i = itemStack.getCount();
					}
				}
			}

			if (i < 64) {
				++i;
			}
		}

		return i;
	}

	private void alignRecipeToGrid(int gridWidth, int gridHeight, int gridOutputSlot, Recipe<?> recipe, Iterator<?> inputs, int amount) {
		int i = gridWidth;
		int j = gridHeight;
		if (recipe instanceof ShapedRecipe shapedRecipe) {
			i = shapedRecipe.getWidth();
			j = shapedRecipe.getHeight();
		}

		int shapedRecipe = 0;

		for(int k = 0; k < gridHeight; ++k) {
			if (shapedRecipe == gridOutputSlot) {
				++shapedRecipe;
			}

			boolean bl = (float)j < (float)gridHeight / 2.0F;
			int l = MathHelper.floor((float)gridHeight / 2.0F - (float)j / 2.0F);
			if (bl && l > k) {
				shapedRecipe += gridWidth;
				++k;
			}

			for(int m = 0; m < gridWidth; ++m) {
				if (!inputs.hasNext()) {
					return;
				}

				bl = (float)i < (float)gridWidth / 2.0F;
				l = MathHelper.floor((float)gridWidth / 2.0F - (float)i / 2.0F);
				int n = i;
				boolean bl2 = m < i;
				if (bl) {
					n = l + i;
					bl2 = l <= m && m < l + i;
				}

				if (bl2) {
					acceptAlignedInput(inputs, shapedRecipe, amount, k, m);
				} else if (n == m) {
					shapedRecipe += gridWidth - m;
					break;
				}

				++shapedRecipe;
			}
		}

	}

	private void acceptAlignedInput(final Iterator<?> inputs, final int slot, final int amount, final int gridX, final int gridY) {
		AbstractRecipeScreenHandler<?> handler = mc.player.playerScreenHandler;
		Slot slot2 = handler.getSlot(slot);
		ItemStack itemStack = RecipeMatcher.getStackFromId((Integer)inputs.next());
		if (!itemStack.isEmpty()) {
			for(int i = 0; i < amount; ++i) {
				fillInputSlot(slot2, itemStack);
			}
		}

	}

	protected void fillInputSlot(Slot slot, ItemStack stack) {
		int i = mc.player.getInventory().indexOf(stack);
		if (i != -1) {
			ItemStack itemStack =  mc.player.getInventory().getStack(i).copy();
			if (!itemStack.isEmpty()) {
				if (itemStack.getCount() > 1) {
					 mc.player.getInventory().removeStack(i, 1);
				} else {
					 mc.player.getInventory().removeStack(i);
				}

				itemStack.setCount(1);
				if (slot.getStack().isEmpty()) {
					slot.setStack(itemStack);
				} else {
					slot.getStack().increment(1);
				}

			}
		}
	}

}