package com.undercooked.game.logic;

import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.Input.InputController;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.PowerUp;
import com.undercooked.game.entity.PowerUpType;
import com.undercooked.game.entity.customer.Customer;
import com.undercooked.game.food.Request;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.GameType;

/**
 * The class for running the endless mode logic of the game.
 */
public class EndlessLogic extends ScenarioLogic {

    /** The number of {@link Customer}s currently spawned. */
    int numOfCustomers;

    /** The maximum number of {@link Customer}s that can be spawned at once. */
    int customerLimit;

    /** The time before the next {@link Customer} can spawn. */
    float spawnTimer;

    /** Where the {@link #spawnTimer} starts counting down from. */
    float spawnTimerStart;

    // Power Up variables

    /**
     * Constructs the {@link ScenarioLogic}, setting up all the
     * variables that will be needed during the game.
     * @param game {@link GameScreen} : The {@link GameScreen} using the {@link ScenarioLogic}.
     * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
     * @param audioManager {@link AudioManager} : The {@link AudioManager} to use.
     */
    public EndlessLogic(GameScreen game, TextureManager textureManager, AudioManager audioManager) {
        super(game, textureManager, audioManager);

        this.gameType = GameType.ENDLESS;
        numOfCustomers = 0;
        customerLimit = 1;
        spawnTimerStart = 10f;

        this.powerUpPool = new PowerUpType[] {
                PowerUpType.COOK_SPEED_UP,
                PowerUpType.COOK_ITEM_MAX_UP,
                PowerUpType.CUSTOMER_WAIT_SLOW,
                PowerUpType.CUSTOMER_SPAWN_PREVENT,
                PowerUpType.INTERACT_FAST,
                PowerUpType.MONEY_UP
                // PowerUpType.REPUTATION_UP
        };
    }

    @Override
    public boolean checkGameOver() {
        // Check for the game ending condition.
        if (reputation <= 0) {
            win();
            return true;
        }

        return false;
    }

    @Override
    public void postLoad() {
        super.postLoad();

        // Reset the game's timer
        resetTimer();
    }

    @Override
    public void reset() {
        super.reset();
        numOfCustomers = 0;
        customerLimit = 1;
        resetTimer();
    }

    @Override
    public void start() {
        resetTimer();
    }

    @Override
    public void update(float delta) {

        // Update inputs
        InputController.updateKeys();

        // Update time
        elapsedTime += delta;

        // Update the Stations
        stationController.update(delta, interactSpeedMultiplier);

        // Update cooks.
        cookController.update(delta);

        // Update Customers.
        customerController.update(delta);
        updateCustomerLimit();


        // Only lower spawn timer if it's not at the customer limit already
        if (numOfCustomers < customerLimit) {
            spawnTimer -= delta;
            // If spawnTimer <= 0, then spawn the customer
            if (spawnTimer <= 0) {
                spawnCustomer();
                resetTimer();
            }
        }

        // Update the Power Ups
        updatePowerUps(delta);

        // Check if game is over.
        checkGameOver();
    }

    @Override
    protected void addEffect(PowerUpType powerUpType, PowerUp powerUp) {
        if (powerUpType == PowerUpType.CUSTOMER_SPAWN_PREVENT) {
            spawnTimer = Math.min(spawnTimer * 1.5f, spawnTimerStart * 2);
            removePowerUp(powerUp, false);
            return;
        }
        super.addEffect(powerUpType, powerUp);
    }

    /**
     * Updates the {@link #customerLimit} for how many {@link Customer}s
     * can be spawned at one time.
     */
    protected void updateCustomerLimit() {
        // After 6 minutes, allow spawning 3
        if (elapsedTime > 60*6) {
            customerLimit = 3;
        }
        // After 3 minutes, allow spawning 3
        else if (elapsedTime > 60*3) {
            customerLimit = 2;
        }
    }

    @Override
    public void loadScenarioContents(JsonValue scenarioRoot) {
        super.loadScenarioContents(scenarioRoot);
        // Set the spawn timer. Anything <= 0 will be an instant spawn.
        spawnTimerStart = scenarioRoot.getFloat("spawn_timer");
    }

    @Override
    public void customerGone(Customer customer) {
        // If it's the display customer, remove it
        if (customer == displayCustomer) {
            displayCustomer = null;
        }
        numOfCustomers -= 1;
    }

    @Override
    public void spawnCustomer() {
        // Spawn the next customer, if there is more to serve
        if (requests.size > 0) {
            Request newRequest = requests.random();
            customerController.spawnCustomer(newRequest);
            numOfCustomers += 1;
        }
    }

    /**
     * Resets the {@link #spawnTimer} of the {@link Customer}s.
     */
    public void resetTimer() {
        spawnTimer = spawnTimerStart;
    }

    @Override
    public float getScore() {
        return requestsComplete;
    }

    @Override
    public String getScoreString() {
        return Integer.toString(requestsComplete);
    }
}
