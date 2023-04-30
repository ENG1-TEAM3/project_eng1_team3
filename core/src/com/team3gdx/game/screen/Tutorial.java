package com.team3gdx.game.screen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.team3gdx.game.entity.Cook;

/**
 * The tutorial of the game. Includes instructions on controls and information
 * on stations.
 * 
 */
public class Tutorial {

	public Tutorial() {
		bitmapFont.setColor(Color.BLACK);
		bitmapFont.getData().setScale(2);
		layout.setText(bitmapFont, " [tab] to skip!");
	}

	/**
	 * A list of sections / pages for the tutorial with the position to jump the
	 * camera to.
	 */
	private final Array<PosTextPair> stages = new Array<>();

	public void start(Array<Cook> cooks) {
		stages.add(new PosTextPair(new Vector2(64, 64),
				" Welcome to Piazza Panic! Customers will arrive one-by-one requesting an order. "));
		stages.add(new PosTextPair(new Vector2(3 * 64, 13 * 64),
				" To display the order, go up to the service station opposite the customer. "));
		stages.add(new PosTextPair(cooks.get(0).pos,
				" Control the cooks (using WASD) in the kitchen to gather ingredients. "));
		stages.add(new PosTextPair(cooks.get(1).pos,
				" Switch between cooks using tab and shift to go to and fro respectively."));
		stages.add(new PosTextPair(new Vector2(10 * 64, 11 * 64),
				" Move to different stations: [Ingredient Station] to collect ingredients (e to pickup, q to drop), "));
		stages.add(new PosTextPair(new Vector2(7 * 64, 11 * 64),
				" [Frying Station] to fry patties and other ingredients (f to flip when ready), "));
		stages.add(new PosTextPair(new Vector2(6 * 64, 5 * 64), " [Baking station] to bake bread buns, "));
		stages.add(new PosTextPair(new Vector2(9 * 64, 8 * 64), " [Cutting Station] to cut ingredients,"));
		stages.add(new PosTextPair(new Vector2(6 * 64, 8 * 64),
				" [Preparation station] to form patties and prepare the order... "));
		stages.add(new PosTextPair(new Vector2(3 * 64, 9 * 64), " to then serve the customer in the shortest time. "));
		stages.add(new PosTextPair(GameScreen.cook.pos, " Goodluck! "));

		for (PosTextPair stage : stages) {
			stage.text = " " + stage.text + " ";
		}
	}

	/**
	 * Represents if the tutorial has been finished.
	 */
	public boolean complete = false;
	/**
	 * Represents the current stage of the tutorial.
	 */
	public int stage = 0;

	private final ShapeRenderer shapeRenderer = new ShapeRenderer();
	private final BitmapFont bitmapFont = new BitmapFont();
	private final GlyphLayout layout = new GlyphLayout();

	/**
	 * A timer used for text typing animation.
	 */
	private float nextCharTimer = 0;
	/**
	 * The currently shown text.
	 */
	private String curText;

	/**
	 * Draws the tutorial's text and white backdrop.
	 * 
	 * @param batch {@link SpriteBatch} to render the text and ingredient textures.
	 * @param dT    The amount of time to increment by between each character.
	 */
	public void drawBox(SpriteBatch batch, float dT) {
		curText = stages.get(stage).text.substring(0, Math.round(nextCharTimer));
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(Gdx.graphics.getWidth() / 10f, 1 * Gdx.graphics.getHeight() / 10f,
				4 * Gdx.graphics.getWidth() / 5f, Gdx.graphics.getHeight() / 5f);
		shapeRenderer.end();
		shapeRenderer.setProjectionMatrix(GameScreen.worldCamera.combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.rect(getStagePos().x, getStagePos().y, 64, 64);
		shapeRenderer.end();
		batch.begin();
		bitmapFont.draw(batch, curText, Gdx.graphics.getWidth() / 10f,
				Gdx.graphics.getHeight() / 10f + Gdx.graphics.getHeight() / 5f - bitmapFont.getCapHeight() / 2);
		bitmapFont.draw(batch, "[tab] to skip!",
				Gdx.graphics.getWidth() / 10f + 4f * Gdx.graphics.getWidth() / 5f - layout.width,
				Gdx.graphics.getHeight() / 10f + bitmapFont.getCapHeight() * 1.5f);
		batch.end();
		if (nextCharTimer < stages.get(stage).text.length()) {
			if (curText.length() > 0 && ".,!?".contains(curText.substring(curText.length() - 1))) {
				if (addDelay(10, dT)) {
					delay = 0;
					nextCharTimer += 1;
				}
			}
			if (delay == 0) {
				nextCharTimer += dT;
			}
		}
	}

	/**
	 * 
	 * @return The coordinates of the current stage's position.
	 */
	public Vector2 getStagePos() {
		return stages.get(stage).pos;
	}

	/**
	 * Skip to the next stage of the tutorial if possible.
	 */
	public void nextStage() {
		delay = 0;
		if (nextCharTimer < stages.get(stage).text.length()) {
			nextCharTimer = stages.get(stage).text.length();
			return;
		}

		nextCharTimer = 0;

		if (stage < stages.size - 1)
			stage++;
		else
			complete = true;
	}

	/**
	 * Go back to the previous tutorial stage.
	 * 
	 */
	public void previousStage() {
		nextCharTimer = 0;
		if (stage > 0)
			stage--;
	}

	float delay = 0;

	/**
	 * Add a delay (used between punctuation).
	 * 
	 * @param amount How long the delay is.
	 * @param dT     How much to increment the delay by.
	 * @return A boolean to indicate if the delay has finished.
	 */
	private boolean addDelay(float amount, float dT) {
		if (delay < amount)
			delay += dT;
		else
			return true;
		return false;
	}
}

/**
 * Used to store a position and string.
 */
class PosTextPair {

	public Vector2 pos;
	public String text;

	public PosTextPair(Vector2 pos, String text) {
		this.pos = pos;
		this.text = text;
	}

}
