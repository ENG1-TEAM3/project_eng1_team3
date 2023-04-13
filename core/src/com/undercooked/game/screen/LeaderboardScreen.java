package com.undercooked.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
import com.undercooked.game.screen.buttons.ModeButton;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.Listener;
import com.undercooked.game.util.leaderboard.Leaderboard;
import com.undercooked.game.util.leaderboard.LeaderboardController;
import com.undercooked.game.util.leaderboard.LeaderboardEntry;
import com.undercooked.game.GameType;

//INCORRECT FILE FORMATTING WILL CRASH GAME
//MAKE SURE ALL LINES IN LEADERBOARD FILE ARE x;y OR JUST s
//NO NEWLINE AT END OF FILE
public class LeaderboardScreen extends Screen {

	Texture background;
	Texture line;
	Texture leaderboardTexture;
	OrthographicCamera camera;
	FitViewport viewport;
	Leaderboard leaderboard;
	Array<LeaderboardEntry> leaderboardData;
	private GameType currentLType;
	private int currentIndex;
	private Stage stage;
	private ModeButton modeButton;
	private TextureRegionDrawable scenarioBtnDrawable;
	private TextureRegionDrawable endlessBtnDrawable;
	private Array<String> leaderboardIDs;
	private GlyphLayout leaderboardNameDisplay;
	private int firstScore;
	private String scoreText;
	private static final int SCORES_AT_ONCE = 5;
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
		textureManager.load(Constants.LEADERBOARD_TEXTURE_ID, "uielements/exittomenu.png");

		game.audioManager.loadMusic("audio/music/MainScreenMusic.ogg", Constants.MUSIC_GROUP);

		leaderboardNameDisplay = new GlyphLayout(game.font, "");

		// Load the leaderboard
		LeaderboardController.loadLeaderboard();

		camera = CameraController.getCamera(Constants.UI_CAMERA_ID);
		viewport = CameraController.getViewport(Constants.UI_CAMERA_ID);

		// Create the stage
		stage = new Stage(viewport, game.batch);

		// Create the mode button
		modeButton = new ModeButton(textureManager);
		modeButton.load(Constants.LEADERBOARD_TEXTURE_ID);

