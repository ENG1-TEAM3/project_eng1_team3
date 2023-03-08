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
import com.undercooked.game.util.Constants;

/**
 * A class to extend from that indicates the logic of the
 * {@link GameScreen}.
 */
public abstract class GameLogic implements Logic {

    /** The number of customers to serve. */
    public static final int NUMBER_OF_WAVES = 5;

    /** The number of customers that have been served. */
    public static int currentWave = 0;

    GameScreen gameScreen;
    Ingredients ingredients;
    CookController cookController;
    CustomerController customerController;
    StationManager stationManager;
    Map map;

    public GameLogic(GameScreen game) {
        this.gameScreen = game;
        this.ingredients = new Ingredients();
    }

    /**
     * Will require a LeaderBoard rework.
     */
    public final void win() {
        gameScreen.getScreenController();
    }

    public void checkGameOver() {
        if (currentWave == NUMBER_OF_WAVES + 1) {
            // game.getLeaderBoardScreen().addLeaderBoardData("PLAYER1",
            // (int) Math.floor((startTime - timeOnStartup) / 1000f));
            // game.resetGameScreen();
            this.resetStatic();
            gameScreen.ChangeGameOver();
        }
    }

    public void resetStatic() {
        currentWave = 0;
    }

    /**
     * Loads a map from the path provided
     * 
     * @param path {@link String} of the path.
     */
    public final void loadMap(String path) {
        gameScreen.getMapManager().load(path, stationManager);
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

    /**
     * Unloads all the objects in this class. (Most likely will call
     * each objects unload method)
     */
    public void unloadObjects() {
        cookController.unload();
    }
}
