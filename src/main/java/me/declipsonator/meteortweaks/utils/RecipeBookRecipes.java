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