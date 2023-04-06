package com.undercooked.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
	private int currentIndex;
	private Stage stage;
	private TextureRegionDrawable scenarioBtnDrawable;
	private TextureRegionDrawable endlessBtnDrawable;
	private Array<String> leaderboardIDs;
	private int firstScore;
	private String scoreText;
	private static final int SCORES_AT_ONCE = 6;
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

	/** What needs to be loaded when this screen is loaded. */
	public void load() {
		TextureManager textureManager = game.getTextureManager();
		textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/MainScreenBackground.jpg");
		textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/LeaderBoard.png");
		textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/line.jpg");
		textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/arrow_left.png");
		textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/arrow_right.png");
		textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/scenario.png");
		textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/scenario_off.png");
		textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/endless.png");
		textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/endless_off.png");

		game.audioManager.loadMusic("audio/music/MainScreenMusic.ogg", Constants.MUSIC_GROUP);

		// Load the leaderboard
		LeaderboardController.loadLeaderboard();
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
		game.font.getData().setScale(2.5F);
		Gdx.input.setInputProcessor(null);

		// Create the stage
		stage = new Stage(viewport, game.batch);

		// Create the buttons
		Button leftBtn = new Button(new TextureRegionDrawable(textureManager.get("uielements/arrow_left.png")));
		Button rightBtn = new Button(new TextureRegionDrawable(textureManager.get("uielements/arrow_right.png")));

		scenarioBtnDrawable = new TextureRegionDrawable(textureManager.get("uielements/scenario.png"));
		Button scenarioBtn = new Button(scenarioBtnDrawable);
		endlessBtnDrawable = new TextureRegionDrawable(textureManager.get("uielements/endless_off.png"));
		Button endlessBtn = new Button(endlessBtnDrawable);

		// Set their position and sizes
		leftBtn.setSize(128,128);
		leftBtn.setPosition(32,Constants.V_HEIGHT/2-64);

		rightBtn.setSize(128,128);
		rightBtn.setPosition(Constants.V_WIDTH-160,Constants.V_HEIGHT/2-64);

		scenarioBtn.setSize(473, 144);
		scenarioBtn.setPosition(Constants.V_WIDTH/2-473,72);

		endlessBtn.setSize(473, 144);
		endlessBtn.setPosition(Constants.V_WIDTH/2,72);

		// Add ClickListeners to the buttons
		leftBtn.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				setIndex(currentIndex-1);
			}
		});

		rightBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				setIndex(currentIndex+1);
			}
		});

		scenarioBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				goToLeaderboard(LeaderboardType.SCENARIO);
			}
		});

		endlessBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				goToLeaderboard(LeaderboardType.ENDLESS);
			}
		});

		// Add the buttons to the stage
		stage.addActor(leftBtn);
		stage.addActor(rightBtn);
		stage.addActor(scenarioBtn);
		stage.addActor(endlessBtn);

		// Set the input processor
		Gdx.input.setInputProcessor(stage);

		// Open the Scenario leaderboard
		currentLType = null;
		goToLeaderboard(LeaderboardType.SCENARIO);
	}

	public void goToLeaderboard(LeaderboardType leaderboardType) {
		// If it's already that leaderboard, just return
		if (leaderboardType == currentLType) return;

		// Update the textures for the buttons
		switch (leaderboardType) {
			case SCENARIO:
				// Scenario is active, Endless is not
				scenarioBtnDrawable.setRegion(new TextureRegion(getTextureManager().get("uielements/scenario.png")));
				endlessBtnDrawable.setRegion(new TextureRegion(getTextureManager().get("uielements/endless_off.png")));
				scoreText = "Time";
				break;
			case ENDLESS:
				// Endless is active, Scenario is not
				endlessBtnDrawable.setRegion(new TextureRegion(getTextureManager().get("uielements/endless.png")));
				scenarioBtnDrawable.setRegion(new TextureRegion(getTextureManager().get("uielements/scenario_off.png")));
				scoreText = "Served";
				break;

		}

		// Add a scroll listener to go up / down on the scores
		stage.addListener(new InputListener() {
			@Override
			public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
				// Scroll the leaderboard
				scrollLeaderboard(amountY);

				return super.scrolled(event, x, y, amountX, amountY);
			}
		});

		// Update the current type
		currentLType = leaderboardType;

		// Otherwise, get the ids for the leaderboards of this type
		leaderboardIDs = LeaderboardController.getIDs(leaderboardType);

		setIndex(0);
	}

	private void scrollLeaderboard(float amountY) {
		// Add the scroll
		firstScore += Math.signum(amountY);
		// Clamp it between 0 and the number of possible
		// Allows one scroll below the lowest to show that there's
		// no more to scroll through.
		firstScore = Math.max(0, Math.min(firstScore, leaderboardData.size-(SCORES_AT_ONCE-1)));
	}

	public void setIndex(int index) {
		// If leaderboardIDs is null, return
		if (leaderboardIDs == null) return;

		// Otherwise, make sure it's in the range
		while (index < 0) index += leaderboardIDs.size;
		if (index > 0) index %= leaderboardIDs.size;

		// And update the currentIndex
		currentIndex = index;

		// Then set it to use the id
		showLeaderboard(currentLType, leaderboardIDs.get(index));
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
		game.font.draw(game.batch, scoreText, 12 * gameResolutionX / 20.0f, 17 * gameResolutionY / 20.0f);
		game.batch.draw(line, lbox, dbox + eachentryhi, boxwid, gameResolutionY / 100.0f);
		game.batch.draw(line, lbox, dbox + 2 * eachentryhi, boxwid, gameResolutionY / 100.0f);
		game.batch.draw(line, lbox, dbox + 3 * eachentryhi, boxwid, gameResolutionY / 100.0f);
		game.batch.draw(line, lbox, dbox + 4 * eachentryhi, boxwid, gameResolutionY / 100.0f);
		game.batch.draw(line, lbox, dbox + 5 * eachentryhi, boxwid, gameResolutionY / 100.0f);
		game.batch.draw(line, lbox, dbox + 6 * eachentryhi, boxwid, gameResolutionY / 100.0f);

		// If the leaderboard isn't null...
		if (leaderboardData != null) {
			int leaderboardIndex = 0;
			for (int scoreno = firstScore; scoreno < Math.min(leaderboardData.size, firstScore+SCORES_AT_ONCE); scoreno++) {
				LeaderboardEntry thisEntry = leaderboardData.get(scoreno);
				float ycord = topentry - leaderboardIndex * eachentryhi - 0.3f * eachentryhi;
				String name = thisEntry.name;
				String stringScore = LeaderboardController.scoreToString(currentLType, thisEntry.score);
				game.font.draw(game.batch, scoreno+1 + ". " + name, 3 * gameResolutionX / 20.0f, ycord);
				game.font.draw(game.batch, stringScore, 11 * gameResolutionX / 20.0f, ycord);
				leaderboardIndex++;
			}
		}

		game.batch.end();

		stage.act();
		stage.draw();

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

		firstScore = 0;

		// System.out.println(leaderboardData);
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
