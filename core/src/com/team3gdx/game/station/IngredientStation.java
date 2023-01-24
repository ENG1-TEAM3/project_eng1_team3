package com.team3gdx.game.station;

import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.food.Ingredient;

public class IngredientStation extends Station {

	public IngredientStation(Vector2 pos, Ingredient ingredient) {
		super(pos, 1, true, null);
		slots.push(ingredient);
	}

}
