package com.team3gdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.team3gdx.game.screen.GameScreen;
import com.team3gdx.game.screen.LeaderBoard;
import com.team3gdx.game.screen.MainScreen;
import com.team3gdx.game.util.AudioController;

public class MainGameClass extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public Music mainScreenMusic;
	public Music gameMusic;
	public static float musicVolumeScale;
	public static float gameVolumeScale;
	private MainScreen mainScreen1;
	private GameScreen gameScreen1;
	private LeaderBoard leaderBoardScreen1;
	public AudioController sounds;
	public ShapeRenderer shapeRenderer;

	@Override
	public void create() {
		// ====================================MUSIC=INITIALISATION======================================================
		musicVolumeScale = 0.4f;
		gameVolumeScale = 0.4f;
		mainScreenMusic = Gdx.audio.newMusic(Gdx.files.internal("uielements/MainScreenMusic.ogg"));
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("uielements/GameMusic.ogg"));
		mainScreenMusic.setLooping(false);
		mainScreenMusic.setVolume(musicVolumeScale);
		gameMusic.setLooping(false);
		gameMusic.setVolume(musicVolumeScale);
		// =================================SPRITEBATCH=AND=SHAPERENDERER================================================
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		// ==================================FONT=INITIALISATION=========================================================
		font = new BitmapFont(Gdx.files.internal("uielements/font.fnt"), Gdx.files.internal("uielements/font.png"),
				false);
		font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		// ====================================GAME=SCREEN=INITIALISATION================================================
		mainScreen1 = new MainScreen(this);
		gameScreen1 = new GameScreen(this, mainScreen1);
		leaderBoardScreen1 = new LeaderBoard(this, mainScreen1);
		this.setScreen(mainScreen1);
		// ==============================================================================================================
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
