package com.team3gdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team3gdx.game.MainGameClass;

public class DiffictuiltyScreen implements Screen{
    final MainGameClass game;
	final MainScreen ms;
	float v = 0;
	float s = 0;

	int gameResolutionX;
	int gameResolutionY;

	float buttonwidth;
	float buttonheight;

	float xSliderMin;
	float xSliderMax;

	float sliderWidth;

	Button easy;
	Button mediaum;
	Button hard;
	Button endless;
	Button exit;

	OrthographicCamera camera;
	Viewport viewport;

	Texture EButton;
	Texture MButton;
	Texture HButton;
	Texture EndButton;
	Texture exitScreen;
	Texture background;
	Stage stage;

	enum STATE {
		main, select, new_game;
	}

	STATE state;

	/**
	 * Constructor for main menu screen
	 * 
	 * @param game - main entry point class
	 * * @param ms   - Title screen class
	 */
	public DiffictuiltyScreen(MainGameClass game, MainScreen ms) {
		this.game = game;
		this.ms = ms;
		this.gameResolutionX = Gdx.graphics.getWidth();
		this.gameResolutionY = Gdx.graphics.getHeight();
		this.buttonwidth = (float) gameResolutionX / 3;
		this.buttonheight = (float) gameResolutionY / 6;
	}

	/**
	 * What should be done when the screen is shown
	 */
	@Override
	public void show() {

		state = STATE.select;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, gameResolutionX, gameResolutionY);
		viewport = new FitViewport(gameResolutionX, gameResolutionY, camera);

		EButton = new Texture(Gdx.files.internal("uielements/button_easy.png"));
		MButton = new Texture(Gdx.files.internal("uielements/button_medium.png"));
		HButton = new Texture(Gdx.files.internal("uielements/button_hard.png"));
		EndButton = new Texture(Gdx.files.internal("uielements/button_endless.png"));
		background = new Texture(Gdx.files.internal("uielements/MainScreenBackground.jpg"));
		exitScreen = new Texture(Gdx.files.internal("uielements/exitmenu.png"));

		easy = new Button(new TextureRegionDrawable(EButton));
		mediaum = new Button(new TextureRegionDrawable(MButton));
		hard = new Button(new TextureRegionDrawable(HButton));
		endless = new Button(new TextureRegionDrawable(EndButton));
		exit = new Button(new TextureRegionDrawable(exitScreen));

		exit.setPosition(gameResolutionX * 6 / 10.0f, 4 * gameResolutionY / 5.0f - buttonheight / 2);
		easy.setPosition(gameResolutionX / 10.0f, 4 * gameResolutionY / 5.0f - buttonheight / 2);
		mediaum.setPosition(gameResolutionX / 10.0f, 3 * gameResolutionY / 5.0f - buttonheight / 2);
		hard.setPosition(gameResolutionX / 10.0f, 2 * gameResolutionY / 5.0f - buttonheight / 2);
		endless.setPosition(gameResolutionX / 10.0f, gameResolutionY / 5.0f - buttonheight / 2);


		easy.setSize(buttonwidth, buttonheight);
		mediaum.setSize(buttonwidth, buttonheight);
		hard.setSize(buttonwidth, buttonheight);
		endless.setSize(buttonwidth, buttonheight);
		exit.setSize(buttonwidth, buttonheight);

		easy.addListener(new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				state = STATE.new_game;
				super.touchUp(event, x, y, pointer, button);
			}
		});
		mediaum.addListener(new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				state = STATE.new_game;
				super.touchUp(event, x, y, pointer, button);
			}
		});
		hard.addListener(new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				state = STATE.new_game;
				super.touchUp(event, x, y, pointer, button);
			}
		});
		endless.addListener(new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				state = STATE.new_game;
				super.touchUp(event, x, y, pointer, button);
			}
		});
		exit.addListener(new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				state = STATE.main;
				super.touchUp(event, x, y, pointer, button);
			}
		});

		stage = new Stage(viewport, game.batch);
		Gdx.input.setInputProcessor(stage);

		stage.addActor(easy);
		stage.addActor(mediaum);
		stage.addActor(hard);
		stage.addActor(endless);
		stage.addActor(exit);
	}

	/**
	 * Main menu render method
	 * 
	 * @param delta - some change in time
	 */
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		ScreenUtils.clear(0, 0, 0, 0);
		game.batch.setProjectionMatrix(camera.combined);
		game.mainScreenMusic.play();

		game.batch.begin();
		game.batch.draw(background, 0, 0, gameResolutionX, gameResolutionY);
		game.batch.end();
		stage.act();
		stage.draw();
		changeScreen(state);

		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			state = STATE.main;
		}
	}

	/**
	 * Change screen to specified state
	 * 
	 * @param state - state to change screen to
	 */
	public void changeScreen(STATE state) {
		if (state == STATE.new_game) {
			game.mainScreenMusic.dispose();
			game.setScreen(game.getGameScreen());
		}
		if (state == STATE.main) {
			game.mainScreenMusic.dispose();
			game.setScreen(game.getMainScreen());
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