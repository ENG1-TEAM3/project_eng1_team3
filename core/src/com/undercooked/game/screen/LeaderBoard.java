package com.undercooked.game.screen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.textures.Textures;

//INCORRECT FILE FORMATTING WILL CRASH GAME
//MAKE SURE ALL LINES IN LEADERBOARD FILE ARE x;y OR JUST s
//NO NEWLINE AT END OF FILE
public class LeaderBoard implements Screen, TextInputListener {
	final MainGameClass game;

	Texture background;
	Texture line;
	Texture leaderboard;
	OrthographicCamera camera;
	FitViewport viewport;
	MainScreen ms;
	ArrayList<ArrayList<String>> playerData;

	/**
	 * Constructor for leaderboard screen
	 * @param game - Entry point class
	 * @param ms - Main screen class
	 */
	public LeaderBoard(MainGameClass game, MainScreen ms) {
		this.game = game;
		this.ms = ms;

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
				return (i2.compareTo(i1));
			}
		});
	}

	/**
	 * What should be done when screen is shown
	 */
	public void show() {
		ScreenUtils.clear(0, 0, 0, 0);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, ms.gameResolutionX, ms.gameResolutionY);
		viewport = new FitViewport(ms.gameResolutionX, ms.gameResolutionY, camera);
		Textures texturesInst = Textures.getInstance();
		background = texturesInst.loadTexture("uielements/MainScreenBackground.jpg", "MSBackground");
		leaderboard = texturesInst.loadTexture("uielements/LeaderBoard.png","leaderboard");
		line = texturesInst.loadTexture("uielements/line.jpg", "line");
		game.font.getData().setScale((float) 2.5);
	}

	/**
	 * Screen render method
	 * @param delta - some change in time
	 */
	public void render(float delta) {
		// TODO Auto-generated method stub
		ScreenUtils.clear(0, 0, 0, 0);
		game.batch.setProjectionMatrix(camera.combined);
		float lbox = ms.gameResolutionX / 10.0f;
		float dbox = ms.gameResolutionY / 10.0f;
		float boxwid = 8 * lbox;
		float boxhi = 8 * dbox;
		float entryhi = 7 * dbox;
		float topentry = dbox + entryhi;
		float eachentryhi = entryhi / 7;

		game.batch.begin();
		game.batch.draw(background, 0, 0, ms.gameResolutionX, ms.gameResolutionY);
		game.batch.draw(leaderboard, lbox, dbox, boxwid, boxhi);
		game.font.draw(game.batch, "Press ESC to return to menu", ms.gameResolutionX / 20.0f,
				19 * ms.gameResolutionY / 19.0f);
		game.font.draw(game.batch, "Name", 4 * ms.gameResolutionX / 20.0f, 17 * ms.gameResolutionY / 20.0f);
		game.font.draw(game.batch, "Time (s)", 12 * ms.gameResolutionX / 20.0f, 17 * ms.gameResolutionY / 20.0f);
		game.batch.draw(line, lbox, dbox + eachentryhi, boxwid, ms.gameResolutionY / 100.0f);
		game.batch.draw(line, lbox, dbox + 2 * eachentryhi, boxwid, ms.gameResolutionY / 100.0f);
		game.batch.draw(line, lbox, dbox + 3 * eachentryhi, boxwid, ms.gameResolutionY / 100.0f);
		game.batch.draw(line, lbox, dbox + 4 * eachentryhi, boxwid, ms.gameResolutionY / 100.0f);
		game.batch.draw(line, lbox, dbox + 5 * eachentryhi, boxwid, ms.gameResolutionY / 100.0f);
		game.batch.draw(line, lbox, dbox + 6 * eachentryhi, boxwid, ms.gameResolutionY / 100.0f);

		for (int scoreno = 0; scoreno < 7; scoreno++) {
			if (this.playerData.size() >= scoreno + 1) {
				float ycord = topentry - scoreno * eachentryhi - 0.3f * eachentryhi;
				String name = this.playerData.get(scoreno).get(0);
				String stringScore = this.playerData.get(scoreno).get(1);
				game.font.draw(game.batch, name, 3 * ms.gameResolutionX / 20.0f, ycord);
				game.font.draw(game.batch, stringScore, 11 * ms.gameResolutionX / 20.0f, ycord);
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
	public void addLeaderBoardData(String name, int score) {
		String stringscore = Integer.toString(score);
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
		game.setScreen(game.getMainScreen());
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
