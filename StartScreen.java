package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class StartScreen implements Screen{
	final Eng1 game;
	final Texture backgound;
	final Viewport viewport;
	
	OrthographicCamera camera;
	
	public StartScreen(final Eng1 game) {
		this.game = game;
		
		backgound = new Texture(Gdx.files.internal("dragonflight.jpg"));
		camera = new OrthographicCamera();
		camera.setToOrtho(false,1920,1080);
		game.changeFontScale(3);
		viewport = new FitViewport(1920,1080,camera);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		ScreenUtils.clear(0, 0, 0, 1);
		game.batch.setProjectionMatrix(camera.combined);
		
		game.MianScreenMusic.play();
		camera.update();
		game.batch.begin();
		game.batch.draw(backgound, 0, 0);
		game.font.draw(game.batch, "ENg1 Game", 600, 800);
		game.font.draw(game.batch,"Press to Continue",450,500);
		game.batch.end();
		
		if(Gdx.input.isTouched()) {
			game.setScreen(new MainScreen(game,1030,260,1030,160));
			dispose();
		}
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		viewport.update(width, height);
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
