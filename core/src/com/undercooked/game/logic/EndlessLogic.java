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
import com.undercooked.game.util.Listener;
import com.undercooked.game.util.StringUtil;
import com.undercooked.game.util.json.JsonFormat;
import com.undercooked.game.util.json.JsonObject;
import com.undercooked.game.util.leaderboard.LeaderboardType;

import java.util.Random;

public class EndlessLogic extends ScenarioLogic {
    int numOfCustomers;

    public EndlessLogic(GameScreen game, TextureManager textureManager, AudioManager audioManager) {
        super(game, textureManager, audioManager);

        this.leaderboardType = LeaderboardType.ENDLESS;
        this.leaderboardName = "Endless";
        numOfCustomers = 0;
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
    public void reset() {
        super.reset();

        // Reset number of customers
        numOfCustomers = 0;
    }

    @Override
    public void update(float delta) {

        // Update inputs
        InputController.updateKeys();

        // Update time
        elapsedTime += delta;

        // Update the Stations
        stationManager.update(delta);

        // Update cooks.
        cookController.update(delta);

        // Update Customers.
        customerController.update(delta);

        //

        // Check if game is over.
        checkGameOver();
    }

    public void customerServed(Customer customer) {
        // Set number completed += 1
        requestsComplete += 1;
        // Add the value of the request
        money += customer.getRequest().getValue();
        // And call customerGone
        customerGone(customer);
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

    @Override
    public float getScore() {
        return requestsComplete;
    }

    @Override
    public String getScoreString() {
        return Integer.toString(requestsComplete);
    }
}
