package com.undercooked.game.food;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.DefaultJson;
import com.undercooked.game.util.json.JsonFormat;

/**
 * All available ingredient.
 */
public class Items {

	// An ObjectMap of ingredient ids to their textures.
	ObjectMap<String, Item> items;

	public Items() {
		this.items = new ObjectMap<>();
	}

	public Item addItem(String ID, String name, String texturePath, int value) {
		// If the item already exists, return that
		if (items.containsKey(ID)) {
			return items.get(ID);
		}
		Item newItem = new Item(ID, name, texturePath, value);
		System.out.println(String.format("New Item (%s, %s) with texturePath '%s' and value '%d'",
				name, ID, texturePath, value));
		items.put(ID, newItem);
		return newItem;
	}

	public Item addItem(String ID) {
		return addItem(ID, ID, ID + ".png", 0);
	}

	public Item addItemAsset(String assetPath) {
		JsonValue ingredientRoot = FileControl.loadJsonAsset(assetPath, "items");
		if (ingredientRoot == null) {
			return null;
		}
		JsonFormat.formatJson(ingredientRoot, DefaultJson.itemFormat());
		Item newItem = addItem(assetPath,
				ingredientRoot.getString("name"),
				ingredientRoot.getString("texture_path"),
				ingredientRoot.getInt("value"));
		newItem.setSize(ingredientRoot.getFloat("width"), ingredientRoot.getFloat("height"));
		return newItem;
	}

	public Item getItem(String itemID) {
		return items.get(itemID);
	}

	public void load(TextureManager textureManager) {
		// Loop through all ingredients and load their textures
		for (Item item : items.values()) {
			System.out.println(String.format("Loading texture %s for item %s.", item.getTexturePath(), item.name));
			textureManager.loadAsset(Constants.GAME_TEXTURE_ID, item.getTexturePath(), "textures");
		}
	}

	public void postLoad(TextureManager textureManager) {
		// Loop through all ingredients and set their textures
		for (Item item : items.values()) {
			System.out.println(String.format("Giving texture %s to item %s", item.getTexturePath(), item.name));
			item.updateSprite(textureManager.getAsset(item.getTexturePath()));
			// item.updateSprite(textureManager.getAsset("<main>:station/blank.png"));
		}
	}

	public void unload(TextureManager textureManager) {
		// Loop through all the ingredients and unload their textures
		for (Item item : items.values()) {
			textureManager.unloadTexture(item.getTexturePath());
		}
		// Clear the ingredients map, as none of them are loaded now
		items.clear();
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
