package com.undercooked.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.undercooked.game.GameType;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.audio.AudioSliders;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.audio.Slider;
import com.undercooked.game.logic.*;
import com.undercooked.game.render.GameRenderer;
import com.undercooked.game.render.TutorialRenderer;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.SaveLoadGame;

public class MainScreen extends Screen {
	float v = 0;
	float s = 0;

	int gameResolutionX;
	int gameResolutionY;

	float buttonwidth;
	float buttonheight;

	float xSliderMin;
	float xSliderMax;

	float sliderWidth;

	Rectangle volSlide;
	Rectangle volSlideBackgr;
	Rectangle musSlide;
	Rectangle musSlideBackgr;

	Texture background;

	OrthographicCamera camera;
	Viewport viewport;

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
		super(game);
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
		TextureManager textureManager = game.getTextureManager();
		textureManager.load(Constants.MENU_TEXTURE_ID, "uielements/vButton.jpg");
		textureManager.load(Constants.MENU_TEXTURE_ID, "uielements/newgame.png");
		textureManager.load(Constants.MENU_TEXTURE_ID, "uielements/MainScreenBackground.jpg");
		textureManager.load(Constants.MENU_TEXTURE_ID, "uielements/leaderboard1.png");
		textureManager.load(Constants.MENU_TEXTURE_ID, "uielements/audio.png");
		textureManager.load(Constants.MENU_TEXTURE_ID, "uielements/background.png");
		textureManager.load(Constants.MENU_TEXTURE_ID, "uielements/game/exit.png");
		textureManager.load(Constants.MENU_TEXTURE_ID, "uielements/game/load.png");
		textureManager.load(Constants.MENU_TEXTURE_ID, "uielements/tutorial.png");

