package com.undercooked.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.undercooked.game.audio.AudioManager;
import com.undercooked.game.audio.AudioSettings;
import com.undercooked.game.screen.*;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.Constants;

public class MainGameClass extends Game {
	public BitmapFont font;
	public Music mainScreenMusic;
	public Music gameMusic;
	public static float musicVolumeScale;
	public static float gameVolumeScale;
	public final ScreenController screenController;
	public static SpriteBatch batch;
	public static ShapeRenderer shapeRenderer;
	public static final AssetManager assetManager = new AssetManager();
	public final AudioManager audioManager;
	/**
	 * Constructor for the Game.
	 */
	public MainGameClass() {
		AudioSettings.game = this;
		audioManager = new AudioManager(assetManager);
		screenController = new ScreenController(this);
	}

	/**
	 * Things that are loaded into the game that won't be unloaded until the game is
	 * over.
	 */
	public void load() {

	}

	@Override
	public void create() {

		// Sprite Batch
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);

		// =============MUSIC=INITIALISATION===========================
		musicVolumeScale = 0.4f;
		gameVolumeScale = 0.4f;
		AudioSettings.setMusicVolume(Constants.DEFAULT_MUSIC, Constants.MUSIC_GROUP);
		AudioSettings.setMusicVolume(Constants.DEFAULT_SOUND, Constants.GAME_GROUP);

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
		screenController.addScreen(new LeaderBoard(this), Constants.LEADERBOARD_SCREEN_ID);

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
		assetManager.dispose();
		batch.dispose();
	}
}
