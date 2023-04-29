package com.undercooked.game.logic;

import com.badlogic.gdx.math.Rectangle;
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
import com.undercooked.game.map.*;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.DefaultJson;
import com.undercooked.game.util.Listener;
import com.undercooked.game.util.json.JsonFormat;
import com.undercooked.game.util.json.JsonObject;
import com.undercooked.game.GameType;

import java.util.Random;

/**
 * The class for running the scenario mode logic of the game.
 */
public class ScenarioLogic extends ScenarioLoadLogic {

    /** The number of requests to serve. */
    protected int requestTarget = 5;

    /**
     * Whether requests in the {@link #requestPool} can be added to the {@link #requests} array more than once.
     */
    protected boolean allowDuplicateRequests;

    // Power up spawning
    /** The maximum number of {@link PowerUp}s that can be in the game at once. */
    protected int maxPowerups;

    /** The maximum time before then ext {@link PowerUp} spawn. */
    protected float powerUpTimerMin;

    /** The maximum time before then ext {@link PowerUp} spawn. */
    protected float powerUpTimerMax;

    /** The time left until the next {@link PowerUp} spawn. */
    protected float powerUpTimer;

    /** The time that a {@link PowerUp} lasts before it runs out. */
    protected float powerUpUseTime;

    /** The time before a {@link PowerUp} despawns. */
    protected float powerUpDespawnTime;

    /** The {@link Listener} for when a {@link PowerUp} is removed from the game. */
    protected Listener<PowerUp> powerUpRemoveListener;

    /** An {@link Array} of the {@link PowerUp}s currently in the game. */
    protected Array<PowerUp> powerUps;

    /** The possible {@link PowerUpType}s that can be spawned.*/
    protected PowerUpType[] powerUpPool;

    // Power Up variables
    /** The multiplier for the speed of the {@link com.undercooked.game.interactions.Interactions}. */
    protected float interactSpeedMultiplier = 1f;

    /**
     * Constructs the {@link ScenarioLogic}, setting up all the
     * variables that will be needed during the game.
     * @param game {@link GameScreen} : The {@link GameScreen} using the {@link ScenarioLogic}.
     * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
     * @param audioManager {@link AudioManager} : The {@link AudioManager} to use.
     */
    public ScenarioLogic(GameScreen game, TextureManager textureManager, AudioManager audioManager) {
        super(game, textureManager, audioManager);
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
                interactRegister(value);
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

    /**
     * Checks if the game has reached an end.
     * @return {@code boolean} : {@code true} if the game has ended,
     *                           {@code false} if it has not.
     */
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

    /**
     * Spawns more {@link PowerUp}s, updates all of the {@link PowerUp}s in the game,
     * and also checks if the currently selected {@link Cook} has collected a
     * {@link PowerUp} to get its effect.
     * @param delta {@code float} : The time since the last frame.
     */
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
        super.load();
        // Load all the power up textures
        for (PowerUpType thisType : powerUpPool) {
            textureManager.loadAsset(Constants.GAME_TEXTURE_ID, thisType.texturePath);
        }
    }

    @Override
    public void postLoad() {
        super.postLoad();
        // Reset the game
        reset();
    }

    @Override
    public void start() {
        super.start();
        // Spawn a customer
        spawnCustomer();
    }

    /**
     * Removes the effects of all {@link PowerUpType}s, and then
     * resets the {@link #powerUpTimer}.
     */
    public void resetPowerUps() {
        // Remove all power up effects
        for (PowerUpType powerUpType : powerUpPool) {
            removeEffect(powerUpType);
        }

        // Start the power up timer
        updatePowerUpTimer();
    }

    /**
     * Creates a new {@link PowerUp} in a random, open {@link MapCell} that doesn't have
     * a {@link PowerUp} already inside, or the currently selected {@link Cook}.
     */
    public void createNewPowerUp() {
        // Get a random map cell that's open (with nothing blocking, and not
        // on the same cell as the current cook or another power up)
        Array<MapCell> openCells = map.openCells(Map.CollisionType.ANY);
        Cook currentCook = cookController.getCurrentCook();
        for (int i = openCells.size-1 ; i >= 0 ; i--) {
            MapCell thisCell = openCells.get(i);
            Rectangle cellCollision = thisCell.getCellCollision();
            boolean valid = true;
            // Check if cook is touching the cell
            if (currentCook != null) {
                if (currentCook.isColliding(cellCollision)) {
                    valid = false;
                    System.out.println("Cook colliding at " + currentCook.getX() + ", " + currentCook.getY());
                }
            }

            // Check against all power ups
            for (PowerUp powerUp : powerUps) {
                if (powerUp.isColliding(cellCollision)) {
                    valid = false;
                    System.out.println("Power up colliding at " + powerUp.getX() + ", " + powerUp.getY());
                }
            }

            // If it's invalid, then remove from the array
            if (!valid) {
                openCells.removeIndex(i);
            }
            // If it's valid, then leave
        }
        // If there are no valid cells, then don't add a power up
        if (openCells.isEmpty()) {
            return;
        }

        MapCell openCell = openCells.random();

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

    /**
     * Adds a {@link PowerUp}'s effect.
     * @param powerUp {@link PowerUp} : The {@link PowerUp} providing the effect.
     */
    public void addEffect(PowerUp powerUp) {
        addEffect(powerUp.getType(), powerUp);
    }

    /**
     * Adds a {@link PowerUp}'s effect.
     * @param powerUpType {@link PowerUpType} : The {@link PowerUpType} to add the
     *                                          effect of.
     * @param powerUp {@link PowerUp} : The {@link PowerUp} providing the effect.
     */
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
        throw new RuntimeException("PowerUpType not accounted for: " + powerUpType.name());
    }