		game.audioManager.loadMusic("audio/music/MainScreenMusic.ogg", Constants.MUSIC_GROUP);
	}

	@Override
	public void unload() {
		game.getTextureManager().unload(Constants.MENU_TEXTURE_ID, true);

		game.audioManager.unloadMusic("audio/music/MainScreenMusic.ogg");
		stage.dispose();
	}

	@Override
	public void postLoad() {
		// AudioManager post load
		game.audioManager.postLoad();

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

		game.mainScreenMusic = game.audioManager.getMusic("audio/music/MainScreenMusic.ogg");

		final TextureManager textureManager = game.getTextureManager();
		Texture vButton = textureManager.get("uielements/vButton.jpg");
		Texture startBtnTex = textureManager.get("uielements/newgame.png");
		background = textureManager.get("uielements/MainScreenBackground.jpg");
		Texture leaderboardBtnTex = textureManager.get("uielements/leaderboard1.png");
		Texture audioBtnTex = textureManager.get("uielements/audio.png");
		Texture audioEdit = textureManager.get("uielements/background.png");
		Texture exitBtnTex = textureManager.get("uielements/game/exit.png");

		Button startBtn = new Button(new TextureRegionDrawable(startBtnTex));
		Button leaderboardBtn = new Button(new TextureRegionDrawable(leaderboardBtnTex));
		Button audioBtn = new Button(new TextureRegionDrawable(audioBtnTex));
		Button exitBtn = new Button(new TextureRegionDrawable(exitBtnTex));

		Button loadGameBtn = new Button(new TextureRegionDrawable(textureManager.get("uielements/game/load.png")));
		Button tutorialBtn = new Button(new TextureRegionDrawable(textureManager.get("uielements/tutorial.png")));

		leaderboardBtn.setSize(buttonwidth, buttonheight);
		audioBtn.setSize(buttonwidth, buttonheight);
		exitBtn.setSize(buttonwidth, buttonheight);
		startBtn.setSize(buttonwidth, buttonheight);
		loadGameBtn.setSize(buttonwidth, buttonheight);
		tutorialBtn.setSize(buttonwidth, buttonheight);

		startBtn.setPosition(gameResolutionX / 10.0f, 4 * gameResolutionY / 5.0f - buttonheight / 2);
		leaderboardBtn.setPosition(gameResolutionX / 10.0f, 3 * gameResolutionY / 5.0f - buttonheight / 2);
		audioBtn.setPosition(gameResolutionX / 10.0f, 2 * gameResolutionY / 5.0f - buttonheight / 2);
		exitBtn.setPosition(gameResolutionX / 10.0f, gameResolutionY / 5.0f - buttonheight / 2);
		loadGameBtn.setPosition(gameResolutionX / 10.0f + startBtn.getWidth() + 50, 4 * gameResolutionY / 5.0f - buttonheight / 2);
		tutorialBtn.setPosition(gameResolutionX / 10.0f + startBtn.getWidth() + 50, 3 * gameResolutionY / 5.0f - buttonheight / 2);

		audioBtn.addListener(new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (state == STATE.main) {
					state = STATE.audio;
				} else {
					state = STATE.main;
				}
				super.touchUp(event, x, y, pointer, button);
			}
		});
		startBtn.addListener(new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				// Go to play screen, if still on the main screen
				if (!getScreenController().onScreen(Constants.MAIN_SCREEN_ID)) return;
				getScreenController().nextScreen(Constants.PLAY_SCREEN_ID);
			}
		});
		leaderboardBtn.addListener(new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (!game.screenController.onScreen(Constants.MAIN_SCREEN_ID)) return;
				getScreenController().nextScreen(Constants.LEADERBOARD_SCREEN_ID);
			}
		});
		exitBtn.addListener(new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.exit();
				super.touchUp(event, x, y, pointer, button);
			}
		});
		loadGameBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!game.screenController.onScreen(Constants.MAIN_SCREEN_ID)) return;
				loadGame();
			}
		});
		tutorialBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!game.screenController.onScreen(Constants.MAIN_SCREEN_ID)) return;
				GameScreen gameScreen = (GameScreen) getScreenController().getScreen(Constants.GAME_SCREEN_ID);
				TutorialLogic tutorialLogic = new TutorialLogic(gameScreen, textureManager, getAudioManager());
				TutorialRenderer tutorialRenderer = new TutorialRenderer(tutorialLogic);

				gameScreen.setGameLogic(tutorialLogic);
				gameScreen.setGameRenderer(tutorialRenderer);

				tutorialLogic.setDifficulty(Difficulty.EASY);

				game.screenController.setScreen(Constants.GAME_SCREEN_ID);
			}
		});

		stage = new Stage(viewport, game.batch);

		stage.addActor(startBtn);
		stage.addActor(leaderboardBtn);
		stage.addActor(audioBtn);
		stage.addActor(exitBtn);
		stage.addActor(loadGameBtn);
		stage.addActor(tutorialBtn);

		audioSliders = game.getAudioSettings().createAudioSliders(audioBtn.getX() + 650, audioBtn.getY() - 10, stage, audioEdit, vButton);

		musicSlider = audioSliders.getSlider(0);
		musicSlider.setTouchable(Touchable.disabled);

		gameSlider = audioSliders.getSlider(1);
		gameSlider.setTouchable(Touchable.disabled);
	}

	/**
	 * What should be done when the screen is shown
	 */
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
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
		if (state == STATE.audio) {

			musicSlider.setTouchable(Touchable.enabled);
			gameSlider.setTouchable(Touchable.enabled);

			game.batch.begin();

			audioSliders.render(game.batch);

			game.batch.end();
		} else {
			musicSlider.setTouchable(Touchable.disabled);
			gameSlider.setTouchable(Touchable.disabled);
		}
	}

	public void loadGame() {
		JsonValue saveData = SaveLoadGame.loadGameJson();
		if (saveData == null) return;
		GameType gameType = GameType.valueOf(saveData.getString("game_type"));
		GameScreen gameScreen = (GameScreen) game.screenController.getScreen(Constants.GAME_SCREEN_ID);
		GameLogic gameLogic;
		GameRenderer gameRenderer;
		switch (gameType) {
			case SCENARIO:
				gameLogic = new ScenarioLogic(gameScreen, getTextureManager(), getAudioManager());
				// If it's custom, set id and leaderboard name to custom values
				gameRenderer = new GameRenderer();
				break;
			case ENDLESS:
				gameLogic = new EndlessLogic(gameScreen, getTextureManager(), getAudioManager());
				gameRenderer = new GameRenderer();
				break;
			default:
				// If it reaches here, it's invalid.
				return;
		}

		gameLogic.setId(saveData.getString("scenario_id"));
		gameLogic.setDifficulty(Difficulty.asInt(saveData.getString("difficulty")));

		gameScreen.setGameLogic(gameLogic);
		gameScreen.setGameRenderer(gameRenderer);

		gameLogic.resetOnLoad = false;

		// Move to the game screen
		game.screenController.setScreen(Constants.GAME_SCREEN_ID);

		// Load the game
		SaveLoadGame.loadGame(gameLogic, saveData);
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
