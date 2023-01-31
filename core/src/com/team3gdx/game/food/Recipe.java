package com.team3gdx.game.food;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Recipe extends Ingredient {

	public final String name;

	public Map<Ingredient, String> ingredientInstructions;

	private boolean shouldBeOrdered;
	private String initialSteps, finalSteps;

	private Ingredient initialIngredient;

	public float cost;

	public Recipe(String initialSteps, Ingredient initialIngredient, Map<Ingredient, String> ingredientInstructions,
			String finalSteps, String name, boolean shouldBeOrdered, Vector2 pos, float width, float height,
			float cost) {
		super(pos, width, height, name, 0, 0);
		this.initialSteps = initialSteps;
		this.initialIngredient = initialIngredient;
		this.ingredientInstructions = ingredientInstructions;
		this.finalSteps = finalSteps;
		this.name = name;
		this.shouldBeOrdered = shouldBeOrdered;
		this.cost = cost;
	}

	ShapeRenderer shapeRenderer = new ShapeRenderer();

	public void displayRecipe(SpriteBatch batch, Vector2 pos) {
		// Display recipe on side with title (name) and images of ingredients along with
		// steps.
		String completeRecipe = initialSteps + "\n\n";
		int i = 0;
		if (initialIngredient != null) {
			Ingredient initial = new Ingredient(initialIngredient);
			initial.pos = new Vector2(pos);
			initial.pos.x -= 48;
			initial.pos.y += --i * 2 * new BitmapFont().getLineHeight() + new BitmapFont().getLineHeight();
			initial.draw(batch);
		} else {
			i = -1;
		}
		for (Ingredient ingredient : ingredientInstructions.keySet()) {
			completeRecipe += ingredientInstructions.get(ingredient) + " " + ingredient.name + "\n\n";
			ingredient.pos = new Vector2(pos);
			ingredient.pos.x -= 48;
			ingredient.pos.y += --i * 2 * new BitmapFont().getLineHeight() + new BitmapFont().getLineHeight();
			ingredient.draw(batch);

		}

		Ingredient result = new Ingredient(this);
		result.pos = new Vector2(pos);
		result.pos.x -= 48;
		result.pos.y += --i * 2 * new BitmapFont().getLineHeight() + new BitmapFont().getLineHeight();
		result.draw(batch);

		completeRecipe += finalSteps;

		batch.begin();
		(new BitmapFont()).draw(batch, completeRecipe, pos.x - 16, pos.y);
		batch.end();

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
