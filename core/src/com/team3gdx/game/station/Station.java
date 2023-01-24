package com.team3gdx.game.station;

import java.util.Stack;

import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.food.Ingredient;

public class Station {

	public Ingredient[] allowedIngredients;

	public Vector2 pos;

	public Stack<Ingredient> slots;
	public boolean infinite;

	protected int numberOfSlots;

	public Station(Vector2 pos, int numberOfSlots, boolean infinite, Ingredient[] allowedIngredients) {
		this.pos = pos;
		this.numberOfSlots = numberOfSlots;
		this.infinite = infinite;
		this.allowedIngredients = allowedIngredients;
		slots = new Stack<Ingredient>();
	}

	public boolean place(Ingredient ingredient) {
		System.out.println(isAllowed(ingredient));
		if ((slots.size() < numberOfSlots && isAllowed(ingredient)) || infinite) {
			ingredient.pos = pos;
			slots.push(ingredient);
			return true;
		}

		return false;
	}

	public boolean isAllowed(Ingredient droppedIngredient) {
		if (allowedIngredients == null)
			return true;
		for (Ingredient ingredient : allowedIngredients) {
			if (ingredient.equals(droppedIngredient))
				return true;
		}

		return false;
	}

	public Ingredient take() {
		if (slots.empty())
			return null;
		if (!infinite) {
			return new Ingredient(slots.pop());
		}

		return new Ingredient(slots.peek());
	}

}
