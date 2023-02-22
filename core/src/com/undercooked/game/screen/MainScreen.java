package com.undercooked.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.undercooked.game.audio.AudioSettings;
import com.undercooked.game.audio.AudioSliders;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.audio.Slider;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.Constants;

public class MainScreen extends Screen {
	final MainGameClass game;
	float v = 0;
	float s = 0;

	int gameResolutionX;
	int gameResolutionY;

	float buttonwidth;
	float buttonheight;

	float xSliderMin;
	float xSliderMax;

	float sliderWidth;

	Button sb;
	Button lb;
	Button ad;
	Button eg;

	Rectangle volSlide;
	Rectangle volSlideBackgr;
	Rectangle musSlide;
	Rectangle musSlideBackgr;

	OrthographicCamera camera;
	Viewport viewport;

	Texture vButton;
	Texture vControl;
	Texture background;
	Texture startButton;
	Texture leaderBoard;
	Texture exitGame;
	Texture audio;
	Texture audioEdit;

	Stage stage;

	enum STATE {
		main, audio, leaderboard, new_game;
	}

	STATE state;


	/**
	 * Constructor for main menu screen
	 * 
	 * @param game - Entry point class
	 */
	public MainScreen(final MainGameClass game) {
		this.game = game;
		this.gameResolutionX = Constants.V_WIDTH;
		this.gameResolutionY = Constants.V_HEIGHT;
		this.buttonwidth = (float) gameResolutionX / 3;
		this.buttonheight = (float) gameResolutionY / 6;



		this.volSlide = new Rectangle();
		volSlide.width = 3 * buttonheight / 12;
		volSlide.height = 3 * buttonheight / 12;

		this.volSlideBackgr = new Rectangle();
		volSlideBackgr.width = 2 * buttonwidth / 6;
		volSlideBackgr.height = buttonheight / 6;

		this.musSlide = new Rectangle();
		musSlide.width = 3 * buttonheight / 12;
		musSlide.height = 3 * buttonheight / 12;

		this.musSlideBackgr = new Rectangle();
		musSlideBackgr.width = 2 * buttonwidth / 6;
		musSlideBackgr.height = buttonheight / 6;

		this.xSliderMin = gameResolutionX / 2.0f + buttonwidth / 12;
		this.xSliderMax = xSliderMin + volSlideBackgr.width;
		this.sliderWidth = volSlideBackgr.width;

		this.camera = CameraController.getCamera(Constants.UI_CAMERA_ID);
		this.viewport = CameraController.getViewport(Constants.UI_CAMERA_ID);
	}

	@Override
	public void load() {
		AssetManager assetManager = MainGameClass.assetManager;
		assetManager.load("uielements/vButton.jpg", Texture.class);
		assetManager.load("uielements/vControl.png", Texture.class);
		assetManager.load("uielements/newgame.png", Texture.class);
		assetManager.load("uielements/MainScreenBackground.jpg", Texture.class);
		assetManager.load("uielements/leaderboard1.png", Texture.class);
		assetManager.load("uielements/audio.png", Texture.class);
		assetManager.load("uielements/background.png", Texture.class);
		assetManager.load("uielements/exitgame.png", Texture.class);

		game.audioManager.loadMusic("uielements/MainScreenMusic.ogg", Constants.MUSIC_GROUP);
	}

	@Override
	public void unload() {
		AssetManager assetManager = MainGameClass.assetManager;
		assetManager.unload("uielements/vButton.jpg");
		assetManager.unload("uielements/vControl.png");
		assetManager.unload("uielements/newgame.png");
		assetManager.unload("uielements/MainScreenBackground.jpg");
		assetManager.unload("uielements/leaderboard1.png");
		assetManager.unload("uielements/audio.png");
		assetManager.unload("uielements/background.png");
		assetManager.unload("uielements/exitgame.png");

		game.audioManager.unloadMusic("uielements/MainScreenMusic.ogg");
	}

