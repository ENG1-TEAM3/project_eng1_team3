package com.team3gdx.game.food;

import com.badlogic.gdx.math.Vector2;

public class Ingredients {

	// Onions, tomatoes, lettuce, patty, buns

	public static Ingredient unformedPatty = new Ingredient(null, 32, 32, "unformed_patty", 0, .5f);
	public static Ingredient formedPatty = new Ingredient(null, 32, 32, "patty", 0, .5f);
	public static Ingredient cookedPatty = new Ingredient(null, 32, 32, "patty", 0, .5f);
	static {
		cookedPatty.status = Status.COOKED;
		cookedPatty.flipped = true;
	}
	public static Ingredient burnedPatty = new Ingredient(null, 32, 32, "patty", 0, .5f);
	static {
		burnedPatty.status = Status.BURNED;
		burnedPatty.flipped = true;
	}
	public static Ingredient lettuce = new Ingredient(null, 32, 32, "lettuce", 1, 0);
	public static Ingredient tomato = new Ingredient(null, 32, 32, "tomato", 1, 0);
	public static Ingredient bun = new Ingredient(new Vector2(0, 0), 32, 32, "burger_bun", 0, .5f);
	public static Ingredient onion = new Ingredient(null, 32, 32, "nil", 1, 0);

}
