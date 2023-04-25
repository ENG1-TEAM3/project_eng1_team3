package com.team3gdx.game.station;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.MainGameClass;
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


	/**
	 * Checks every station for ingredients and updates them accordingly.
	 * 
	 * @param batch - SpriteBatch to render ingredient textures.
	 */
	public void handleStations(MainGameClass game) {
		for (Station station : stations.values()) {
			if (station.active() == false){
				if (GameScreen.cook.heldItems.empty()){
					station.drawBuyBackText(game);
					if (GameScreen.control.interact) {
						if (GameScreen.money >= 60){
							station.buyBack();
							GameScreen.money -= 60;
						}
				}
			}
			}
			else if (!station.slots.empty() && !station.infinite) {
				for (int i = 0; i < station.slots.size(); i++) {
					// Handle each ingredient in slot.
					Ingredient currentIngredient = station.slots.get(i);
					if (station instanceof PrepStation) {
						currentIngredient.pos = new Vector2(station.pos.x * 64 + 16, i * 8 + station.pos.y * 64);
						if (((PrepStation) station).lockedCook != null)
							((PrepStation) station).updateProgress(game.batch, .01f);
					} else {
						currentIngredient.pos = new Vector2(station.pos.x * 64 + ((i * 32) % 64),
								Math.floorDiv((i * 32), 64) * 32 + station.pos.y * 64);
					}

					if (station instanceof CuttingStation && currentIngredient.slicing) {
						((CuttingStation) station).interact(game, .1f);
						station.interactSound();
					}

					if (currentIngredient.cooking && station instanceof CookingStation) {
						((CookingStation) station).drawParticles(game.batch, i);
						currentIngredient.cook(.0005f, game);
						station.interactSound();
					} else {
						currentIngredient.draw(game.batch);
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
	public void checkInteractedTile(MainGameClass game, String type, Vector2 pos, CustomerController customerController, GameMode gameMode) {
		switch (type) {
		case "Buns":
			takeIngredientStation(game, pos, Ingredients.bun);
			break;
		case "Patties":
			takeIngredientStation(game, pos, Ingredients.unformedPatty);
			break;
		case "Lettuces":
			takeIngredientStation(game, pos, Ingredients.lettuce);
			break;
		case "Tomatoes":
			takeIngredientStation(game, pos, Ingredients.tomato);
			break;
		case "Onions":
			takeIngredientStation(game, pos, Ingredients.onion);
			break;
		case "cheese":
			takeIngredientStation(game, pos, Ingredients.cheese);
			break;
		case "sauce":
			takeIngredientStation(game, pos, Ingredients.tomato_sauce);
			break;
		case "potato":
			takeIngredientStation(game, pos, Ingredients.potato);
			break;
		case "dough":
			takeIngredientStation(game, pos, Ingredients.unformedDough);
			break;
		case "Frying":
			checkStationExists(pos, new FryingStation(pos,true));
			((CookingStation) stations.get(pos)).checkCookingStation(game);
			((CookingStation) stations.get(pos)).lockCook();
			break;
		case "Prep":
			if (!stations.containsKey(pos)) {
				stations.put(pos, new PrepStation(pos,true));
			}
			placeIngredientStation(game, pos);
			PrepStation station = ((PrepStation) stations.get(pos));
			station.lockCook();
			
			break;
		case "Chopping":
			if (!stations.containsKey(pos)) {
				stations.put(pos, new CuttingStation(pos, 1,true));
			}

			placeIngredientStation(game, pos);
			CuttingStation cutStation = ((CuttingStation) stations.get(pos));
			cutStation.lockCook();

			break;
		case "Baking":
			checkStationExists(pos, new BakingStation(pos,true));
			((CookingStation) stations.get(pos)).checkCookingStation(game);
			((CookingStation) stations.get(pos)).lockCook();
			break;
		case "Frying_inactivce":
			checkStationExists(pos, new FryingStation(pos,false));
			((CookingStation) stations.get(pos)).checkCookingStation(game);
			((CookingStation) stations.get(pos)).lockCook();
			break;
		case "Prep_inactive":
			if (!stations.containsKey(pos)) {
				stations.put(pos, new PrepStation(pos,false));
			}
			placeIngredientStation(game, pos);
			PrepStation station2 = ((PrepStation) stations.get(pos));
			station2.lockCook();
			
			break;
		case "Chopping_inactive":
			if (!stations.containsKey(pos)) {
				stations.put(pos, new CuttingStation(pos, 1,false));
			}

			placeIngredientStation(game, pos);
			CuttingStation cutStation2 = ((CuttingStation) stations.get(pos));
			cutStation2.lockCook();

			break;
		case "Baking_inactive":
			checkStationExists(pos, new BakingStation(pos,false));
			((CookingStation) stations.get(pos)).checkCookingStation(game);
			((CookingStation) stations.get(pos)).lockCook();
			break;

		case "Service":
			if (!stations.containsKey(pos)) {
				stations.put(pos, new ServingStation(pos, customerController, gameMode));
			}

			((ServingStation) stations.get(pos)).serveCustomer();
			placeIngredientStation(game, pos);
			break;
		case "Bin":
			if (!GameScreen.cook.heldItems.empty()) {
				game.batch.begin();
				game.font2.draw(game.batch, "Drop [e]", pos.x * 64, pos.y * 64);
				game.batch.end();
				if (GameScreen.control.drop) {
					GameScreen.cook.dropItem();
				}
			}
			break;
		default:
			placeIngredientStation(game, pos);
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
	private void placeIngredientStation(MainGameClass game, Vector2 pos) {
		checkStationExists(pos, new Station(pos, 4, false, null, null,true));
		stations.get(pos).drawTakeText(game);
		stations.get(pos).drawDropText(game);
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
	private void takeIngredientStation(MainGameClass game, Vector2 pos, Ingredient ingredient) {
		checkStationExists(pos, new IngredientStation(pos, ingredient));
		stations.get(pos).drawTakeText(game);

		if (GameScreen.control.interact) {
			GameScreen.cook.pickUpItem(stations.get(pos).take());
		}

	}

}
