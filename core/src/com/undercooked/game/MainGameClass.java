package com.undercooked.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.undercooked.game.audio.Audio;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.screen.LeaderBoard;
import com.undercooked.game.screen.MainScreen;
import com.undercooked.game.audio.AudioController;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.Constants;

public class MainGameClass extends Game {
	public BitmapFont font;
	public Music mainScreenMusic;
	public Music gameMusic;
	public static float musicVolumeScale;
	public static float gameVolumeScale;
	private MainScreen mainScreen1;
	private GameScreen gameScreen1;
	private LeaderBoard leaderBoardScreen1;
	public AudioController sounds;
	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;

	@Override
	public void create() {
		// =============MUSIC=INITIALISATION===========================
		musicVolumeScale = 0.4f;
		gameVolumeScale = 0.4f;
		Audio audioInst = Audio.getInstance();
		mainScreenMusic = audioInst.loadMusic("uielements/MainScreenMusic.ogg", Constants.MENU_SONG_ID, Constants.MUSIC_GROUP);
		gameMusic = audioInst.loadMusic("uielements/GameMusic.ogg", Constants.GAME_SONG_ID, Constants.MUSIC_GROUP);
		mainScreenMusic.setLooping(false);
		mainScreenMusic.setVolume(musicVolumeScale);
		gameMusic.setLooping(false);
		gameMusic.setVolume(musicVolumeScale);

		// Camera Initialisation
		CameraController.getCamera(Constants.WORLD_CAMERA_ID);
		CameraController.getCamera(Constants.UI_CAMERA_ID);

		// ===============SPRITEBATCH=AND=SHAPERENDERER==========================
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();


		// ===================FONT=INITIALISATION======================
		font = new BitmapFont(Gdx.files.internal("uielements/font.fnt"), Gdx.files.internal("uielements/font.png"),
				false);
		font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


		// ===============GAME=SCREEN=INITIALISATION===========================
		mainScreen1 = new MainScreen(this);
		gameScreen1 = new GameScreen(this, mainScreen1);
		leaderBoardScreen1 = new LeaderBoard(this, mainScreen1);
		this.setScreen(mainScreen1);


		// ==============================================================================================================
	}

	@Override
	public void resize(int width, int height) {
		CameraController.getViewport(Constants.WORLD_CAMERA_ID).update(width, height);
		CameraController.getViewport(Constants.UI_CAMERA_ID).update(width, height);
	}

	public MainScreen getMainScreen() {
		return mainScreen1;
	}

	public GameScreen getGameScreen() {
		return gameScreen1;
	}

	public LeaderBoard getLeaderBoardScreen() {
		return leaderBoardScreen1;
	}

	public void resetGameScreen() {
		this.gameScreen1 = new GameScreen(this, mainScreen1);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
