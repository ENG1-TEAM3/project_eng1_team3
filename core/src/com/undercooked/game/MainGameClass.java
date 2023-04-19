package com.undercooked.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.undercooked.game.Input.InputController;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.audio.SoundStateChecker;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.files.SettingsControl;
import com.undercooked.game.food.Item;
import com.undercooked.game.food.Request;
import com.undercooked.game.map.MapManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.audio.AudioSettings;
import com.undercooked.game.screen.*;
import com.undercooked.game.station.Station;
import com.undercooked.game.station.StationData;
import com.undercooked.game.station.StationManager;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.Constants;

public class MainGameClass extends Game {
	public Music mainScreenMusic;
	public Music gameMusic;
	public static float musicVolumeScale;
	public static float gameVolumeScale;
	public final ScreenController screenController;
	private final AssetManager assetManager;
	public final AudioManager audioManager;
	public final TextureManager textureManager;
	public final MapManager mapManager;
	public final AudioSettings audioSettings;
	public final SettingsControl settingsControl;
	public final StationManager stationManager;
	public static SpriteBatch batch;
	public static BitmapFont font;
	public static ShapeRenderer shapeRenderer;

	/**
	 * Constructor for the Game.
	 */
	public MainGameClass(SoundStateChecker soundChecker) {
		settingsControl = new SettingsControl("settings.json");
		audioSettings = new AudioSettings(this, settingsControl);
		assetManager = new AssetManager();
		// assetManager.setLoader(TiledMap.class, new TmxMapLoader(new
		// InternalFileHandleResolver()));
		audioManager = new AudioManager(assetManager, soundChecker);
		textureManager = new TextureManager(assetManager);
		mapManager = new MapManager(textureManager, audioManager);
		screenController = new ScreenController(this, assetManager);
		stationManager = new StationManager();
	}

	/**
	 * Things that are loaded into the game that won't be unloaded until the game is
	 * over.
	 */
	public void load() {
		// Load the settings
		settingsControl.loadData();

		// Load the controls
		InputController.loadControls();
	}

	@Override
	public void create() {

		// Temp Testing {#782, 18}
		// ! Temp Testing Code
		// 9 myCook = new Cook(new Vector2(0, 0), 0, textureManager, null);
		// myCook.addItem(new Item("<main>:burger"));
		// myCook.addItem(new Item("<main>:pizza"));
		// // myCook.addItem(new Item("Else"));
		// System.out.println(myCook.serial());

		// StationData myStationData = new StationData("<main>:counter");
		// Station myStation = new Station(myStationData);
		// myStation.addItem(new Item("<main>:burger"));
		// myStation.addItem(new Item("<main>:pizza"));
		// System.out.println(myStation.serial());

		// Request myRequest = new Request("<main>:burger");
		// myRequest.addInstruction("<main>:item/patty_unformed.png", "Grab Patty");
		// myRequest.addInstruction("<main>:item/patty.png", "Form Patty at Preparation
		// Station");
		// System.out.println(myRequest.serial());

		// Load the game
		load();

		// JsonFormat.main(new String[] {});

		// Load the default assets
		assetManager.finishLoading();

		// Renderers
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		shapeRenderer.setAutoShapeType(true);

		// =============MUSIC=INITIALISATION===========================
		audioSettings.loadVolumes();

		// Camera Initialisation
		CameraController.getCamera(Constants.WORLD_CAMERA_ID);
		CameraController.getCamera(Constants.UI_CAMERA_ID);

		// ===================FONT=INITIALISATION======================
		font = new BitmapFont(Gdx.files.internal("uielements/font.fnt"), Gdx.files.internal("uielements/font.png"),
				false);
		font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		// ===============GAME=SCREEN=INITIALISATION===========================

		screenController.addScreen(new MainScreen(this), Constants.MAIN_SCREEN_ID);
		screenController.addScreen(new GameScreen(this), Constants.GAME_SCREEN_ID);
		screenController.addScreen(new LeaderboardScreen(this), Constants.LEADERBOARD_SCREEN_ID);
		screenController.addScreen(new PauseScreen(this), Constants.PAUSE_SCREEN_ID);
		screenController.addScreen(new WinScreen(this), Constants.WIN_SCREEN_ID);
		screenController.addScreen(new LossScreen(this), Constants.LOSS_SCREEN_ID);
		screenController.addScreen(new PlayScreen(this), Constants.PLAY_SCREEN_ID);

		screenController.nextScreen(Constants.MAIN_SCREEN_ID);

		// ==============================================================================================================
	}

	@Override
	public void resize(int width, int height) {
		CameraController.getViewport(Constants.WORLD_CAMERA_ID).update(width, height);
		CameraController.getViewport(Constants.UI_CAMERA_ID).update(width, height);
	}

	public Screen getScreen() {
		return (Screen) super.getScreen();
	}

	public AudioManager getAudioManager() {
		return audioManager;
	}

	public TextureManager getTextureManager() {
		return textureManager;
	}

	public AudioSettings getAudioSettings() {
		return audioSettings;
	}

	public static SpriteBatch getSpriteBatch() {
		return batch;
	}

	public static ShapeRenderer getShapeRenderer() {
		return shapeRenderer;
	}

	public void unloadCurrentScreen() {
		screenController.unload((Screen) screen);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		mapManager.unload();
		assetManager.dispose();
		batch.dispose();
		shapeRenderer.dispose();
	}
}
