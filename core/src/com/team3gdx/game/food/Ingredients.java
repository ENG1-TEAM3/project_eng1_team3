package com.team3gdx.game.food;

import com.badlogic.gdx.math.Vector2;

/**
 * All available ingredient.
 * 
 */
public class Ingredients {

	// Meats.
	public static Ingredient unformedPatty = new Ingredient(null, 32, 32, "unformed_patty", 0, .5f);
	public static Ingredient formedPatty = new Ingredient(null, 32, 32, "patty", 0, .5f);
	public static Ingredient cookedPatty = new Ingredient(null, 32, 32, "patty", 0, .5f);
	// Cooked meats.
	static {
		cookedPatty.status = Status.COOKED;
		cookedPatty.flipped = true;
	}
	public static Ingredient burnedPatty = new Ingredient(null, 32, 32, "patty", 0, .5f);
	static {
		burnedPatty.status = Status.BURNED;
		burnedPatty.flipped = true;
	}
	//Dairy
	public static Ingredient cheese = new Ingredient(null, 32, 32, "Cheese", 1, 0);
	//Chopped Dairy
	public static Ingredient cheeseChopped = new Ingredient(null, 32, 32, "Cheese", 1, 0);
	static {
		cheeseChopped.slices = 1;
	}
	// Vegetables.
	public static Ingredient lettuce = new Ingredient(null, 32, 32, "lettuce", 1, 0);
	public static Ingredient tomato = new Ingredient(null, 32, 32, "tomato", 1, 0);
	public static Ingredient onion = new Ingredient(null, 32, 32, "onion", 1, 0);
	public static Ingredient potato = new Ingredient(null, 32, 32, "Potato", 1, .5f);
	// Chopped vegetables.
	public static Ingredient lettuceChopped = new Ingredient(null, 32, 32, "lettuce", 1, 0);
	static {
		lettuceChopped.slices = 1;
	}
	public static Ingredient tomatoChopped = new Ingredient(null, 32, 32, "tomato", 1, 0);
	static {
		tomatoChopped.slices = 1;
	}
	public static Ingredient onionChopped = new Ingredient(null, 32, 32, "onion", 1, 0);
	static {
		onionChopped.slices = 1;
	}
	public static Ingredient potatoChopped = new Ingredient(null, 32, 32, "Potato", 1, .5f);
	static {
		potatoChopped.slices = 1;
	}
	// Cooked vegtables.
	public static Ingredient cookedPotato = new Ingredient(null, 32, 32, "Potato", 1, .5f);
	static {
		cookedPotato.status = Status.COOKED;
		cookedPotato.flipped = true;
	}
	public static Ingredient burnedPotato = new Ingredient(null, 32, 32, "Potato", 1, .5f);
	static {
		burnedPotato.status = Status.BURNED;
		burnedPotato.flipped = true;
	}
	//sauce vegtables.
	public static Ingredient tomato_sauce = new Ingredient(null, 32, 32, "Tomato_sauce", 0, 0);
	// Breads.
	public static Ingredient bun = new Ingredient(new Vector2(0, 0), 32, 32, "burger_bun", 0, .5f);
	public static Ingredient unformedDough = new Ingredient(null, 32, 32, "unformed_dough", 0, .5f);
	public static Ingredient formedDough = new Ingredient(null, 32, 32, "Dough", 0, .5f);
	public static Ingredient raw_pizza = new Ingredient(null, 32, 32, "Dough", 0, .5f);
	// Toasted breads.
	public static Ingredient cooked_bun = new Ingredient(new Vector2(0, 0), 32, 32, "burger_bun", 0, .5f);
	static {
		cooked_bun.status = Status.COOKED;
		cooked_bun.flipped = true;
	}
	public static Ingredient cooked_raw_pizza = new Ingredient(null, 32, 32, "Dough", 0, .5f);
	static {
		cooked_raw_pizza.status = Status.COOKED;
	}

}
