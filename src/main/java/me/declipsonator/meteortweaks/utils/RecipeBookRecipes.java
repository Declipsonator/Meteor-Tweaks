/*
 *  This file is part of the Meteor Tweaks distribution (https://github.com/Declipsonator/Meteor-Tweaks/).
 *  Copyright (c) 2022 Meteor Tweaks.
 *  Licensed Under the GNU Lesser General Public License v3.0
 */

package me.declipsonator.meteortweaks.utils;

import net.minecraft.recipe.Recipe;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeBookRecipes {
	private static final Set<Recipe<?>> RECIPES = new HashSet<>();

	public static boolean isCached(Recipe<?> recipe) {
		return RECIPES.contains(recipe);
	}

	public static void setRecipes(List<Recipe<?>> recipeCache) {
		recipeCache.forEach(RecipeBookRecipes::addRecipe);
	}

	public static void addRecipe(Recipe<?> recipe) {
		RECIPES.add(recipe);
	}

	public static void removeRecipeFromCache(Recipe<?> recipe) {
		RECIPES.remove(recipe);
	}
}