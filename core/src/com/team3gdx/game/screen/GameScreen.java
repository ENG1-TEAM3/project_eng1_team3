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
import com.team3gdx.game.entity.CustomerController;
import com.team3gdx.game.entity.Entity;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.station.StationManager;
import com.team3gdx.game.util.CollisionTile;
import com.team3gdx.game.util.Control;

public class GameScreen implements Screen {
	final MainGameClass game;
	final MainScreen ms;

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
	OrthographicCamera worldCamera;

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
	long tempTime;
	public static Control control;
	TiledMapRenderer tiledMapRenderer;
	public TiledMap map1;

	public static Cook[] cooks = { new Cook(new Vector2(64 * 5, 64 * 3),1), new Cook(new Vector2(64 * 5, 64 * 5),2) };
	public static int currentCookIndex = 0;
	public static Cook cook = cooks[currentCookIndex];
	CustomerController cc;
	InputMultiplexer multi;

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

	StationManager stationManager = new StationManager();

	public void show() {
		// =======================================START=FRAME=TIMER======================================================
		startTime = System.currentTimeMillis();
		timeOnStartup = startTime;
		// =======================================SET=POSITIONS=OF=SLIDERS===============================================
		float currentMusicVolumeSliderX = (game.musicVolumeScale * sliderWidth) + xSliderMin;
		float currentGameVolumeSliderX = (game.gameVolumeScale * sliderWidth) + xSliderMin;
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
		MENU = new Texture(Gdx.files.internal("uielements/MENU.jpg"));
		ESC = new Texture(Gdx.files.internal("uielements/ESC.jpg"));
		BACKTOMAINSCREEN = new Texture(Gdx.files.internal("uielements/backtomainscreen.jpg"));
		RESUME = new Texture(Gdx.files.internal("uielements/resume.jpg"));
		AUDIO = new Texture(Gdx.files.internal("uielements/audio.jpg"));
		audioEdit = new Texture(Gdx.files.internal("uielements/background.jpg"));
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

	public void render(float delta) {
		// =====================================CLEAR=SCREEN=============================================================
		ScreenUtils.clear(0, 0, 0, 0);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// =====================================SET=INPUT=PROCESSOR======================================================
		Gdx.input.setInputProcessor(multi);
		// =====================================SET=PROJECTION=MATRICES=FOR=GAME=RENDERING===============================
		game.shapeRenderer.setProjectionMatrix(worldCamera.combined);
		MainGameClass.batch.setProjectionMatrix(worldCamera.combined);
		// =====================================RENDER=BOTTOM=MAP=LAYER==================================================
		tiledMapRenderer.setView(worldCamera);
		tiledMapRenderer.render(new int[] { 0 });
		// =====================================DRAW=COOK=LEGS===========================================================
		MainGameClass.batch.begin();
		for (Cook curCook : cooks)
			curCook.draw_bot(MainGameClass.batch);
		MainGameClass.batch.end();
		// =====================================RENDER=TOP=MAP=LAYER=====================================================
		tiledMapRenderer.render(new int[] { 1 });
		// =====================================DRAW=COOK=TOP=HALF=======================================================
		stationManager.handleStations();
		drawHeldItems();
		MainGameClass.batch.begin();
		for (Cook curCook : cooks)
			curCook.draw_top(MainGameClass.batch);
		cc.drawCustTop(MainGameClass.batch); // todo fix customer z ordering
		MainGameClass.batch.end();
		// ==================================MOVE=COOK===================================================================
		tempTime = System.currentTimeMillis();
		cook.update(control, (tempTime - startTime), CLTiles);
		startTime = tempTime;
		checkInteraction(cook, game.shapeRenderer);
		// =====================================SET=MATRIX=FOR=UI=ELEMENTS===============================================
		Matrix4 uiMatrix = worldCamera.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		MainGameClass.batch.setProjectionMatrix(uiMatrix);
		// =====================================DRAW=UI=ELEMENTS=========================================================
		MainGameClass.batch.begin();
		game.font.draw(MainGameClass.batch, String.valueOf(cook.getDirection()), 0, 300);
		game.font.draw(MainGameClass.batch, Long.toString(startTime - timeOnStartup), 0, 500);
		game.font.draw(MainGameClass.batch, "Time in ms:", 0, 550);
		MainGameClass.batch.draw(new Texture("entities/cook.png"), 0, 0);
		game.font.draw(MainGameClass.batch, state1.toString(), gameResolutionX / 20.0f, gameResolutionY / 20.0f);
		MainGameClass.batch.end();
		// =====================================SET=MATRIX=BACK=TO=GAME=MATRIX===========================================
		MainGameClass.batch.setProjectionMatrix(worldCamera.combined);
		// ==================================MOVE=CAMERA=================================================================
		if (Math.abs(worldCamera.position.x - cook.pos.x) < 2 && Math.abs(worldCamera.position.y - cook.pos.y) < 2){
			worldCamera.position.x = cook.pos.x;
			worldCamera.position.y = cook.pos.y;
		}
		else {
			worldCamera.position.lerp(new Vector3(cook.pos.x, cook.pos.y, 0), .065f);
		}
		worldCamera.update();
		uiCamera.update();
		// ==================================PLAY=MUSIC==================================================================
		game.gameMusic.play();
		// ==================================DRAW=INTERACTIVE=UI=ELEMENTS================================================
		stage.act();
		stage.draw();
		// ==================================JUMP=TO=STATE=SPECIFIC=LOGIC================================================
		MainGameClass.batch.setProjectionMatrix(uiMatrix);
		changeScreen(state1);
		MainGameClass.batch.setProjectionMatrix(worldCamera.combined);
		
		if (control.tab) {
			currentCookIndex += currentCookIndex < cooks.length - 1 ? 1 : - currentCookIndex;
			cook = cooks[currentCookIndex];
		}

		control.interact = false;
		control.drop = false;
		control.flip = false;
		control.tab = false;
		
	}

	private void drawHeldItems() {
		for (Cook ck : cooks) {
			int itemIndex = 0;
			for (Entity ingredient : ck.heldItems) {
				ingredient.pos = new Vector2(ck.pos.x + 16, ck.pos.y + 112 + itemIndex * 8);
				ingredient.draw(MainGameClass.batch);
				itemIndex++;
			}
		}
	}

	public void changeScreen(STATE state1) {
		if (state1 == STATE.main) {
			game.gameMusic.dispose();
			game.resetGameScreen();
			game.setScreen(game.getMainScreen());

		}
		if (state1 == STATE.Pause) {
			Gdx.input.setInputProcessor(stage2);
			MainGameClass.batch.begin();
			MainGameClass.batch.draw(ESC, optionsBackground.getX(), optionsBackground.getY(),
					optionsBackground.getWidth(), optionsBackground.getHeight());
			MainGameClass.batch.end();
			stage2.act();
			stage2.draw();
		}
		if (state1 == STATE.audio) {
			musicVolumeUpdate();
			gameVolumeUpdate();
			checkState();

			Gdx.input.setInputProcessor(stage2);
			MainGameClass.batch.begin();
			MainGameClass.batch.draw(ESC, optionsBackground.getX(), optionsBackground.getY(),
					optionsBackground.getWidth(), optionsBackground.getHeight());
			MainGameClass.batch.end();
			stage2.act();
			stage2.draw();
			MainGameClass.batch.begin();
			MainGameClass.batch.draw(audioEdit, audioBackground.getX(), audioBackground.getY(),
					audioBackground.getWidth(), audioBackground.getHeight());
			MainGameClass.batch.draw(vControl, volSlideBackgr.getX(), volSlideBackgr.getY(), volSlideBackgr.getWidth(),
					volSlideBackgr.getHeight());
			MainGameClass.batch.draw(vButton, volSlide.getX() - volSlide.getWidth() / 2, volSlide.getY(),
					volSlide.width, volSlide.height);
			MainGameClass.batch.draw(vControl, musSlideBackgr.getX(), musSlideBackgr.getY(), musSlideBackgr.getWidth(),
					musSlideBackgr.getHeight());
			MainGameClass.batch.draw(vButton, musSlide.getX() - musSlide.getWidth() / 2, musSlide.getY(),
					musSlide.width, musSlide.height);
			MainGameClass.batch.end();
		}
		if (state1 == STATE.Continue) {
			cc.updateCustomers(control);
		}
	}

	public void checkState() {
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			state1 = STATE.Pause;
		}
	}

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
				game.musicVolumeScale = v;
			}
		}
	}

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
				game.gameVolumeScale = s;
			}
		}
	}

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

	private void constructCollisionData(TiledMap mp) {
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

	public void checkInteraction(Cook ck, ShapeRenderer sr) {
		float centralcookx = ck.getX() + ck.getWidth() / 2;
		float centralcooky = ck.getY();
		int cellx = (int) Math.floor(centralcookx / 64);
		int celly = (int) Math.floor(centralcooky / 64);
		int checkCellX = cellx;
		int checkCellY = celly;
		switch (ck.getDirection()) {
		case 'u':
			checkCellY += 2;
			break;
		case 'd':
			break;
		case 'l':
			checkCellX -= 1;
			checkCellY += 1;
			break;
		case 'r':
			checkCellX += 1;
			checkCellY += 1;
			break;
		}
		Cell viewedTile = ((TiledMapTileLayer) map1.getLayers().get(1)).getCell(checkCellX, checkCellY);
		if (viewedTile != null) {
//			System.out.println(viewedTile.getTile().getId());
			stationManager.checkInteractedTile(viewedTile.getTile().getId(), new Vector2(checkCellX, checkCellY));
		}
		sr.begin(ShapeRenderer.ShapeType.Line);
		sr.setColor(new Color(1, 0, 1, 1));
		sr.rect(checkCellX * 64, checkCellY * 64, 64, 64);
		sr.end();
	}

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
