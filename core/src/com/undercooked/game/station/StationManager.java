package com.undercooked.game.station;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.map.MapManager;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.json.JsonFormat;

import java.awt.datatransfer.StringSelection;

/**
 * 
 * Deals with all the stations and cook interactions. To create a new station,
 * extend from {@link Station}, check tile in
 * {@link this#checkInteractedTile(String, Vector2)}, and update station's
 * ingredients in {@link this#handleStations(SpriteBatch)} if necessary.
 *
 */
public class StationManager {

	/**
	 * A Map representing every station and its (x, y) coordinates.
	 */
	public static Array<Station> stations = new Array<>();
	public static ObjectMap<String, StationData> stationData;

	SpriteBatch batch;
	GameScreen game;

	public StationManager() {
		this.stations = new Array<>();
		this.stationData = new ObjectMap<>();
	}

	public void update(float delta) {
		// Update all the stations.
		for (Station station : stations) {
			station.update(delta);
		}
	}

	/**
	 * Checks every station for ingredients and updates them accordingly.
	 * 
	 * @param batch - SpriteBatch to render ingredient textures.
	 */
	/*public void handleStations(SpriteBatch batch) {
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
						((CuttingStation) station).interact(batch, .1f);
						station.interactSound();
					}

					if (currentIngredient.cooking && station instanceof CookingStation) {
						((CookingStation) station).drawParticles(batch, i);
						currentIngredient.cook(.0005f, batch);
						station.interactSound();
					} else {
						currentIngredient.draw(batch);
					}
				}
			}
		}
	}*/

	/**
	 * Check the currently looked at tile for a station.
	 * 
	 * @param type The station type.
	 * @param pos  The position of the tile.
	 */
	/*public void checkInteractedTile(String type, Vector2 pos) {
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
			checkStationExists(pos, new FryingStation(pos, game));
			((CookingStation) stations.get(pos)).checkCookingStation(batch);
			((CookingStation) stations.get(pos)).lockCook();
			break;
		case "Prep":
			if (!stations.containsKey(pos)) {
				stations.put(pos, new PrepStation(pos, game));
			}

			placeIngredientStation(pos);
			PrepStation station = ((PrepStation) stations.get(pos));
			station.lockCook();

			break;
		case "Chopping":
			if (!stations.containsKey(pos)) {
				stations.put(pos, new CuttingStation(pos, 1, game));
			}

			placeIngredientStation(pos);
			CuttingStation cutStation = ((CuttingStation) stations.get(pos));
			cutStation.lockCook();

			break;
		case "Baking":
			checkStationExists(pos, new BakingStation(pos, game));
			((CookingStation) stations.get(pos)).checkCookingStation(batch);
			((CookingStation) stations.get(pos)).lockCook();
			break;
		case "Service":
			if (!stations.containsKey(pos)) {
				stations.put(pos, new ServingStation(pos, game));
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

	}*/

	/**
	 * Check if the given station exists at the given position.
	 * 
	 * @param pos     Position to look for.
	 * @param station The station to check for.
	 * @return A boolean indicating if the station exists at that position.
	 */
	/*private boolean checkStationExists(Vector2 pos, Station station) {
		if (!stations.containsKey(pos)) {
			stations.put(pos, station);
			return false;
		}

		return true;
	}*/

	/**
	 * Place ingredient on top of cook's stack on station at given position.
	 * 
	 * @param pos The position to lookup the station.
	 */
	/*private void placeIngredientStation(Vector2 pos) {
		checkStationExists(pos, new Station(pos, 4, false, null, null, game));
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
	}*/

	/**
	 * Take an item from the ingredient station
	 * 
	 * @param pos        The position of the station.
	 * @param ingredient The ingredient that the station holds.
	 */
	/*private void takeIngredientStation(Vector2 pos, Ingredient ingredient) {
		checkStationExists(pos, new IngredientStation(pos, ingredient, game));
		stations.get(pos).drawTakeText(batch);

		if (GameScreen.control.interact) {
			GameScreen.cook.pickUpItem(stations.get(pos).take());
		}

	}*/

	private void loadStationsFromPath(String path, boolean internal, String pathPrefix) {
		FileHandle stations = FileControl.getFileHandle("game/stations/" + path, internal);
		for (FileHandle file : stations.list()) {
			// If it's a directory, then recurse
			if (file.isDirectory()) {
				loadStationsFromPath(path + file.name() + "/", internal, pathPrefix);
			} else {
				// If it's not, ensure it's a json.
				if (file.extension().equals("json")) {
					StationData newData = loadStationPath(pathPrefix + path + file.nameWithoutExtension());
					if (newData != null) {
						newData.setPath(file.path());
						stationData.put(pathPrefix + path + file.nameWithoutExtension(), newData);
					}
				}
			}
		}
	}

	private void loadStationsFromPath(String path, boolean internal) {
		if (internal) {
			loadStationsFromPath(path, true, "<main>:");
		} else {
			// Look at the stations folder
			loadStationsFromPath(path, false, "");
		}
	}

	/**
	 * Loads all the station paths in the game and stores them in an {@link ObjectMap}
	 * pairing of the station's id and its path.
	 */
	public void loadStationPaths() {
		// First clear the object map
		stationData.clear();

		// Load the internal path.
		loadStationsFromPath("", true);

		// Load from the external path.
		loadStationsFromPath(FileControl.getDataPath(), false);

		// System.out.println(stationData);
	}

	public StationData loadStationPath(String stationPath) {
		// Try to load this single station path
		// Read the file data
		JsonValue stationRoot = JsonFormat.formatJson(
				FileControl.loadJsonAsset(stationPath, "stations"),
				Constants.DefaultJson.stationFormat());
		// System.out.println("stationPath: " + stationPath);
		// System.out.println("stationRoot: " + stationRoot);
		// If it's not null...
		if (stationRoot != null) {
			// Load the data
			StationData data = new StationData();
			// data.setPath(file.path());
			data.setTexturePath(stationRoot.getString("texture_path"));
			data.setWidth(stationRoot.getInt("width"));
			data.setHeight(stationRoot.getInt("height"));
			data.setDefaultBase(stationRoot.getString("default_base"));
			data.setCollidable(stationRoot.getBoolean("has_collision"));

			// For collision width and height, if they are <= 0, then default to
			// grid size of the width and height
			float stationWidth = stationRoot.getFloat("collision_width");
			float stationHeight = stationRoot.getFloat("collision_height");

			data.setCollisionWidth(stationWidth > 0 ? stationWidth : MapManager.gridToPos(data.getWidth()));
			data.setCollisionHeight(stationHeight > 0 ? stationHeight : MapManager.gridToPos(data.getHeight()));

			data.setCollisionOffsetX(stationRoot.getFloat("collision_offset_x"));
			data.setCollisionOffsetY(stationRoot.getFloat("collision_offset_y"));
			// System.out.println("Width: " + data.getWidth() + ", Height: " + data.getHeight());
			// Then add it to the stations list
			return data;
		}
		return null;
	}

	public StationData getStationData(String stationID) {
		return stationData.get(stationID);
	}

	public void addStation(Station station) {
		// Only add if it's not contained already
		if (!stations.contains(station, true)) {
			stations.add(station);
		}
	}

	public void clear() {
		// Unload the stations
		stations.clear();
	}

	public boolean hasID(String stationID) {
		return stationData.containsKey(stationID);
	}
}
