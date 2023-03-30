package com.undercooked.game.logic;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.Input.InputController;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.entity.customer.Customer;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.food.Item;
import com.undercooked.game.food.Request;
import com.undercooked.game.load.LoadResult;
import com.undercooked.game.map.MapCell;
import com.undercooked.game.map.MapEntity;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.Listener;
import com.undercooked.game.util.json.JsonFormat;

public class ScenarioLogic extends GameLogic {

    /** The number of cooks in-game. */
    int cookCount;

    /** The number of requests to serve. */
    private int requestTarget = 5;

    /** The number of requests that have been served correctly. */
    private int requestsComplete = 0;
    /** How much reputation the player has. */
    public int reputation;
    private String scenario;
    private Array<Request> requests;

    public ScenarioLogic(GameScreen game, TextureManager textureManager, AudioManager audioManager) {
        super(game, textureManager, audioManager);
        cookCount = 1;
        requests = new Array<>();
        // Set the listeners for the CustomerController
        customerController.setServedListener(new Listener<Request>() {
            @Override
            public void tell(Request value) {
                customerServed(value);
            }
        });

        customerController.setReputationListener(new Listener<Request>() {
            @Override
            public void tell(Request value) {
                customerFailed(value);
            }
        });

        // Set listener for CookController
        cookController.setServeListener(new Listener<Cook>() {
            @Override
            public void tell(Cook cook) {
                // Only if Cook is not null
                if (cook == null) return;

                // Get the top item
                Item cookItemTop = cook.heldItems.peek();
                // If it's not null, continue
                if (cookItemTop == null) return;

                // Then get the interactTarget of the Cook
                MapCell target = cook.getInteractTarget();
                // Make sure that the target is not null
                if (target == null) return;
                // And that it's also a register
                MapEntity targetEntity = target.getMapEntity();
                if (targetEntity == null || !targetEntity.getID().equals(Constants.REGISTER_ID)) return;

                // If all of that is valid, then send the item and cell to the CustomerController
                if (customerController.serve(target, cookItemTop)) {
                    // If it was successful, then remove the item from the cook
                    cook.takeItem();
                }
            }
        });

        this.reputation = 0;
    }

    public ScenarioLogic() {
        this(null, null, null);
    }

    public void checkGameOver() {
        if (requestsComplete == requestTarget + 1) {
            // game.getLeaderBoardScreen().addLeaderBoardData("PLAYER1",
            // (int) Math.floor((startTime - timeOnStartup) / 1000f));
            // game.resetGameScreen();
        }
    }

    @Override
    public void update(float delta) {

        // Update inputs
        InputController.updateKeys();

        elapsedTime += delta;

        // Check if game is over.

        // Update the Stations
        stationManager.update(delta);

        // Update cooks.
        cookController.update(delta);

        // Update Customers.
        customerController.update(delta);

        // Spawn Customers
        if (requests.size > 0) {
            customerController.spawnCustomer(requests.get(0));
            requests.removeIndex(0);
        }
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void load() {
        // Load the Scenario
        loadScenario(scenario);
        // Set the request target to the number of requests loaded
        requestTarget = requests.size;
        // Load all the items
        items.load(textureManager);
        // Load the map's floor textures
        map.loadFloor(textureManager, Constants.GAME_TEXTURE_ID);
        // Find the registers on the map for the Customers
        customerController.findRegisters();
    }

    @Override
    public void postLoad() {
        super.postLoad();
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
        if (customerController != null) customerController.unload();
        if (requests != null) requests.clear();
    }

    public void addIngredient() {
        items.load(textureManager);
    }

    public void setCookCount(int cookCount) {
        this.cookCount = cookCount;
    }

    private LoadResult loadScenario(String scenarioAsset) {
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
            // System.out.println(interaction);
            interactions.loadInteractionAsset(interaction.asString(), audioManager, items);
        }

        // Update all the stations to use the interactions
        stationManager.updateStationInteractions();

        // Load all the requests
        loadRequests(scenarioData.get("requests"));

        // Set the reputation
        reputation = scenarioData.getInt("reputation");

        return LoadResult.SUCCESS;
    }

    /**
     * Loads the requests of the {@link ScenarioLogic}, which the
     * {@link Customer}s will request
     * from. Each request will have a limited number of times it can
     * be requested.
     * @param requestData
     */
    protected void loadRequests(JsonValue requestData) {
        // Load the requests within the request data
        // Loop through all the requests
        for (JsonValue request : requestData) {
            // System.out.println(request);
            // Add the item
            // If the addition wasn't successful, then skip
            if (items.addItemAsset(request.getString("item_id")) == null) {
                continue;
            }
            // If it was successfully added, then add it to the array
            Request newRequest = new Request(request.getString("item_id"));
            newRequest.setValue(request.getInt("value"));

            requests.add(newRequest);
        }
    }

    public void customerServed(Request request) {
        // Set number completed += 1
        requestsComplete += 1;
    }

    public void customerFailed(Request request) {
        // Just add it to the list of requests again
        requests.add(request);
        // And remove the reputation threat from the player's reputation
        reputation -= request.getReputationThreat();
    }
}
