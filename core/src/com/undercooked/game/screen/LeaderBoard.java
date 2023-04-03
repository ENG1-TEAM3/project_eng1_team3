package com.undercooked.game.screen;

import java.awt.event.ContainerAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.Constants;

//INCORRECT FILE FORMATTING WILL CRASH GAME
//MAKE SURE ALL LINES IN LEADERBOARD FILE ARE x;y OR JUST s
//NO NEWLINE AT END OF FILE
public class LeaderBoard extends Screen implements TextInputListener {

	Texture background;
	Texture line;
	Texture leaderboard;
	OrthographicCamera camera;
	FitViewport viewport;
	ArrayList<ArrayList<String>> playerData;

	/**
	 * Constructor for leaderboard screen
	 * @param game - Entry point class
	 */
	public LeaderBoard(MainGameClass game) {
		super(game);

		readPlayerData();
		sortPlayerData();
	}

	/**
	 * Read player data from leaderboard file
	 * Delete the leaderboard txt file to clear leaderboard
	 * The file starts with "s" and then adds scores
	 */
	public void readPlayerData() {
		playerData = new ArrayList<>();
		boolean doesPlayerDataExist = Gdx.files.local("leaderboarddata/playerData.txt").exists();
		if (doesPlayerDataExist) {
			FileHandle handle = Gdx.files.local("leaderboarddata/playerData.txt");
			String text = handle.readString();
			String[] entries = text.split("\\n");
			for (String s : entries) {
				if(!s.equals("s")) {
					String[] parts = s.split(";");
					if (parts.length < 2) continue;
					String name = parts[0];
					String stringScore = parts[1].trim();
					ArrayList<String> sublist = new ArrayList<>();
					sublist.add(name);
					sublist.add(stringScore);
					playerData.add(sublist);
				}
			}
		} else {
			FileHandle file = Gdx.files.local("leaderboarddata/playerData.txt");
			file.writeString("s",true);
		}
	}

	/**
	 * Order leaderboard data
	 */
	public void sortPlayerData() {
		Collections.sort(playerData, new Comparator<ArrayList<String>>() {
			@Override
			public int compare(ArrayList<String> e1, ArrayList<String> e2) {
				Integer i1 = Integer.valueOf(e1.get(1));
				Integer i2 = Integer.valueOf(e2.get(1));
				return (i1.compareTo(i2));
			}
		});
	}

	/**
	 * What should be done when screen is shown
	 */
	public void show() {
		TextureManager textureManager = game.getTextureManager();
		game.gameMusic = game.getAudioManager().getMusic("audio/music/GameMusic.ogg");
		ScreenUtils.clear(0, 0, 0, 0);
		background = textureManager.get("uielements/MainScreenBackground.jpg");
		leaderboard = textureManager.get("uielements/LeaderBoard.png");
		line = textureManager.get("uielements/line.jpg");
		camera = CameraController.getCamera(Constants.UI_CAMERA_ID);
		viewport = CameraController.getViewport(Constants.UI_CAMERA_ID);
		game.font.getData().setScale((float) 2.5);
	}

	/** What needs to be loaded when this screen is loaded. */
	public void load() {
		TextureManager textureManager = game.getTextureManager();
		textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/MainScreenBackground.jpg");
		textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/LeaderBoard.png");
		textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/line.jpg");

		game.audioManager.loadMusic("audio/music/GameMusic.ogg", Constants.MUSIC_GROUP);
	}

	@Override
	public void unload() {
		game.getTextureManager().unload(Constants.LEADERBOARD_TEXTURE_ID);
		game.getTextureManager().unload(Constants.GAME_TEXTURE_ID);

		game.audioManager.unloadMusic("audio/music/GameMusic.ogg");
	}

	/**
	 * Screen render method
	 * @param delta - some change in time
	 */
	public void render(float delta) {
		// TODO Auto-generated method stub
		ScreenUtils.clear(0, 0, 0, 0);
		game.batch.setProjectionMatrix(camera.combined);
		float gameResolutionX = Constants.V_WIDTH,
			  gameResolutionY = Constants.V_HEIGHT;
		float lbox = gameResolutionX / 10.0f;
		float dbox = gameResolutionY / 10.0f;
		float boxwid = 8 * lbox;
		float boxhi = 8 * dbox;
		float entryhi = 7 * dbox;
		float topentry = dbox + entryhi;
		float eachentryhi = entryhi / 7;

		game.batch.begin();
		game.batch.draw(background, 0, 0, gameResolutionX, gameResolutionY);
		game.batch.draw(leaderboard, lbox, dbox, boxwid, boxhi);
		game.font.draw(game.batch, "Press ESC to return to menu", gameResolutionX / 20.0f,
				19 * gameResolutionY / 19.0f);
		game.font.draw(game.batch, "Name", 4 * gameResolutionX / 20.0f, 17 * gameResolutionY / 20.0f);
		game.font.draw(game.batch, "Time (s)", 12 * gameResolutionX / 20.0f, 17 * gameResolutionY / 20.0f);
		game.batch.draw(line, lbox, dbox + eachentryhi, boxwid, gameResolutionY / 100.0f);
		game.batch.draw(line, lbox, dbox + 2 * eachentryhi, boxwid, gameResolutionY / 100.0f);
		game.batch.draw(line, lbox, dbox + 3 * eachentryhi, boxwid, gameResolutionY / 100.0f);
		game.batch.draw(line, lbox, dbox + 4 * eachentryhi, boxwid, gameResolutionY / 100.0f);
		game.batch.draw(line, lbox, dbox + 5 * eachentryhi, boxwid, gameResolutionY / 100.0f);
		game.batch.draw(line, lbox, dbox + 6 * eachentryhi, boxwid, gameResolutionY / 100.0f);

		for (int scoreno = 0; scoreno < 7; scoreno++) {
			if (this.playerData.size() >= scoreno + 1) {
				float ycord = topentry - scoreno * eachentryhi - 0.3f * eachentryhi;
				String name = this.playerData.get(scoreno).get(0);
				String stringScore = this.playerData.get(scoreno).get(1);
				game.font.draw(game.batch, name, 3 * gameResolutionX / 20.0f, ycord);
				game.font.draw(game.batch, stringScore, 11 * gameResolutionX / 20.0f, ycord);
			}
		}

		game.batch.end();
		Gdx.input.setInputProcessor(new InputAdapter() {
			public boolean keyDown(int key) {
				if (key == Input.Keys.ESCAPE) {
					changeScreenToMain();
				}
				return super.keyDown(key);
			}
		});
	}

	/**
	 * Add data to leaderboard
	 * @param name - name of player
	 * @param score - score of player
	 */
	public void addLeaderBoardData(String name, float score) {
		String stringscore = Integer.toString((int) score);
		FileHandle handle = Gdx.files.local("leaderboarddata/playerData.txt");
		handle.writeString("\n" + name + ";" + stringscore, true);
		this.readPlayerData();
		this.sortPlayerData();
	}

	/**
	 * Change screen back to main menu
	 */
	public void changeScreenToMain() {
		game.gameMusic.pause();
		game.screenController.setScreen(Constants.MAIN_SCREEN_ID);
	}

	/**
	 * Change window size
	 * @param width - new window size
	 * @param height - new window height
	 */
	@Override
	public void resize(int width, int height) {
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

	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void create() {
		// TODO Auto-generated method stub

	}

	@Override
	public void input(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void canceled() {
		// TODO Auto-generated method stub

	}
}
