package com.team3gdx.game.station;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.entity.Customer;
import com.team3gdx.game.entity.CustomerController;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Menu;
import com.team3gdx.game.food.Recipe;
import com.team3gdx.game.screen.GameScreen;
import com.team3gdx.game.util.GameMode;

public class ServingStation extends Station {

	private final CustomerController customerController;
	private final GameMode gameMode;
	String[] possibleOrders = new String[] { "Burger", "Salad","Pizza","Jacket_potato" };
	public float waitTime = 30000;
	/**
	 * Configure allowed ingredient to be those on the menu.
	 */
	static Ingredient[] allowedIngredients = new Ingredient[Menu.RECIPES.size()];
	static {
		int i = 0;
		for (Recipe recipe : Menu.RECIPES.values()) {
			allowedIngredients[i] = new Ingredient(recipe);
			i++;
		}
	};

	public ServingStation(Vector2 pos, CustomerController customerController, GameMode gameMode) {
		super(pos, 1, false, allowedIngredients, "audio/soundFX/money-collect.mp3",true);
		this.customerController = customerController;
		this.gameMode = gameMode;
	}

	/**
	 * Check if there is a customer waiting, get their order and check if the
	 * serving station contains it.
	 */
	public void serveCustomer(float priceMultiplier) {
		Customer waitingCustomer = customerController.isCustomerAtPos(new Vector2(pos.x - 1, pos.y));
		if (waitingCustomer != null && waitingCustomer.locked) {
			if (GameScreen.currentWaitingCustomer == null) {
				waitingCustomer.order = possibleOrders[new Random().nextInt(possibleOrders.length)];
				waitingCustomer.arrived();
				GameScreen.currentWaitingCustomer = waitingCustomer;
			}
			if (waitingCustomer.waitTime() >= waitTime){
				customerController.delCustomer(waitingCustomer);
				if (GameScreen.currentWave < gameMode.getNumberOfWaves()){
					customerController.spawnCustomer();
				}
				waitingCustomer.locked = false;
				GameScreen.currentWaitingCustomer = null;
				GameScreen.reputationPoints --;
			}
			if (waitingCustomer == GameScreen.currentWaitingCustomer && !slots.empty()
					&& slots.peek().equals(Menu.RECIPES.get(waitingCustomer.order))) {
				slots.pop();
				GameScreen.money += (Menu.RECIPES.get(waitingCustomer.order)).cost() * priceMultiplier;
				customerController.delCustomer(waitingCustomer);
				if (GameScreen.currentWave < gameMode.getNumberOfWaves()){
					customerController.spawnCustomer();
				}
				GameScreen.currentWave++;
				waitingCustomer.locked = false;
				GameScreen.currentWaitingCustomer = null;
			}

		}

	}
	
}
