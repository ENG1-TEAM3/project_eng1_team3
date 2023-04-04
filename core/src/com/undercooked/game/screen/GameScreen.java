package com.undercooked.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.audio.AudioSettings;
import com.undercooked.game.audio.AudioSliders;
import com.undercooked.game.audio.Slider;
import com.undercooked.game.logic.GameLogic;
import com.undercooked.game.logic.ScenarioLogic;
import com.undercooked.game.map.Map;
import com.undercooked.game.render.GameRenderer;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.CollisionTile;
import com.undercooked.game.util.Constants;

/**
 * Responsible for handling all the rendering related tasks.
 * For game logic, see {@link GameLogic}.
 */
public class GameScreen extends Screen {
	GameLogic gameLogic;
	GameRenderer gameRenderer;

	Rectangle volSlideBackgr;
	Rectangle volSlide;
	Rectangle musSlideBackgr;
	Rectangle musSlide;
	Rectangle audioBackground;
	Rectangle optionsBackground;
	Texture ESC;
	Texture MENU;
	Texture BACKTOMAINSCREEN;
	Texture RESUME;
	Texture AUDIO;
	Texture audioEdit;
	Texture vControl;
	Texture vButton;
	Button mn;
	Button rs;
	Button ad;
	Button btms;
	/** The map tiles with collision. */
	public static CollisionTile[][] CLTiles;
	Viewport uiViewport;
	Viewport worldViewport;
	Stage stage;
	Stage stage2;
	OrthographicCamera uiCamera;
	public static OrthographicCamera worldCamera;

	int gameResolutionX;
	int gameResolutionY;
	float buttonwidth;
	float buttonheight;
	float xSliderMax;
	float xSliderMin;
	float sliderWidth;

	float audioBackgroundWidth;
	float audioBackgroundHeight;
	float audioBackgroundx;
	float audioBackgroundy;
	boolean paused;
	public Map map;

	/**
	 * Constructor to initialise game screen;
	 *
	 * @param game - Main entry point class
	 */
	public GameScreen(MainGameClass game) {
		super(game);
		this.calculateBoxMaths();
	}

	/**
	 * Set the {@link GameLogic}.
	 * @param gameLogic - The {@link GameLogic} instance.
	 */
	public void setGameLogic(GameLogic gameLogic) {
		this.gameLogic = gameLogic;
		gameLogic.setGameScreen(this);
		gameLogic.setStationManager(game.stationManager);
		// If it has a GameRenderer, update it there
		if (this.gameRenderer != null) {
			this.gameLogic.setGameRenderer(this.gameRenderer);
			this.gameRenderer.setLogic(gameLogic);
		}
	}

	/**
	 * Set the {@link GameRenderer}.
	 * @param gameRenderer - The {@link GameRenderer} instance.
	 */
	public void setGameRenderer(GameRenderer gameRenderer) {
		// Update the GameRenderer
		this.gameRenderer = gameRenderer;
		// If it's not null, update its SpriteBatch and ShapeRenderer
		if (gameRenderer != null) {
			gameRenderer.setSpriteBatch(game.batch);
			gameRenderer.setShapeRenderer(game.shapeRenderer);
		}
		// If this has a GameLogic, set it
		if (this.gameLogic != null) {
			this.gameLogic.setGameRenderer(this.gameRenderer);
			this.gameRenderer.setLogic(this.gameLogic);
		}
	}

	@Override
	public void load() {
		TextureManager textureManager = game.getTextureManager();
		textureManager.load(Constants.GAME_TEXTURE_ID, "entities/cook_walk_1.png");
		textureManager.load(Constants.GAME_TEXTURE_ID, "entities/cook_walk_2.png");
		textureManager.load(Constants.GAME_TEXTURE_ID, "entities/cook_walk_hands_1.png");
		textureManager.load(Constants.GAME_TEXTURE_ID, "entities/cook_walk_hands_2.png");
		// cc.load(Constants.GAME_TEXTURE_ID);
		textureManager.load(Constants.GAME_TEXTURE_ID, "entities/cust3f.png");
		textureManager.load(Constants.GAME_TEXTURE_ID, "entities/cust3b.png");
		textureManager.load(Constants.GAME_TEXTURE_ID, "entities/cust3r.png");
		textureManager.load(Constants.GAME_TEXTURE_ID, "entities/cust3l.png");
		textureManager.load(Constants.GAME_TEXTURE_ID, "uielements/settings.png");

		game.audioManager.loadMusic("audio/music/GameMusic.ogg", Constants.MUSIC_GROUP);
		game.audioManager.loadMusic("audio/soundFX/cash-register-opening.mp3", Constants.GAME_GROUP);
		game.audioManager.loadMusic("audio/soundFX/chopping.mp3", Constants.GAME_GROUP);
		game.audioManager.loadMusic("audio/soundFX/frying.mp3", Constants.GAME_GROUP);
		game.audioManager.loadMusic("audio/soundFX/money-collect.mp3", Constants.GAME_GROUP);
		game.audioManager.loadMusic("audio/soundFX/timer-bell-ring.mp3", Constants.GAME_GROUP);

		gameLogic.setTextureManager(textureManager);
		gameLogic.setAudioManager(getAudioManager());
		((ScenarioLogic) gameLogic).setId("<main>:main");
		gameLogic.load();
		gameRenderer.load(textureManager);
		// System.out.println(map.getAllEntities());
		// Add the map entities to the GameRenderer

	}

