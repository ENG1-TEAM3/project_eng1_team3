package com.team3gdx.game.station;

import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.MainGameClass;
import com.team3gdx.game.entity.Cook;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.screen.GameScreen;

/**
 * Represents a station.
 * 
 */
public class Station {

	private Music interactSound = Gdx.audio.newMusic(Gdx.files.internal("audio/soundFX/chopping.mp3"));;

	/**
	 * A list of allowed ingredients in the station's slots.
	 */
	public Ingredient[] allowedIngredients;

	/**
	 * The (x, y) coordinates of the station.
	 */
	public Vector2 pos;

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

	/* 
	 * if the station can be used or not
	*/
	public Boolean active;

	/**
	 * 
	 * @param pos                The (x, y) coordinates of the station.
	 * @param numberOfSlots      The number of slots on the station.
	 * @param infinite           Indicates if the station has an unlimited supply of
	 *                           ingredients.
	 * @param allowedIngredients A list of allowed ingredients in the station's
	 *                           slots.
	 */
	public Station(Vector2 pos, int numberOfSlots, boolean infinite, Ingredient[] allowedIngredients,
			String soundPath, Boolean active) {
		this.pos = pos;
		this.numberOfSlots = numberOfSlots;
		this.infinite = infinite;
		this.allowedIngredients = allowedIngredients;
		this.active = active;
		slots = new Stack<Ingredient>();
		if (soundPath != null)
			interactSound = Gdx.audio.newMusic(Gdx.files.internal(soundPath));
	}

	/**
	 * Places an ingredient in the station's top slot.
	 * 
	 * @param ingredient The ingredient to be placed.
	 * @return A boolean to indicate if the ingredient was successfully placed.
	 */
	public boolean place(Ingredient ingredient) {
		if ((slots.size() < numberOfSlots && isAllowed(ingredient)) || infinite) {
			ingredient.pos = pos;
			slots.push(ingredient);
			return true;
		}

		return false;
	}

	/**
	 * Check if the ingredient can be placed on the station.
	 * 
	 * @param droppedIngredient The ingredient to check.
	 * @return A boolean to indicate if the ingredient was allowed.
	 */
	public boolean isAllowed(Ingredient droppedIngredient) {
		if (active == false)
			{return false;}
		else if (allowedIngredients == null)
			{return true;}
		for (Ingredient ingredient : allowedIngredients) {
			if (ingredient.equals(droppedIngredient) && slots.size() < numberOfSlots)
				return true;
		}

		return false;
	}

	/**
	 * Take the ingredient from the top slot.
	 * 
	 * @return The ingredient taken if successful, null otherwise.
	 */
	public Ingredient take() {
		if (slots.empty())
			return null;
		interactSound.stop();
		if (active == false)
			{return null;}
		if (!infinite) {
			return new Ingredient(slots.pop());
		}

		return new Ingredient(slots.peek());
	}

	/**
	 * Display text indicating to take the ingredient.
	 * 
	 * @param pos The position to draw at.
	 */
	public void drawTakeText(SpriteBatch batch) {
		if (!slots.empty() && !GameScreen.cook.full()) {
			drawText(batch, "Take [q]", new Vector2(pos.x * 64, pos.y * 64 - 16));
		}

	}

	/**
	 * Display text indicating to drop an item in the station's slot.
	 * 
	 * @param pos The position of the station.
	 */
	public void drawDropText(SpriteBatch batch) {
		if (GameScreen.cook.heldItems.size() > 0 && isAllowed(GameScreen.cook.heldItems.peek())) {
			drawText(batch, "Drop [e]", new Vector2(pos.x * 64, pos.y * 64));
		}
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
		interactSound.setVolume(MainGameClass.gameVolumeScale);
		interactSound.play();
	}
	public void buyBack() {
		if (active == false){
			active = true;
		} 
	}
	public void drawBuyBackText(SpriteBatch batch, float constructionCost){
		if (active == false){
			drawText(batch, "Buy back for " + constructionCost + " money [e]", new Vector2(pos.x * 64, pos.y * 64));
		}
	}
	public boolean active(){
		return active;
	}
}
