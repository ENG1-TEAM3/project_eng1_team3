package com.undercooked.game.station;

import java.util.Stack;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.Cook;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.food.Ingredient;
import com.undercooked.game.map.MapEntity;
import com.undercooked.game.util.Constants;

import static com.undercooked.game.MainGameClass.shapeRenderer;

/**
 * Represents a station.
 * 
 */
public class Station extends MapEntity {

	StationData stationData;

	private Music interactSound;// = Gdx.audio.newMusic(Gdx.files.internal("audio/soundFX/chopping.mp3"));

	/**
	 * A list of allowed ingredients in the station's slots.
	 */
	public Ingredient[] allowedIngredients;

	/*
	 * The station's slots to hold ingredients.
	 */
	public Stack<Ingredient> slots;
	/*
	 * Indicates if the station has an unlimited supply of ingredients.
	 */
	public boolean infinite;

	/*
	 * The number of slots on the station.
	 */
	protected int numberOfSlots;

	/*
	 * The cook that is currently locked to the station.
	 */
	public Cook lockedCook;

	private String soundPath;
	private AudioManager audioManager;

	public Station(StationData stationData) {
		super();
		this.stationData = stationData;
	}

	public void update(float delta) {
	}

	public void create() {
		interactSound = audioManager.getMusic(soundPath);
	}

	public void draw(SpriteBatch batch) {
		sprite.setPosition(pos.x,pos.y);
		sprite.setSize(width*64, height*64);
		sprite.draw(MainGameClass.batch);
	}

	public void drawStatusBar(SpriteBatch batch) {
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(pos.x * 64, pos.y * 64 + 64 + 64 / 10, 64, 64 / 8);
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.rect(pos.x * 64, pos.y * 64 + 64 + 64 / 10, 64, 64 / 10);
		shapeRenderer.end();

	}

	/**
	 * 
	 * @param text Text to be drawn.
	 * @param pos  Position to draw at.
	 */
	public void drawText(SpriteBatch batch, String text, Vector2 pos) {
		batch.begin();
		(new BitmapFont()).draw(batch, text, pos.x, pos.y);
		batch.end();
	}

	public void interactSound() {
		if (interactSound != null) {
			interactSound.play();
		}
	}

	/**
	 * Places an ingredient in the station's top slot.
	 *
	 * @param ingredient The ingredient to be placed.
	 * @return A boolean to indicate if the ingredient was successfully placed.
	 */
	/*public boolean place(Ingredient ingredient) {
		if ((slots.size() < numberOfSlots && isAllowed(ingredient)) || infinite) {
			ingredient.pos = pos;
			slots.push(ingredient);
			return true;
		}

		return false;
	}*/

	/**
	 * Check if the ingredient can be placed on the station.
	 *
	 * @param droppedIngredient The ingredient to check.
	 * @return A boolean to indicate if the ingredient was allowed.
	 */
	/*public boolean isAllowed(Ingredient droppedIngredient) {
		if (allowedIngredients == null)
			return true;
		for (Ingredient ingredient : allowedIngredients) {
			if (ingredient.equals(droppedIngredient) && slots.size() < numberOfSlots)
				return true;
		}

		return false;
	}*/

	/**
	 * Take the ingredient from the top slot.
	 *
	 * @return The ingredient taken if successful, null otherwise.
	 */
	/*public Ingredient take() {
		if (slots.empty())
			return null;
		interactSound.stop();
		if (!infinite) {
			return new Ingredient(slots.pop());
		}

		return new Ingredient(slots.peek());
	}*/

	/**
	 * Display text indicating to take the ingredient.
	 *
	 * @param batch The {@link SpriteBatch} that will draw.
	 */
	/*public void drawTakeText(SpriteBatch batch) {
		if (!slots.empty() && !GameScreen.cook.full()) {
			drawText(batch, "Take [q]", new Vector2(pos.x * 64, pos.y * 64 - 16));
		}

	}*/

	/**
	 * Display text indicating to drop an item in the station's slot.
	 *
	 * @param batch The {@link SpriteBatch} that will draw.
	 */
	/*public void drawDropText(SpriteBatch batch) {
		if (GameScreen.cook.heldItems.size() > 0 && isAllowed(GameScreen.cook.heldItems.peek())) {
			drawText(batch, "Drop [e]", new Vector2(pos.x * 64, pos.y * 64));
		}
	}*/

}
