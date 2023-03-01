package com.undercooked.game.map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.station.Station;
import com.undercooked.game.station.StationManager;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.json.JsonFormat;
import com.undercooked.game.util.json.JsonVal;

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

    public Map load(String path, StationManager stationManager, boolean internal) {
        // Try loading the Json
        JsonValue root = JsonFormat.formatJson(FileControl.loadJsonData(path, internal), Constants.DefaultJson.mapFormat());
        // If it's null, then just load the default map and return that.
        if (root == null) {
            // Make sure this isn't the Default Map, to avoid an infinite loop.
            if (path != Constants.DEFAULT_MAP){
                return load(Constants.DEFAULT_MAP, stationManager, internal);
            } else {
                return null;
            }
        }

        // First clear the stationManager, as it will be used for this Map#
        stationManager.clear();

        // Convert the Map Json into an actual map
        Map outputMap = mapOfSize(root.getInt("width"), root.getInt("height"));

        // Loop through the stations
        for (JsonValue stationData : root.get("stations").iterator()) {
            // Put the whole thing in a try, just in case
            try {
                // Check station ID isn't null
                // If it is, just ignore.
                if (stationData.getString("id") != null) {
                    // If it isn't, load the json data for it
                    JsonValue stationRoot = JsonFormat.formatJson(FileControl.loadJsonData(stationManager.getStationPath(stationData.getString("id"))),
                            Constants.DefaultJson.stationFormat());
                    // If station root is null, then ignore.
                    // This will happen if the file wasn't found.
                    if (stationRoot == null) {
                        String stationID = stationRoot.getString("id");
                        // If station ID already exists, then skip the following
                        if (stationManager.hasID(stationID)) {
                            continue;
                        }
                        // Initialise the Station
                        Station newStation = new Station();
                        newStation.setTexture(stationData.getString("texture_path"));
                        newStation.setWidth(stationRoot.getInt("width"));
                        newStation.setHeight(stationRoot.getInt("height"));

                        stationManager.addStation(stationID, newStation);

                        // Add it to the map
                        outputMap.addMapEntity(newStation,
                                               stationRoot.getInt("x"),
                                               stationRoot.getInt("y"));
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
