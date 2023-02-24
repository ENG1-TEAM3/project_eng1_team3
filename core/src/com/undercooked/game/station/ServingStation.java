package com.undercooked.game.station;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.undercooked.game.entity.Customer;
import com.undercooked.game.food.Ingredient;
import com.undercooked.game.food.Menu;
import com.undercooked.game.food.Recipe;
import com.undercooked.game.screen.GameScreen;

public class ServingStation extends Station {

	String[] possibleOrders = new String[] { "Burger", "Salad" };

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

	public ServingStation(Vector2 pos, GameScreen game) {
		super(pos, 1, false, allowedIngredients, "audio/soundFX/money-collect.mp3", game);
	}

	/**
	 * Check if there is a customer waiting, get their order and check if the
	 * serving station contains it.
	 */
	public void serveCustomer() {
		Customer waitingCustomer = GameScreen.cc.isCustomerAtPos(new Vector2(pos.x - 1, pos.y));
		if (waitingCustomer != null && waitingCustomer.locked) {
			if (GameScreen.currentWaitingCustomer == null) {
				waitingCustomer.order = possibleOrders[new Random().nextInt(possibleOrders.length)];
				waitingCustomer.arrived();
				GameScreen.currentWaitingCustomer = waitingCustomer;
			}
			if (waitingCustomer == GameScreen.currentWaitingCustomer && !slots.empty()
					&& slots.peek().equals(Menu.RECIPES.get(waitingCustomer.order))) {
				slots.pop();
				GameScreen.cc.delCustomer(waitingCustomer);
				if (GameScreen.currentWave < GameScreen.NUMBER_OF_WAVES){
					GameScreen.cc.spawnCustomer();
				}
				GameScreen.currentWave++;
				waitingCustomer.locked = false;
				GameScreen.currentWaitingCustomer = null;
			}

		}

	}
	
}
