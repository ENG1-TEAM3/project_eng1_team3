package com.undercooked.game.logic;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.entity.Cook;
import com.undercooked.game.entity.CookController;
import com.undercooked.game.entity.CustomerController;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.food.Ingredients;
import com.undercooked.game.map.Map;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.screen.ScreenController;
import com.undercooked.game.station.StationManager;

/**
 * A class to extend from that indicates the logic of the {@link GameScreen}.
 */
public abstract class GameLogic implements Logic {

    GameScreen game;
    Ingredients ingredients;
    CookController cookController;
    CustomerController customerController;
    StationManager stationManager;
    Map map;
    public GameLogic(GameScreen game) {
        this.game = game;
        this.ingredients = new Ingredients();
    }

    /**
     * Will require a LeaderBoard rework.
     */
    public final void win() {
        game.getScreenController();
    }

    /**
     * Loads a map from the path provided
     * @param path {@link String} of the path.
     */
    public final void loadMap(String path, boolean internal) {
        game.getMapManager().load(path, stationManager, internal);
    }

    /**
     * Loads a map from the path provided
     * @param path {@link String} of the path.
     */
    public final void loadMap(String path) {
        map = game.getMapManager().load(path, stationManager, FileControl.isInternal(path));
    }

    /**
     * Called before the game has loaded.
     */
    public void preLoad() {

    }

    /**
     * Called after the game has loaded.
     */
    public void postLoad() {

    }

    /** Unloads all the objects in this class.
     * (Most likely will call each objects unload method)
     */
    public void unloadObjects() {
        cookController.unload();
    }
}
