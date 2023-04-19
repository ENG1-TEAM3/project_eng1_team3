package com.undercooked.game.logic;

import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.Input.InputController;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.customer.Customer;
import com.undercooked.game.food.Request;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.GameType;

public class EndlessLogic extends ScenarioLogic {
    int numOfCustomers;
    int customerLimit;
    float spawnTimer;
    float spawnTimerStart;

    public EndlessLogic(GameScreen game, TextureManager textureManager, AudioManager audioManager) {
        super(game, textureManager, audioManager);

        this.gameType = GameType.ENDLESS;
        numOfCustomers = 0;
        customerLimit = 1;
        spawnTimerStart = 10f;
    }

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
        stationController.update(delta);

        // Update cooks.
        cookController.update(delta);

        // Update Customers.
        customerController.update(delta);
        updateCustomerLimit();

        spawnTimer -= delta;
        // If spawnTimer <= 0, then spawn the customer
        if (spawnTimer <= 0 && numOfCustomers < customerLimit) {
            spawnCustomer();
            resetTimer();
        }

        // Check if game is over.
        checkGameOver();
    }

    public void updateCustomerLimit() {
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
