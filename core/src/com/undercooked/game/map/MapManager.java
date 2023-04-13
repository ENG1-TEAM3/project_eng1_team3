package com.undercooked.game.map;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.cook.CookController;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.food.Items;
import com.undercooked.game.interactions.Interactions;
import com.undercooked.game.station.Station;
import com.undercooked.game.station.StationData;
import com.undercooked.game.station.StationManager;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.DefaultJson;
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

    public Map load(String path, StationManager stationManager, CookController cookController, Interactions interactions, Items gameItems) {
        // Try loading the Json
        JsonValue root = JsonFormat.formatJson(FileControl.loadJsonAsset(path, "maps"), DefaultJson.mapFormat());
        // If it's null, then just load the default map and return that.
        if (root == null) {
            // Make sure this isn't the Default Map, to avoid an infinite loop.
            if (path != Constants.DEFAULT_MAP){
                return load(Constants.DEFAULT_MAP, stationManager, cookController, interactions, gameItems);
            } else {
                return null;
            }
        }

        // First clear the stationManager, as it will be used for this Map
        stationManager.clear();

        // Convert the Map Json into an actual map
        Map outputMap = mapOfSize(root.getInt("width"), root.getInt("height"), stationManager, textureManager, audioManager, interactions, gameItems);

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
                        stationManager.loadStation(stationID);
                    }
                    // If it's loaded, get the data for the Station
                    StationData data = stationManager.getStationData(stationID);
                    // If station root is null, then ignore.
                    // This will happen if the file wasn't found.
                    if (data != null) {
                        // Initialise the Station
                        Station newStation = new Station(data);
                        newStation.makeInteractionController(audioManager, gameItems);
                        newStation.setInteractions(interactions);

                        // Check if there is a custom base
                        String basePath = stationData.getString("base_texture");
                        if (basePath != null) {
                            // If there is, then set the newStation to use it
                            newStation.setBasePath(basePath);
                        }
                        // Add it to the station manager
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

                        // Check if it has a custom price
                        if (stationData.has("price")) {
                            // If it does, then set the station to use it
                            newStation.setPrice(stationData.getInt("price"));
                            // If not, the station will use the StationData,
                            // which it does when give the StationData
                        }

                        // Add it to the map
                        outputMap.addMapEntity(newStation,
                                               stationData.getInt("x"),
                                               stationData.getInt("y"),
                                               data.getFloorTile(),
                                               hasCollision);

                        // If it does have collision
                        if (hasCollision) {
                            // Then set up the collision values
                            newStation.collision.setWidth(data.getCollisionWidth());
                            newStation.collision.setHeight(data.getCollisionHeight());
                            newStation.offsetX = data.getCollisionOffsetX();
                            newStation.offsetY = data.getCollisionOffsetY();
                        } else {
                            // If it doesn't, just set the collision to the size of the
                            // station
                            newStation.collision.setWidth(gridToPos(data.getWidth()));
                            newStation.collision.setHeight(gridToPos(data.getHeight()));
                        }
                    }
                }
            } catch (GdxRuntimeException e) {
                e.printStackTrace();
            }
        }

        // Give the map to the CookController so that it can load the cooks
        cookController.loadCooksIntoMap(root, outputMap, textureManager);

        return outputMap;
    }

    /**
     * Unloads the map and renderer, if there is one of either.
     */
    public void unload() {

    }

    public static Station newCounter(StationManager stationManager, AudioManager audioManager, Interactions interactions, Items gameItems) {
        // If the counter isn't loaded, then try to load it
        if (!stationManager.hasID("<main>:counter")) {
            StationData loadedData = stationManager.loadStation("<main>:counter");
            if (loadedData== null) {
                // If it doesn't load, throw an error
                throw new RuntimeException("Counter could not load.");
            }
        }
        // Create the station
        Station newCounter = new Station(stationManager.getStationData("<main>:counter"));
        newCounter.makeInteractionController(audioManager, gameItems);
        newCounter.setInteractions(interactions);
        // Add it to the station manager
        stationManager.addStation(newCounter);

        // Finally, return the counter
        return newCounter;
    }

    // Creates a map of size width and height.
    public static Map mapOfSize(int width, int height, StationManager stationManager, TextureManager textureManager, AudioManager audioManager, Interactions interactions, Items gameItems) {
        // The map size is the area that the players can run around in.
        // Therefore, height += 2, for top and bottom counters, and then
        // width has a few more added, primarily on the left.
        int fullWidth = width + 4,
            fullHeight = height + 1;
        Map returnMap = new Map(width, height, fullWidth, fullHeight, textureManager);
        int offsetX = 4, offsetY = 1;
        returnMap.setOffsetX(offsetX);
        returnMap.setOffsetY(offsetY);

        // Add the counter border
        // X entities
        for (int i = 0 ; i < width ; i++) {
            returnMap.addMapEntity(newCounter(stationManager, audioManager, interactions, gameItems),i,0,null);
            returnMap.addMapEntity(newCounter(stationManager, audioManager, interactions, gameItems),i,height-1,null);
        }
        // Y entities
        for (int j = 1 ; j < height-1 ; j++) {
            returnMap.addMapEntity(newCounter(stationManager, audioManager, interactions, gameItems),0,j,null);
            returnMap.addMapEntity(newCounter(stationManager, audioManager, interactions, gameItems),width-1,j,null);
        }

        // Leftmost wall
        for (int j = 1 ; j < fullHeight ; j++) {
            returnMap.addFullMapEntity(newCounter(stationManager, audioManager, interactions, gameItems),0,j,null);
        }

        // Add 2 at the top at x=1, x=2
        returnMap.addFullMapEntity(newCounter(stationManager, audioManager, interactions, gameItems), 1, fullHeight-1,null);
        returnMap.addFullMapEntity(newCounter(stationManager, audioManager, interactions, gameItems), 2, fullHeight-1,null);
        returnMap.addFullMapEntity(newCounter(stationManager, audioManager, interactions, gameItems), 3, fullHeight-1,null);

        // Add the customer floor tiles
        for (int i = 2 ; i <= 3 ; i++) {
            for (int j = 0 ; j < fullHeight-2; j++) {
                MapCell thisCell = returnMap.getCellFull(i, j);
                if (j == 0) thisCell.setBelowTile("<main>:floor/customer_tile_bot.png");
                else if (j == fullHeight-3) thisCell.setBelowTile("<main>:floor/customer_tile_top.png");
                else thisCell.setBelowTile("<main>:floor/customer_tile_mid.png");
            }
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