	/**
	 * What should be done when the screen is shown
	 */
	@Override
	public void show() {
		float currentMusicVolumeSliderX = (MainGameClass.musicVolumeScale * sliderWidth) + xSliderMin;
		float currentGameVolumeSliderX = (MainGameClass.gameVolumeScale * sliderWidth) + xSliderMin;
		volSlide.setPosition(currentGameVolumeSliderX, 2 * gameResolutionY / 5.0f - buttonheight / 2 + buttonheight / 6
				+ volSlideBackgr.height / 2 - volSlide.height / 2);
		volSlideBackgr.setPosition((gameResolutionX / 2.0f) + buttonwidth / 12,
				2 * gameResolutionY / 5.0f - buttonheight / 2 + buttonheight / 6);
		musSlide.setPosition(currentMusicVolumeSliderX, 2 * gameResolutionY / 5.0f - buttonheight / 2
				+ 4 * buttonheight / 6 + musSlideBackgr.height / 2 - musSlide.height / 2);
		musSlideBackgr.setPosition((gameResolutionX / 2.0f) + buttonwidth / 12,
				2 * gameResolutionY / 5.0f - buttonheight / 2 + 4 * buttonheight / 6);

		state = STATE.main;

		game.mainScreenMusic = MainGameClass.assetManager.get("uielements/MainScreenMusic.ogg");

		AssetManager assetManager = MainGameClass.assetManager;
		vButton = assetManager.get("uielements/vButton.jpg");
		vControl = assetManager.get("uielements/vControl.png");
		startButton = assetManager.get("uielements/newgame.png");
		background = assetManager.get("uielements/MainScreenBackground.jpg");
		leaderBoard = assetManager.get("uielements/leaderboard1.png");
		audio = assetManager.get("uielements/audio.png");
		audioEdit = assetManager.get("uielements/background.png");
		exitGame = assetManager.get("uielements/exitgame.png");

		sb = new Button(new TextureRegionDrawable(startButton));
		lb = new Button(new TextureRegionDrawable(leaderBoard));
		ad = new Button(new TextureRegionDrawable(audio));
		eg = new Button(new TextureRegionDrawable(exitGame));

		sb.setPosition(gameResolutionX / 10.0f, 4 * gameResolutionY / 5.0f - buttonheight / 2);
		lb.setPosition(gameResolutionX / 10.0f, 3 * gameResolutionY / 5.0f - buttonheight / 2);
		ad.setPosition(gameResolutionX / 10.0f, 2 * gameResolutionY / 5.0f - buttonheight / 2);
		eg.setPosition(gameResolutionX / 10.0f, gameResolutionY / 5.0f - buttonheight / 2);

		lb.setSize(buttonwidth, buttonheight);
		ad.setSize(buttonwidth, buttonheight);
		eg.setSize(buttonwidth, buttonheight);
		sb.setSize(buttonwidth, buttonheight);

		ad.addListener(new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				state = STATE.audio;
				super.touchUp(event, x, y, pointer, button);
			}
		});
		sb.addListener(new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				state = STATE.new_game;
				super.touchUp(event, x, y, pointer, button);
			}
		});
		lb.addListener(new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				state = STATE.leaderboard;
				super.touchUp(event, x, y, pointer, button);
			}
		});
		eg.addListener(new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (state == STATE.main) {
					Gdx.app.exit();
				}
				super.touchUp(event, x, y, pointer, button);
			}
		});

		stage = new Stage(viewport, game.batch);
		Gdx.input.setInputProcessor(stage);

		stage.addActor(sb);
		stage.addActor(lb);
		stage.addActor(ad);
		stage.addActor(eg);

		audioSliders = AudioSettings.createAudioSliders(ad.getX() + 650, ad.getY() - 10, stage, audioEdit);

		musicSlider = audioSliders.getSlider(0);
		musicSlider.setTouchable(Touchable.disabled);

		gameSlider = audioSliders.getSlider(1);
		gameSlider.setTouchable(Touchable.disabled);
	}

	AudioSliders audioSliders;
	Slider musicSlider, gameSlider;

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
		game.batch.draw(background, 0, 0, Constants.V_WIDTH, Constants.V_HEIGHT);
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
			game.screenController.setScreen(Constants.GAME_SCREEN_ID);
		}
		if (state == STATE.leaderboard) {
			game.mainScreenMusic.dispose();
			game.screenController.nextScreen(Constants.LEADERBOARD_SCREEN_ID);
		}
		if (state == STATE.audio) {

			musicSlider.setTouchable(Touchable.enabled);
			gameSlider.setTouchable(Touchable.enabled);

			game.batch.begin();

			audioSliders.render(game.batch);

			/*game.batch.draw(audioEdit, (float) gameResolutionX / 2, (float) 2 * gameResolutionY / 5 - buttonheight / 2,
					buttonwidth / 2, buttonheight);

			game.batch.draw(vControl, volSlideBackgr.getX(), volSlideBackgr.getY(), volSlideBackgr.width,
					volSlideBackgr.height);
			game.batch.draw(vButton, volSlide.getX() - volSlide.width / 2, volSlide.getY(), volSlide.width,
					volSlide.height);

			game.batch.draw(vControl, musSlideBackgr.getX(), musSlideBackgr.getY(), musSlideBackgr.width,
					musSlideBackgr.height);
			game.batch.draw(vButton, musSlide.getX() - musSlide.width / 2, musSlide.getY(), musSlide.width,
					musSlide.height);*/

			game.batch.end();
		} else {
			musicSlider.setTouchable(Touchable.disabled);
			gameSlider.setTouchable(Touchable.disabled);
		}
	}

	/**
	 * Update music volume slider
	 */
	public void musicVolumeUpdate() {
		float fromTopy = Gdx.input.getY();
		float fromBottomy = gameResolutionY - fromTopy;
		float x = Gdx.input.getX();
		boolean change = musSlide.getY() <= fromBottomy & fromBottomy <= musSlide.getY() + musSlide.getHeight();
		if (Gdx.input.isTouched() & change) {
			if (x >= musSlideBackgr.getX() & x <= musSlideBackgr.getX() + musSlideBackgr.getWidth()) {
				musSlide.setPosition(Gdx.input.getX(), musSlide.getY());
				v = (musSlide.getX() - musSlideBackgr.getX()) / musSlideBackgr.getWidth();
				if (v < 0.01) {
					v = 0;
				}
				game.mainScreenMusic.setVolume(v);
				game.gameMusic.setVolume(v);
				MainGameClass.musicVolumeScale = v;
			}
		}
	}

	/**
	 * Update game volume slider
	 */
	public void gameVolumeUpdate() {
		float fromTopy = Gdx.input.getY();
		float fromBottomy = gameResolutionY - fromTopy;
		float x = Gdx.input.getX();
		boolean change = volSlide.getY() <= fromBottomy & fromBottomy <= volSlide.getY() + volSlide.getHeight();
		if (Gdx.input.isTouched() & change) {
			if (x >= volSlideBackgr.getX() & x <= volSlideBackgr.getX() + volSlideBackgr.getWidth()) {
				volSlide.setPosition(Gdx.input.getX(), volSlide.getY());
				s = (volSlide.getX() - volSlideBackgr.getX()) / volSlideBackgr.getWidth();
				if (s < 0.01) {
					s = 0;
				}
				// game.sound.setVolume(game.soundid, s);
				MainGameClass.gameVolumeScale = s;
				MainGameClass.gameVolumeScale = s;
			}
		}
	}

	/**
	 * Resize window
	 * 
	 * @param width  - new window width
	 * @param height - new window height
	 */
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
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
