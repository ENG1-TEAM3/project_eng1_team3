package com.undercooked.game.station;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.map.MapManager;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.util.DefaultJson;
import com.undercooked.game.util.json.JsonFormat;

/**
 * Deals with the loading and storage of all {@link Station}s
 * and their {@link StationData} within the game.
 */
public class StationController {

	/**
	 * An {@link Array} of the {@link Station}s that are in the
	 * game.
	 */
	public Array<Station> stations;
	/**
	 * An {@link ObjectMap<String, StationData>} that links a
	 * {@link Station}'s id to the {@link StationData} that it
	 * has loaded.
	 */
	public ObjectMap<String, StationData> stationData;

	SpriteBatch batch;
	GameScreen game;

	/**
	 * Constructor for the {@link StationController}.
	 * This sets up the {@link Array<Station>} for {@link #stations}
	 * and the {@link ObjectMap<String, StationData>} for {@link #stationData}.
	 */
	public StationController() {
		this.stations = new Array<>();
		this.stationData = new ObjectMap<>();
	}

	/**
	 * A function that calls the {@link Station#update(float)} function
	 * for all {@link Station}s that are stored by the {@link #stations}
	 * {@link Array}.
	 * 
	 * @param delta
	 */
	public void update(float delta, float powerUpMultiplier) {
		// Update all the stations.
		for (Station station : stations) {
			station.update(delta, powerUpMultiplier);
		}
	}

	/**
	 * Calls the {@link Station#updateInteractions()} and
	 * {@link Station#updateStationInteractions()} functions for
	 * all of the {@link Station}s stored in the {@link #stations}
	 * {@link Array}.
	 * <br>
	 * This sets up their interactions.
	 */
	public void updateStationInteractions() {
		// Update all interactions for the stations.
		for (Station station : stations) {
			station.updateInteractions();
			station.updateStationInteractions();
		}
	}

	/**
	 * Loads all of the {@link StationData} for each {@link Station} ID
	 * into the {@link #stationData} {@link ObjectMap}
	 * 
	 * @param path       {@link String} : The asset path to the {@link StationData}
	 *                   JSON.
	 * @param internal
	 * @param pathPrefix
	 */
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
						newData.setPath(pathPrefix + path + file.nameWithoutExtension());
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
	 * Loads all the station paths in the game and stores them in an
	 * {@link ObjectMap}
	 * pairing of the station's id and its path.
	 */
	public void loadStationPaths() {
		// First clear the object map
		stationData.clear();

		// Load the internal path.
		loadStationsFromPath("", true);

		// Load from the external path.
		loadStationsFromPath(FileControl.getDataPath(), false);

	}

	public StationData loadStation(String stationPath) {
		StationData loadedData = loadStationPath(stationPath);
		// Add it to the array, if it's not null.
		if (loadedData != null) {
			stationData.put(stationPath, loadedData);
		}
		return loadedData;
	}

	public StationData loadStationPath(String stationPath) {
		// Try to load this single station path
		// Read the file data
		JsonValue stationRoot = JsonFormat.formatJson(
				FileControl.loadJsonAsset(stationPath, "stations"),
				DefaultJson.stationFormat());

		// If it's not null...
		if (stationRoot != null) {
			// Load the data
			StationData data = new StationData(stationPath);
			// data.setPath(file.path());
			data.setTexturePath(stationRoot.getString("texture_path"));
			data.setWidth(stationRoot.getInt("width"));
			data.setHeight(stationRoot.getInt("height"));
			data.setDefaultBase(stationRoot.getString("default_base"));
			data.setFloorTile(stationRoot.getString("floor_tile"));
			data.setCollidable(stationRoot.getBoolean("has_collision"));

			// For collision width and height, if they are <= 0, then default to
			// grid size of the width and height
			float stationWidth = stationRoot.getFloat("collision_width");
			float stationHeight = stationRoot.getFloat("collision_height");

			data.setCollisionWidth(stationWidth > 0 ? stationWidth : MapManager.gridToPos(data.getWidth()));
			data.setCollisionHeight(stationHeight > 0 ? stationHeight : MapManager.gridToPos(data.getHeight()));

			data.setCollisionOffsetX(stationRoot.getFloat("collision_offset_x"));
			data.setCollisionOffsetY(stationRoot.getFloat("collision_offset_y"));

			data.setHoldCount(stationRoot.getInt("holds"));

			data.setPrice(stationRoot.getInt("price"));
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

	public void reset() {
		// Reset all stations
		for (Station station : stations) {
			station.reset();
		}
	}

	public void stopAll() {
		// Stop all stations
		for (Station station : stations) {
			station.stop();
		}
	}

	public void clear() {
		// Clear the stations
		stations.clear();
	}

	public void dispose() {
		// Clear
		clear();

		// And remove all StationData
		stationData.clear();
	}

	public boolean hasID(String stationID) {
		return stationData.containsKey(stationID);
	}

	public JsonValue serializeStations() {
		// JsonValue stationsRoot = new JsonValue(JsonValue.ValueType.object);
		JsonValue stationsArrayRoot = new JsonValue(JsonValue.ValueType.array);
		// stationsRoot.addChild("stations", stationsArrayRoot);

		for (Station station : this.stations) {
			stationsArrayRoot.addChild(station.serial());
		}

		// return stationsRoot;
		return stationsArrayRoot;
	}
}
