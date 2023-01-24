package com.team3gdx.game.food;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.entity.Entity;

public class Recipe extends Ingredient {

	public final String name;

	public Map<Ingredient, String> ingredientInstructions;

	private boolean shouldBeOrdered;
	private String initialSteps, finalSteps;

	public Recipe(String initialSteps, Map<Ingredient, String> ingredientInstructions, String finalSteps, String name,
			boolean shouldBeOrdered, Vector2 pos, float width, float height) {
		super(pos, width, height, name, 0, 0);
		this.initialSteps = initialSteps;
		this.ingredientInstructions = ingredientInstructions;
		this.finalSteps = finalSteps;
		this.name = name;
		this.shouldBeOrdered = shouldBeOrdered;
	}

	public void displayRecipe(SpriteBatch batch) {
		// Display recipe on side with title (name) and images of ingredients along with
		// steps.
		String completeRecipe = initialSteps + "\n";
		for (Ingredient ingredient : ingredientInstructions.keySet()) {
			completeRecipe += ingredientInstructions.get(ingredient) + " " + ingredient.name + "\n";
		}
		completeRecipe += finalSteps;
		System.out.println(completeRecipe);
	}

	public boolean matches(Stack<Ingredient> givenIngredients) {
		ArrayList<Ingredient> toCheck = new ArrayList<Ingredient>(ingredientInstructions.keySet());
		if (givenIngredients.size() != toCheck.size())
			return false;
		if (shouldBeOrdered) {
			for (int i = 0; i < toCheck.size(); i++)
				if (!toCheck.get(i).equals(givenIngredients.get(i)))
					return false;
			return true;
		}

		for (Ingredient ingredient : givenIngredients) {
			if (contains(ingredient, toCheck)) {
				toCheck.remove(ingredient);
			}
		}
		if (toCheck.isEmpty())
			return true;

		return false;
	}

	private boolean contains(Ingredient checkIngredient, ArrayList<Ingredient> ingredients) {
		for (Ingredient ingredient : ingredientInstructions.keySet()) {
			if (ingredient.equals(checkIngredient)) {
				return true;
			}
		}

		return false;
	}

}
