package com.team3gdx.game.food;

import java.util.HashMap;
import java.util.Map;

public class Menu {

	private static final Map<Ingredient, String> BURGER_STEPS = new HashMap<Ingredient, String>();
	static {
		BURGER_STEPS.put(Ingredients.bun, "Toast");
		BURGER_STEPS.put(Ingredients.cookedPatty, "Fry");
	}
	private static final Map<Ingredient, String> SALAD_STEPS = new HashMap<Ingredient, String>();
	static {
//		saladSteps.put(Ingredients.onion, "Cut");
		SALAD_STEPS.put(Ingredients.tomato, "Cut");
		SALAD_STEPS.put(Ingredients.lettuce, "Cut");
		SALAD_STEPS.put(Ingredients.bun, "Toast");
		SALAD_STEPS.put(Ingredients.formedPatty, "Fry");
	}

	public static final Map<String, Recipe> RECIPES = new HashMap<String, Recipe>();
	static {
		RECIPES.put("Burger", new Recipe("Form patty", BURGER_STEPS, "serve together", "burger", true, null, 32, 32));
		RECIPES.put("Salad", new Recipe("", SALAD_STEPS, "serve together", "nil", false, null, 32, 32));
	}

	public static final Map<Ingredient, Ingredient> INGREDIENT_PREP = new HashMap<Ingredient, Ingredient>();
	static {
		INGREDIENT_PREP.put(Ingredients.unformedPatty, Ingredients.formedPatty);
	}
}
