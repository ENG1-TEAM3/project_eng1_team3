package com.undercooked.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.undercooked.game.GameType;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.logic.EndlessLogic;
import com.undercooked.game.logic.GameLogic;
import com.undercooked.game.logic.ScenarioLogic;
import com.undercooked.game.render.GameRenderer;
import com.undercooked.game.screen.buttons.ModeButton;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.DefaultJson;
import com.undercooked.game.util.Listener;
import com.undercooked.game.util.json.JsonFormat;
import com.undercooked.game.util.json.JsonObject;

public class PlayScreen extends Screen {

    private Stage stage;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Texture background;
    private Sprite plate;
    private GlyphLayout title;
    private GlyphLayout description;
    private ModeButton modeButton;
    private int currentIndex;
    private Array<JsonValue> scenarioArray;

    private final float plateSize = 1000;
    private final float titleScale = 2;
    private final float titlePadding = 200;
    private final float descScale = 1;
    private final float descPadding = 100;
    private final String[] scenarioFiles = {
            "everything",
            "burger_salad",
            "serve_quick"
    };

    public PlayScreen(MainGameClass game) {
        super(game);
    }

    @Override
    public void load() {
        TextureManager textureManager = getTextureManager();
        textureManager.load(Constants.PLAY_TEXTURE_ID, "uielements/MainScreenBackground.jpg");
        textureManager.load(Constants.PLAY_TEXTURE_ID, "uielements/exittomenu.png");
        textureManager.load(Constants.PLAY_TEXTURE_ID, "uielements/playte.png");
        textureManager.load(Constants.PLAY_TEXTURE_ID, "uielements/newgame.png");
        textureManager.load(Constants.PLAY_TEXTURE_ID, "uielements/arrow_left.png");
        textureManager.load(Constants.PLAY_TEXTURE_ID, "uielements/arrow_right.png");

        game.audioManager.loadMusic("audio/music/MainScreenMusic.ogg", Constants.MUSIC_GROUP);

        camera = CameraController.getCamera(Constants.UI_CAMERA_ID);
        viewport = CameraController.getViewport(Constants.UI_CAMERA_ID);

        // Create the stage
        stage = new Stage(viewport, game.batch);

        // Create the Mode Button
        modeButton = new ModeButton(textureManager);
        modeButton.load(Constants.PLAY_TEXTURE_ID);

        title = new GlyphLayout();
        description = new GlyphLayout();

        scenarioArray = new Array<>();

        // Load the game's Scenario's
        loadScenarios();

        // Then set to index 0
        setIndex(0);

    }

    private void loadScenarios() {
        JsonObject format = DefaultJson.scenarioFormat();
        // Load all the files in the array
        for (String filePath : scenarioFiles) {
            JsonValue root = FileControl.loadJsonFile("game/scenarios", filePath, true);
            JsonFormat.formatJson(root, format);
            scenarioArray.add(root);

            root.addChild("id", new JsonValue("<main>:" + filePath));
        }
    }

    @Override
    public void unload() {
        TextureManager textureManager = getTextureManager();
        textureManager.unload(Constants.PLAY_TEXTURE_ID, true);

        game.audioManager.unloadMusic("audio/music/MainScreenMusic.ogg");

        stage.dispose();
        stage = null;
        modeButton = null;

        background = null;
        plate = null;

        title = null;
        description = null;

        scenarioArray = null;
    }

