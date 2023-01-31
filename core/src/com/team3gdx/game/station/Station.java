package com.team3gdx.game.station;

import java.util.Stack;

import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.entity.Cook;
import com.team3gdx.game.food.Ingredient;

/**
 * Represents a station.
 * 
 */
public class Station {

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

	/**
	 * 
	 * @param pos                The (x, y) coordinates of the station.
	 * @param numberOfSlots      The number of slots on the station.
	 * @param infinite           Indicates if the station has an unlimited supply of
	 *                           ingredients.
	 * @param allowedIngredients A list of allowed ingredients in the station's
	 *                           slots.
	 */
	public Station(Vector2 pos, int numberOfSlots, boolean infinite, Ingredient[] allowedIngredients) {
		this.pos = pos;
		this.numberOfSlots = numberOfSlots;
		this.infinite = infinite;
		this.allowedIngredients = allowedIngredients;
		slots = new Stack<Ingredient>();
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
		if (allowedIngredients == null)
			return true;
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
		if (!infinite) {
			return new Ingredient(slots.pop());
		}

		return new Ingredient(slots.peek());
	}

}
