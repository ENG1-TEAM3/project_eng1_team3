package com.undercooked.game.station;

import com.badlogic.gdx.math.Vector2;
import com.undercooked.game.food.Ingredient;
import com.undercooked.game.food.Ingredients;
import com.undercooked.game.screen.GameScreen;

public class BakingStation extends CookingStation {

	private final static Ingredient[] ALLOWED_INGREDIENTS = { Ingredients.bun, Ingredients.cooked_bun };

	public BakingStation(Vector2 pos, GameScreen game) {
		super(pos, 4, ALLOWED_INGREDIENTS, "particles/smokes.party", "audio/soundFX/frying.mp3", game);
	}

	@Override
	public boolean place(Ingredient ingredient) {
		if (super.place(ingredient)) {
			ingredient.flipped = true;
			return true;
		}

		return false;
	}

}
