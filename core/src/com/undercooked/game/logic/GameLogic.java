package com.undercooked.game.logic;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.entity.Cook;
import com.undercooked.game.food.Ingredients;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.screen.ScreenController;

/**
 * A class to extend from that indicates the logic of the {@link GameScreen}.
 */
public abstract class GameLogic implements Logic {

    GameScreen game;
    Ingredients ingredients;
    Array<Cook> cooks;
    TiledMap map;
    public GameLogic(GameScreen game) {
        this.game = game;
        this.ingredients = new Ingredients();
        this.cooks = new Array<>();
    }

    public final void addCook(Cook cook) {
        cooks.add(cook);
    }

    public final Array<Cook> getCooks() {
        return cooks;
    }

    /**
     * Will require a LeaderBoard rework.
     */
    public final void win() {
        game.getScreenController();
    }

    public final void loadMap(String path) {
        game.getMapManager().load(path);
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
        map = game.getMapManager().get();
    }
}
