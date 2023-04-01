package com.undercooked.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.ScreenUtils;
import com.undercooked.game.MainGameClass;

public class LossScreen extends Screen {

    public LossScreen(MainGameClass game) {
        super(game);
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

    @Override
    public void render(float delta) {
        // Clear the Screen
        ScreenUtils.clear(0, 0, 0, 0);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
