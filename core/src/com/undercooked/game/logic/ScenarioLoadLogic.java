package com.undercooked.game.logic;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.entity.customer.Customer;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.food.Instruction;
import com.undercooked.game.food.Request;
import com.undercooked.game.map.MapCell;
import com.undercooked.game.map.Register;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.DefaultJson;
import com.undercooked.game.util.json.JsonFormat;
import com.undercooked.game.util.json.JsonObject;

import java.util.Random;

/**
 * A class that extends the {@link GameLogic} that has the functionality
 * to load the data of scenario files to use for the game.
 */
abstract class ScenarioLoadLogic extends GameLogic {

    @Override
    public void load() {
        // Load the Scenario
        loadScenario(id);
        // Load all the items
        items.load(textureManager, Constants.GAME_TEXTURE_ID);
        // Load the map's floor textures
        map.loadFloor(textureManager, Constants.GAME_TEXTURE_ID);
        // Find the registers on the map for the Customers
        customerController.load(Constants.GAME_TEXTURE_ID);
        // Load all the Cook textures
        cookController.loadAll(Constants.GAME_TEXTURE_ID);
    }

    @Override
    public void postLoad() {
        super.postLoad();

        // Post Load the CustomerController
        customerController.postLoad();

        // Loop through the requests
        for (Request request : requestPool) {
            // Make sure the textures are loaded
            request.postLoad(textureManager);
        }
        for (Request request : requests) {
            // Make sure the textures are loaded
            request.postLoad(textureManager);
        }
    }

    /**
     * Called whenever a {@link com.undercooked.game.entity.cook.Cook}
     * has interacted with a {@link Register}.
     * <br>Will display the {@link Customer}'s request if successful.
     * @param mapCell {@link MapCell} : The {@link MapCell} of the {@link Register}.
     * @return {@code boolean} : Whether the interaction was successful or not.
     */
    public boolean interactRegister(MapCell mapCell) {
        // Ask the CustomerController if there is a Customer on this Register
        Register targetRegister = customerController.getRegisterFromCell(mapCell);

        // Make sure it's a valid register
        if (targetRegister == null) {
            return false;
        }

        // If it's valid, then check if it has a customer
        if (!targetRegister.hasCustomer()) {
            return false;
        }

        // Finally, make sure the customer is waiting
        if (!targetRegister.getCustomer().isWaiting()) {
            return false;
        }

        // If all of the above apply, then set the display customer to that customer
        displayCustomer = targetRegister.getCustomer();
        return true;
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

    /**
     * Load the scenario from the asset location provided
     * @param scenarioAsset {@link String} : The asset path to the
     *                                       scenario file.
     */
    protected void loadScenario(String scenarioAsset) {
        JsonValue scenarioData = FileControl.loadJsonAsset(scenarioAsset, "scenarios");
        if (scenarioData == null) {
            // It didn't load the scenario, so it's a failure.
            return;
        }
        // If it's loaded, then unload in case something is already
        // loaded.
        unload();

        // If it loaded, format it correctly
        JsonFormat.formatJson(scenarioData, DefaultJson.scenarioFormat());

        // Try to load the map
        // If map fails to load
        loadMap(scenarioData.getString("map_id"));

        // Load all the Interactions
        for (JsonValue interaction : scenarioData.get("interactions")) {
            interactions.loadInteractionAsset(interaction.asString(), stationController, audioManager, items);
        }

        // Update all the stations to use the interactions
        stationController.updateStationInteractions();

        // Set the Customer's speed
        customerController.setCustomerSpeed(scenarioData.getFloat("customer_speed"));

        // Load all the requests
        loadRequests(scenarioData.get("requests"));

        // Set the reputation
        startReputation = scenarioData.getInt("reputation");

        // Set the leaderboard name, if it's currently null
        if (leaderboardName == null) {
            leaderboardName = scenarioData.getString("name");
        }

        // Set the cook's price
        cookCost = scenarioData.getInt("cook_cost");

        // And call loadScenarioContents
        loadScenarioContents(scenarioData);
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
     * @param requestData {@link JsonValue} : The {@link Request}s data
     *                                        in Json format.
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
                    requestPool.add(loadedRequests.get(request.asString()));
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
            if (time < 0) {
                // If the time is invalid, default to -1
                time = -1;
                // If it's < 0, then check for time_min and time_max
                float timeMin = rData.getFloat("time_min"),
                        timeMax = rData.getFloat("time_max");
                if (timeMin >= 0 && timeMax >= timeMin) {
                    // If they're valid, then randomly select time
                    // in the range
                    time = rand.nextFloat() * (timeMax - timeMin) + timeMin;
                }
                // If either of the two don't apply, it'll just use the default
                // -1, which is no timer.
            }
            // Multiply the time, based on difficulty.
            // Easy is x2, Medium is x1.5, Hard does nothing
            time *= getDifficultyMultiplier();
            // Set the time value
            newRequest.setTime(time);

            // Finally, add the request to requests
            requestPool.add(newRequest);
        }
    }
}
