package com.team3gdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team3gdx.game.MainGameClass;
import com.team3gdx.game.entity.Cook;
import com.team3gdx.game.entity.Customer;
import com.team3gdx.game.entity.CustomerController;
import com.team3gdx.game.entity.Entity;
import com.team3gdx.game.food.Menu;
import com.team3gdx.game.station.StationManager;
import com.team3gdx.game.util.CollisionTile;
import com.team3gdx.game.util.Control;

public class GameScreen implements Screen {

	public static final int NUMBER_OF_WAVES = 5;

	final MainGameClass game;
	final MainScreen ms;

	public static int currentWave = 0;
	public static int money = 0;

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
	public static CollisionTile[][] CLTiles;
	Viewport uiViewport;
	Viewport worldViewport;
	Stage stage;
	Stage stage2;
	OrthographicCamera uiCamera;
	public static OrthographicCamera worldCamera;

	public static Customer currentWaitingCustomer = null;

	public enum STATE {
		Pause, Continue, main, audio
	}

	public static STATE state1;
	float v;
	float s;
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
	long startTime;
	long timeOnStartup;
	long tempTime, tempThenTime;
	public static Control control;
	TiledMapRenderer tiledMapRenderer;
	public TiledMap map1;
	public static Cook[] cooks = { new Cook(new Vector2(64 * 5, 64 * 3), 1), new Cook(new Vector2(64 * 5, 64 * 5), 2) };
	public static int currentCookIndex = 0;
	public static Cook cook = cooks[currentCookIndex];
	public static CustomerController cc;
	InputMultiplexer multi;
	StationManager stationManager = new StationManager();

