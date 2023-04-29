package com.team3gdx.game.station;

import java.util.HashMap;
import java.util.Map;

import javax.swing.text.Position;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.entity.CustomerController;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Ingredients;
import com.team3gdx.game.screen.GameScreen;
import com.team3gdx.game.util.GameMode;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

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
	public void handleStations(SpriteBatch batch, ShapeRenderer shapeRenderer, float cookingTime, float constructionCost) {
		this.batch = batch;
		for (Station station : stations.values()) {
			if (station.active() == false){
				if (GameScreen.cook.heldItems.empty()){
					station.drawBuyBackText(batch, constructionCost);
					if (GameScreen.control.interact) {
						if (GameScreen.money >= constructionCost){
							station.buyBack();
							GameScreen.money -= constructionCost;
							TiledMapTileLayer Layer =(TiledMapTileLayer)GameScreen.map1.getLayers().get(1);
							Cell cell = Layer.getCell((int)station.pos.x,(int)station.pos.y);
							TiledMapTile tile =cell.getTile();
							int newId = tile.getId()+10;
							tile.setId(newId);
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
							((PrepStation) station).updateProgress(batch, cookingTime * 2);
					} else {
						currentIngredient.pos = new Vector2(station.pos.x * 64 + ((i * 32) % 64),
								Math.floorDiv((i * 32), 64) * 32 + station.pos.y * 64);
					}

					if (station instanceof CuttingStation && currentIngredient.slicing) {
						((CuttingStation) station).interact(batch, shapeRenderer, cookingTime * 2);
						station.interactSound();
					}

					if (currentIngredient.cooking && station instanceof CookingStation) {
						((CookingStation) station).drawParticles(batch, i);
						currentIngredient.cook(cookingTime * 0.05f, batch, shapeRenderer);
						station.interactSound();
					} else {
						currentIngredient.draw(batch);
					}
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
	public void checkInteractedTile(String type, Vector2 pos, CustomerController customerController, GameMode gameMode, float priceMultiplier) {
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
		case "cheese":
			takeIngredientStation(pos, Ingredients.cheese);
			break;
		case "sauce":
			takeIngredientStation(pos, Ingredients.tomato_sauce);
			break;
		case "potato":
			takeIngredientStation(pos, Ingredients.potato);
			break;
		case "dough":
			takeIngredientStation(pos, Ingredients.unformedDough);
			break;
		case "Frying":
			checkStationExists(pos, new FryingStation(pos,true));
			((CookingStation) stations.get(pos)).checkCookingStation(batch);
			((CookingStation) stations.get(pos)).lockCook();
			break;
		case "Prep":
			if (!stations.containsKey(pos)) {
				stations.put(pos, new PrepStation(pos,true));
			}
			placeIngredientStation(pos);
			PrepStation station = ((PrepStation) stations.get(pos));
			station.lockCook();
			
			break;
		case "Chopping":
			if (!stations.containsKey(pos)) {
				stations.put(pos, new CuttingStation(pos, 1,true));
			}

			placeIngredientStation(pos);
			CuttingStation cutStation = ((CuttingStation) stations.get(pos));
			cutStation.lockCook();

			break;
		case "Baking":
			checkStationExists(pos, new BakingStation(pos,true));
			((CookingStation) stations.get(pos)).checkCookingStation(batch);
			((CookingStation) stations.get(pos)).lockCook();
			break;
		case "Frying_inactivce":
			checkStationExists(pos, new FryingStation(pos,false));
			((CookingStation) stations.get(pos)).checkCookingStation(batch);
			((CookingStation) stations.get(pos)).lockCook();
			break;
		case "Prep_inactive":
			if (!stations.containsKey(pos)) {
				stations.put(pos, new PrepStation(pos,false));
			}
			placeIngredientStation(pos);
			PrepStation station2 = ((PrepStation) stations.get(pos));
			station2.lockCook();
			
			break;
		case "Chopping_inactive":
			if (!stations.containsKey(pos)) {
				stations.put(pos, new CuttingStation(pos, 1,false));
			}

			placeIngredientStation(pos);
			CuttingStation cutStation2 = ((CuttingStation) stations.get(pos));
			cutStation2.lockCook();

			break;
		case "Baking_inactive":
			checkStationExists(pos, new BakingStation(pos,false));
			((CookingStation) stations.get(pos)).checkCookingStation(batch);
			((CookingStation) stations.get(pos)).lockCook();
			break;

		case "Service":
			if (!stations.containsKey(pos)) {
				stations.put(pos, new ServingStation(pos, customerController, gameMode));
			}

			((ServingStation) stations.get(pos)).serveCustomer(priceMultiplier);
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
		checkStationExists(pos, new Station(pos, 4, false, null, null,true));
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
