package com.undercooked.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.Constants;

public class LossScreen extends Screen {

    private Stage stage;

    public LossScreen(MainGameClass game) {
        super(game);
    }

    @Override
    public void load() {
        // Load the textures for the buttons
        TextureManager textureManager = getTextureManager();
        textureManager.load(Constants.LOSS_TEXTURE_ID, "uielements/exittomenu.png");
        textureManager.load(Constants.LOSS_TEXTURE_ID, "uielements/retry.png");

        // Create the stage
        Viewport viewport = CameraController.getViewport(Constants.UI_CAMERA_ID);
        stage = new Stage(viewport, game.batch);
    }

    @Override
    public void unload() {
        // Unload the textures
        getTextureManager().unload(Constants.LOSS_TEXTURE_ID, true);
        // Dispose the stage
        stage.dispose();
    }

    @Override
    public void show() {
        TextureManager textureManager = getTextureManager();

        // Stop the game's music
        game.gameMusic.stop();
        // Create the buttons
        Button quitBtn = new Button(new TextureRegionDrawable(textureManager.get("uielements/exittomenu.png")));
        Button retryBtn = new Button(new TextureRegionDrawable(textureManager.get("uielements/retry.png")));

        // Add listeners to the buttons
        quitBtn.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                // Go to main screen
                getScreenController().setScreen(Constants.MAIN_SCREEN_ID);
            }
        });

        retryBtn.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                // Get the ScreenController
                ScreenController screenController = getScreenController();
                // Get the GameScreen
                GameScreen gameScreen = (GameScreen) screenController.getScreen(Constants.GAME_SCREEN_ID);
                // Reset the GameScreen
                gameScreen.reset();
                // Go back to the GameScreen
                screenController.backScreen();
            }
        });

        // Position the buttons
        quitBtn.setPosition(100,100);
        retryBtn.setPosition(100,300);

        // Set their size
        quitBtn.setSize(473, 144);
        retryBtn.setSize(473, 144);

        // Add the buttons to the stage
        stage.addActor(quitBtn);
        stage.addActor(retryBtn);


        // Set the input processor to the stage
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        // Clear the Screen
        ScreenUtils.clear(0, 0, 0, 0);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the stage
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }


}
