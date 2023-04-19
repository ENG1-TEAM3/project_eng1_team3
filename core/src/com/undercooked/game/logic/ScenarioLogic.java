package com.undercooked.game.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.Input.InputController;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.PowerUp;
import com.undercooked.game.entity.PowerUpType;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.entity.customer.Customer;
import com.undercooked.game.entity.customer.CustomerTarget;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.food.Instruction;
import com.undercooked.game.food.Item;
import com.undercooked.game.food.Request;
import com.undercooked.game.load.LoadResult;
import com.undercooked.game.map.*;
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

    /**
     * Whether requests in the {@link #requestPool} can be added to the {@link #requests} array more than once.
     */
    protected boolean allowDuplicateRequests;

    // Power up spawning
    protected int maxPowerups;
    protected float powerUpTimerMin;
    protected float powerUpTimerMax;
    protected float powerUpTimer;
    protected float powerUpUseTime;
    protected float powerUpDespawnTime;
    protected Listener<PowerUp> powerUpRemoveListener;
    protected Array<PowerUp> powerUps;
    protected PowerUpType[] powerUpPool;

    // Power Up variables
    protected float interactSpeedMultiplier = 1f;


    public ScenarioLogic(GameScreen game, TextureManager textureManager, AudioManager audioManager) {
        super(game, textureManager, audioManager);
        requests = new Array<>();
        requestPool = new Array<>();
        powerUps = new Array<>();
        allowDuplicateRequests = false;
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

        cookController.setInteractPhoneListener(new Listener<MapCell>() {
            @Override
            public void tell(MapCell value) {
                buyCook();
            }
        });

        powerUpRemoveListener = new Listener<PowerUp>() {
            @Override
            public void tell(PowerUp powerUp) {
                removePowerUp(powerUp, true);
            }
        };

        this.gameType = GameType.SCENARIO;
        this.requestTarget = -1;

        this.powerUpPool = new PowerUpType[] {
                PowerUpType.COOK_SPEED_UP,
                PowerUpType.COOK_ITEM_MAX_UP,
                PowerUpType.CUSTOMER_WAIT_SLOW,
                PowerUpType.INTERACT_FAST,
                /*PowerUpType.MONEY_UP,
                PowerUpType.REPUTATION_UP*/
        };
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
        stationController.update(delta, interactSpeedMultiplier);

        // Update cooks.
        cookController.update(delta);

        // Update Customers.
        customerController.update(delta);

        // Update the Power Ups
        updatePowerUps(delta);

        // Check if game is over.
        checkGameOver();
    }

    public void updatePowerUps(float delta) {
        // Update the active power ups
        Cook currentCook = cookController.getCurrentCook();
        for (PowerUp powerup : powerUps) {
            // Check if the player is colliding. If they are, then use it
            if (currentCook != null && powerup.checkCollision(currentCook)) {
                usePowerup(powerup);
            } else {
                // Otherwise, update the power up
                powerup.update(delta);
            }
        }

        // If not currently at the max number of power ups,
        // go to spawn one (if the timer is >= 0)
        if (powerUpTimer >= 0) {
            if (powerUps.size < maxPowerups) {
                powerUpTimer -= delta;
                if (powerUpTimer <= 0) {
                    createNewPowerUp();
                    updatePowerUpTimer();
                }
            }
        }
    }

    @Override
    public void preLoad() {

    }

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
        // Load all the power up textures

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

        // Reset the game
        reset();
    }

    @Override
    public void start() {
        // Spawn a customer
        spawnCustomer();
    }

    public void resetPowerUps() {
        // Remove all power up effects
        for (PowerUpType powerUpType : powerUpPool) {
            removeEffect(powerUpType);
        }

        // Start the power up timer
        updatePowerUpTimer();
    }

    public void createNewPowerUp() {
        // Get a random map cell that's open (with nothing blocking)
        MapCell openCell = map.randomOpenCell(Map.CollisionType.ANY);

        // If there's no open cell, don't add a power up
        if (openCell == null) return;

        // If it's <= 0, then spawn the PowerUp.
        PowerUp newPowerUp = new PowerUp();
        // Randomly pick a power up from the pool
        Random random = new Random();
        PowerUpType newType = powerUpPool[random.nextInt(0,powerUpPool.length)];
        newPowerUp.setType(newType);
        newPowerUp.setTexture(newType.texturePath);
        newPowerUp.postLoad(textureManager);
        newPowerUp.setDespawnTime(powerUpDespawnTime);
        newPowerUp.setUseTimer(powerUpUseTime);
        newPowerUp.setRemoveListener(powerUpRemoveListener);

        // Then add it to the map
        newPowerUp.setX(openCell.getDisplayX() + MapManager.gridToPos(0.5f));
        newPowerUp.setY(openCell.getDisplayY() + MapManager.gridToPos(0.5f));

        // And add it to the arrays
        gameRenderer.addEntity(newPowerUp);
        powerUps.add(newPowerUp);
    }

    public void addEffect(PowerUpType powerUpType) {
        addEffect(powerUpType, null);
    }

    public void addEffect(PowerUp powerUp) {
        addEffect(powerUp.getType(), powerUp);
    }

    protected void addEffect(PowerUpType powerUpType, PowerUp powerUp) {
        switch (powerUpType) {
            case COOK_SPEED_UP:
                cookController.setCookSpeed(1.5f);
                return;
            case COOK_ITEM_MAX_UP:
                cookController.setCookHoldLimit(10);
                return;
            case CUSTOMER_WAIT_SLOW:
                customerController.setCustomerWaitSpeed(0.4f);
                return;
            case INTERACT_FAST:
                interactSpeedMultiplier = 3f;
                return;

            // Instant effect power ups
            case MONEY_UP:
                money += 100;
                removePowerUp(powerUp, false);
                return;
            case REPUTATION_UP:
                reputation += 1;
                removePowerUp(powerUp, false);
                return;
        }
    }

    public void removeEffect(PowerUpType powerUpType) {
        removeEffect(powerUpType, null);
    }

    public void removeEffect(PowerUp powerUp) {
        removeEffect(powerUp.getType(), powerUp);
    }

    protected void removeEffect(PowerUpType powerUpType, PowerUp powerUp) {
        switch (powerUpType) {
            case COOK_SPEED_UP:
                cookController.setCookSpeed(1f);
                break;
            case COOK_ITEM_MAX_UP:
                cookController.setCookHoldLimit(5);
                return;
            case CUSTOMER_WAIT_SLOW:
                customerController.setCustomerWaitSpeed(1f);
                return;
            case INTERACT_FAST:
                interactSpeedMultiplier = 1f;
                return;
        }
    }

    public void usePowerup(PowerUp powerUp) {
        PowerUpType powerUpType = powerUp.getType();
        // If there's a power up of the same type already, remove it
        for (int i = 0 ; i < powerUps.size ; i++) {
            PowerUp thisPowerUp = powerUps.get(i);
            // Only check the power up if it's in use
            if (!thisPowerUp.isInUse()) continue;
            // If the types match...
            if (powerUpType == thisPowerUp.getType()) {
                // Then remove the one in use
                removePowerUp(thisPowerUp, false);
                break;
            }
        }

        // Hide it visually
        gameRenderer.removeEntity(powerUp);

        // Use the power up
        powerUp.use();

        // Add the effect
        addEffect(powerUp);
    }

    public void removePowerUp(PowerUp powerUp, boolean removeEffect) {
        // Remove it from the game
        gameRenderer.removeEntity(powerUp);
        powerUps.removeValue(powerUp, true);

        // Remove the effect, if it's in use, if set to true
        if (removeEffect && powerUp.isInUse()) {
            removeEffect(powerUp);
        }
    }

    public void updatePowerUpTimer() {
        // If timer max and / or min < 0 then just set to -1 and stop
        if (powerUpTimerMin < 0) {
            powerUpTimer = -1f;
            return;
        }
        // If timer max <= min, then just use min
        if (powerUpTimerMax <= powerUpTimerMin) {
            powerUpTimer = powerUpTimerMin;
        } else {
            Random random = new Random();
            // Otherwise, randomly pick a value between the two
            powerUpTimer = random.nextFloat() * (powerUpTimerMax - powerUpTimerMin) + powerUpTimerMin;
        }
    }

    @Override
    public void reset() {

        // Reset power ups
        resetPowerUps();

        if (!resetOnLoad) {
            resetOnLoad = true;
            return;
        }
        // Reset the GameLogic
        super.reset();

        // Then reset the Scenario variables
        requestsComplete = 0;
        money = 0;

        // Clear the requests
        requests.clear();

        // Duplicate the start requests array
        Array<Request> duplicateRequests = new Array<>();
        for (Request request : requestPool) {
            duplicateRequests.add(request);
        }

        // Loop requestTarget number of times, randomly picking out a request and adding
        // it to the requests array
        Random random = new Random();
        for (int i = 0 ; i < requestTarget ; i++) {
            // if duplicateRequests is empty, then set requestTarget to i
            if (duplicateRequests.size == 0) {
                requestTarget = i;
            }
            // Randomly select a request from the duplicate requests
            int newIndex = random.nextInt(0, duplicateRequests.size);
            // Get the new request and add it
            requests.add(duplicateRequests.get(newIndex));
            // However, if allowDuplicateRequests is false, then remove the index
            if (!allowDuplicateRequests) {
                duplicateRequests.removeIndex(newIndex);
            }
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

        // Set whether requests can be selected multiple times or not
        allowDuplicateRequests = scenarioData.getBoolean("duplicate_requests");

        // Set the request target, only if requestTarget == -1 currently,
        // which means it hasn't been set yet
        if (requestTarget == -1) {
            requestTarget = scenarioData.getInt("num_of_requests");
            // If it's < 0, invalid, then set it to use all requests
            if (requestTarget < 0) {
                requestTarget = requestPool.size;
            }
        }

        // Load the Power up data
        JsonValue powerUpData = scenarioData.get("power_ups");
        float time = powerUpData.getFloat("time");
        // Default max to -1f.
        powerUpTimerMax = -1f;
        // If it's < 0, check the min / max
        if (time < 0) {
            // Set the max time
            powerUpTimerMax = powerUpData.getFloat("time_max");
            time = powerUpData.getFloat("time_min");
        }
        // Set the min time
        powerUpTimerMin = time;

        // Set the power up use time
        powerUpUseTime = powerUpData.getFloat("use_time");

        // Set the despawn time of the power up
        powerUpDespawnTime = powerUpData.getFloat("despawn_time");

        // Set the maximum number of power ups that can spawn at a time
        maxPowerups = powerUpData.getInt("max");

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

    public void customerServed(Customer customer) {
        // Set number completed += 1
        requestsComplete += 1;
        // Add the money
        money += customer.getRequest().getValue();
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

    public void buyCook() {
        // If the cook price > 0...
        if (cookCost < 0) return;
        // And the money the player has is enough...
        if (money < cookCost) return;

        // Get a random open cell
        MapCell openCell = map.randomOpenCell();
        // If it's null, then stop as there's no cell to add a cook to
        if (openCell == null) return;

        // If all the above is valid, take the money and add the cook
        money -= cookCost;
        Random random = new Random();
        Cook newCook = new Cook(new Vector2(openCell.getDisplayX() + MapManager.gridToPos(0), openCell.getDisplayY() + MapManager.gridToPos(0.5f)),
                random.nextInt(1,Constants.NUM_COOK_TEXTURES+1),
                textureManager, map);
        newCook.postLoad(textureManager);
        cookController.addCook(newCook);
        gameRenderer.addEntity(newCook);


    }

    public void setAllowDuplicateRequests(boolean allowDuplicateRequests) {
        this.allowDuplicateRequests = allowDuplicateRequests;
    }

    public void setRequestTarget(int customNumber) {
        this.requestTarget = Math.max(0, customNumber);
    }

    @Override
    JsonValue seralise(JsonValue gameRoot) {
        gameRoot.addChild("customer_target", new JsonValue(requestTarget));
        return super.seralise(gameRoot);
    }

    @Override
    public void deserialise(JsonValue gameRoot) {
        // And then remove what is no longer needed
        requests.clear();
        Array<Cook> cooks = cookController.getCooks();
        for (Cook cook : cooks) {
            gameRenderer.removeEntity(cook);
        }
        cookController.getCooks().clear();

        // Set request target
        requestTarget = gameRoot.getInt("customer_target");

        // Load requests
        loadRequests(gameRoot.get("requests"));

        super.deserialise(gameRoot);
    }
}
