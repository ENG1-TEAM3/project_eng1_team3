package com.undercooked.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.Input.InputController;
import com.undercooked.game.Input.Keys;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.map.Map;
import com.undercooked.game.map.MapManager;
import com.undercooked.game.util.Control;

/** Responsible for all functions cook related. */
public class CookController {

    // =======================================CookController ATTRIBUTES======================================================
    /** The array containing all the cooks in the game. */
    Array<Cook> cooks;
    /** The index of the current cook. */
	int currentCook = 0;
    /** Manages the textures for the cooks. */
    TextureManager textureManager;
    /** The number of cook textures */
    static final int COOK_TEXTURES = 3;

    // =======================================CONSTRUCTOR======================================================
    public CookController(TextureManager textureManager) {
        this.textureManager = textureManager;
        this.cooks = new Array<>();
    }

    // =======================================INITIALISE======================================================
    public void initialiseCooks() {
        //cooks.add(new Cook(new Vector2(MapManager.gridToPos(5), MapManager.gridToPos(3)), 1, textureManager));
		//cooks.add(new Cook(new Vector2(MapManager.gridToPos(5), MapManager.gridToPos(5)), 2, textureManager));

		currentCook = 0;
    }

    // =======================================LOGIC======================================================
    public void addCook(Cook cook) {
        // If the cook isn't there already, add it
        if (cooks.contains(cook, true)) {
            return;
        }
        cooks.add(cook);
    }

    /**
	 * Change selected cook.
	 */
    public void update(float delta) {
        // Change between cooks if needed
		if (InputController.isKeyJustPressed(Keys.cook_next)) {
			getCurrentCook().locked = false;
            currentCook = (currentCook + 1) % cooks.size;
            currentCook = Math.max(currentCook, 0);
		}
		if (InputController.isKeyJustPressed(Keys.cook_prev)) {
            currentCook = currentCook - 1;
            currentCook = Math.min(currentCook, cooks.size-1);
            if (currentCook < 0) {
                currentCook += cooks.size;
            }
		}

        // If there are cooks...
        if (cooks.size > 0) {
            // Get the current cook,
            Cook cCook = cooks.get(currentCook);
            // and if it's not null
            if (cCook != null) {
                // then check inputs for it.
                cCook.checkInput(delta);
            }
        }

        // Update all the Cooks
        for (Cook cook : cooks) {
            cook.update(delta);
        }
	}

    /**
     * Function to stop all {@link Cook} movement for when the
     * {@link com.undercooked.game.screen.GameScreen} pauses.
     */
    public void stopMovement() {
        for (Cook cook : cooks) {
            cook.dirX = 0;
            cook.dirY = 0;
        }
    }

    // =======================================GETTERS======================================================
    /**
     * Cooks array getter. Get the cooks array.
     * @return Array<Cook>
     */
    public final Array<Cook> getCooks() {
        return cooks;
    }

    /**
     * Current cook getter. Get the current cook.
     * @return Cook
     */
    public final Cook getCurrentCook() {
        if (cooks.size == 0) {
            return null;
        }
        return cooks.get(currentCook);
    }

    // =======================================TEXTURES======================================================
    public void load(String textureGroup) {
        // Load all the cooks
        for (Cook cook : cooks) {
            cook.load(textureManager, textureGroup);
        }
    }

    public void postLoad() {
        // Load all the cooks' textures
        for (Cook cook : cooks) {
            cook.postLoad(textureManager);
        }
    }

    public void loadAll(String textureGroup) {
        // Load all the Cook textures
        for (int i = 1 ; i <= COOK_TEXTURES ; i++) {
            textureManager.load(textureGroup, "entities/cook_walk_" + i + ".png");
            textureManager.load(textureGroup, "entities/cook_walk_hands_" + i + ".png");
        }
    }

    /**
     * Unloads all objects in this class.
     * TODO: Unload textures.
     */
    public void unload() {
        cooks.clear();
    }

    public int getCurrentCookIndex() {
        return currentCook;
    }

    public void loadCooksIntoMap(JsonValue mapRoot, Map map, TextureManager textureManager) {
        JsonValue cookArray = mapRoot.get("cooks");
        currentCook = 0;
        // System.out.println(cookArray);
        int cookNo = 1;
        // Iterate through the cooks and add them
        for (JsonValue cookData : cookArray.iterator()) {
            // System.out.println(cookData);
            Vector2 cookPos = new Vector2(MapManager.gridToPos(cookData.getFloat("x")+map.getOffsetX()), MapManager.gridToPos(cookData.getFloat("y")+map.getOffsetY()));
            Cook newCook = new Cook(cookPos, cookNo, textureManager, map);
            cooks.add(newCook);
            cookNo = Math.max(1, (cookNo+1) % (COOK_TEXTURES+1));
        }
    }
}
