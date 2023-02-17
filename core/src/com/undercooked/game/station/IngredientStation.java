package com.undercooked.game.station;

import com.badlogic.gdx.math.Vector2;
import com.undercooked.game.food.Ingredient;

public class IngredientStation extends Station {

	public IngredientStation(Vector2 pos, Ingredient ingredient) {
		super(pos, 1, true, null, null);
		slots.push(ingredient);
	}

}
