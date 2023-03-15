package com.undercooked.game.logic;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.Cook;
import com.undercooked.game.entity.CookController;
import com.undercooked.game.entity.CustomerController;
import com.undercooked.game.entity.Entity;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.food.Ingredients;
import com.undercooked.game.map.Map;
import com.undercooked.game.render.GameRenderer;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.screen.ScreenController;
import com.undercooked.game.station.StationManager;
import com.undercooked.game.util.Constants;

/**
 * A class to extend from that indicates the logic of the
 * {@link GameScreen}.
 */
public abstract class GameLogic implements Logic {

    GameScreen gameScreen;
    Ingredients ingredients;
    CookController cookController;
    CustomerController customerController;
    StationManager stationManager;
    GameRenderer gameRenderer;
    TextureManager textureManager;
    Map map;
    float elapsedTime;

    public GameLogic(GameScreen game, TextureManager textureManager) {
        this.gameScreen = game;
        this.ingredients = new Ingredients();
        this.elapsedTime = 0;

        this.cookController = new CookController(textureManager);
        this.customerController = new CustomerController(textureManager);
        this.textureManager = textureManager;
    }

    public GameLogic(GameScreen game) {
        this(game, null);
    }

    public GameLogic() {
        this(null, null);
    }

    /**
     * Will require a LeaderBoard rework.
     */
    public final void win() {
        gameScreen.getScreenController();
    }

    /**
     * Loads a map from the path provided
     *
     * @param path {@link String} of the path.
     */
    public final void loadMap(String path) {
        map = gameScreen.getMapManager().load(path, stationManager, cookController);
        // Load the map into the renderer
        for (Entity mapEntity : map.getAllEntities()) {
            gameRenderer.addEntity(mapEntity);
            mapEntity.load(textureManager);
            // System.out.println(mapEntity.pos.x);
        }
        cookController.load(Constants.GAME_TEXTURE_ID);
        // Load the cooks into the renderer
        for (Entity cook : cookController.getCooks()) {
            gameRenderer.addEntity(cook);
            // cook.load(textureManager);
        }
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
        // post load all the entities in the GameRenderer
        for (Entity entity : gameRenderer.getEntities()) {
            entity.postLoad(textureManager);
        }
    }

    /**
     * Unloads all the objects in this class. (Most likely will call
     * each objects unload method)
     */
    public void unloadObjects() {
        cookController.unload();
    }

    /**
     * Called when the {@link GameScreen} moves to the {@link com.undercooked.game.screen.PauseScreen}.
     */
    public void pause() {
        cookController.stopMovement();
    };

    public CookController getCookController() {
        return cookController;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void setStationManager(StationManager stationManager) {
        this.stationManager = stationManager;
    }

    public void setTextureManager(TextureManager textureManager) {
        this.textureManager = textureManager;
    }

    public void setGameRenderer(GameRenderer gameRenderer) {
        this.gameRenderer = gameRenderer;
    }

    public void dispose() {
        map.dispose();
    }

    public Map getMap() {
        return map;
    }
}
