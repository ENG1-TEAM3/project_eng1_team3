package com.undercooked.game.logic;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.Input.InputController;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.entity.customer.Customer;
import com.undercooked.game.entity.customer.CustomerTarget;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.food.Instruction;
import com.undercooked.game.food.Item;
import com.undercooked.game.food.Request;
import com.undercooked.game.load.LoadResult;
import com.undercooked.game.map.MapCell;
import com.undercooked.game.map.MapEntity;
import com.undercooked.game.map.Register;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.DefaultJson;
import com.undercooked.game.util.Listener;
import com.undercooked.game.util.json.JsonFormat;
import com.undercooked.game.util.json.JsonObject;
import com.undercooked.game.GameType;

import java.util.Random;

public class ScenarioLogic extends GameLogic {

    /** The number of requests to serve. */
    protected int requestTarget = 5;

    /** The number of requests that have been served correctly. */
    protected int requestsComplete = 0;
    protected Array<Request> requests;
    protected Array<Request> startRequests;

    public ScenarioLogic(GameScreen game, TextureManager textureManager, AudioManager audioManager) {
        super(game, textureManager, audioManager);
        requests = new Array<>();
        startRequests = new Array<>();
        // Set the listeners for the CustomerController
        customerController.setServedListener(new Listener<Customer>() {
            @Override
            public void tell(Customer customer) {
                customerServed(customer);
            }
        });

        customerController.setReputationListener(new Listener<Customer>() {
            @Override
            public void tell(Customer customer) {
                customerFailed(customer);
            }
        });

        customerController.setTargetType(CustomerTarget.RANDOM);

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

        cookController.setInteractRegisterListener(new Listener<MapCell>() {
            @Override
            public void tell(MapCell value) {
                // Ask the CustomerController if there is a Customer on this Register
                Register targetRegister = customerController.getRegisterFromCell(value);

                // Make sure it's a valid register
                if (targetRegister == null) {
                    return;
                }

                // If it's valid, then check if it has a customer
                if (!targetRegister.hasCustomer()) {
                    return;
                }

                // Finally, make sure the customer is waiting
                if (!targetRegister.getCustomer().isWaiting()) {
                    return;
                }

                // If all of the above apply, then set the display customer to that customer
                displayCustomer = targetRegister.getCustomer();
            }
        });

        this.gameType = GameType.SCENARIO;
        this.leaderboardName = "Scenario";
    }

    public ScenarioLogic() {
        this(null, null, null);
    }

    public boolean checkGameOver() {
        // First check for win condition
        if (requestsComplete == requestTarget) {
            win();
            return true;
        }

        // Then check for loss condition
        if (reputation <= 0) {
            lose();
            return true;
        }

        return false;
    }



