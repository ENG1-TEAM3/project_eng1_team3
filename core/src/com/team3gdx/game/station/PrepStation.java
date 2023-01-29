package com.team3gdx.game.station;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.MainGameClass;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Menu;
import com.team3gdx.game.food.Recipe;
import com.team3gdx.game.screen.GameScreen;

public class PrepStation extends Station {

	public float progress = 0;

	public PrepStation(Vector2 pos) {
		super(pos, 5, false, null);
	}

	public boolean slotsToRecipe() {
		for (Recipe recipe : Menu.RECIPES.values()) {
			if (recipe.matches(slots)) {
				if (progress == 1) {
					progress = 0;
					slots.clear();
					slots.add(recipe);
				}
				return true;
			}

		}

		if (ingredientMatch(slots.peek()) != null) {
			if (progress == 1) {
				progress = 0;
				slots.add(ingredientMatch(slots.pop()));
			}

			return true;
		}

		if (slots.peek().slices < slots.peek().idealSlices) {
			if (progress == 1) {
				progress = 0;
				slots.peek().slice();
			}

			return true;
		}

		GameScreen.cook.locked = false;
		slots.peek().slicing = false;

		return false;
	}

	private static ShapeRenderer shapeRenderer = new ShapeRenderer();

	public void updateProgress(float delta) {
		if (progress < 1)
			progress += delta;
		else {
			progress = 1;
//			slots.peek().slicing = false;
			slotsToRecipe();
		}
		drawStatusBar();
	}

	private void drawStatusBar() {
		shapeRenderer.setProjectionMatrix(MainGameClass.batch.getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(pos.x * 64, pos.y * 64 + 64 + 64 / 10, 64, 64 / 8);
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.rect(pos.x * 64, pos.y * 64 + 64 + 64 / 10, progress * 64, 64 / 10);
		shapeRenderer.end();
		if (slots.peek().slicing) {
			MainGameClass.batch.begin();
			(new BitmapFont()).draw(MainGameClass.batch, String.valueOf(slots.peek().slices), pos.x * 64 + 64 + 8,
					pos.y * 64 + 64 + 16);
			MainGameClass.batch.end();
		}

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
