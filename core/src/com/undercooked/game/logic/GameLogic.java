package com.undercooked.game.logic;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.cook.CookController;
import com.undercooked.game.entity.customer.CustomerController;
import com.undercooked.game.entity.Entity;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.entity.customer.Customer;
import com.undercooked.game.food.Items;
import com.undercooked.game.food.Request;
import com.undercooked.game.interactions.Interactions;
import com.undercooked.game.map.Map;
import com.undercooked.game.render.GameRenderer;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.screen.WinScreen;
import com.undercooked.game.station.StationController;
import com.undercooked.game.util.*;
import com.undercooked.game.GameType;

/**
 * A class to extend from that indicates the logic of the
 * {@link GameScreen}.
 */
public abstract class GameLogic {

    /** The {@link GameScreen} that is using the {@link GameLogic}. */
    GameScreen gameScreen;

    /** The {@link Items} for the game.*/
    Items items;

    /** The {@link CookController} for the game.*/
    CookController cookController;

    /** The {@link CustomerController} for the game.*/
    CustomerController customerController;

    /** The {@link StationController} for the game.*/
    StationController stationController;

    /** The renderer of the game. */
    GameRenderer gameRenderer;

    /** The {@link TextureManager} to load and get {@link com.badlogic.gdx.graphics.Texture}s. */
    TextureManager textureManager;

    /**
     * The {@link AudioManager} to load and get
     * {@link com.badlogic.gdx.audio.Sound} and {@link com.badlogic.gdx.audio.Music}.
     */
    AudioManager audioManager;

    /** The {@link Interactions} of the game. */
    Interactions interactions;

    /** The {@link Map} that the game uses. */
    Map map;
    float elapsedTime;
    /** How much reputation the player has. */
    public int reputation;
    /** How much reputation the player starts with. */
    public int startReputation;
    /** How much money the player has. */
    public int money;
    /** How much a {@link Cook} costs to get. */
    protected int cookCost;

    /** The number of requests that have been served correctly. */
    protected int requestsComplete = 0;

    /**
     * The requests that will be given to the player.
     */
    protected Array<Request> requests;

    /**
     * An array containing a list of possible {@link Request}s that
     * can be made throughout the game.
     * <br>Not used to take requests from, but to load into {@link #requests}.
     */
    protected Array<Request> requestPool;

    /**
     * The {@link Customer} that should have their request displayed.
     */
    protected Customer displayCustomer;

    /**
     * The id of the game / scenario.
     */
    protected String id;

    /**
     * The id of the {@link com.undercooked.game.util.leaderboard.Leaderboard} for
     * the leaderboards.json file.
     */
    protected String leaderboardId;

    /**
     * The {@link GameType} of the game, so that the
     * {@link com.undercooked.game.util.leaderboard.LeaderboardController} knows
     * which leaderboard type the {@link com.undercooked.game.util.leaderboard.Leaderboard}
     * should be saved to.
     */
    protected GameType gameType;

    /**
     * The name of the {@link com.undercooked.game.util.leaderboard.Leaderboard} to save the
     * player's score to.
     */
    protected String leaderboardName;

    /** The difficulty of the game. */
    protected int difficulty;

    /** Whether the game should reset on loading or not. */
    public boolean resetOnLoad;

    /**
     * Constructor for the game. Sets up all variables and class instances
     * that are used by the {@link GameLogic}.
     * @param game {@link GameScreen} : The {@link GameScreen} that will use
     *                                  the {@link GameLogic}.
     * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
     * @param audioManager {@link AudioManager} : The {@link AudioManager} to use
     */
    public GameLogic(GameScreen game, TextureManager textureManager, AudioManager audioManager) {
        this.gameScreen = game;
        this.items = new Items();
        this.elapsedTime = 0;
        this.money = 0;
        this.reputation = 0;
        this.startReputation = 3;
        this.resetOnLoad = true;

        this.requests = new Array<>();
        this.requestPool = new Array<>();

        this.cookController = new CookController(textureManager);
        this.customerController = new CustomerController(textureManager);
        this.interactions = new Interactions();

        this.textureManager = textureManager;
        this.audioManager = audioManager;

        this.cookController.setMoneyObserver(new Observer<Integer>() {
            @Override
            public Integer observe() {
                return money;
            }
        });

        this.cookController.setMoneyUsedListener(new Listener<Integer>() {
            @Override
            public void tell(Integer value) {
                money -= value;
            }
        });
    }

