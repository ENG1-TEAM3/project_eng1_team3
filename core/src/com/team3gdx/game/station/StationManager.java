package com.team3gdx.game.station;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.MainGameClass;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Ingredients;
import com.team3gdx.game.screen.GameScreen;
import com.team3gdx.game.screen.GameScreen.STATE;

public class StationManager {

	public static Map<Vector2, Station> stations = new HashMap<Vector2, Station>();

	public StationManager() {

	}

	public void handleStations() {
		for (Station station : stations.values()) {
			if (!station.slots.empty() && !station.infinite) {
				for (int i = 0; i < station.slots.size(); i++) {
					// Handle each ingredient in slot
					Ingredient currentIngredient = station.slots.get(i);
					if (station instanceof PrepStation) {
						currentIngredient.pos = new Vector2(station.pos.x * 64 + 16, i * 8 + station.pos.y * 64);
					} else {
						currentIngredient.pos = new Vector2(station.pos.x * 64 + ((i * 32) % 64),
								Math.floorDiv((i * 32), 64) * 32 + station.pos.y * 64);
					}

					if (currentIngredient.cooking && station instanceof CookingStation) {
						((CookingStation) station).drawParticles(MainGameClass.batch, i);
						currentIngredient.cook(.0005f, MainGameClass.batch);
					} else {
						currentIngredient.draw(MainGameClass.batch);
					}

				}

			}

		}
	}

	public void checkInteractedTile(int id, Vector2 pos) {
		switch (id) {
		case 50:
			takeIngredientStation(pos, Ingredients.bun);
			break;
		case 51:
			takeIngredientStation(pos, Ingredients.unformedPatty);
			break;
		case 33:
			takeIngredientStation(pos, Ingredients.lettuce);
			break;
		case 39:
			takeIngredientStation(pos, Ingredients.tomato);
			break;
		case 46:
			// Frying station

			if (!stations.containsKey(pos)) {
				stations.put(pos, new FryingStation(pos));
			}
			checkCookingStation(pos);

			break;
		case 47:
			// Prep station
			if (!stations.containsKey(pos)) {
				stations.put(pos, new PrepStation(pos));
			}
			if (!stations.get(pos).slots.empty() && !GameScreen.cook.full()) {
				MainGameClass.batch.begin();
				(new BitmapFont()).draw(MainGameClass.batch, "Take [q]", pos.x * 64, pos.y * 64 - 16);
				MainGameClass.batch.end();
			}
			if (GameScreen.cook.heldItems.size() > 0) {
				MainGameClass.batch.begin();
				(new BitmapFont()).draw(MainGameClass.batch, "Drop [e]", pos.x * 64, pos.y * 64);
				MainGameClass.batch.end();
			}
			if (GameScreen.control.interact) {
				if (!stations.get(pos).slots.empty() && !GameScreen.cook.full()) {
					GameScreen.cook.pickUpItem(stations.get(pos).take());
					return;
				}
			}

			if (GameScreen.cook.heldItems.empty())
				return;
			if (GameScreen.control.drop)
				if (stations.get(pos).place(GameScreen.cook.heldItems.peek())) {
					GameScreen.cook.dropItem();
					((PrepStation) stations.get(pos)).slotsToRecipe();
				}
			break;

		case 49:
			// baking stationq
			if (!stations.containsKey(pos)) {
				stations.put(pos, new BakingStation(pos));
			}
			checkCookingStation(pos);

			break;
//		case 34:
//			break;
		default:
			placeIngredientStation(pos);
			break;
		}

	}

	private void checkCookingStation(Vector2 pos) {
		if (!stations.get(pos).slots.empty() && !GameScreen.cook.full() && stations.get(pos).slots.peek().flipped) {
			MainGameClass.batch.begin();
			(new BitmapFont()).draw(MainGameClass.batch, "Take [q]", pos.x * 64, pos.y * 64 - 16);
			MainGameClass.batch.end();
		} else if (GameScreen.cook.heldItems.size() > 0
				&& stations.get(pos).isAllowed(GameScreen.cook.heldItems.peek())) {
			MainGameClass.batch.begin();
			(new BitmapFont()).draw(MainGameClass.batch, "Drop [e]", pos.x * 64, pos.y * 64);
			MainGameClass.batch.end();
		}
		if (GameScreen.control.interact) {
			if (!stations.get(pos).slots.empty() && !GameScreen.cook.full()) {
				if (stations.get(pos).slots.peek().flipped)
					GameScreen.cook.pickUpItem(stations.get(pos).take());

				return;
			}
		}
		if (GameScreen.control.drop) {

			if (!GameScreen.cook.heldItems.empty() && stations.get(pos).place(GameScreen.cook.heldItems.peek())) {
				GameScreen.cook.dropItem();
				stations.get(pos).slots.peek().cooking = true;
			}
		}
		if (!stations.get(pos).slots.empty() && GameScreen.control.flip)
			stations.get(pos).slots.peek().flip();
	}

	private void placeIngredientStation(Vector2 pos) {
		if (!stations.containsKey(pos)) {
			stations.put(pos, new Station(pos, 4, false, null));
		}
		if (!stations.get(pos).slots.empty() && !GameScreen.cook.full()) {
			MainGameClass.batch.begin();
			(new BitmapFont()).draw(MainGameClass.batch, "Take [q]", pos.x * 64, pos.y * 64 - 16);
			MainGameClass.batch.end();
		}
		if (GameScreen.cook.heldItems.size() > 0) {
			MainGameClass.batch.begin();
			(new BitmapFont()).draw(MainGameClass.batch, "Drop [e]", pos.x * 64, pos.y * 64);
			MainGameClass.batch.end();
		}
		if (GameScreen.control.interact) {
			if (!stations.get(pos).slots.empty() && !GameScreen.cook.full()) {
				GameScreen.cook.pickUpItem(stations.get(pos).take());
				return;
			}
		}

		if (GameScreen.cook.heldItems.empty())
			return;
		if (GameScreen.control.drop)
			if (stations.get(pos).place(GameScreen.cook.heldItems.peek()))
				GameScreen.cook.dropItem();

	}

	private void takeIngredientStation(Vector2 pos, Ingredient ingredient) {
		if (!stations.containsKey(pos)) {
			stations.put(pos, new IngredientStation(pos, ingredient));
		}
		if (!stations.get(pos).slots.empty() && !GameScreen.cook.full()) {
			MainGameClass.batch.begin();
			(new BitmapFont()).draw(MainGameClass.batch, "Take [q]", pos.x * 64, pos.y * 64 - 8);
			MainGameClass.batch.end();
		}

		if (GameScreen.control.interact) {

			GameScreen.cook.pickUpItem(stations.get(pos).take());
		}

	}

}
