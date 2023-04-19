package com.team3gdx.game.food;

import java.util.HashMap;
import java.util.Map;

/**
 * All recipes. See {@link Ingredients} if your recipe requires a new ingredient
 * or an existing with different state.
 * 
 */
public class Menu {

	/**
	 * Maps of ingredients in corresponding recipes and steps on what to do with
	 * them.
	 */
	public static final Map<Ingredient, String> BURGER_STEPS = new HashMap<Ingredient, String>();
	static {
		BURGER_STEPS.put(Ingredients.cooked_bun, "Toast");
		BURGER_STEPS.put(Ingredients.cookedPatty, "Fry");
	}
	private static final Map<Ingredient, String> BURGER_BURNED_STEPS = new HashMap<Ingredient, String>();
	static {
		BURGER_BURNED_STEPS.put(Ingredients.cooked_bun, "Toast");
		BURGER_BURNED_STEPS.put(Ingredients.burnedPatty, "Fry");
	}
	private static final Map<Ingredient, String> JACKET_POTATO_STEPS = new HashMap<Ingredient, String>();
	static {
		JACKET_POTATO_STEPS.put(Ingredients.cookedPotato, "Toast");
		JACKET_POTATO_STEPS.put(Ingredients.cheeseChopped, "cut");
	}
	private static final Map<Ingredient, String> SALAD_STEPS = new HashMap<Ingredient, String>();
	static {
		SALAD_STEPS.put(Ingredients.lettuceChopped, "Cut");
		SALAD_STEPS.put(Ingredients.tomatoChopped, "Cut");
		SALAD_STEPS.put(Ingredients.onionChopped, "Cut");
	}
	private static final Map<Ingredient, String> PIZZA_STEPS = new HashMap<Ingredient, String>();
	static {
		PIZZA_STEPS.put(Ingredients.formedDough, "formed");
		PIZZA_STEPS.put(Ingredients.tomato_sauce, "raw");
		PIZZA_STEPS.put(Ingredients.cheeseChopped, "cut");
	}

	/**
	 * Map of recipes
	 */
	public static final Map<String, Recipe> RECIPES = new HashMap<String, Recipe>();
	static {
		RECIPES.put("Burger", new Recipe("Form patty", Ingredients.unformedPatty, BURGER_STEPS, "serve together",
				"burger", false, null, 32, 32, 0));
		RECIPES.put("Burned burger", new Recipe("Form patty", Ingredients.unformedPatty, BURGER_BURNED_STEPS,
				"serve together", "burger_burned", false, null, 32, 32, 0));
		RECIPES.put("Salad", new Recipe("", null, SALAD_STEPS, "serve together", "salad", false, null, 32, 32, 0));
		RECIPES.put("Jacket_potato", new Recipe("cook_potato", Ingredients.potato, JACKET_POTATO_STEPS, "serve together",
				"Jacket_potato", false, null, 32, 32, 0));
		RECIPES.put("Raw_pizza",new Recipe("Form dough", Ingredients.unformedDough, PIZZA_STEPS,"serve together", "raw_Pizza", false, null, 32, 32, 0));
	}

	/**
	 * Map of ingredient transformations on preparation station (ingredient ->
	 * ingredient)
	 */
	public static final Map<Ingredient, Ingredient> INGREDIENT_PREP = new HashMap<Ingredient, Ingredient>();
	static {
		INGREDIENT_PREP.put(Ingredients.unformedPatty, Ingredients.formedPatty);
		INGREDIENT_PREP.put(Ingredients.unformedDough, Ingredients.formedDough);

	}
}
