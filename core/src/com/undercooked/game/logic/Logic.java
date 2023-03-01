package com.undercooked.game.logic;

/**
 * A class which controls the logic of the game.
 */
public interface Logic {

    /**
     * Update the game logic. Move chefs, update stations, spawn customer etc.
     */
    void update(float delta);

    /**
     * The GameLogic should load the game assets in this function.
     * For example, a Scenario would only need to load the Ingredients that
     * will be available, while Endless mode should load all the game's food items.
     */
    void load();

    /**
     * Should unload everything that was loaded in load.
     */
    void unload();

}
