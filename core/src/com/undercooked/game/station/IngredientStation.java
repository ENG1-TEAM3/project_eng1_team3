package com.undercooked.game.station;

import com.badlogic.gdx.math.Vector2;
import com.undercooked.game.food.Ingredient;
import com.undercooked.game.screen.GameScreen;

public class IngredientStation extends Station {

	public IngredientStation(Vector2 pos, Ingredient ingredient, GameScreen game) {
		super(pos, 1, true, null, null, game);
		slots.push(ingredient);
	}

}
