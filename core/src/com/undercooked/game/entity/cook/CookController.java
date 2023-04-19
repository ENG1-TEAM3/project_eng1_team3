package com.undercooked.game.entity.cook;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.Input.InputController;
import com.undercooked.game.Input.Keys;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.map.Map;
import com.undercooked.game.map.MapCell;
import com.undercooked.game.map.MapManager;
import com.undercooked.game.util.Listener;
import com.undercooked.game.util.Observer;

/**
 * Responsible for all functions that affect cook adding,
 * updating and removing.
 */
public class CookController {

    private class CookData {
        public Cook cook;
        public Vector2 startPos;
    }

    // =======================================CookController
    // ATTRIBUTES======================================================
    /** The array containing all the cooks in the game. */
    Array<Cook> cooks;
    /** An ObjectMap mapping a Cook to their start position. */
    ObjectMap<Cook, Vector2> cookStart;
    /** The index of the current cook. */
    int currentCook = 0;
    /** Manages the textures for the cooks. */
    TextureManager textureManager;
    /** The number of cook textures */
    static final int COOK_TEXTURES = 3;
    /** The served listener to call when a {@link Cook} serves at a register. */
    private Listener<Cook> serveListener;
    private Listener<MapCell> interactRegister;
    private Listener<MapCell> interactPhone;
    private Listener<Integer> moneyUsedListener;
    /** An {@link Observer} to find what the money is. */
    private Observer<Integer> moneyObserver;

    /**
     * The constructor for the {@link CookController}.
     * 
     * @param textureManager {@link TextureManager} : The {@link TextureManager} to
     *                       get and load
     *                       {@link com.badlogic.gdx.graphics.Texture}s from
     */
    public CookController(TextureManager textureManager) {
        this.textureManager = textureManager;
        this.cooks = new Array<>();
        this.cookStart = new ObjectMap<>();
    }

    // =======================================LOGIC======================================================

