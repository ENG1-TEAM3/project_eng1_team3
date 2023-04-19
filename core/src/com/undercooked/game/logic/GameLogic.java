package com.undercooked.game.logic;

import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.cook.CookController;
import com.undercooked.game.entity.customer.CustomerController;
import com.undercooked.game.entity.Entity;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.entity.customer.Customer;
import com.undercooked.game.food.Items;
import com.undercooked.game.interactions.Interactions;
import com.undercooked.game.load.LoadResult;
import com.undercooked.game.map.Map;
import com.undercooked.game.render.GameRenderer;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.screen.WinScreen;
import com.undercooked.game.station.StationController;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.Listener;
import com.undercooked.game.util.Observer;
import com.undercooked.game.util.StringUtil;
import com.undercooked.game.GameType;

/**
 * A class to extend from that indicates the logic of the
 * {@link GameScreen}.
 */
public abstract class GameLogic {

    GameScreen gameScreen;
    Items items;
    CookController cookController;
    CustomerController customerController;
    StationController stationController;
    GameRenderer gameRenderer;
    TextureManager textureManager;
    AudioManager audioManager;
    Interactions interactions;
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

    protected Customer displayCustomer;

    protected String id;
    protected String leaderboardId;
    protected GameType gameType;
    protected String leaderboardName;
    protected int difficulty;

    public GameLogic(GameScreen game, TextureManager textureManager, AudioManager audioManager) {
        this.gameScreen = game;
        this.items = new Items();
        this.elapsedTime = 0;
        this.money = 0;
        this.reputation = 0;
        this.startReputation = 3;

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

    public GameLogic(GameScreen game) {
        this(game, null, null);
    }

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
        gameScreen.getScreenController().setScreen(winScreen);
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
    public final LoadResult loadMap(String path) {
        map = gameScreen.getMapManager().load(path, stationController, cookController, interactions, items);
        // If the map fails to load, then return that
        if (map == null) {
            return LoadResult.FAILURE;
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
        return LoadResult.SUCCESS;
    }

    /**
     * Update the {@link GameLogic}. Move {@link Cook}s,
     * update {@link com.undercooked.game.station.Station}s,
     * spawn {@link Customer}s, etc.
     */
    public abstract void update(float delta);

    /**
     * The GameLogic should load the game assets in this function.
     * For example, a {@link ScenarioLogic} would only need to load the
     * {@link Items} that
     * will be available, while {@link Endless} mode should load all the game's food
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
    };

    public CookController getCookController() {
        return cookController;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void setStationController(StationController stationController) {
        this.stationController = stationController;
    }

    public void setTextureManager(TextureManager textureManager) {
        this.textureManager = textureManager;
    }

    public void setGameRenderer(GameRenderer gameRenderer) {
        this.gameRenderer = gameRenderer;
    }

    public void setAudioManager(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    public void dispose() {
        map.dispose();
        cookController.dispose();
        customerController.dispose();
        stationController.dispose();
    }

    public Map getMap() {
        return map;
    }

    public CustomerController getCustomerController() {
        return customerController;
    }

    public Customer getDisplayCustomer() {
        return displayCustomer;
    }

    public Items getItems() {
        return items;
    }

    public float getScore() {
        return elapsedTime;
    }

    public String getScoreString() {
        return StringUtil.formatSeconds(elapsedTime);
    }

    public int getReputation() {
        return reputation;
    }

    public int getMoney() {
        return money;
    }

    public int getCookCost() {
        return cookCost;
    }

    public void reset() {
        // Reset the game
        elapsedTime = 0;
        reputation = startReputation;
        displayCustomer = null;
        customerController.reset();
        cookController.reset();
        stationController.reset();
    }

    public void setLeaderboardName(String name) {
        this.leaderboardName = name;
    }

    public void setId(String id) {
        this.id = id;
        // If it's null, or the same, set leaderboardId to use it to
        if (leaderboardId == null || leaderboardId.equals(id)) {
            setLeaderboardId(id);
        }
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setLeaderboardId(String id) {
        this.leaderboardId = id;
    }
}
