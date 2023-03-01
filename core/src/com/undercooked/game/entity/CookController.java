package com.undercooked.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.Input.InputController;
import com.undercooked.game.Input.Keys;
import com.undercooked.game.map.MapManager;
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
        //cooks.add(new Cook(new Vector2(MapManager.gridToPos(5), MapManager.gridToPos(3)), 1, textureManager));
		//cooks.add(new Cook(new Vector2(MapManager.gridToPos(5), MapManager.gridToPos(5)), 2, textureManager));

		currentCook = 0;
    }

    // =======================================LOGIC======================================================
    public void addCook(Cook cook) {
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

		Control.interact = false;
		Control.drop = false;
		Control.flip = false;
		Control.tab = false;
		Control.shift = false;

        // Check input for the current cook
        if (cooks.size > 0) {
            cooks.get(currentCook).checkInput(delta);
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