    /**
     * Adds an {@link Cook} to the {@link #cooks} {@link Array}.
     * 
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
        cook.interactPhoneListener = interactPhone;
        cook.moneyUsedListener = moneyUsedListener;
        cook.moneyObserver = moneyObserver;
    }

    /**
     * Change selected cook, and update the {@link Cook}s, making the
     * {@link Cook} check for inputs if it's the selected {@link Cook}.
     * 
     * @param delta {@code float} : The time since the last frame.
     */
    public void update(float delta) {

        // If there are cooks...
        if (cooks.size > 0) {
            // Change between cooks if needed
            if (InputController.isKeyJustPressed(Keys.cook_next)) {
                currentCook = (currentCook + 1) % cooks.size;
                currentCook = Math.max(currentCook, 0);
            }
            if (InputController.isKeyJustPressed(Keys.cook_prev)) {
                currentCook = currentCook - 1;
                currentCook = Math.min(currentCook, cooks.size - 1);
                if (currentCook < 0) {
                    currentCook += cooks.size;
                }
            }

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
     * 
     * @return {@link Array<Cook>} : An {@link Array} of the {@link Cook}s
     *         that the {@link CookController} controls.
     */
    public final Array<Cook> getCooks() {
        return cooks;
    }

    /**
     * Current cook getter. Get the current cook.
     * 
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
     * 
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
     * <br>
     * This updates their {@link com.badlogic.gdx.graphics.g2d.Sprite} to use
     * the loaded {@link com.badlogic.gdx.graphics.Texture}.
     */
    public void postLoad() {
        // Load all the cooks' textures
        for (Cook cook : cooks) {
            cook.postLoad(textureManager);
        }
    }

    /**
     * Loads all the possible {@link Cook}
     * {@link com.badlogic.gdx.graphics.Texture}s,
     * if all of them are needed.
     * <br>
     * Needed for the {@link com.undercooked.game.logic.Endless} mode.
     * 
     * @param textureGroup
     */
    public void loadAll(String textureGroup) {
        // Load all the Cook textures
        for (int i = 1; i <= COOK_TEXTURES; i++) {
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
     * 
     * @return {@link Cook} : The {@code int} index of the current {@link Cook}.
     */
    public int getCurrentCookIndex() {
        return currentCook;
    }

    /**
     * Loads all of the {@link Cook}s from the map {@link JsonValue}, onto
     * the {@link Map}
     * 
     * @param mapRoot        {@link JsonValue} : The {@link Map}'s JSON.
     * @param map            {@link Map} : The {@link Map} to add the {@link Cook}s
     *                       to.
     * @param textureManager {@link TextureManager} : The {@link TextureManager} to
     *                       load the
     *                       {@link Cook}'s
     *                       {@link com.badlogic.gdx.graphics.Texture}s
     *                       to.
     */
    public void loadCooksIntoMap(JsonValue mapRoot, Map map, TextureManager textureManager) {
        // Clear the startCooks array
        cookStart.clear();

        // Get the cooks Json data
        JsonValue cookArray = mapRoot.get("cooks");
        currentCook = 0;
        int cookNo = 1;
        // Iterate through the cooks and add them
        for (JsonValue cookData : cookArray.iterator()) {
            Vector2 cookPos = new Vector2(MapManager.gridToPos(cookData.getFloat("x") + map.getOffsetX()),
                    MapManager.gridToPos(cookData.getFloat("y") + map.getOffsetY()));
            Cook newCook = new Cook(cookPos, cookNo, textureManager, map);
            addCook(newCook);
            cookNo = Math.max(1, (cookNo + 1) % (COOK_TEXTURES + 1));

            cookStart.put(newCook, new Vector2(cookPos));
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
     *                      to use.
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

    public void setInteractPhoneListener(Listener<MapCell> mapCellListener) {
        this.interactPhone = mapCellListener;
        // Make sure to update it for all cooks
        for (Cook cook : cooks) {
            cook.interactPhoneListener = mapCellListener;
        }
    }

    public void setMoneyUsedListener(Listener<Integer> moneyUsedListener) {
        this.moneyUsedListener = moneyUsedListener;
        // Make sure to update it for all cooks
        for (Cook cook : cooks) {
            cook.moneyUsedListener = moneyUsedListener;
        }
    }

    public void setMoneyObserver(Observer<Integer> moneyObserver) {
        this.moneyObserver = moneyObserver;
        // Make sure to update it for all cooks
        for (Cook cook : cooks) {
            cook.moneyObserver = moneyObserver;
        }
    }

    public void reset() {
        // For each Cook, check if it has a start position
        for (int i = cooks.size - 1; i >= 0; i--) {
            Cook thisCook = cooks.get(i);
            if (!cookStart.containsKey(thisCook)) {
                // If it doesn't, then remove it
                cooks.removeIndex(i);
                continue;
            }
            // If it does, then set the Cook's position
            Vector2 cookPos = cookStart.get(thisCook);
            thisCook.setX(cookPos.x);
            thisCook.setY(cookPos.y);

            // And clear the Cook's ItemStack
            thisCook.clear();
        }

        // Reset currentCook to 0
        currentCook = 0;
    }

    public void dispose() {
        // Clear all data
        cooks.clear();
        cookStart.clear();
    }

    /**
     * Serialises the {@link Cook}s to a {@link JsonValue}.
     */
    public JsonValue serializeCooks() {
        // Create the cooks JsonValue
        JsonValue cooksArrayRoot = new JsonValue(JsonValue.ValueType.array);

        // For each Cook, add it to the cooks JsonValue
        for (Cook cook : cooks) {
            cooksArrayRoot.addChild(cook.serial());
        }

        // JsonValue cooksRoot = new JsonValue(JsonValue.ValueType.object);
        // // Add the cooks JsonValue to the root JsonValue
        // cooksRoot.addChild("cooks", cooksRoot);

        // return cooksRoot;
        return cooksArrayRoot;
    }
}
