package com.undercooked.game.logic;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.Input.InputController;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.food.Item;
import com.undercooked.game.food.Request;
import com.undercooked.game.load.LoadResult;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.json.JsonFormat;

public class ScenarioLogic extends GameLogic {

    /** The number of cooks in-game. */
    int cookCount;

    /** The number of customers to serve. */
    public final int NUMBER_OF_WAVES = 5;

    /** The number of customers that have been served. */
    public int currentWave = 0;
    private Array<Request> requests;

    public ScenarioLogic(GameScreen game, TextureManager textureManager, AudioManager audioManager) {
        super(game, textureManager, audioManager);
        cookCount = 1;
        requests = new Array<>();
    }

    public ScenarioLogic() {
        this(null, null, null);
    }

    public void checkGameOver() {
        if (currentWave == NUMBER_OF_WAVES + 1) {
            // game.getLeaderBoardScreen().addLeaderBoardData("PLAYER1",
            // (int) Math.floor((startTime - timeOnStartup) / 1000f));
            // game.resetGameScreen();
            this.resetStatic();
        }
    }

    public void resetStatic() {
        currentWave = 0;
    }

    @Override
    public void update(float delta) {

        InputController.updateKeys();

        elapsedTime += delta;

        // Check if game is over.

        // Update the Stations
        stationManager.update(delta);

        // Update cooks.
        cookController.update(delta);

        // Update Customers.
        customerController.update(delta);

    }

    @Override
    public void preLoad() {

    }

    @Override
    public void load() {
        // Load all the items

        // Add listener to CustomerController to remove 1 from count

        // Load the map's floor sprite
        map.load(textureManager);
    }

    @Override
    public void unload() {
        if (items != null) items.unload(gameScreen.getTextureManager());
        if (interactions != null) interactions.unload();
        if (map != null) {
            map.dispose();
            map = null;
        }
        if (cookController != null) cookController.unload();
        if (customerController != null) customerController.unload(Constants.GAME_TEXTURE_ID);
        if (requests != null) requests.clear();
    }

    public void addIngredient() {
        items.load(textureManager);
    }

    public void setCookCount(int cookCount) {
        this.cookCount = cookCount;
    }

    public LoadResult loadScenario(String scenarioAsset) {
        JsonValue scenarioData = FileControl.loadJsonAsset(scenarioAsset, "scenarios");
        if (scenarioData == null) {
            // It didn't load the scenario, so it's a failure.
            return LoadResult.FAILURE;
        }
        // If it's loaded, then unload in case something is already
        // loaded.
        unload();

        // If it loaded, format it correctly
        JsonFormat.formatJson(scenarioData, Constants.DefaultJson.scenarioFormat());

        // Try to load the map
        // If map fails to load, then return failure
        if (loadMap(scenarioData.getString("map_id")) == LoadResult.FAILURE) {
            return LoadResult.FAILURE;
        }

        // Load all the Interactions
        for (JsonValue interaction : scenarioData.get("interactions")) {
            System.out.println(interaction);
            interactions.loadInteractionAsset(interaction.asString(), audioManager, items);
        }

        // Update all the stations to use the interactions
        stationManager.updateStationInteractions();

        // Load all the requests
        loadRequests(scenarioData.get("requests"));

        // Load the items
        items.load(textureManager);

        return LoadResult.SUCCESS;
    }

    /**
     * Loads the requests of the {@link ScenarioLogic}, which the
     * {@link com.undercooked.game.entity.Customer}s will request
     * from. Each request will have a limited number of times it can
     * be requested.
     * @param requestData
     */
    protected void loadRequests(JsonValue requestData) {
        // Load the requests within the request data
        // Loop through all the requests
        for (JsonValue request : requestData) {
            System.out.println(request);
            // Add the item
            // If the addition wasn't successful, then skip
            if (items.addItemAsset(request.getString("item_id")) == null) {
                continue;
            }
            // If it was successfully added, then add it to the array
            Request newRequest = new Request();
            newRequest.itemID = request.getString("item_id");
            newRequest.setValue(request.getInt("value"));
        }
    }
}