    /**
     * Removes a {@link PowerUp}'s effect.
     * @param powerUpType {@link PowerUpType} : The {@link PowerUpType} to remove the
     *                                          effect of.
     */
    public void removeEffect(PowerUpType powerUpType) {
        removeEffect(powerUpType, null);
    }

    /**
     * Removes a {@link PowerUp}'s effect.
     * @param powerUp {@link PowerUp} : The {@link PowerUp} that provided the effect.
     */
    public void removeEffect(PowerUp powerUp) {
        removeEffect(powerUp.getType(), powerUp);
    }

    /**
     * Removes a {@link PowerUp}'s effect.
     * @param powerUpType {@link PowerUpType} : The {@link PowerUpType} to remove the
     *                                          effect of.
     * @param powerUp {@link PowerUp} : The {@link PowerUp} that provided the effect.
     */
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

    /**
     * Uses a {@link PowerUp}, adding the effect and stopping it
     * from being rendered.
     * <br>In addition, if another power up of the same type is already
     * active, then it removes the one that was already providing the
     * effect and sets up the new one to do so instead.
     * @param powerUp {@link PowerUp} : The {@link PowerUp} to use.
     */
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

    /**
     * Removes a {@link PowerUp} {@link com.undercooked.game.entity.Entity} from the game.
     * @param powerUp {@link PowerUp} : The {@link PowerUp} to remove.
     * @param removeEffect {@code boolean} : Whether the effect of the {@link PowerUp}
     *                                       should also be removed, or not.
     */
    public void removePowerUp(PowerUp powerUp, boolean removeEffect) {
        // Remove it from the game
        gameRenderer.removeEntity(powerUp);
        powerUps.removeValue(powerUp, true);

        // Remove the effect, if it's in use, if set to true
        if (removeEffect && powerUp.isInUse()) {
            removeEffect(powerUp);
        }
    }

    /**
     * Updates the {@link #powerUpTimer} if the timer for it
     * is valid.
     */
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

        // Clear the render entities
        gameRenderer.getEntities().clear();

        // And then add all the needed ones
        gameRenderer.addEntities(map.getAllEntities());
        gameRenderer.addEntities(cookController.getCooks());


        // And start
        start();
    }

    @Override
    public void unload() {
        super.unload();
    }

    @Override
    public void loadScenarioContents(JsonValue scenarioRoot) {

        // Set whether requests can be selected multiple times or not
        allowDuplicateRequests = scenarioRoot.getBoolean("duplicate_requests");

        // Set the request target, only if requestTarget == -1 currently,
        // which means it hasn't been set yet
        if (requestTarget == -1) {
            requestTarget = scenarioRoot.getInt("num_of_requests");
            // If it's < 0, invalid, then set it to use all requests
            if (requestTarget < 0) {
                requestTarget = requestPool.size;
            }
        }

        // Load the Power up data
        JsonValue powerUpData = scenarioRoot.get("power_ups");
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
    }

    /**
     * Called when a {@link Customer} has been served.
     * @param customer {@link Customer} : The {@link Customer} that was
     *                                    served successfully.
     */
    public void customerServed(Customer customer) {
        // Set number completed += 1
        requestsComplete += 1;
        // Add the money
        money += customer.getRequest().getValue();
        // And call customerGone
        customerGone(customer);
    }

    /**
     * Called when a {@link Customer} has failed to be served.
     * @param customer {@link Customer} : The {@link Customer} that was
     *                                    not served successfully.
     */
    public void customerFailed(Customer customer) {
        Request request = customer.getRequest();
        // Just add it to the list of requests again
        requests.add(request);
        // And remove the reputation threat from the player's reputation
        reputation -= request.getReputationThreat();
        // And call customerGone
        customerGone(customer);
    }

    /**
     * Called when a {@link Customer} has just started leaving.
     * @param customer {@link Customer} : The {@link Customer} that
     *                                    has just started leaving.
     */
    public void customerGone(Customer customer) {
        // If it's the display customer, remove it
        if (customer == displayCustomer) {
            displayCustomer = null;
        }
        // Spawn a new customer
        spawnCustomer();
    }

    /**
     * Spawns a {@link Customer} with a random request.
     */
    public void spawnCustomer() {
        // Spawn the next customer, if there is more to serve
        if (requests.size > 0) {
            Request newRequest = requests.random();
            customerController.spawnCustomer(newRequest);
            requests.removeValue(newRequest, true);
        }
    }

    /**
     * Attempts to buy a {@link Cook}, only doing so successfully if
     * there is enough money, and if {@link #cookCost} is 0 or higher.
     */
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

    /**
     * Sets the number of requests that must be served before the
     * game finishes.
     * @param customNumber {@code int} : The number of customers to serve.
     */
    public void setRequestTarget(int customNumber) {
        this.requestTarget = Math.max(0, customNumber);
    }

    @Override
    protected JsonValue seralise(JsonValue gameRoot) {
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
