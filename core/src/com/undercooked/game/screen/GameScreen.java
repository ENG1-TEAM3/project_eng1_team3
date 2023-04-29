package com.undercooked.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.audio.AudioSliders;
import com.undercooked.game.audio.Slider;
import com.undercooked.game.logic.GameLogic;
import com.undercooked.game.render.GameRenderer;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.Constants;

/**
 * Responsible for the pause button, and for combining the {@link GameLogic}
 * and {@link GameRenderer}.
 */
public class GameScreen extends Screen {

	/** The {@link GameLogic} that the game will use. */
    GameLogic gameLogic;

	/** The {@link GameRenderer} that the game will use. */
	GameRenderer gameRenderer;

	/** The {@link Texture} for the {@link #pauseBtn}. */
	Texture pauseBtnTexture;

	/** The {@link Button} for going to the {@link PauseScreen}. */
	Button pauseBtn;

	/** The {@link Viewport} for the {@link #uiCamera}. */
	Viewport uiViewport;

	/** The {@link Viewport} for the {@link #worldCamera}. */
	Viewport worldViewport;

	/** The {@link Stage} for the {@link #pauseBtn}. */
	Stage stage;

	/** The {@link OrthographicCamera} for the UI. */
	OrthographicCamera uiCamera;

	/** The {@link OrthographicCamera} for the world. */
	OrthographicCamera worldCamera;

	/** The width of the buttons. */
	float buttonWidth;

	/** The height of the buttons. */
	float buttonHeight;

	/**
	 * Constructor to initialise game screen;
	 *
	 * @param game - Main entry point class
	 */
	public GameScreen(MainGameClass game) {
		super(game);
		this.buttonWidth = Constants.V_WIDTH / 10.0f;
		this.buttonHeight = Constants.V_HEIGHT / 20.0f;
	}

	/**
	 * Set the {@link GameLogic}.
	 * @param gameLogic - The {@link GameLogic} instance.
	 */
	public void setGameLogic(GameLogic gameLogic) {
		this.gameLogic = gameLogic;
		gameLogic.setGameScreen(this);
		gameLogic.setStationController(game.stationController);
		// If it has a GameRenderer, update it there
		if (this.gameRenderer != null) {
			this.gameLogic.setGameRenderer(this.gameRenderer);
			this.gameRenderer.setLogic(gameLogic);
		}
	}

	/**
	 * Set the {@link GameRenderer}.
	 * @param gameRenderer - The {@link GameRenderer} instance.
	 */
	public void setGameRenderer(GameRenderer gameRenderer) {
		// Update the GameRenderer
		this.gameRenderer = gameRenderer;
		// If it's not null, update its SpriteBatch and ShapeRenderer
		if (gameRenderer != null) {
			gameRenderer.setSpriteBatch(game.batch);
			gameRenderer.setShapeRenderer(game.shapeRenderer);
		}
		// If this has a GameLogic, set it
		if (this.gameLogic != null) {
			this.gameLogic.setGameRenderer(this.gameRenderer);
			this.gameRenderer.setLogic(this.gameLogic);
		}
	}

	@Override
	public void load() {
		TextureManager textureManager = game.getTextureManager();
		textureManager.load(Constants.GAME_TEXTURE_ID, "uielements/pause.png");

		game.audioManager.loadMusic("audio/music/GameMusic.ogg", Constants.MUSIC_GROUP);

		/*
		game.audioManager.loadMusic("audio/soundFX/cash-register-opening.mp3", Constants.GAME_GROUP);
		game.audioManager.loadMusic("audio/soundFX/chopping.mp3", Constants.GAME_GROUP);
		game.audioManager.loadMusic("audio/soundFX/frying.mp3", Constants.GAME_GROUP);
		game.audioManager.loadMusic("audio/soundFX/money-collect.mp3", Constants.GAME_GROUP);
		game.audioManager.loadMusic("audio/soundFX/timer-bell-ring.mp3", Constants.GAME_GROUP);
		*/

		gameLogic.setTextureManager(textureManager);
		gameLogic.setAudioManager(getAudioManager());
		gameLogic.load();
		gameRenderer.load(Constants.GAME_TEXTURE_ID, textureManager);

	}

