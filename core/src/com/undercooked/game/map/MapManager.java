package com.undercooked.game.map;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.CookController;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.station.UniqueStation;
import com.undercooked.game.station.Station;
import com.undercooked.game.station.StationData;
import com.undercooked.game.station.StationManager;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.json.JsonFormat;

public class MapManager {

    private TextureManager textureManager;
    private AudioManager audioManager;

    public MapManager(TextureManager textureManager, AudioManager audioManager) {
        this.textureManager = textureManager;
        this.audioManager = audioManager;
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

    public Map load(String path, StationManager stationManager, CookController cookController) {
        // Try loading the Json
        JsonValue root = JsonFormat.formatJson(FileControl.loadJsonAsset(path, "maps"), Constants.DefaultJson.mapFormat());
        // If it's null, then just load the default map and return that.
        if (root == null) {
            // Make sure this isn't the Default Map, to avoid an infinite loop.
            if (path != Constants.DEFAULT_MAP){
                return load(Constants.DEFAULT_MAP, stationManager, cookController);
            } else {
                return null;
            }
        }

        // First clear the stationManager, as it will be used for this Map
        stationManager.clear();

        // Convert the Map Json into an actual map
        Map outputMap = mapOfSize(root.getInt("width"), root.getInt("height"));

        // Loop through the stations
        for (JsonValue stationData : root.get("stations").iterator()) {
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
                    // If station root is null, then ignore.
                    // This will happen if the file wasn't found.
                    if (data != null) {
                        // Initialise the Station
                        Station newStation = new Station(data);

                        // Check if there is a custom base
                        String basePath = stationData.getString("base_texture");
                        if (basePath != null) {
                            // If there is, then set the newStation to use it
                            newStation.setBasePath(basePath);
                        }
                        stationManager.addStation(newStation);

                        boolean hasCollision;
                        // Check if it has a custom has_collision tag
                        if (stationData.has("has_collision")) {
                            // If it does, then set the station to use it
                            hasCollision = stationData.getBoolean("has_collision");
                        } else {
                            // If it doesn't, get it from the station data
                            hasCollision = data.isCollidable();
                        }

                        // Add it to the map
                        outputMap.addMapEntity(newStation,
                                               stationData.getInt("x"),
                                               stationData.getInt("y"),
                                               hasCollision);
                    }
                }
            } catch (GdxRuntimeException e) {
                e.printStackTrace();
            }
        }

        // Give the map to the CookController so that it can load the cooks
        cookController.loadCooksIntoMap(root, outputMap, textureManager);

        System.out.println(outputMap);
        return outputMap;
    }

    /**
     * Unloads the map and renderer, if there is one of either.
     */
    public void unload() {

    }

    public static UniqueStation newCounter() {
        UniqueStation newCounter = new UniqueStation(StationManager.stationData.get("<main>:counter"));
        return newCounter;
    }

    // Creates a map of size width and height.
    public static Map mapOfSize(int width, int height) {
        // The map size is the area that the players can run around in.
        // Therefore, height += 2, for top and bottom counters, and then
        // width has a few more added, primarily on the left.
        Map returnMap = new Map(width, height, width+5, height+2);
        int offsetX = 3, offsetY = 1;
        returnMap.setOffsetX(offsetX);
        returnMap.setOffsetY(offsetY);

        // Add the counter border
        // X entities
        for (int i = 0 ; i < width ; i++) {
            returnMap.addMapEntity(newCounter(),i,0);
            returnMap.addMapEntity(newCounter(),i,height-1);
        }
        // Y entities
        for (int j = 1 ; j < height-1 ; j++) {
            returnMap.addMapEntity(newCounter(),0,j);
            returnMap.addMapEntity(newCounter(),width-1,j);
        }
        return returnMap;
    }

    public static float gridToPos(int gridPos) {
        return gridPos * 64F;
    }

    public static float gridToPos(float gridPos) {
        return gridPos * 64F;
    }

    public static int posToGridFloor(float pos) {
        return (int) (pos / 64F);
    }

    public static float posToGrid(float pos) {
        return (pos / 64F);
    }

}
