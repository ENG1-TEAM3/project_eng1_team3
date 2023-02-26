package com.undercooked.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.assets.MapManager;
import com.undercooked.game.assets.TextureManager;
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

    // =======================================CONSTRUCTOR======================================================
    public CookController(TextureManager textureManager) {
        this.textureManager = textureManager;
        this.cooks = new Array<>();
    }

    // =======================================INITIALISE======================================================
    public void initialiseCooks() {
        cooks.add(new Cook(new Vector2(MapManager.gridToPos(5), MapManager.gridToPos(3)), 1, textureManager));
		cooks.add(new Cook(new Vector2(MapManager.gridToPos(5), MapManager.gridToPos(5)), 2, textureManager));

		currentCook = 0;
    }

    // =======================================LOGIC======================================================
    public void addCook(Cook cook) {
        cooks.add(cook);
    }

    /**
	 * Change selected cook.
	 */
	private void checkCookSwitch() {
		if (Control.tab && Tutorial.complete) {
			getCurrentCook().locked = false;
			currentCookIndex += currentCookIndex < cookController.getCooks().size - 1 ? 1 : -currentCookIndex;
			cookController.getCurrentCook() = cookController.getCooks().get(currentCookIndex);
		}
		if (Control.shift && Tutorial.complete) {
			cookController.getCurrentCook().locked = false;
			currentCookIndex -= currentCookIndex > 0 ? 1 : -cookController.getCooks().size + 1;
			cookController.getCurrentCook() = cookController.getCooks().get(currentCookIndex);
		}

		Control.interact = false;
		Control.drop = false;
		Control.flip = false;
		Control.tab = false;
		Control.shift = false;
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
        return cooks.get(currentCook);
    }

    // =======================================TEXTURES======================================================
    public void load(String textureGroup) {
        textureManager.load(textureGroup, "entities/cook_walk_1.png");
        textureManager.load(textureGroup, "entities/cook_walk_2.png");
    }

    /**
     * Unloads all objects in this class.
     * TODO: Unload textures.
     */
    public void unload() {
        cooks.clear();
    }

}