	@Override
	public void unload() {
		TextureManager textureManager = game.getTextureManager();

		gameLogic.unloadObjects();

		textureManager.unload(Constants.GAME_TEXTURE_ID, true);

		game.audioManager.unload(Constants.GAME_GROUP);
		game.audioManager.unload(Constants.MUSIC_GROUP);
		stage.dispose();

		gameLogic.dispose();
		gameRenderer.unload(textureManager);
	}

	@Override
	public void show() {
		// When this screen is shown, reset the input processor
		Gdx.input.setInputProcessor(stage);
	}

	/**
	 * Resets the currently loaded game.
	 */
	public void reset() {
		// When this function is called, reset all the game's variables,
		// and current situation. It should be as if the game was just
		// started.

		// Reset the game logic
		gameLogic.reset();
	}

	@Override
	public void postLoad() {

		// Get the game music
		game.gameMusic = game.audioManager.getMusic("audio/music/GameMusic.ogg");

		// ======================================START=CAMERAS===========================================================
		worldCamera = CameraController.getCamera(Constants.WORLD_CAMERA_ID);
		uiCamera = CameraController.getCamera(Constants.UI_CAMERA_ID);
		// ======================================START=VIEWPORTS=========================================================
		worldViewport = CameraController.getViewport(Constants.WORLD_CAMERA_ID);
		uiViewport = CameraController.getViewport(Constants.UI_CAMERA_ID);
		// ======================================START=STAGES============================================================
		stage = new Stage(uiViewport);
		// ======================================LOAD=TEXTURES===========================================================
		TextureManager textureManager = game.getTextureManager();
		pauseBtnTexture = textureManager.get("uielements/pause.png");
		// ======================================CREATE=BUTTONS==========================================================
		pauseBtn = new Button(new TextureRegionDrawable(pauseBtnTexture));
		// ======================================POSITION=AND=SCALE=BUTTONS==============================================
		pauseBtn.setPosition(Constants.V_WIDTH / 40.0f, 18 * Constants.V_HEIGHT / 20.0f);
		pauseBtn.setSize(buttonWidth, buttonHeight);
		// ======================================ADD=LISTENERS=TO=BUTTONS================================================
		pauseBtn.addListener(new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				pauseGame();
			}
		});
		// ======================================ADD=BUTTONS=TO=STAGES===================================================
		stage.addActor(pauseBtn);

		// GameLogic post load
		gameLogic.postLoad();

		// GameRenderer post load
		gameRenderer.postLoad(textureManager);

		// AudioManager post load
		game.audioManager.postLoad();

	}

	/**
	 * If on the {@link GameScreen}, changes the current screen to
	 * the {@link PauseScreen}.
	 */
	public void pauseGame() {
		// Make sure it's on the GameScreen
		if (!getScreenController().onScreen(this)) return;

		gameLogic.pause();

		PauseScreen pauseScreen = (PauseScreen) game.screenController.getScreen(Constants.PAUSE_SCREEN_ID);
		pauseScreen.gameScreen = this;
		pauseScreen.setSaveEnabled(gameLogic.canSave());

		game.screenController.nextScreen(Constants.PAUSE_SCREEN_ID);
	}

	@Override
	public void render(float delta) {

		// Pause the game before anything else
		stage.act();
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			pauseGame();
			return;
		}
		// If it's not on the GameScreen, stop here
		if (!getScreenController().onScreen(this)) {
			return;
		}

		// Play Game Music
		game.gameMusic.play();

		// Update the game logic.
		gameLogic.update(delta);

		// Move the camera for the game renderer
		gameLogic.moveCamera(delta);

		// Render the game (if still on this screen)
		if (getScreenController().onScreen(this)) {
			renderScreen(delta);
			// Draw Pause Button
			MainGameClass.batch.setProjectionMatrix(uiCamera.combined);
			stage.draw();
		}

		// =========================================CHECK=GAME=OVER======================================================

	}

	@Override
	public void renderScreen(float delta) {
		// Render the game
		gameRenderer.render(delta);
		// And then render UI
		gameRenderer.renderUI(delta);
	}

	@Override
	public void resize(int width, int height) {
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