    /**
     * Constructor for the game. Takes no inputs, and defaults everything
     * to {@code null}, requiring use of the setter functions for
     * {@link GameScreen}, {@link TextureManager} and {@link AudioManager}.
     * @param game {@link GameScreen} : The {@link GameScreen} that will use
     *                                  the {@link GameLogic}.
     */
    public GameLogic(GameScreen game) {
        this(game, null, null);
    }

    /**
     * Constructor for the game. Takes no inputs, and defaults everything
     * to {@code null}, requiring use of the setter functions for
     * {@link GameScreen}, {@link TextureManager} and {@link AudioManager}.
     */
    public GameLogic() {
        this(null, null, null);
    }

    /**
     * Called when the game ends.
     */
    public void stop() {
        // Stop all station interactions (so it stops the sounds)
        stationController.stopAll();
    }

    /**
     * Called when the game starts
     */
    public void start() {
        // Move the camera to the start cook, if there is one.
        Cook currentCook = cookController.getCurrentCook();
        if (currentCook != null) {
            OrthographicCamera camera = CameraController.getCamera(Constants.WORLD_CAMERA_ID);
            camera.position.x = currentCook.getX();
            camera.position.y = currentCook.getY();
        }
    }

    /**
     * Called once the game is won. Goes to the win screen.
     * <br>
     * Should be called by children, otherwise call the
     * "stop" function and change screen.
     */
    public void win() {
        // Stop anything that needs stopped
        stop();
        // Get the WinScreen
        WinScreen winScreen = (WinScreen) gameScreen.getScreenController().getScreen(Constants.WIN_SCREEN_ID);
        // Go to win screen
        gameScreen.getScreenController().setScreen(Constants.WIN_SCREEN_ID);
        // Set the leaderboard type and id
        winScreen.setLeaderboardID(leaderboardId + "-" + Difficulty.toString(difficulty));
        winScreen.setLeaderboardType(gameType);
        winScreen.setLeaderboardName(
                leaderboardName + " - " + StringUtil.convertToTitleCase(Difficulty.toString(difficulty)));
    }

    /**
     * Called once the game is lost. Goes to the loss screen.
     * <br>
     * Should be called by children, otherwise call the
     * "stop" function and change screen.
     */
    public void lose() {
        // Stop anything that needs stopped
        stop();
        // If lost, go to loss screen
        gameScreen.getScreenController().nextScreen(Constants.LOSS_SCREEN_ID);
    }

    /**
     * Loads a {@link Map} from the path provided
     *
     * @param path {@link String} of the path.
     */
    public final void loadMap(String path) {
        map = gameScreen.getMapManager().load(path, stationController, cookController, interactions, items);
        // If the map fails to load, then return that
        if (map == null) {
            return;
        }

        customerController.setMap(map);

        // Load the map into the renderer
        for (Entity mapEntity : map.getAllEntities()) {
            gameRenderer.addEntity(mapEntity);
            mapEntity.load(textureManager);
        }
        // Load the cooks into the renderer
        for (Entity cook : cookController.getCooks()) {
            gameRenderer.addEntity(cook);
            // cook.load(textureManager);
        }
    }

    /**
     * Update the {@link GameLogic}. Move {@link Cook}s,
     * update {@link com.undercooked.game.station.Station}s,
     * spawn {@link Customer}s, etc.
     * @param delta {@code float} : The time since the last frame.
     */
    public abstract void update(float delta);

    /**
     * The GameLogic should load the game assets in this function.
     * For example, a {@link ScenarioLogic} would only need to load the
     * {@link Items} that
     * will be available, while {@link EndlessLogic} mode should load all the game's food
     * {@link Items}.
     */
    public abstract void load();

    /**
     * Should unload everything that was loaded in load.
     */
    public abstract void unload();

    /**
     * Called before the game has loaded.
     */
    public void preLoad() {

    }

    /**
     * Called after the game has loaded.
     */
    public void postLoad() {
        // post load all the entities in the GameRenderer
        for (Entity entity : gameRenderer.getEntities()) {
            entity.postLoad(textureManager);
        }
        // Post load all the items.
        items.postLoad(textureManager);
        // Post load the map
        map.postLoad(textureManager);
        // Update the reputation
        reputation = startReputation;
    }

    /**
     * Unloads all the objects in this class. (Most likely will call
     * each objects unload method)
     */
    public void unloadObjects() {
        cookController.unload();
    }

    /**
     * Called when the {@link GameScreen} moves to the
     * {@link com.undercooked.game.screen.PauseScreen}.
     */
    public void pause() {
        // Stop cooks
        cookController.stopMovement();
        // Stop the game, as it's paused
        stop();
    }