	@Override
	public void unload() {
		TextureManager textureManager = game.getTextureManager();

		gameLogic.unloadObjects();

		textureManager.unload(Constants.GAME_TEXTURE_ID);

		game.audioManager.unload(Constants.GAME_GROUP);
		game.audioManager.unload(Constants.MUSIC_GROUP);
		game.audioManager.unload(Constants.GAME_SOUND_GROUP);
		stage.dispose();
		stage2.dispose();

		gameLogic.dispose();
		gameRenderer.unload(textureManager);
	}

	/**
	 * When the GameScreen is shown.
	 */
	@Override
	public void show() {
		// When this screen is shown, reset the input processor
		Gdx.input.setInputProcessor(stage);
		// As it's showing, it's not paused
		paused = false;
	}

	/**
	 * Resets the currently loaded game.
	 */
	public void reset() {
		// When this function is called, reset all the game's variables,
		// and current situation. It should be as if the game was just
		// started.

		// Reset the game logic
		gameLogic.reset();
	}

	/**
	 * Things that should be done when the GameScreen has finished
	 * loading.
	 */
	@Override
	public void postLoad() {

		// Get the game music
		game.gameMusic = game.audioManager.getMusic("audio/music/GameMusic.ogg");

		// =======================================SET=POSITIONS=OF=SLIDERS===============================================
		float currentMusicVolumeSliderX = (AudioSettings.getMusicVolume() * sliderWidth) + xSliderMin;
		float currentGameVolumeSliderX = (AudioSettings.getGameVolume() * sliderWidth) + xSliderMin;
		musSlide.setPosition(currentMusicVolumeSliderX, audioBackgroundy + 4 * audioBackgroundHeight / 6
				+ musSlideBackgr.getHeight() / 2 - musSlide.getHeight() / 2);
		volSlide.setPosition(currentGameVolumeSliderX, audioBackgroundy + audioBackgroundHeight / 6
				+ volSlideBackgr.getHeight() / 2 - volSlide.getHeight() / 2);
		// ======================================INHERIT=TEXTURES=FROM=MAIN=SCREEN=======================================
		vButton = game.getTextureManager().get("uielements/vButton.jpg");
		vControl = game.getTextureManager().get("uielements/vControl.png");
		// ======================================START=CAMERAS===========================================================
		worldCamera = CameraController.getCamera(Constants.WORLD_CAMERA_ID);
		uiCamera = CameraController.getCamera(Constants.UI_CAMERA_ID);
		// ======================================SET=INITAL=STATE========================================================

		// ======================================START=VIEWPORTS=========================================================
		worldViewport = CameraController.getViewport(Constants.WORLD_CAMERA_ID);
		uiViewport = CameraController.getViewport(Constants.UI_CAMERA_ID);
		// ======================================START=STAGES============================================================
		stage = new Stage(uiViewport);
		stage2 = new Stage(uiViewport);
		// ======================================LOAD=TEXTURES===========================================================
		TextureManager textureManager = game.getTextureManager();
		MENU = textureManager.get("uielements/settings.png");
		ESC = textureManager.get("uielements/background.png");
		BACKTOMAINSCREEN = textureManager.get("uielements/exitmenu.png");
		RESUME = textureManager.get("uielements/resume.png");
		AUDIO = textureManager.get("uielements/audio2.png");
		audioEdit = textureManager.get("uielements/background.png");
		// ======================================CREATE=BUTTONS==========================================================
		mn = new Button(new TextureRegionDrawable(MENU));
		ad = new Button(new TextureRegionDrawable(AUDIO));
		rs = new Button(new TextureRegionDrawable(RESUME));
		btms = new Button(new TextureRegionDrawable(BACKTOMAINSCREEN));
		// ======================================POSITION=AND=SCALE=BUTTONS==============================================
		mn.setPosition(gameResolutionX / 40.0f, 18 * gameResolutionY / 20.0f);
		mn.setSize(buttonwidth, buttonheight);
		rs.setPosition(gameResolutionX / 40.0f, 18 * gameResolutionY / 20.0f);
		rs.setSize(buttonwidth, buttonheight);
		ad.setPosition(rs.getX() + rs.getWidth() + 2 * (gameResolutionX / 40.0f - gameResolutionX / 50.0f), rs.getY());
		ad.setSize(buttonwidth, buttonheight);
		btms.setPosition(ad.getX() + ad.getWidth() + 2 * (gameResolutionX / 40.0f - gameResolutionX / 50.0f),
				ad.getY());
		btms.setSize(buttonwidth, buttonheight);
		// ======================================ADD=LISTENERS=TO=BUTTONS================================================
		final GameScreen gs = this;
		mn.addListener(new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				pauseGame();
			}
		});
		// ======================================ADD=BUTTONS=TO=STAGES===================================================
		stage.addActor(mn);
		stage2.addActor(rs);
		stage2.addActor(btms);
		stage2.addActor(ad);

		// GameLogic post load
		gameLogic.postLoad();

		// GameRenderer post load
		gameRenderer.postLoad(textureManager);

		// AudioManager post load
		game.audioManager.postLoad();

	}

	public void pauseGame() {
		gameLogic.pause();
		game.screenController.nextScreen(Constants.PAUSE_SCREEN_ID);
	}

	AudioSliders audioSliders;
	Slider musicSlider, gameSlider;

	/**
	 * Render method for main game
	 *
	 * @param delta - some change in time
	 */

	public void render(float delta) {

		// Pause the game before anything else
		stage.act();
		if (!paused && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			pauseGame();
			return;
		}
		// If the game is paused, then don't continue
		if (paused) return;

		// Play Game Music
		game.gameMusic.play();

		// Update the game logic.
		gameLogic.update(delta);

		// Move the camera for the game renderer
		gameRenderer.moveCamera(delta);

		// Render the game
		renderScreen(delta);
		// Draw Pause Button
		MainGameClass.batch.setProjectionMatrix(uiCamera.combined);
		stage.draw();

		// =========================================CHECK=GAME=OVER======================================================

	}

	@Override
	public void renderScreen(float delta) {
		gameRenderer.render(delta);
	}

	public static final float MAX_WAIT_TIME = 1000000; //Customer wait time in ms

	long nowTime = 0;
	long thenTime = 0;

	/**
	 * Calculates coordinates for UI element scaling;
	 */
	private void calculateBoxMaths() {
		this.gameResolutionX = Constants.V_WIDTH;
		this.gameResolutionY = Constants.V_HEIGHT;
		this.buttonwidth = gameResolutionX / 10.0f;
		this.buttonheight = gameResolutionY / 20.0f;

		this.audioBackgroundWidth = gameResolutionX / 6.0f;
		this.audioBackgroundHeight = gameResolutionY / 6.0f;

		this.audioBackgroundx = gameResolutionX / 40.0f + buttonwidth
				+ 2 * (gameResolutionX / 40.0f - gameResolutionX / 50.0f);
		this.audioBackgroundy = 7 * gameResolutionY / 10.0f;

		this.optionsBackground = new Rectangle();
		optionsBackground.setPosition(gameResolutionX / 50.0f, 35 * gameResolutionY / 40.0f);
		optionsBackground.width = 3 * (buttonwidth + 2 * (gameResolutionX / 40.0f - gameResolutionX / 50.0f));
		optionsBackground.height = 4 * gameResolutionY / 40.0f;

		this.audioBackground = new Rectangle();
		audioBackground.setPosition(audioBackgroundx, audioBackgroundy);
		audioBackground.width = audioBackgroundWidth;
		audioBackground.height = audioBackgroundHeight;

		this.volSlide = new Rectangle();
		volSlide.width = audioBackgroundHeight / 4.0f;
		volSlide.height = audioBackgroundHeight / 4.0f;

		this.volSlideBackgr = new Rectangle();
		volSlideBackgr.width = 2 * audioBackgroundWidth / 3.0f;
		volSlideBackgr.height = audioBackgroundHeight / 6.0f;
		volSlideBackgr.setPosition(audioBackgroundx + audioBackgroundWidth / 6,
				audioBackgroundy + audioBackgroundHeight / 6);

		this.musSlide = new Rectangle();
		musSlide.width = audioBackgroundHeight / 4.0f;
		musSlide.height = audioBackgroundHeight / 4.0f;

		this.musSlideBackgr = new Rectangle();
		musSlideBackgr.width = 2 * audioBackgroundWidth / 3.0f;
		musSlideBackgr.height = audioBackgroundHeight / 6.0f;
		musSlideBackgr.setPosition(audioBackgroundx + audioBackgroundWidth / 6,
				audioBackgroundy + 4 * audioBackgroundHeight / 6);

		this.xSliderMin = audioBackgroundx + audioBackgroundWidth / 6;
		this.xSliderMax = xSliderMin + volSlideBackgr.width;
		this.sliderWidth = volSlideBackgr.width;
	}

	/**
	 * Resize game screen
	 *
	 * @param width  - width to resize to
	 * @param height - height to resize to
	 */
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}
}
