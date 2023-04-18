package com.team3gdx.game.station;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.entity.CustomerController;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Ingredients;
import com.team3gdx.game.screen.GameScreen;
import com.team3gdx.game.util.GameMode;

/**
 * 
 * Deals with all the stations and cook interactions. To create a new station,
 * extend from {@link Station}, check tile in
 * {@link this#checkInteractedTile(String, Vector2)}, and update station's
 * ingredients in {@link this#handleStations(SpriteBatch, ShapeRenderer)} if necessary.
 *
 */
public class StationManager {

	/**
	 * A Map representing every station and its (x, y) coordinates.
	 */
	public static Map<Vector2, Station> stations = new HashMap<Vector2, Station>();

	SpriteBatch batch;

	/**
	 * Checks every station for ingredients and updates them accordingly.
	 * 
	 * @param batch - SpriteBatch to render ingredient textures.
	 */
	public void handleStations(SpriteBatch batch, ShapeRenderer shapeRenderer) {
		this.batch = batch;
		for (Station station : stations.values()) {
			if (!station.slots.empty() && !station.infinite) {
				for (int i = 0; i < station.slots.size(); i++) {
					// Handle each ingredient in slot.
					Ingredient currentIngredient = station.slots.get(i);
					if (station instanceof PrepStation) {
						currentIngredient.pos = new Vector2(station.pos.x * 64 + 16, i * 8 + station.pos.y * 64);
						if (((PrepStation) station).lockedCook != null)
							((PrepStation) station).updateProgress(batch, .01f);
					} else {
						currentIngredient.pos = new Vector2(station.pos.x * 64 + ((i * 32) % 64),
								Math.floorDiv((i * 32), 64) * 32 + station.pos.y * 64);
					}

					if (station instanceof CuttingStation && currentIngredient.slicing) {
						((CuttingStation) station).interact(batch, shapeRenderer, .1f);
						station.interactSound();
					}

					if (currentIngredient.cooking && station instanceof CookingStation) {
						((CookingStation) station).drawParticles(batch, i);
						currentIngredient.cook(.0005f, batch, shapeRenderer);
						station.interactSound();
					} else {
						currentIngredient.draw(batch);
					}
				}
			}
		}
	}

	/**
	 * Check the currently looked at tile for a station.
	 * 
	 * @param type The station type.
	 * @param pos  The position of the tile.
	 */
	public void checkInteractedTile(String type, Vector2 pos, CustomerController customerController, GameMode gameMode) {
		switch (type) {
		case "Buns":
			takeIngredientStation(pos, Ingredients.bun);
			break;
		case "Patties":
			takeIngredientStation(pos, Ingredients.unformedPatty);
			break;
		case "Lettuces":
			takeIngredientStation(pos, Ingredients.lettuce);
			break;
		case "Tomatoes":
			takeIngredientStation(pos, Ingredients.tomato);
			break;
		case "Onions":
			takeIngredientStation(pos, Ingredients.onion);
			break;
		case "Frying":
			checkStationExists(pos, new FryingStation(pos));
			((CookingStation) stations.get(pos)).checkCookingStation(batch);
			((CookingStation) stations.get(pos)).lockCook();
			break;
		case "Prep":
			if (!stations.containsKey(pos)) {
				stations.put(pos, new PrepStation(pos));
			}

			placeIngredientStation(pos);
			PrepStation station = ((PrepStation) stations.get(pos));
			station.lockCook();

			break;
		case "Chopping":
			if (!stations.containsKey(pos)) {
				stations.put(pos, new CuttingStation(pos, 1));
			}

			placeIngredientStation(pos);
			CuttingStation cutStation = ((CuttingStation) stations.get(pos));
			cutStation.lockCook();

			break;
		case "Baking":
			checkStationExists(pos, new BakingStation(pos));
			((CookingStation) stations.get(pos)).checkCookingStation(batch);
			((CookingStation) stations.get(pos)).lockCook();
			break;
		case "Service":
			if (!stations.containsKey(pos)) {
				stations.put(pos, new ServingStation(pos, customerController, gameMode));
			}

			((ServingStation) stations.get(pos)).serveCustomer();
			placeIngredientStation(pos);

			break;
		case "Bin":
			if (!GameScreen.cook.heldItems.empty()) {
				batch.begin();
				(new BitmapFont()).draw(batch, "Drop [e]", pos.x * 64, pos.y * 64);
				batch.end();
				if (GameScreen.control.drop) {
					GameScreen.cook.dropItem();
				}
			}
			break;
		default:
			placeIngredientStation(pos);
			break;
		}

	}

	/**
	 * Check if the given station exists at the given position.
	 * 
	 * @param pos     Position to look for.
	 * @param station The station to check for.
	 * @return A boolean indicating if the station exists at that position.
	 */
	private boolean checkStationExists(Vector2 pos, Station station) {
		if (!stations.containsKey(pos)) {
			stations.put(pos, station);
			return false;
		}

		return true;
	}

	/**
	 * Place ingredient on top of cook's stack on station at given position.
	 * 
	 * @param pos The position to lookup the station.
	 */
	private void placeIngredientStation(Vector2 pos) {
		checkStationExists(pos, new Station(pos, 4, false, null, null));
		stations.get(pos).drawTakeText(batch);
		stations.get(pos).drawDropText(batch);
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

	/**
	 * Take an item from the ingredient station
	 * 
	 * @param pos        The position of the station.
	 * @param ingredient The ingredient that the station holds.
	 */
	private void takeIngredientStation(Vector2 pos, Ingredient ingredient) {
		checkStationExists(pos, new IngredientStation(pos, ingredient));
		stations.get(pos).drawTakeText(batch);

		if (GameScreen.control.interact) {
			GameScreen.cook.pickUpItem(stations.get(pos).take());
		}

	}

}
