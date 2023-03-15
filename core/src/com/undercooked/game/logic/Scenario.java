package com.undercooked.game.logic;

import com.badlogic.gdx.Gdx;
import com.undercooked.game.Input.InputController;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.Cook;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.screen.GameScreen.STATE;

public class Scenario extends GameLogic {

    /** The number of cooks in-game. */
    int cookCount;

    /** The number of customers to serve. */
    public static final int NUMBER_OF_WAVES = 5;

    /** The number of customers that have been served. */
    public static int currentWave = 0;

    public Scenario(GameScreen game, TextureManager textureManager) {
        super(game, textureManager);
        cookCount = 1;
    }

    public void checkGameOver() {
        if (currentWave == NUMBER_OF_WAVES + 1) {
            // game.getLeaderBoardScreen().addLeaderBoardData("PLAYER1",
            // (int) Math.floor((startTime - timeOnStartup) / 1000f));
            // game.resetGameScreen();
            this.resetStatic();
        }
    }

    public void resetStatic() {
        currentWave = 0;
    }

    public Scenario() {
        this(null, null);
    }

    @Override
    public void update(float delta) {

        InputController.updateKeys();

        elapsedTime += delta;

        // Check if game is over.

        // Update cooks.
        cookController.update(delta);

        // Update Customers.
        customerController.update(delta);

    }

    @Override
    public void preLoad() {

    }

    @Override
    public void load() {
        // Add listener to CustomerController to remove 1 from count
    }

    @Override
    public void unload() {
        ingredients.unload(gameScreen.getTextureManager());
    }

    public void addIngredient() {
        ingredients.load(gameScreen.getTextureManager());
    }

    public void setCookCount(int cookCount) {
        this.cookCount = cookCount;
    }
}