    @Override
    public void show() {

        final TextureManager textureManager = getTextureManager();

        // Textures
        background = textureManager.get("uielements/MainScreenBackground.jpg");
        plate = new Sprite(textureManager.get("uielements/playte.png"));

        // Set up plate sprite
        plate.setSize(plateSize, plateSize);
        plate.setPosition(Constants.V_WIDTH/2f-plate.getWidth()/2f, Constants.V_HEIGHT/2f-plate.getHeight()/2f);

        // Make the buttons
        Button menuBtn = new Button(new TextureRegionDrawable(textureManager.get("uielements/exittomenu.png")));
        Button leftBtn = new Button(new TextureRegionDrawable(textureManager.get("uielements/arrow_left.png")));
        Button rightBtn = new Button(new TextureRegionDrawable(textureManager.get("uielements/arrow_right.png")));
        Button playBtn = new Button(new TextureRegionDrawable(textureManager.get("uielements/newgame.png")));

        // Set the position and size of the buttons
        menuBtn.setSize(473, 144);
        menuBtn.setPosition(16,Constants.V_HEIGHT-menuBtn.getHeight()-16);

        leftBtn.setSize(128,128);
        leftBtn.setPosition(32,Constants.V_HEIGHT/2-leftBtn.getHeight());

        rightBtn.setSize(128,128);
        rightBtn.setPosition(Constants.V_WIDTH-rightBtn.getWidth()-32,Constants.V_HEIGHT/2-rightBtn.getHeight()/2);

        playBtn.setSize(473, 144);
        playBtn.setPosition(Constants.V_WIDTH/2f-playBtn.getWidth()/2f, 0);

        // Add the listeners to the buttons
        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Go to main menu
                backScreen();
            }
        });

        leftBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Go back one index
                changeIndex(-1);
            }
        });

        rightBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Go forward one index
                changeIndex(1);
            }
        });

        playBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGame();
            }
        });

        // Set up the mode button
        modeButton.postLoad();
        modeButton.update();
        modeButton.setScale(0.8f);

        modeButton.setPosition(Constants.V_WIDTH/2f, Constants.V_HEIGHT-modeButton.getButtonHeight());

        modeButton.setListener(new Listener<GameType>() {
            @Override
            public void tell(GameType value) {

            }
        });

        // Add the buttons to the stage
        stage.addActor(menuBtn);
        stage.addActor(leftBtn);
        stage.addActor(rightBtn);
        stage.addActor(playBtn);
        modeButton.addToStage(stage);

        // Set the input processor
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float v) {

        // Clear the screen
        ScreenUtils.clear(0, 0, 0, 0);
        game.batch.setProjectionMatrix(camera.combined);
        game.mainScreenMusic.play();

        game.batch.begin();
        game.batch.draw(background, 0, 0, Constants.V_WIDTH, Constants.V_HEIGHT);
        plate.draw(game.batch);
        game.font.getData().setScale(titleScale);
        float titleY = Constants.V_HEIGHT/2f+(0.7f*plate.getHeight()/2f);
        game.font.draw(game.batch, title, Constants.V_WIDTH/2f - title.width/2f, titleY);
        game.font.getData().setScale(descScale);
        game.font.draw(game.batch, description, Constants.V_WIDTH/2f - description.width/2f,
                titleY-title.height-descPadding);
        game.batch.end();

        stage.draw();
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            backScreen();
        } else {
            stage.act();
        }

    }

    private void setIndex(int index) {
        // If it's not valid, don't do anything
        if (index < 0 || index >= scenarioArray.size) return;
        // If it's valid, update the current index and show the scenario data
        currentIndex = index;
        showScenarioData(scenarioArray.get(index));
    }

    private void changeIndex(int change) {
        // Update the index number
        int newIndex = currentIndex + change;
        while (newIndex < 0) newIndex += scenarioArray.size;
        newIndex %= scenarioArray.size;

        // Then set the new index
        setIndex(newIndex);
    }

    private void showScenarioData(JsonValue scenarioData) {
        game.font.getData().setScale(titleScale);
        title.setText(game.font, scenarioData.getString("name"), Color.BLACK, plateSize - titlePadding, Align.left,true);

        game.font.getData().setScale(descScale);
        description.setText(game.font, scenarioData.getString("description"), Color.BLACK, plateSize - descPadding, Align.left, true);
        game.font.getData().setScale(1f);
    }

    private void startGame() {
        // Make sure the currentIndex is valid
        if (currentIndex < 0 || currentIndex >= scenarioArray.size) return;
        // Make sure it's on the play screen
        if (!getScreenController().onScreen(Constants.PLAY_SCREEN_ID)) return;
        // Turn off the game music if it's on.
        if (game.mainScreenMusic != null) game.mainScreenMusic.stop();
        GameScreen gameScreen = (GameScreen) game.screenController.getScreen(Constants.GAME_SCREEN_ID);
        // gameScreen.setGameLogic(new ScenarioLogic(gameScreen, textureManager, getAudioManager()));
        GameLogic gameLogic;
        GameRenderer gameRenderer;
        JsonValue currentData = scenarioArray.get(currentIndex);
        switch (modeButton.getCurrentType()) {
            case SCENARIO:
                ScenarioLogic scenario = new ScenarioLogic(gameScreen, getTextureManager(), getAudioManager());
                scenario.setId(currentData.getString("id"));
                gameLogic = scenario;
                gameRenderer = new GameRenderer();
                break;
            case ENDLESS:
                EndlessLogic endless = new EndlessLogic(gameScreen, getTextureManager(), getAudioManager());
                endless.setId(currentData.getString("id"));
                gameLogic = endless;
                gameRenderer = new GameRenderer();
                break;
            default:
                // If it reaches here, it's invalid.
                return;
        }


        gameScreen.setGameLogic(gameLogic);
        gameScreen.setGameRenderer(gameRenderer);

        // Move to the game screen
        game.screenController.setScreen(Constants.GAME_SCREEN_ID);
    }

    public void backScreen() {
        if (getScreenController().onScreen(Constants.MAIN_SCREEN_ID)) return;
        game.screenController.backScreen();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