	/**
	 * Constructor to initialise game screen;
	 * 
	 * @param game - Main entry point class
	 * @param ms   - Title screen class
	 */
	public GameScreen(MainGameClass game, MainScreen ms) {
		this.game = game;
		this.ms = ms;
		this.calculateBoxMaths();
		control = new Control();
		// map = new TmxMapLoader().load("map/art_map/prototype_map.tmx");
		map1 = new TmxMapLoader().load("map/art_map/customertest.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(map1);
		constructCollisionData(map1);
		cc = new CustomerController(map1);
		cc.spawnCustomer();
	}

	/**
	 * Things that should be done while the game screen is shown
	 */
	public void show() {
		// =======================================START=FRAME=TIMER======================================================
		startTime = System.currentTimeMillis();
		timeOnStartup = startTime;
		tempThenTime = startTime;
		// =======================================SET=POSITIONS=OF=SLIDERS===============================================
		float currentMusicVolumeSliderX = (MainGameClass.musicVolumeScale * sliderWidth) + xSliderMin;
		float currentGameVolumeSliderX = (MainGameClass.gameVolumeScale * sliderWidth) + xSliderMin;
		musSlide.setPosition(currentMusicVolumeSliderX, audioBackgroundy + 4 * audioBackgroundHeight / 6
				+ musSlideBackgr.getHeight() / 2 - musSlide.getHeight() / 2);
		volSlide.setPosition(currentGameVolumeSliderX, audioBackgroundy + audioBackgroundHeight / 6
				+ volSlideBackgr.getHeight() / 2 - volSlide.getHeight() / 2);
		// ======================================INHERIT=TEXTURES=FROM=MAIN=SCREEN=======================================
		vButton = ms.vButton;
		vControl = ms.vControl;
		// ======================================START=CAMERAS===========================================================
		uiCamera = new OrthographicCamera();
		worldCamera = new OrthographicCamera();
		uiCamera.setToOrtho(false, gameResolutionX, gameResolutionY);
		worldCamera.setToOrtho(false, gameResolutionX, gameResolutionY);
		// ======================================SET=INITAL=STATE========================================================
		state1 = STATE.Continue;
		// ======================================START=VIEWPORTS=========================================================
		worldViewport = new FitViewport(gameResolutionX, gameResolutionY, worldCamera);
		uiViewport = new FitViewport(gameResolutionX, gameResolutionY, uiCamera);
		// ======================================START=STAGES============================================================
		stage = new Stage(uiViewport);
		stage2 = new Stage(uiViewport);
		// ======================================CREATE=INPUTMULTIPLEXER=================================================
		multi = new InputMultiplexer(stage, control);
		// ======================================LOAD=TEXTURES===========================================================
		MENU = new Texture(Gdx.files.internal("uielements/settings.png"));
		ESC = new Texture(Gdx.files.internal("uielements/background.png"));
		BACKTOMAINSCREEN = new Texture(Gdx.files.internal("uielements/exitmenu.png"));
		RESUME = new Texture(Gdx.files.internal("uielements/resume.png"));
		AUDIO = new Texture(Gdx.files.internal("uielements/audio2.png"));
		audioEdit = new Texture(Gdx.files.internal("uielements/background.png"));
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
		mn.addListener(new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				state1 = STATE.Pause;
				super.touchUp(event, x, y, pointer, button);
			}
		});
		rs.addListener(new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				state1 = STATE.Continue;
				super.touchUp(event, x, y, pointer, button);
			}
		});
		ad.addListener(new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				state1 = STATE.audio;
				super.touchUp(event, x, y, pointer, button);
			}
		});
		btms.addListener(new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				state1 = STATE.main;
				super.touchUp(event, x, y, pointer, button);
			}
		});
		// ======================================ADD=BUTTONS=TO=STAGES===================================================
		stage.addActor(mn);
		stage2.addActor(rs);
		stage2.addActor(btms);
		stage2.addActor(ad);

	}

	ShapeRenderer selectedPlayerBox = new ShapeRenderer();

	/**
	 * Render method for main game
	 * 
	 * @param delta - some change in time
	 */

	public void render(float delta) {
		// =====================================CLEAR=SCREEN=============================================================
		ScreenUtils.clear(0, 0, 0, 0);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// =====================================SET=INPUT=PROCESSOR======================================================
		Gdx.input.setInputProcessor(multi);
		// =====================================SET=PROJECTION=MATRICES=FOR=GAME=RENDERING===============================
		game.shapeRenderer.setProjectionMatrix(worldCamera.combined);
		game.batch.setProjectionMatrix(worldCamera.combined);
		// =====================================RENDER=BOTTOM=MAP=LAYER==================================================
		tiledMapRenderer.setView(worldCamera);
		tiledMapRenderer.render(new int[] { 0 });
		// =====================================DRAW=COOK=LEGS===========================================================
		game.batch.begin();
		for (Cook curCook : cooks)
			curCook.draw_bot(game.batch);
		game.batch.end();
		// =====================================RENDER=TOP=MAP=LAYER=====================================================
		tiledMapRenderer.render(new int[] { 1 });
		// =====================================DRAW=COOK=TOP=HALF=======================================================
		stationManager.handleStations(game.batch, game.shapeRenderer);
		drawHeldItems();
		game.batch.begin();
		for (Cook curCook : cooks)
			curCook.draw_top(game.batch);
		cc.drawCustTop(game.batch); // todo fix customer z ordering
		game.batch.end();
		// ==================================MOVE=COOK===================================================================
		tempTime = System.currentTimeMillis();
		if (!cook.locked && Tutorial.complete)
			cook.update(control, (tempTime - tempThenTime), CLTiles);
		tempThenTime = tempTime;
		checkInteraction(cook, game.shapeRenderer);
		// =====================================SET=MATRIX=FOR=UI=ELEMENTS===============================================
		Matrix4 uiMatrix = worldCamera.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		game.batch.setProjectionMatrix(uiMatrix);
		// =====================================DRAW=UI=ELEMENTS=========================================================
		drawUI();
		// =====================================SET=MATRIX=BACK=TO=GAME=MATRIX===========================================

		setCameraLerp(delta);

		game.batch.setProjectionMatrix(worldCamera.combined);
		// ==================================MOVE=CAMERA=================================================================

		worldCamera.update();
		uiCamera.update();
		// ==================================PLAY=MUSIC==================================================================
		game.gameMusic.play();
		// ==================================DRAW=INTERACTIVE=UI=ELEMENTS================================================
		stage.act();
		stage.draw();
		// ==================================JUMP=TO=STATE=SPECIFIC=LOGIC================================================
		game.batch.setProjectionMatrix(uiMatrix);
		changeScreen(state1);
		game.batch.setProjectionMatrix(worldCamera.combined);

		checkCookSwitch();
		// =========================================CHECK=GAME=OVER======================================================
		checkGameOver();

	}

    /**
     * Change selected cook
     */
	private void checkCookSwitch() {
		if (control.tab && Tutorial.complete) {
			cook.locked = false;
			currentCookIndex += currentCookIndex < cooks.length - 1 ? 1 : -currentCookIndex;
			cook = cooks[currentCookIndex];
		}
		if (control.shift && Tutorial.complete) {
			cook.locked = false;
			currentCookIndex -= currentCookIndex > 0 ? 1 : -cooks.length + 1;
			cook = cooks[currentCookIndex];
		}

		control.interact = false;
		control.drop = false;
		control.flip = false;
		control.tab = false;
		control.shift = false;
	}

	public static final float MAX_WAIT_TIME = 1000000; //Customer wait time in ms

    /**
     * Draw UI elements
     */
	private void drawUI() {
		if (currentWaitingCustomer != null && currentWaitingCustomer.waitTime() < MAX_WAIT_TIME) {
			Menu.RECIPES.get(currentWaitingCustomer.order).displayRecipe(game.batch, new Vector2(64, 256));
		}
		for (int i = 0; i < cooks.length; i++) {
			if (i == currentCookIndex) {
				selectedPlayerBox.setAutoShapeType(true);
				selectedPlayerBox.begin(ShapeType.Line);

				selectedPlayerBox.setColor(Color.GREEN);
				selectedPlayerBox.rect(Gdx.graphics.getWidth() - 128 * cooks.length + i * 128,
						Gdx.graphics.getHeight() - 128 - 8, 128, 128);
				selectedPlayerBox.end();
			}
			game.batch.begin();
			cooks[i].draw_top(game.batch, new Vector2(Gdx.graphics.getWidth() - 128 * cooks.length + i * 128,
					Gdx.graphics.getHeight() - 256));
			game.batch.end();
		}

		game.batch.begin();
		game.font.draw(game.batch, Long.toString((startTime - timeOnStartup) / 1000),
				gameResolutionX / 2f + gameResolutionX / 10f, 19 * gameResolutionY / 20f);
		game.font.draw(game.batch, "Time in S:", gameResolutionX / 2f, 19 * gameResolutionY / 20f);
		game.font.draw(game.batch,Integer.toString(money), gameResolutionX / 3f + gameResolutionX / 10f, 19 * gameResolutionY / 20f);
		game.font.draw(game.batch, "Money:", gameResolutionX / 3f + gameResolutionX / 35f, 19 * gameResolutionY / 20f);
		game.batch.end();
	}

    /**
     * Change camera movement type depending on position to cook
     * @param delta - some change in time
     */
	private void setCameraLerp(float delta) {
		if (!Tutorial.complete) {
			worldCamera.position.lerp(new Vector3(Tutorial.getStagePos(), 0), .065f);
			if (control.tab) {
				Tutorial.nextStage();
			} else if (control.shift) {
				Tutorial.previousStage();
			}
			Tutorial.drawBox(game.batch, delta * 20);
		} else {
			if (Math.abs(worldCamera.position.x - cook.pos.x) < 2
					&& Math.abs(worldCamera.position.y - cook.pos.y) < 2) {
				worldCamera.position.x = cook.pos.x;
				worldCamera.position.y = cook.pos.y;
			} else {
				worldCamera.position.lerp(new Vector3(cook.pos.x, cook.pos.y, 0), .065f);
			}
		}
	}

	/**
	 * Draws the held items for all cooks on the screen
	 */
	private void drawHeldItems() {
		for (Cook ck : cooks) {
			int itemIndex = 0;
			for (Entity ingredient : ck.heldItems) {
				ingredient.pos = new Vector2(ck.pos.x + 16, ck.pos.y + 112 + itemIndex * 8);
				ingredient.draw(game.batch);
				itemIndex++;
			}
		}
	}

	long nowTime = 0;
	long thenTime = 0;

	/**
	 * Changes game window state
	 * 
	 * @param state1 - the state to change to
	 */
	public void changeScreen(STATE state1) {
		if (state1 == STATE.main) {
			game.gameMusic.dispose();
			game.resetGameScreen();
			game.setScreen(game.getMainScreen());

		}
		if (state1 == STATE.Pause) {
			thenTime = System.currentTimeMillis() - timeOnStartup;
			Gdx.input.setInputProcessor(stage2);
			game.batch.begin();
			game.batch.draw(ESC, optionsBackground.getX(), optionsBackground.getY(), optionsBackground.getWidth(),
					optionsBackground.getHeight());
			game.batch.end();
			stage2.act();
			stage2.draw();
		}
		if (state1 == STATE.audio) {
			musicVolumeUpdate();
			gameVolumeUpdate();
			checkState();

			Gdx.input.setInputProcessor(stage2);
			game.batch.begin();
			game.batch.draw(ESC, optionsBackground.getX(), optionsBackground.getY(), optionsBackground.getWidth(),
					optionsBackground.getHeight());
			game.batch.end();
			stage2.act();
			stage2.draw();
			game.batch.begin();
			game.batch.draw(audioEdit, audioBackground.getX(), audioBackground.getY(), audioBackground.getWidth(),
					audioBackground.getHeight());
			game.batch.draw(vControl, volSlideBackgr.getX(), volSlideBackgr.getY(), volSlideBackgr.getWidth(),
					volSlideBackgr.getHeight());
			game.batch.draw(vButton, volSlide.getX() - volSlide.getWidth() / 2, volSlide.getY(), volSlide.width,
					volSlide.height);
			game.batch.draw(vControl, musSlideBackgr.getX(), musSlideBackgr.getY(), musSlideBackgr.getWidth(),
					musSlideBackgr.getHeight());
			game.batch.draw(vButton, musSlide.getX() - musSlide.getWidth() / 2, musSlide.getY(), musSlide.width,
					musSlide.height);
			game.batch.end();
		}
		if (state1 == STATE.Continue) {
			nowTime = System.currentTimeMillis() - timeOnStartup;
			startTime += nowTime - thenTime;
			cc.updateCustomers();
			thenTime = System.currentTimeMillis() - timeOnStartup;
		}
	}

	public void paid(int pay){
		money += pay;
	}
	/**
	 * Checks to see whether escape has been pressed to pause the game
	 */
	public void checkState() {
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			state1 = STATE.Pause;
		}
	}

	/**
	 * Updates the music volume slider
	 */
	public void musicVolumeUpdate() {
		float fromTopy = Gdx.input.getY();
		float fromBottomy = gameResolutionY - fromTopy;
		float x = Gdx.input.getX();
		boolean change = musSlide.getY() <= fromBottomy & fromBottomy <= musSlide.getY() + musSlide.getHeight();
		if (Gdx.input.isTouched() & change) {
			if (x >= musSlideBackgr.getX() & x <= musSlideBackgr.getX() + musSlideBackgr.getWidth()) {
				musSlide.setPosition(Gdx.input.getX(), musSlide.getY());
				v = (musSlide.getX() - musSlideBackgr.getX()) / musSlideBackgr.getWidth();
				if (v < 0.01) {
					v = 0;
				}
				game.mainScreenMusic.setVolume(v);
				game.gameMusic.setVolume(v);
				MainGameClass.musicVolumeScale = v;
			}
		}
	}

	/**
	 * Updates the game volume slider
	 */
	public void gameVolumeUpdate() {
		float fromTopy = Gdx.input.getY();
		float fromBottomy = gameResolutionY - fromTopy;
		float x = Gdx.input.getX();
		boolean change = volSlide.getY() <= fromBottomy & fromBottomy <= volSlide.getY() + volSlide.getHeight();
		if (Gdx.input.isTouched() & change) {
			if (x >= volSlideBackgr.getX() & x <= volSlideBackgr.getX() + volSlideBackgr.getWidth()) {
				volSlide.setPosition(Gdx.input.getX(), volSlide.getY());
				s = (volSlide.getX() - volSlideBackgr.getX()) / volSlideBackgr.getWidth();
				if (s < 0.01) {
					s = 0;
				}
				// game.sound.setVolume(game.soundid, s);
				MainGameClass.gameVolumeScale = s;
			}
		}
	}

	/**
	 * Calculates coordinates for UI element scaling;
	 */
	private void calculateBoxMaths() {
		this.gameResolutionX = ms.gameResolutionX;
		this.gameResolutionY = ms.gameResolutionY;
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
	 * Construct an array of CollisionTile objects for collision detection
	 * 
	 * @param mp- game tilemap
	 */
	public static void constructCollisionData(TiledMap mp) {
		TiledMapTileLayer botlayer = (TiledMapTileLayer) mp.getLayers().get(0);
		int mapwidth = botlayer.getWidth();
		int mapheight = botlayer.getHeight();
		CLTiles = new CollisionTile[mapwidth][mapheight];
		TiledMapTileLayer toplayer = (TiledMapTileLayer) mp.getLayers().get(1);
		int topwidth = toplayer.getWidth();
		int topheight = toplayer.getHeight();
		for (int y = 0; y < topheight; y++) {
			for (int x = 0; x < topwidth; x++) {
				TiledMapTileLayer.Cell tl2 = toplayer.getCell(x, y);
				if (tl2 != null) {
					CLTiles[x][y] = new CollisionTile(x * 64, y * 64, 64, 64);
				}
			}
		}
		for (int y = 0; y < mapheight; y++) {
			for (int x = 0; x < mapwidth; x++) {
				TiledMapTileLayer.Cell tl = botlayer.getCell(x, y);
				if (tl != null) {
					TiledMapTile tlt = tl.getTile();
					MapProperties mpr = tlt.getProperties();
                    //Checks if tile have the "name" custom property
                    //In this implementation only the floor tiles have "name" property
					if (mpr.get("name") == null) {
						CLTiles[x][y] = new CollisionTile(x * 64, y * 64, 64, 64);
					} else {
						if (y != 0) {
							if (CLTiles[x][y - 1] != null) {
								CLTiles[x][y - 1] = null;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Check the tile the cook is looking at for interaction
	 * 
	 * @param ck - Selected cook
	 * @param sr - ShapeRenderer to draw the coloured box
	 */
	public void checkInteraction(Cook ck, ShapeRenderer sr) {
		float centralCookX = ck.getX() + ck.getWidth() / 2;
		float centralCookY = ck.getY();
		int cellx = (int) Math.floor(centralCookX / 64);
		int celly = (int) Math.floor(centralCookY / 64);
		int checkCellX = cellx;
		int checkCellY = celly;
		checkCellX += ck.getDirection().x;
		checkCellY += ck.getDirection().y + 1;
		Cell viewedTile = ((TiledMapTileLayer) map1.getLayers().get(1)).getCell(checkCellX, checkCellY);

		if (viewedTile != null) {
			Object stationType = viewedTile.getTile().getProperties().get("Station");
			if (stationType != null) {
				stationManager.checkInteractedTile((String) viewedTile.getTile().getProperties().get("Station"),
						new Vector2(checkCellX, checkCellY));
			} else {
				stationManager.checkInteractedTile("", new Vector2(checkCellX, checkCellY));
			}
		}
		sr.begin(ShapeRenderer.ShapeType.Line);
		sr.setColor(new Color(1, 0, 1, 1));
		sr.rect(checkCellX * 64, checkCellY * 64, 64, 64);
		sr.end();
	}

	public void checkGameOver() {
		if (currentWave == NUMBER_OF_WAVES + 1) {
			game.getLeaderBoardScreen().addLeaderBoardData("PLAYER1",
					(int) Math.floor((startTime - timeOnStartup) / 1000f));
			game.resetGameScreen();
			this.resetStatic();
			game.setScreen(game.getLeaderBoardScreen());
		}
	}

	public void resetStatic() {
		currentWave = 0;
	}

	/**
	 * Resize game screen - Not used in fullscreen mode
	 * 
	 * @param width  - width to resize to
	 * @param height - height to resize to
	 */
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		worldViewport.update(width, height);
		uiViewport.update(width, height);
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
