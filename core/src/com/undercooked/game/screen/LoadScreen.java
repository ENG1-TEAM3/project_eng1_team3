package com.undercooked.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.Constants;

public class LoadScreen extends Screen {

    Screen previous;
    Screen next;
    AssetManager assetManager;
    MainGameClass game;
    OrthographicCamera camera;
    Viewport viewport;

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

    public void update(float delta) {

        // Update the AssetManager
        assetManager.update();

        // System.out.println("Progress: " + assetManager.getProgress());

        // Check the progress
        if (assetManager.getProgress() >= 1F) {
            // If it's finished, swap to the screen that was loading
            System.out.println("Swapped to Screen " + next);
            game.setScreen(next);
        }

    }

    @Override
    public void render(float delta) {

        // First update
        update(delta);

        // Then render a loading bar
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ShapeRenderer shape = game.shapeRenderer;
        shape.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        // Draw a rectangle with the shapeRenderer, at the bottom of the screen
        shape.begin(ShapeRenderer.ShapeType.Filled);
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
}
