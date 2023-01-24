package com.team3gdx.game.station;

import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Menu;
import com.team3gdx.game.food.Recipe;

public class PrepStation extends Station {

	public PrepStation(Vector2 pos) {
		super(pos, 5, false, null);
	}

	public boolean slotsToRecipe() {
		for (Recipe recipe : Menu.RECIPES.values()) {
			if (recipe.matches(slots)) {
				slots.clear();
				slots.add(recipe);
				return true;
			}

		}

		if (ingredientMatch(slots.peek()) != null) {
			slots.add(ingredientMatch(slots.pop()));

			return true;
		}

		return false;
	}

	private Ingredient ingredientMatch(Ingredient toMatch) {
		for (Ingredient ingredient : Menu.INGREDIENT_PREP.keySet()) {
			if (ingredient.equals(toMatch)) {
				return Menu.INGREDIENT_PREP.get(ingredient);
			}
		}

		return null;
	}

}
