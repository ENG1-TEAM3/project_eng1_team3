package com.undercooked.game.food;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.entity.Entity;
import com.undercooked.game.screen.GameScreen;

/**
 * Represents an ingredient.
 * 
 */
public class Ingredient extends Entity {

	/**
	 * Represents internal states of ingredient.
	 */
	public int slices = 0;
	private int idealSlices;
	private float cookedTime = 0;
	private float idealCookedTime;

	public Status status = Status.RAW;

	/**
	 * Represents ongoing states of the ingredient.
	 */
	public boolean cooking = false;
	public boolean slicing = false;
	public boolean flipped = false;

	/**
	 * Name of ingredient to get texture.
	 */
	public String name;

	private static ShapeRenderer shapeRenderer = new ShapeRenderer();

	/**
	 * Sets the appropriate properties.
	 * 
	 * @param pos             The (x, y) coordinates of the ingredient.
	 * @param width           The ingredient's texture width.
	 * @param height          The ingredient's texture height.
	 * @param name            The name of the ingredient and texture.
	 * @param idealSlices     The ideal number of times to slice the ingredient.
	 * @param idealCookedTime The ideal length of time to cook the ingredient.
	 */
	public Ingredient(Vector2 pos, float width, float height, String name, int idealSlices, float idealCookedTime) {
		this.pos = pos;
		this.width = width;
		this.height = height;
		this.name = name;
		this.texture = MainGameClass.assetManager.get("items/" + name + ".png");
		this.idealSlices = idealSlices;
		this.idealCookedTime = idealCookedTime;

	}

	/**
	 * Creates a new instance with identical properties.
	 * 
	 * @param ingredient The ingredient to clone.
	 */
	public Ingredient(Ingredient ingredient) {
		this.pos = ingredient.pos;
		this.width = ingredient.width;
		this.height = ingredient.height;
		this.name = ingredient.name;
		this.texture = ingredient.texture;
		this.idealSlices = ingredient.idealSlices;
		this.idealCookedTime = ingredient.idealCookedTime;
		this.cookedTime = ingredient.cookedTime;
		this.slices = ingredient.slices;
		this.flipped = ingredient.flipped;
		this.status = ingredient.status;
	}

	/**
	 * Changes the {@link this#flipped} to true if possible.
	 * 
	 * @return A boolean representing if the ingredient was successfully flipped.
	 */
	public boolean flip() {
		if (cookedTime / idealCookedTime < idealCookedTime * .65f)
			return false;
		return (flipped = true);
	}

	/**
	 * Begin process of slicing ingredient and show status.
	 * 
	 * @param batch {@link SpriteBatch} to render texture and status.
	 * @param dT    The amount of time to increment by when slicing.
	 * @return A boolean representing if a complete slice has occurred.
	 */
	public boolean slice(SpriteBatch batch, float dT) {

		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

		if (dT / width * width <= width) {
			drawStatusBar(dT / width, 0, 1);
		} else {
			slices++;
			texture = MainGameClass.assetManager.get("items/" + name + "_chopped.png");
			return true;
		}

		batch.begin();
		(new BitmapFont()).draw(batch, String.valueOf(slices), pos.x * 64 + 64 + 8, pos.y * 64 + 64 + 16);
		batch.end();

		draw(batch);
		return false;
	}

	BitmapFont flipText = new BitmapFont();

	/**
	 * Begin process of cooking ingredient and show status.
	 * 
	 * @param dT    The amount of time to increment {@link this#cookedTime} by.
	 * @param batch {@link SpriteBatch} to render texture and status.
	 * @return A double representing the current {@link this#cookedTime}.
	 */
	public double cook(float dT, SpriteBatch batch) {
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		if (!flipped && cookedTime / idealCookedTime * width > idealCookedTime * width * .65f) {
			batch.begin();
			flipText.draw(batch, "Flip [f]", pos.x, pos.y);
			batch.end();
		}
		if (cookedTime / idealCookedTime * width <= width) {
			if (GameScreen.state1 == GameScreen.STATE.Continue)
				cookedTime += dT;
			drawStatusBar(cookedTime / idealCookedTime, idealCookedTime * .65f, idealCookedTime * 1.35f);
			if (cookedTime / idealCookedTime * width > idealCookedTime * width * .65f) {
				texture = MainGameClass.assetManager.get("items/" + name + "_cooked.png");
				status = Status.COOKED;
			}
		} else {
//			status = Status.BURNED;
//			texture = new Texture("items/" + name + "_burned.png");
		}

		draw(batch);
		return cookedTime;
	}

	/**
	 * Draw a status bar.
	 * 
	 * @param percentage The current progress of the status bar.
	 * @param optimumLower    The optimal status to reach (shown by a black bar).
	 * @param optimumUpper
	 */
	private void drawStatusBar(float percentage, float optimumLower, float optimumUpper) {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(pos.x - width / 2, pos.y + height + height / 10, width * 2, height / 4);

		if (percentage * width < optimumLower * width)
			shapeRenderer.setColor(Color.RED);
		else if (percentage * width < optimumUpper * width)
			shapeRenderer.setColor(Color.GREEN);
		else
			shapeRenderer.setColor(Color.RED);

		shapeRenderer.rect(pos.x - width / 2, pos.y + height + height / 10, percentage * width * 2, height / 5);

		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(pos.x - width / 2 + optimumLower * width * 2, pos.y + height + height / 10, height / 10,
				2 * height / 5);
		shapeRenderer.rect(pos.x - width / 2 + optimumUpper * width * 2, pos.y + height + height / 10, height / 10,
				2 * height / 5);
		shapeRenderer.end();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Ingredient))
			return false;

		Ingredient compareTo = (Ingredient) o;

		if (compareTo.name.equals(name) && compareTo.idealSlices >= idealSlices && compareTo.status == status)
			return true;

		return false;
	}

}

enum Status {
	RAW, COOKED, BURNED
}
