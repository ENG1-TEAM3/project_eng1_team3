package com.undercooked.game.map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.station.Station;
import com.undercooked.game.station.StationData;
import com.undercooked.game.station.StationManager;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.json.JsonFormat;

public class MapManager {

    public MapManager() {
        load();
    }

    private void load() {

    }

    /*public Map get() {
        if (assetManager.isLoaded(mapPath)) {
            return assetManager.get(mapPath, TiledMap.class);
        } else {
            System.out.println(mapPath + " not loaded.");
            // If the Texture isn't loaded, then return the default texture.
            // If it's not loaded, then just return null.
            if (!assetManager.isLoaded(Constants.DEFAULT_MAP)) {
                System.out.println("Default path not loaded.");
                return null;
            }
            return assetManager.get(Constants.DEFAULT_MAP, TiledMap.class);
        }
    }*/

    public Map load(String path, StationManager stationManager) {
        // Try loading the Json
        JsonValue root = JsonFormat.formatJson(FileControl.loadJsonAsset(path, "maps"), Constants.DefaultJson.mapFormat());
        // If it's null, then just load the default map and return that.
        if (root == null) {
            // Make sure this isn't the Default Map, to avoid an infinite loop.
            if (path != Constants.DEFAULT_MAP){
                return load(Constants.DEFAULT_MAP, stationManager);
            } else {
                return null;
            }
        }

        // First clear the stationManager, as it will be used for this Map
        stationManager.clear();

        // Convert the Map Json into an actual map
        Map outputMap = mapOfSize(root.getInt("width"), root.getInt("height"));

        System.out.println(root);

        // Loop through the stations
        for (JsonValue stationData : root.get("stations").iterator()) {
            System.out.println(stationData);
            // Put the whole thing in a try, just in case
            try {
                // Check station ID isn't null
                // If it is, just ignore.
                String stationID = stationData.getString("station_id");
                if (stationID != null) {
                    // If stationManager doesn't have this station loaded, then load it
                    if (!stationManager.hasID(stationID)) {
                        stationManager.loadStationPath(stationID);
                    }
                    // If it's loaded, get the data for the Station
                    StationData data = stationManager.getStationData(stationID);
                    System.out.println(data);
                    // If station root is null, then ignore.
                    // This will happen if the file wasn't found.
                    if (data != null) {
                        // Initialise the Station
                        Station newStation = new Station(data);
                        newStation.setTexture(data.getTexturePath());
                        newStation.setWidth(data.getWidth());
                        newStation.setHeight(data.getWidth());
                        newStation.pos.x = stationData.getInt("x");
                        newStation.pos.y = stationData.getInt("y");

                        stationManager.addStation(newStation);

                        // Add it to the map
                        outputMap.addMapEntity(newStation,
                                               stationData.getInt("x"),
                                               stationData.getInt("y"));
                    }
                }
            } catch (GdxRuntimeException e) {
                e.printStackTrace();
            }
        }
        return outputMap;
    }

    /**
     * Unloads the map and renderer, if there is one of either.
     */
    public void unload() {

    }

    // Creates a map of size width and height.
    public static Map mapOfSize(int width, int height) {
        return new Map(width, height);
    }

    public static float gridToPos(int gridPos) {
        return gridPos * 64F;
    }

    public static int posToGrid(float pos) {
        return (int) (pos / 64F);
    }

}
