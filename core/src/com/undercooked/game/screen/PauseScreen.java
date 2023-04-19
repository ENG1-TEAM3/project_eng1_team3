package com.undercooked.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.audio.AudioSettings;
import com.undercooked.game.audio.AudioSliders;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.Constants;

public class PauseScreen extends Screen {

    Screen displayScreen;
    SpriteBatch batch;
    Stage stage;
    Texture menuBack;
    AudioSliders audioSliders;

    public PauseScreen(MainGameClass game) {
        super(game);
        this.batch = MainGameClass.batch;
    }

    @Override
    public void load() {
        // Load menu Textures
        TextureManager textureManager = getTextureManager();
        textureManager.load(Constants.PAUSE_TEXTURE_ID, "uielements/settings.png");
        textureManager.load(Constants.PAUSE_TEXTURE_ID, "uielements/background.png");
        textureManager.load(Constants.PAUSE_TEXTURE_ID, "uielements/exitmenu.png");
        textureManager.load(Constants.PAUSE_TEXTURE_ID, "uielements/resume.png");
        textureManager.load(Constants.PAUSE_TEXTURE_ID, "uielements/audio2.png");
        textureManager.load(Constants.PAUSE_TEXTURE_ID, "uielements/background.png");
        textureManager.load(Constants.PAUSE_TEXTURE_ID, "uielements/vButton.jpg");
        textureManager.load(Constants.PAUSE_TEXTURE_ID, "uielements/vControl.png");
        textureManager.load(Constants.PAUSE_TEXTURE_ID, "uielements/background.png");
        textureManager.load(Constants.PAUSE_TEXTURE_ID, "uielements/vButton.jpg");

        // Create Stage
        stage = new Stage(CameraController.getViewport(Constants.UI_CAMERA_ID));
    }

    @Override
    public void unload() {
        // Unload all Textures
        getTextureManager().unload(Constants.PAUSE_TEXTURE_ID, true);

        // Dispose stage
        stage.dispose();
    }

    /**
     * Go to the previous screen.
     */
    private void previous() {
        // Go to the previous screen.
        game.screenController.backScreen();
        // Set the screen to null.
        displayScreen = null;
    }

    /**
     * Set the {@link Screen} to display on the {@link PauseScreen}.
     * @param screen The {@link Screen}.
     */
    @Override
    public void fromScreen(Screen screen) {
        displayScreen = screen;
    }

    /**
     * Set up the visuals of the PauseScreen when loaded.
     */
    @Override
    public void postLoad() {
        // Set up the Stage
        TextureManager textureManager = getTextureManager();
        menuBack = textureManager.get("uielements/background.png");

        // Create the Buttons
        Button unpause = new Button(new TextureRegionDrawable(textureManager.get("uielements/resume.png")));
        Button menu = new Button(new TextureRegionDrawable(textureManager.get("uielements/exitmenu.png")));

        // Set the Button positions
        unpause.setPosition(100, 100);
        menu.setPosition(100, 200);

        // Set the Button visuals
        unpause.setSize(200,100);
        menu.setSize(200,100);

        // Add Button listeners
        unpause.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                resumeGame();
            }
        });

        menu.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Go to the main menu, if still on the pause screen
                if (!getScreenController().onScreen(Constants.PAUSE_SCREEN_ID)) return;
                game.screenController.setScreen(Constants.MAIN_SCREEN_ID);
            }
        });

        // Add the Buttons to the Stage
        stage.addActor(unpause);
        stage.addActor(menu);

        // Add the audio sliders
        audioSliders = game.getAudioSettings().createAudioSliders(320, 100, stage, textureManager.get("uielements/background.png"), textureManager.get("uielements/vButton.jpg"));

        // Finally, set the Gdx inputProcessor to use the stage
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        // First render the screen below, if there is one.
        if (displayScreen != null) {
            displayScreen.renderScreen(delta);
        } else {
            // Otherwise, just draw a blank screen.
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }

        // Render the pause menu over the top of the previous screen
        // If escape is pressed, resume
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            resumeGame();
        } else {
            // Otherwise, check button
            stage.act(delta);
        }
        stage.draw();
        batch.begin();
        audioSliders.render(game.batch);
        batch.end();

    }

    private void resumeGame() {
        if (!getScreenController().onScreen(Constants.PAUSE_SCREEN_ID)) return;
        previous();
    }

    public void renderScreen() {

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
