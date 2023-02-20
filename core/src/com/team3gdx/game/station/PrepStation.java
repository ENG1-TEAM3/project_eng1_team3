package com.team3gdx.game.station;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Menu;
import com.team3gdx.game.food.Recipe;
import com.team3gdx.game.screen.GameScreen;

public class PrepStation extends Station {

	public float progress = 0;

	public PrepStation(Vector2 pos) {
		super(pos, 5, false, null, null);
	}

	/**
	 * Check if the current ingredients are part of a recipe or an ingredient can be
	 * formed to another begin progress on creating it.
	 * 
	 * @return A boolean representing whether the transformation happens.
	 */
	public boolean slotsToRecipe() {
		for (Recipe recipe : Menu.RECIPES.values()) {
			if (recipe.matches(slots)) {
				if (progress == 1) {
//					progress = 0;
					slots.clear();
					slots.add(recipe);
				}
				return true;
			}

		}

		if (ingredientMatch(slots.peek()) != null) {
			if (progress == 1) {
//				progress = 0;
				slots.add(ingredientMatch(slots.pop()));
			}

			return true;
		}

		GameScreen.cook.locked = false;
		slots.peek().slicing = false;

		return false;
	}

	/**
	 * Lock currently interacting cook to station.
	 * 
	 * @return A boolean indicating if the cook was locked.
	 */
	public boolean lockCook() {
		if (!slots.isEmpty() && slotsToRecipe()) {
			if (lockedCook == null) {
				GameScreen.cook.locked = true;
				lockedCook = GameScreen.cook;
			} else {
				lockedCook.locked = true;
			}
			return true;
		}
		if (lockedCook != null) {
			lockedCook.locked = false;
			lockedCook = null;
			progress = 0;
		}

		return false;
	}

	private static ShapeRenderer shapeRenderer = new ShapeRenderer();

	/**
	 * Update and display the progress bar.
	 * 
	 * @param batch
	 * @param delta The amount to update the progress bar by.
	 */
	public void updateProgress(SpriteBatch batch, float delta) {
		if (progress < 1)
			progress += delta;
		else {
			progress = 1;
			slotsToRecipe();
		}
		drawStatusBar(batch);
	}

	private void drawStatusBar(SpriteBatch batch) {
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(pos.x * 64, pos.y * 64 + 64 + 64 / 10, 64, 64 / 8);
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.rect(pos.x * 64, pos.y * 64 + 64 + 64 / 10, progress * 64, 64 / 10);
		shapeRenderer.end();

	}

	/**
	 * Check whether the ingredient can be formed into another.
	 * 
	 * @param toMatch The ingredient to transform.
	 * @return The ingredient that is formed.
	 */
	private Ingredient ingredientMatch(Ingredient toMatch) {
		for (Ingredient ingredient : Menu.INGREDIENT_PREP.keySet()) {
			if (ingredient.equals(toMatch)) {
				return Menu.INGREDIENT_PREP.get(ingredient);
			}
		}

		return null;
	}

}
