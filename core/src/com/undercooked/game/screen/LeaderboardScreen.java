package com.undercooked.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.leaderboard.LeaderboardController;
import com.undercooked.game.util.leaderboard.LeaderboardEntry;
import com.undercooked.game.util.leaderboard.LeaderboardType;

//INCORRECT FILE FORMATTING WILL CRASH GAME
//MAKE SURE ALL LINES IN LEADERBOARD FILE ARE x;y OR JUST s
//NO NEWLINE AT END OF FILE
public class LeaderboardScreen extends Screen {

	Texture background;
	Texture line;
	Texture leaderboard;
	OrthographicCamera camera;
	FitViewport viewport;
	Array<LeaderboardEntry> leaderboardData;
	private LeaderboardType currentLType;
	private String currentID;

	/**
	 * Constructor for leaderboard screen
	 * @param game - Entry point class
	 */
	public LeaderboardScreen(MainGameClass game) {
		super(game);
	}

	/**
	 * Read player data from leaderboard file
	 * Delete the leaderboard txt file to clear leaderboard
	 * The file starts with "s" and then adds scores
	 */
	public void readPlayerData() {
		/*playerData = new ArrayList<>();
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
		}*/
	}

	/**
	 * Order leaderboard data
	 */
	public void sortPlayerData() {
		/*Collections.sort(playerData, new Comparator<ArrayList<String>>() {
			@Override
			public int compare(ArrayList<String> e1, ArrayList<String> e2) {
				Integer i1 = Integer.valueOf(e1.get(1));
				Integer i2 = Integer.valueOf(e2.get(1));
				return (i1.compareTo(i2));
			}
		});*/
	}

	/**
	 * What should be done when screen is shown
	 */
	public void show() {
		TextureManager textureManager = game.getTextureManager();
		// Play the menu music
		game.mainScreenMusic.play();
		ScreenUtils.clear(0, 0, 0, 0);
		background = textureManager.get("uielements/MainScreenBackground.jpg");
		leaderboard = textureManager.get("uielements/LeaderBoard.png");
		line = textureManager.get("uielements/line.jpg");
		camera = CameraController.getCamera(Constants.UI_CAMERA_ID);
		viewport = CameraController.getViewport(Constants.UI_CAMERA_ID);
		game.font.getData().setScale((float) 2.5);
		Gdx.input.setInputProcessor(null);
	}

	/** What needs to be loaded when this screen is loaded. */
	public void load() {
		TextureManager textureManager = game.getTextureManager();
		textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/MainScreenBackground.jpg");
		textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/LeaderBoard.png");
		textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/line.jpg");

		game.audioManager.loadMusic("audio/music/MainScreenMusic.ogg", Constants.MUSIC_GROUP);

		// Load the leaderboard
		LeaderboardController.loadLeaderboard();

		showLeaderboard(LeaderboardType.SCENARIO, "<main>:main");
	}

	@Override
	public void unload() {
		game.getTextureManager().unload(Constants.LEADERBOARD_TEXTURE_ID);
		game.getTextureManager().unload(Constants.GAME_TEXTURE_ID);

		game.audioManager.unloadMusic("audio/music/MainScreenMusic.ogg");

		leaderboardData = null;

		// Unload the leaderboard
		LeaderboardController.unloadLeaderboard();
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

		// If the leaderboard isn't null...
		if (leaderboardData != null) {
				for (int scoreno = 0; scoreno < Math.min(leaderboardData.size, 7); scoreno++) {
					LeaderboardEntry thisEntry = leaderboardData.get(scoreno);
					float ycord = topentry - scoreno * eachentryhi - 0.3f * eachentryhi;
					String name = thisEntry.name;
					String stringScore = LeaderboardController.scoreToString(currentLType, thisEntry.score);
					game.font.draw(game.batch, name, 3 * gameResolutionX / 20.0f, ycord);
					game.font.draw(game.batch, stringScore, 11 * gameResolutionX / 20.0f, ycord);
				}
		}

		game.batch.end();

		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			changeScreenToMain();
		}
	}

	public void showLeaderboard(LeaderboardType lType, String id) {
		// Get the leaderboard
		Array<LeaderboardEntry> leaderboard = LeaderboardController.getEntries(lType, id);
		// Set the leaderboardData
		// If it's null, then it won't draw anything
		leaderboardData = leaderboard;

		System.out.println(leaderboardData);
	}

	/**
	 * Add data to leaderboard
	 * @param name - name of player
	 * @param score - score of player
	 */
	public void addLeaderBoardData(LeaderboardType lType, String id, String leaderboardName, String name, float score) {
		// Only continue if it's loaded
		if (leaderboardData == null) return;
		// Add it to the leaderboard
		LeaderboardController.addEntry(lType, id, leaderboardName, name, score);
	}

	/**
	 * Change screen back to main menu
	 */
	public void changeScreenToMain() {
		if (game.gameMusic != null) game.gameMusic.pause();
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
}
