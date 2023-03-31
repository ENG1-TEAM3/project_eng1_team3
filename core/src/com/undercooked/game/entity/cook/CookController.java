package com.undercooked.game.entity.cook;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.Input.InputController;
import com.undercooked.game.Input.Keys;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.map.Map;
import com.undercooked.game.map.MapCell;
import com.undercooked.game.map.MapManager;
import com.undercooked.game.util.Listener;

/**
 * Responsible for all functions that affect cook adding,
 * updating and removing.
 */
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
    /** The served listener to call when a {@link Cook} serves at a register. */
    Listener<Cook> serveListener;
    Listener<MapCell> interactRegister;

    /**
     * The constructor for the {@link CookController}.
     * @param textureManager {@link TextureManager} : The {@link TextureManager} to get and load
     *                                                {@link com.badlogic.gdx.graphics.Texture}s from
     */
    public CookController(TextureManager textureManager) {
        this.textureManager = textureManager;
        this.cooks = new Array<>();
    }

    // =======================================LOGIC======================================================

    /**
     * Adds an {@link Cook} to the {@link #cooks} {@link Array}.
     * @param cook {@link Cook} : The {@link Cook} to add.
     */
    public void addCook(Cook cook) {
        // If the cook isn't there already, add it
        if (cooks.contains(cook, true)) {
            return;
        }
        cooks.add(cook);
        cook.serveListener = serveListener;
        cook.interactRegisterListener = interactRegister;
    }

    /**
	 * Change selected cook, and update the {@link Cook}s, making the
     * {@link Cook} check for inputs if it's the selected {@link Cook}.
     * @param delta {@code float} : The time since the last frame.
	 */
    public void update(float delta) {
        // Change between cooks if needed
		if (InputController.isKeyJustPressed(Keys.cook_next)) {
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
     * @return {@link Array<Cook>} : An {@link Array} of the {@link Cook}s
     *                      that the {@link CookController} controls.
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

    /**
     * Loads all of the {@link Cook}'s {@link com.badlogic.gdx.graphics.Texture}s.
     * @param textureGroup
     */
    public void load(String textureGroup) {
        // Load all the cooks
        for (Cook cook : cooks) {
            cook.load(textureManager, textureGroup);
        }
    }

    /**
     * Call the {@link Cook#postLoad(TextureManager)} function for all
     * of the {@link Cook}s in the {@link #cooks} {@link Array}.
     * <br>This updates their {@link com.badlogic.gdx.graphics.g2d.Sprite} to use
     * the loaded {@link com.badlogic.gdx.graphics.Texture}.
     */
    public void postLoad() {
        // Load all the cooks' textures
        for (Cook cook : cooks) {
            cook.postLoad(textureManager);
        }
    }

    /**
     * Loads all the possible {@link Cook} {@link com.badlogic.gdx.graphics.Texture}s,
     * if all of them are needed.
     * <br>Needed for the {@link com.undercooked.game.logic.Endless} mode.
     * @param textureGroup
     */
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

    /**
     * Returns the index of the current {@link Cook}.
     * @return {@link Cook} : The {@code int} index of the current {@link Cook}.
     */
    public int getCurrentCookIndex() {
        return currentCook;
    }

    /**
     * Loads all of the {@link Cook}s from the map {@link JsonValue}, onto
     * the {@link Map}
     * @param mapRoot {@link JsonValue} : The {@link Map}'s JSON.
     * @param map {@link Map} : The {@link Map} to add the {@link Cook}s to.
     * @param textureManager {@link TextureManager} : The {@link TextureManager} to load the
     *                                                {@link Cook}'s {@link com.badlogic.gdx.graphics.Texture}s
     *                                                to.
     */
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
            addCook(newCook);
            cookNo = Math.max(1, (cookNo+1) % (COOK_TEXTURES+1));
        }
    }

    /**
     * Update the serve {@link Listener} which is called
     * when a {@link Cook} tries to place down an
     * {@link com.undercooked.game.food.Item} onto a
     * {@link com.undercooked.game.station.Station} that
     * is a register.
     *
     * @param serveListener {@link Listener<Cook>} : The {@link Listener}
     *                                            to use.
     */
    public void setServeListener(Listener<Cook> serveListener) {
        this.serveListener = serveListener;
        // Make sure to update it for all cooks
        for (Cook cook : cooks) {
            cook.serveListener = serveListener;
        }
    }

    public void setInteractRegisterListener(Listener<MapCell> mapCellListener) {
        this.interactRegister = mapCellListener;
        // Make sure to update it for all cooks
        for (Cook cook : cooks) {
            cook.interactRegisterListener = mapCellListener;
        }
    }
}
