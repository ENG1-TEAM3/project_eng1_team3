package com.undercooked.game.load;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.screen.Screen;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.Constants;

public class LoadScreen extends Screen {

    Screen previous;
    Screen next;
    AssetManager assetManager;
    MainGameClass game;
    OrthographicCamera camera;
    Viewport viewport;
    long lastLoad;

    public LoadScreen(AssetManager assetManager, MainGameClass game) {
        this.assetManager = assetManager;
        this.game = game;
        this.camera = CameraController.getCamera(Constants.UI_CAMERA_ID);
        this.viewport = CameraController.getViewport(Constants.UI_CAMERA_ID);
    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }

    @Override
    public void show() {

    }

    public void update() {

        // Update the AssetManager for a short bit before moving on
        while (TimeUtils.timeSinceMillis(lastLoad) <= 100
                && !assetManager.update()) { }

        lastLoad = TimeUtils.millis();

        // System.out.println("Progress: " + assetManager.getProgress());

        // Check if the AssetManager is finished
        if (assetManager.isFinished()) {
            // Then swap to the screen that was loading
            System.out.println("Swapped to Screen " + next);
            game.setScreen(next);
        }

    }

    @Override
    public void render(float delta) {

        // Update the screen
        update();

        // Render the previous screen underneath (if there is one)
        if (previous != null) {
            previous.renderScreen();
        }

        // Render the loading bar
        renderBar();

    }

    public void renderBar() {
        // Then render a loading bar
        // Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        // Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ShapeRenderer shape = game.shapeRenderer;
        shape.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        // Draw a rectangle with the shapeRenderer, at the bottom of the screen
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.WHITE);
        shape.rect(20,20,200,50);
        shape.setColor(Color.GREEN);
        shape.rect(30,30,180*assetManager.getProgress(),30);
        shape.setColor(Color.WHITE);
        shape.end();
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

    public void setScreens(Screen previous, Screen next) {
        this.previous = previous;
        this.next = next;
    }

    public void start(MainGameClass game) {
        // Make the game change to this screen
        game.setScreen(this);

        // Set the load time
        lastLoad = TimeUtils.millis();

        // Then update once before moving on to the next frame
        // This means that if it's done in one go, then it can just go
        // straight to the screen rather than wait a frame
        update();

        // Render the bar, if it's not finished updating yet
        if (!assetManager.isFinished()) {
            renderBar();
        }
    }
}