    @Override
    public void update(float delta) {

        // Update inputs
        InputController.updateKeys();

        elapsedTime += delta;

        // Update the Stations
        stationManager.update(delta);

        // Update cooks.
        cookController.update(delta);

        // Update Customers.
        customerController.update(delta);

        // Check if game is over.
        checkGameOver();
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void load() {
        // Load the Scenario
        loadScenario(id);
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

        // Reset the start requests array
        startRequests.clear();

        // Loop through the requests
        for (Request request : requests) {
            // Make sure the textures are loaded
            request.postLoad(textureManager);

            // And then add them to the start requests array
            startRequests.add(request);
        }

        start();
    }

    @Override
    public void start() {
        spawnCustomer();
    }

    @Override
    public void reset() {
        // Reset the GameLogic
        super.reset();

        // Then reset the Scenario variables
        requestsComplete = 0;
        money = 0;

        // Loop through the start requests
        for (Request request : startRequests) {
            // And then add them to the requests array
            requests.add(request);
        }

        // And start
        start();
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

    protected LoadResult loadScenario(String scenarioAsset) {
        JsonValue scenarioData = FileControl.loadJsonAsset(scenarioAsset, "scenarios");
        if (scenarioData == null) {
            // It didn't load the scenario, so it's a failure.
            return LoadResult.FAILURE;
        }
        // If it's loaded, then unload in case something is already
        // loaded.
        unload();

        // If it loaded, format it correctly
        JsonFormat.formatJson(scenarioData, DefaultJson.scenarioFormat());

        // Try to load the map
        // If map fails to load, then return failure
        if (loadMap(scenarioData.getString("map_id")) == LoadResult.FAILURE) {
            return LoadResult.FAILURE;
        }

        // Load all the Interactions
        for (JsonValue interaction : scenarioData.get("interactions")) {
            interactions.loadInteractionAsset(interaction.asString(), stationManager, audioManager, items);
        }

        // Update all the stations to use the interactions
        stationManager.updateStationInteractions();

        // Set the Customer's speed
        customerController.setCustomerSpeed(scenarioData.getFloat("customer_speed"));

        // Load all the requests
        loadRequests(scenarioData.get("requests"));

        // Set the reputation
        startReputation = scenarioData.getInt("reputation");

        // Set the leaderboard name
        leaderboardName = scenarioData.getString("name");

        // And call loadScenarioContents
        loadScenarioContents(scenarioData);

        return LoadResult.SUCCESS;
    }

    /**
     * For loading scenario data from the {@link #loadScenario(String)}
     * {@link JsonValue}.
     *
     * @param scenarioRoot {@link JsonValue} : The Json for the scenario.
     */
    public void loadScenarioContents(JsonValue scenarioRoot) {

    }

    /**
     * Loads the requests of the {@link ScenarioLogic}, which the
     * {@link Customer}s will request
     * from. Each request will have a limited number of times it can
     * be requested.
     * @param requestData
     */
    protected void loadRequests(JsonValue requestData) {
        // Load the request format in case it's needed.
        // However, the format should only allow the object.
        JsonObject requestFormat = (JsonObject) DefaultJson.requestFormat(false);

        // This is for requests that have been loaded using the asset system, so that
        // they don't have to be loaded multiple times.
        ObjectMap<String, Request> loadedRequests = new ObjectMap<>();

        // Create random
        Random rand = new Random();

        // Loop through all the requests
        for (JsonValue request : requestData) {
            // First check if it's a String or an Object.
            // If it's a string, it's a path to a request, otherwise
            // it's a request
            JsonValue rData;
            boolean storeRequest = false;
            if (request.isString()) {
                // If it's a String, it needs to be loaded from the requests
                // folder

                // But if it's loaded already, just add it to the list
                if (loadedRequests.containsKey(request.asString())) {
                    requests.add(loadedRequests.get(request.asString()));
                    // And then the following can be skipped
                    continue;
                }
                // If it's not loaded yet, load it.
                rData = FileControl.loadJsonAsset(request.asString(), "requests");
                // If it's null, skip
                if (rData == null) {
                    continue;
                }
                // Otherwise, make sure it's formatted correctly
                JsonFormat.formatJson(rData, requestFormat);
                // And store the request
                storeRequest = true;
            } else {
                // Otherwise it's just the request
                rData = request;
            }
            // If the addition wasn't successful, then skip
            if (items.addItemAsset(rData.getString("item_id")) == null) {
                continue;
            }
            // If it was successfully added, then add it to the array
            Request newRequest = new Request(rData.getString("item_id"));
            newRequest.setValue(rData.getInt("value"));

            if (storeRequest) {
                loadedRequests.put(request.asString(), newRequest);
            }

            // Get the instructions
            JsonValue instructions = rData.get("instructions");
            // Loop through the instructions
            for (JsonValue instruction : instructions) {
                // Add them to the Request's instructions
                Instruction newInstruction = newRequest.addInstruction(instruction.getString("texture_path"),
                        instruction.getString("text"));
                // As it's going, load the textures for the requests too.
                newInstruction.load(textureManager, Constants.GAME_TEXTURE_ID);
            }

            // Set the reputation threat of the request
            newRequest.setReputationThreat(rData.getInt("reputation_threat"));

            // Check the time value of the request
            float time = rData.getFloat("time");
            if (time >= 0) {
                // If the time is valid, then set the request's timer
                newRequest.setTime(time);

            } else {
                // If it's < 0, then check for time_min and time_max
                float timeMin = rData.getFloat("time_min"),
                      timeMax = rData.getFloat("time_max");
                if (timeMin >= 0 && timeMax >= timeMin) {
                    // If they're valid, then randomly select time
                    // in the range
                    newRequest.setTime(rand.nextFloat() * (timeMax - timeMin) + timeMin);
                }
                // If either of the two don't apply, it'll just use the default
                // -1, which is no timer.
            }

            // Finally, add the request to requests
            requests.add(newRequest);
        }
    }

    public void customerServed(Customer customer) {
        // Set number completed += 1
        requestsComplete += 1;
        // And call customerGone
        customerGone(customer);
    }

    public void customerFailed(Customer customer) {
        Request request = customer.getRequest();
        // Just add it to the list of requests again
        requests.add(request);
        // And remove the reputation threat from the player's reputation
        reputation -= request.getReputationThreat();
        // And call customerGone
        customerGone(customer);
    }

    public void customerGone(Customer customer) {
        // If it's the display customer, remove it
        if (customer == displayCustomer) {
            displayCustomer = null;
        }
        // Spawn a new customer
        spawnCustomer();
    }

    public void spawnCustomer() {
        // Spawn the next customer, if there is more to serve
        if (requests.size > 0) {
            Request newRequest = requests.random();
            customerController.spawnCustomer(newRequest);
            requests.removeValue(newRequest, true);
        }
    }
}
