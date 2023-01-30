package com.team3gdx.game.station;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Ingredients;

public class CuttingStation extends Station {

	private final static Ingredient[] ALLOWED_INGREDIENTS = { Ingredients.lettuce, Ingredients.tomato,
			Ingredients.onion };

	public float currentCutTime;

	public CuttingStation(Vector2 pos, float timeBetweenCuts) {
		super(pos, 1, false, ALLOWED_INGREDIENTS);
	}

	public void interact(SpriteBatch batch, float time) {
		currentCutTime += time;
		if (slots.peek().slice(batch, currentCutTime)) {
			currentCutTime = 0;
		}
	}

}