    /**
     * Dispose the game.
     */
    public void dispose() {
        map.dispose();
        cookController.dispose();
        customerController.dispose();
        stationController.dispose();
    }

    /**
     * Set the {@link GameScreen} that will use this {@link GameLogic}.
     * @param gameScreen {@link GameScreen} : The {@link GameScreen} to use.
     */
    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    /**
     * Set the {@link StationController} that the game will use.
     * @param stationController {@link StationController} : The {@link StationController} to use.
     */
    public void setStationController(StationController stationController) {
        this.stationController = stationController;
    }

    /**
     * Set the {@link TextureManager} that the game will use.
     * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
     */
    public void setTextureManager(TextureManager textureManager) {
        this.textureManager = textureManager;
    }

    /**
     * Set the {@link GameRenderer} that the game will use.
     * @param gameRenderer {@link GameRenderer} : The {@link GameRenderer} to use.
     */
    public void setGameRenderer(GameRenderer gameRenderer) {
        this.gameRenderer = gameRenderer;
    }

    /**
     * Set the {@link AudioManager} that the game will use.
     * @param audioManager {@link AudioManager} : The {@link AudioManager} to use.
     */
    public void setAudioManager(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    /**
     * Resets the state of the game.
     */
    public void reset() {
        if (!resetOnLoad) {
            resetOnLoad = true;
            return;
        }
        // Reset the game
        elapsedTime = 0;
        reputation = startReputation;
        displayCustomer = null;
        customerController.reset();
        cookController.reset();
        stationController.reset();
    }

    /**
     * Sets the name that the {@link com.undercooked.game.util.leaderboard.Leaderboard}
     * will use, if it doesn't have one already.
     * @param name {@link String} : The name of the {@link com.undercooked.game.util.leaderboard.Leaderboard}
     */
    public void setLeaderboardName(String name) {
        this.leaderboardName = name;
    }

    /**
     * Sets the id of the game. This is used for setting which Scenario
     * should be used.
     * @param id {@link String} : The id to use.
     */
    public void setId(String id) {
        this.id = id;
        // If it's null, or the same, set leaderboardId to use it to
        if (leaderboardId == null || leaderboardId.equals(id)) {
            setLeaderboardId(id);
        }
    }

    /**
     * Sets the difficulty of the game.
     * <br>These can be found in {@link Difficulty} as public
     * constants.
     * @param difficulty {@code int} : The difficulty to set to.
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = Math.min(Math.max(difficulty, Difficulty.EASY), Difficulty.HARD);
    }

    /**
     * Sets the {@link #leaderboardId}, the id of the {@link com.undercooked.game.util.leaderboard.Leaderboard}
     * for scores to be saved to.
     * @param id {@link String} : The leaderboard id.
     */
    public void setLeaderboardId(String id) {
        this.leaderboardId = id;
    }

    /**
     * @return {@link Map} : The game's {@link Map}.
     */
    public Map getMap() {
        return map;
    }

    /**
     * @return {@link CustomerController} : The {@link CustomerController} for
     *                                      the game.
     */
    public CustomerController getCustomerController() {
        return customerController;
    }

    /**
     * @return {@link CookController} : The {@link CookController} for the game.
     */
    public CookController getCookController() {
        return cookController;
    }

    /**
     * @return {@link Customer} : The {@link Customer} to display the
     *                            {@link Request} for.
     */
    public Customer getDisplayCustomer() {
        return displayCustomer;
    }

    /**
     * @return {@link Items} : The {@link Items} instance for the game.
     */
    public Items getItems() {
        return items;
    }

    /**
     * @return {@code float} : The player's score.
     */
    public float getScore() {
        return elapsedTime;
    }

    /**
     * @return {@link String} : The player's score as a {@link String}.
     */
    public String getScoreString() {
        return StringUtil.formatSeconds(elapsedTime);
    }

    /**
     * @return {@code int} : The reputation that the player has left.
     */
    public int getReputation() {
        return reputation;
    }

    /**
     * @return {@code int} : The amount of money that the player has.
     */
    public int getMoney() {
        return money;
    }

    /**
     * Returns the amount of money needed to be able to add
     * a {@link Cook} o the game via a phone cell.
     * @return {@code int} : The cost for a {@link Cook} to be added to
     *                       the game.
     */
    public int getCookCost() {
        return cookCost;
    }

    /**
     * Returns the {@link Difficulty} of the game.
     * @return {@code int} : The game's {@link Difficulty}.
     */
    public int getDifficulty() {
        return this.difficulty;
    }

    /**
     * Returns the {@link GameRenderer} that the game renders using.
     * @return {@link GameRenderer} : The game's renderer.
     */
    public GameRenderer getGameRenderer() {
        return gameRenderer;
    }

    /**
     * Returns the multiplier for {@link Customer} wait time based
     * on the current difficulty.
     * @return {@code float} : The difficulty multiplier.
     */
    public float getDifficultyMultiplier() {
        switch (difficulty) {
            case Difficulty.EASY:
                return 2f;
            case Difficulty.MEDIUM:
                return 1.5f;
        }
        return 1f;
    }

    /**
     * Returns the elapsed time that the game has been going on for.
     * @return {@link float} : The elapsed time.
     */
    public float getElapsedTime() {
        return elapsedTime;
    }

    /**
     * Returns whether the {@link GameLogic} can or can't be saved.
     * @return {@code boolean} : {@code true} if it can be saved,
     *                           {@code false} if it cannot.
     */
    public boolean canSave() {
        return true;
    }

    /**
     * Function called by the {@link GameRenderer} to move the {@link OrthographicCamera}.
     * <br>It is here so that it can be overridden if the functionality needed is different.
     * @param delta {@code float} : The time since the last frame.
     */
    public void moveCamera(float delta) {
        gameRenderer.moveCamera(delta, cookController.getCurrentCook());
    }

    /**
     * Updates the {@link JsonValue} for the entire game.
     * <br>This one is to be overridden in children classes.
     * @param gameRoot {@link JsonValue} : The game's current {@link JsonValue}.
     * @return {@link JsonValue} : The game's updated {@link JsonValue}.
     */
    protected JsonValue seralise(JsonValue gameRoot) {
        gameRoot.addChild("scenario_id", new JsonValue(id));
        gameRoot.addChild("leaderboard_id", new JsonValue(leaderboardId));
        gameRoot.addChild("leaderboard_name", new JsonValue(leaderboardName));
        gameRoot.addChild("money", new JsonValue(money));
        gameRoot.addChild("reputation", new JsonValue(reputation));
        gameRoot.addChild("difficulty", new JsonValue(Difficulty.toString(difficulty)));
        gameRoot.addChild("game_type", new JsonValue(gameType.name()));
        gameRoot.addChild("customers_served", new JsonValue(requestsComplete));
        gameRoot.addChild("time", new JsonValue(elapsedTime));

        JsonValue requestsData = new JsonValue(JsonValue.ValueType.array);
        gameRoot.addChild("requests", requestsData);
        for (Request request : requests) {
            requestsData.addChild("", request.serial());
        }

        gameRoot.addChild("cooks", cookController.serializeCooks(map));
        gameRoot.addChild("stations", stationController.serializeStations(map));
        gameRoot.addChild("customers", customerController.serializeCustomers());

        return gameRoot;
    }

    /**
     * Returns the {@link JsonValue} serialised for the entire game.
     * @return {@link JsonValue} : The game as a {@link JsonValue}.
     */
    public final JsonValue serialise() {
        return seralise(new JsonValue(JsonValue.ValueType.object));
    }

    /**
     * Takes a {@link JsonValue} serialized save of the game, and uses it to
     * load the entire game.
     * @param gameRoot {@link JsonValue} : The Json to use.
     */
    public void deserialise(JsonValue gameRoot) {

        for (JsonValue request : gameRoot.get("requests")) {
            Request newRequest = new Request(request);
            requests.add(newRequest);
            newRequest.load(textureManager, Constants.GAME_TEXTURE_ID);
        }

        this.id = gameRoot.getString("scenario_id");
        this.leaderboardId = gameRoot.getString("leaderboard_id");
        this.leaderboardName = gameRoot.getString("leaderboard_name");
        this.money = gameRoot.getInt("money");
        this.reputation = gameRoot.getInt("reputation");
        this.requestsComplete = gameRoot.getInt("customers_served");
        this.elapsedTime = gameRoot.getFloat("time");
        setDifficulty(Difficulty.asInt(gameRoot.getString("difficulty")));

        // Load the coooks
        cookController.deserializeCooks(gameRoot, items, map);
        // And add them to the game renderer
        for (Cook cook: cookController.getCooks()) {
            gameRenderer.addEntity(cook);
        }

        // Loads the stations
        stationController.deserializeStations(this, gameRoot.get("stations"), audioManager, interactions, items, map);

        // Loads the customers
        customerController.deserializeCustomers(gameRoot.get("customers"));
    }
}
