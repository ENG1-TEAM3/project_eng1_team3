package com.undercooked.game.food;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.util.Constants;

/**
 * All available ingredient.
 */
public class Ingredients {

	// An ObjectMap of ingredient ids to their textures.
	ObjectMap<String, String> ingredients;

	public void addIngredient(String ID, String texturePath) {
		ingredients.put(ID, texturePath);
	}

	public void addIngredient(String ID) {
		addIngredient(ID, ID + ".png");
	}

	public boolean addIngredientAsset(String assetPath) {
		JsonValue ingredientRoot = FileControl.loadJsonAsset(assetPath, "items");
		if (ingredientRoot != null) {
			return false;
		}
		return true;
	}

	public void load(TextureManager textureManager) {
		// Loop through all ingredients and load their textures
		for (String path : ingredients.values()) {
			textureManager.load(Constants.GAME_TEXTURE_ID, path);
		}
	}

	public void unload(TextureManager textureManager) {
		// Loop through all the ingredients and unload their textures
		for (String path : ingredients.values()) {
			textureManager.load(path);
		}
		// Clear the ingredients map, as none of them are loaded now
		ingredients.clear();
	}

	/** TEMP. Final will use the functions above. */
	/*public static void setupIngredients(GameScreen game) {
		// Meats
		unformedPatty = new Ingredient(null, 32, 32, "unformed_patty", 0, .5f, game);
		formedPatty = new Ingredient(null, 32, 32, "patty", 0, .5f, game);

		// Cooked Meats
		cookedPatty = new Ingredient(null, 32, 32, "patty", 0, .5f, game);
		cookedPatty.status = Status.COOKED;
		cookedPatty.flipped = true;
		burnedPatty = new Ingredient(null, 32, 32, "patty", 0, .5f, game);
		burnedPatty.status = Status.BURNED;
		burnedPatty.flipped = true;

		// Vegetables
		lettuce = new Ingredient(null, 32, 32, "lettuce", 1, 0, game);
		tomato = new Ingredient(null, 32, 32, "tomato", 1, 0, game);
		onion = new Ingredient(null, 32, 32, "onion", 1, 0, game);
		lettuceChopped = new Ingredient(null, 32, 32, "lettuce", 1, 0, game);
		lettuceChopped.slices = 1;
		tomatoChopped = new Ingredient(null, 32, 32, "tomato", 1, 0, game);
		tomatoChopped.slices = 1;
		onionChopped = new Ingredient(null, 32, 32, "onion", 1, 0, game);
		onionChopped.slices = 1;

		// Breads
		bun = new Ingredient(new Vector2(0, 0), 32, 32, "burger_bun", 0, .5f, game);
		cooked_bun = new Ingredient(new Vector2(0, 0), 32, 32, "burger_bun", 0, .5f, game);
		cooked_bun.status = Status.COOKED;
		cooked_bun.flipped = true;
	}

	// Meats.
	public static Ingredient unformedPatty;
	public static Ingredient formedPatty;
	public static Ingredient cookedPatty;
	// Cooked Meats.
	public static Ingredient burnedPatty;

	// Vegetables.
	public static Ingredient lettuce;
	public static Ingredient tomato;
	public static Ingredient onion;
	// Chopped vegetables.
	public static Ingredient lettuceChopped;
	public static Ingredient tomatoChopped;
	public static Ingredient onionChopped;

	// Breads.
	public static Ingredient bun;
	// Toasted breads.
	public static Ingredient cooked_bun;*/

}