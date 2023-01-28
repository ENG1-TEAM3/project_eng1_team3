package com.team3gdx.game.station;

import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Menu;
import com.team3gdx.game.food.Recipe;

public class ServingStation extends Station {

	// Allow only components to be placed at station (to give to customer!)

	static Ingredient[] allowedIngredients = new Ingredient[Menu.RECIPES.size()];
	static {
		int i = 0;
		for (Recipe recipe : Menu.RECIPES.values()) {
			allowedIngredients[i] = new Ingredient(recipe);
			i++;
		}
	};

	public ServingStation(Vector2 pos) {
		super(pos, 1, false, allowedIngredients);
	}

}