		// And go to the scenario leaderboard by default
		currentLType = null;
		goToLeaderboard(GameType.SCENARIO);
	}

	@Override
	public void unload() {
		game.getTextureManager().unload(Constants.LEADERBOARD_TEXTURE_ID, true);

		game.audioManager.unloadMusic("audio/music/MainScreenMusic.ogg");

		currentLType = null;
		currentIndex = 0;
		leaderboard = null;
		leaderboardData = null;
		leaderboardNameDisplay = null;

		stage.dispose();
		stage = null;
		modeButton = null;

		// Unload the leaderboard
		LeaderboardController.unloadLeaderboard();
	}

	/**
	 * What should be done when screen is shown
	 */
	public void show() {
		final TextureManager textureManager = game.getTextureManager();

		// Update the main screen music variable
		game.mainScreenMusic = game.audioManager.getMusic("audio/music/MainScreenMusic.ogg");
		// Play the menu music
		game.mainScreenMusic.play();
		ScreenUtils.clear(0, 0, 0, 0);
		background = textureManager.get("uielements/MainScreenBackground.jpg");
		leaderboardTexture = textureManager.get("uielements/LeaderBoard.png");
		line = textureManager.get("uielements/line.jpg");
		game.font.getData().setScale(2.5F);

		// Create the buttons
		Button leftBtn = new Button(new TextureRegionDrawable(textureManager.get("uielements/arrow_left.png")));
		Button rightBtn = new Button(new TextureRegionDrawable(textureManager.get("uielements/arrow_right.png")));
		Button menuBtn = new Button(new TextureRegionDrawable(textureManager.get("uielements/exittomenu.png")));

		modeButton.postLoad();
		modeButton.update();

		// Set their position and sizes
		leftBtn.setSize(128,128);
		leftBtn.setPosition(32,Constants.V_HEIGHT/2-leftBtn.getHeight());

		rightBtn.setSize(128,128);
		rightBtn.setPosition(Constants.V_WIDTH-rightBtn.getWidth()-32,Constants.V_HEIGHT/2-rightBtn.getHeight()/2);

		menuBtn.setSize(473, 144);
		menuBtn.setPosition(16,Constants.V_HEIGHT-menuBtn.getHeight()-16);

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

		modeButton.setListener(new Listener<GameType>() {
			@Override
			public void tell(GameType value) {
				goToLeaderboard(value);
			}
		});

		modeButton.setPosition(Constants.V_WIDTH/2, 72);

		menuBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changeScreenToMain();
			}
		});

		// Add the buttons to the stage
		stage.addActor(leftBtn);
		stage.addActor(rightBtn);
		stage.addActor(menuBtn);
		modeButton.addToStage(stage);

		// Add a scroll listener to go up / down on the scores
		stage.addListener(new InputListener() {
			@Override
			public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
				// Scroll the leaderboard
				scrollLeaderboard(amountY);

				return super.scrolled(event, x, y, amountX, amountY);
			}
		});

		// Update the name text
		updateNameText();

		// Set the input processor
		Gdx.input.setInputProcessor(stage);
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
		game.batch.draw(leaderboardTexture, lbox, dbox, boxwid, boxhi);
		game.batch.draw(line, lbox, dbox + eachentryhi, boxwid, gameResolutionY / 100.0f);
		game.batch.draw(line, lbox, dbox + 2 * eachentryhi, boxwid, gameResolutionY / 100.0f);
		game.batch.draw(line, lbox, dbox + 3 * eachentryhi, boxwid, gameResolutionY / 100.0f);
		game.batch.draw(line, lbox, dbox + 4 * eachentryhi, boxwid, gameResolutionY / 100.0f);
		game.batch.draw(line, lbox, dbox + 5 * eachentryhi, boxwid, gameResolutionY / 100.0f);
		game.batch.draw(line, lbox, dbox + 6 * eachentryhi, boxwid, gameResolutionY / 100.0f);
		game.font.setColor(Color.BLACK);

		game.font.draw(game.batch, leaderboardNameDisplay, Constants.V_WIDTH/2-(leaderboardNameDisplay.width/2), 17 * gameResolutionY / 20.0f);
		game.font.draw(game.batch, "Name", 4 * gameResolutionX / 20.0f, 15.5f * gameResolutionY / 20.0f);
		game.font.draw(game.batch, scoreText, 12 * gameResolutionX / 20.0f + 40, 15.5f * gameResolutionY / 20.0f);

		// If the leaderboard isn't null...
		if (leaderboardData != null) {
			int leaderboardIndex = 1;
			for (int scoreno = firstScore; scoreno < Math.min(leaderboardData.size, firstScore+SCORES_AT_ONCE); scoreno++) {
				LeaderboardEntry thisEntry = leaderboardData.get(scoreno);
				float ycord = topentry - leaderboardIndex * eachentryhi - 0.3f * eachentryhi;
				String name = thisEntry.name;
				String stringScore = LeaderboardController.scoreToString(currentLType, thisEntry.score);
				game.font.draw(game.batch, scoreno+1 + ". " + name, 3 * gameResolutionX / 20.0f, ycord);
				game.font.draw(game.batch, stringScore, 11 * gameResolutionX / 20.0f + 100, ycord);
				leaderboardIndex++;
			}
		}

		game.font.setColor(Color.WHITE);

		game.batch.end();
		stage.draw();

		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			changeScreenToMain();
		} else {
			stage.act();
		}
	}

	public void goToLeaderboard(GameType gameType) {
		// If it's already that leaderboard, just return
		if (gameType == currentLType) return;

		// Update the textures for the buttons
		modeButton.setCurrentType(gameType);
		modeButton.update();

		// Update the current type
		currentLType = gameType;

		// Update the visual
		switch (gameType) {
			case SCENARIO:
				scoreText = "Time";
				break;
			case ENDLESS:
				scoreText = "Served";
				break;
			default:
				scoreText = "";
		}

		// Otherwise, get the ids for the leaderboards of this type
		updateIDs();

		setIndex(0);
	}

	private void updateIDs() {
		leaderboardIDs = LeaderboardController.getIDs(currentLType);
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
		// If it's empty, just ignore
		if (leaderboardIDs.size == 0) {
			leaderboard = null;
			leaderboardData = null;
			updateNameText();
			return;
		}

		// Otherwise, make sure it's in the range
		while (index < 0) index += leaderboardIDs.size;
		if (index > 0) index %= leaderboardIDs.size;

		// And update the currentIndex
		currentIndex = index;

		// Then set it to use the id
		if (leaderboardIDs.size <= 0) return;
		showLeaderboard(currentLType, leaderboardIDs.get(index));
	}

	public void showLeaderboard(String id) {
		showLeaderboard(currentLType, id);
	}

	protected void updateNameText() {
		game.font.setColor(Color.BLACK);
		if (leaderboard == null) {
			leaderboardNameDisplay.setText(game.font, "No Leaderboards Available");
			game.font.setColor(Color.WHITE);
			return;
		}
		if (leaderboard.name != null) {
			leaderboardNameDisplay.setText(game.font, leaderboard.name);
		} else {
			leaderboardNameDisplay.setText(game.font, "");
		}
		game.font.setColor(Color.WHITE);
	}

	protected void showLeaderboard(GameType lType, String id) {
		// Get the leaderboard
		leaderboard = LeaderboardController.getLeaderboard(lType, id);
		// If it's null, set leaderboard data to null
		if (leaderboard == null) {
			leaderboardData = null;
			leaderboardNameDisplay.setText(game.font, "");
			return;
		}
		// Set the leaderboardData
		leaderboardData = leaderboard.copyLeaderboard();

		// Update the name text
		updateNameText();

		firstScore = 0;

	}

	/**
	 * Add data to leaderboard
	 * @param name - name of player
	 * @param score - score of player
	 */
	public void addLeaderBoardData(GameType lType, String id, String leaderboardName, String name, float score) {
		// Only continue if it's loaded
		if (!LeaderboardController.isLoaded()) return;
		// Add it to the leaderboard
		LeaderboardController.addEntry(lType, id, leaderboardName, name, score);
		// And save the leaderboard
		LeaderboardController.saveLeaderboard();
		// If the currently displayed leaderboard is the same, update the IDs.
		updateIDs();
	}

	/**
	 * Change screen back to main menu
	 */
	public void changeScreenToMain() {
		if (game.gameMusic != null) game.gameMusic.pause();
		if (getScreenController().onScreen(Constants.MAIN_SCREEN_ID)) return;
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
