package com.team3gdx.game.food;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.MainGameClass;
import com.team3gdx.game.entity.Entity;
import com.team3gdx.game.save.IngredientInfo;
import com.team3gdx.game.screen.GameScreen;
import com.team3gdx.game.screen.GameScreen.STATE;

/**
 * Represents an ingredient.
 * 
 */
public class Ingredient extends Entity {

	/**
	 * Represents internal states of ingredient.
	 */
	public int slices = 0;
	public int idealSlices;
	private float cookedTime = 0;
	private float idealCookedTime;
	private boolean usable = true;
	public Status status = Status.RAW;

	public int getSlices() {
		return slices;
	}

	public int getIdealSlices() {
		return idealSlices;
	}

	public float getCookedTime() {
		return cookedTime;
	}

	public float getIdealCookedTime() {
		return idealCookedTime;
	}

	public boolean isUsable() {
		return usable;
	}

	/**
	 * Represents ongoing states of the ingredient.
	 */
	public boolean cooking = false;
	public boolean slicing = false;
	public boolean flipped = false;
	public boolean rolling = false;

	/**
	 * Name of ingredient to get texture.
	 */
	public String name;

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
		this.texture = new Texture("items/" + name + ".png");
		this.idealSlices = idealSlices;
		this.idealCookedTime = idealCookedTime;

	}

	public Ingredient(IngredientInfo ingredient) {
		this.pos = new Vector2(ingredient.x, ingredient.y);
		this.width = ingredient.width;
		this.height = ingredient.height;
		this.name = ingredient.name;
		this.texture = new Texture("items/"+name+".png");
		this.idealSlices = ingredient.idealSlices;
		this.idealCookedTime = ingredient.idealCookedTime;

		this.slices = ingredient.slices;
		this.cookedTime = ingredient.cookedTime;
		this.usable = ingredient.usable;
		this.status = ingredient.status;
		this.cooking = ingredient.cooking;
		this.slicing = ingredient.slicing;
		this.flipped = ingredient.flipped;
		this.rolling = ingredient.rolling;
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
	public boolean slice(MainGameClass game, float dT) {

		game.shapeRenderer.setProjectionMatrix(game.batch.getProjectionMatrix());
		if (idealSlices < slices) {
			slices++;
			texture = new Texture("items/" + name + "_mushy.png");
		}
		if (dT / width * width <= width) {
			drawStatusBar(game.shapeRenderer, dT / width, 0, 1);
		} else {
			slices++;
			texture = new Texture("items/" + name + "_chopped.png");
			return true;
		}

		game.batch.begin();
		game.font2.draw(game.batch, String.valueOf(slices), pos.x * 64 + 64 + 8, pos.y * 64 + 64 + 16);
		game.batch.end();

		//draw(batch);
		return false;
	}

	/**
	 * Begin process of cooking ingredient and show status.
	 *
	 * @param dT    The amount of time to increment {@link this#cookedTime} by.
	 * @param batch {@link SpriteBatch} to render texture and status.
	 * @return A double representing the current {@link this#cookedTime}.
	 */
	public double cook(float dT, MainGameClass game) {
		game.shapeRenderer.setProjectionMatrix(game.batch.getProjectionMatrix());
		if (!flipped && cookedTime / idealCookedTime * width > idealCookedTime * width * .65f) {
			game.batch.begin();
			game.font2.draw(game.batch, "Flip [f]", pos.x, pos.y);
			game.batch.end();
		}
		if (cookedTime / idealCookedTime * width <= width) {
			if (GameScreen.state1 == STATE.Continue)
				cookedTime += dT;
			drawStatusBar(game.shapeRenderer, cookedTime / idealCookedTime, idealCookedTime * .65f, idealCookedTime * 1.35f);
			if (cookedTime / idealCookedTime * width > idealCookedTime * width * .65f) {
				texture = new Texture("items/" + name + "_cooked.png");
				status = Status.COOKED;
			}
		} else {
			status = Status.BURNED;
			texture = new Texture("items/" + name + "_burned.png");
			usable = false;
		}

		draw(game.batch);
		return cookedTime;
	}

	/**
	 * Draw a status bar.
	 *
	 * @param percentage The current progress of the status bar.
	 * @param optimum    The optimal status to reach (shown by a black bar).
	 */
	private void drawStatusBar(ShapeRenderer shapeRenderer, float percentage, float optimumLower, float optimumUpper) {
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

	public boolean checkUsable(Ingredient I){
		if (I.idealSlices < I.slices){
			return false;
		}
		return usable;
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