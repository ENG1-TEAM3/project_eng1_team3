package com.undercooked.game.files.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.textures.Textures;

/**
 *
 * TODO: JavaDocs
 *
 */

public class AssetController {

    /**
     * An {@link Array} holding {@link ObjectMap} of the {@link Texture}s, allowing
     * each {@link Texture}s to be accessed via a simple {@link String}.
     */
    protected ObjectMap<String, ObjectMap<String, Asset<?>>> assetMaps = new ObjectMap<>();

    /**
     * Adds a sprite to the {@link Textures} {@link #assetMaps} that
     * can be accessed elsewhere.
     * <br>Avoid creating multiple {@link Asset}s with the same ID, as
     * all {@link Object}s that use it must be updated if replaced.
     *
     * @param asset The {@link Asset} to add.
     * @param assetID The {@link String} ID of the {@link Asset}.
     */
    protected Asset setAsset(Asset asset, String assetID, String mapKey) {
        ObjectMap<String, Asset<?>> assetMap = assetMaps.get(mapKey);
        // If the texture is already initialised, dispose of the old one.
        if (assetMap.containsKey(assetID)) {
            assetMap.get(assetID).dispose();
            assetMap.put(assetID, asset);

            // Assets have been updated for assetID, so update it in the map.
            assetUpdate(assetID, asset, mapKey);
            return asset;
        }
        assetMap.put(assetID, asset);
        return asset;
    }

    /**
     * A function to Override if an {@link Asset} is updated.
     *
     * @param assetID The ID of asset that needs updating.
     * @param asset The new {@link Asset}.
     */
    protected void assetUpdate(String assetID, Asset asset, String mapKey) {

    }

    /**
     * Loads an {@link Asset} to {@link #assetMaps} using
     * the {@link String} {@code assetID} if there is
     * nothing mapped to it already.
     *
     * @param asset The {@link Asset} to add.
     * @param assetID The {@link String} ID to use for the {@link Asset}.
     */
    protected Asset loadAsset(Asset asset, String assetID, String mapKey) {
        if (!assetMaps.containsKey(mapKey)) {
            assetMaps.put(mapKey, new ObjectMap<String, Asset<?>>());
        }
        ObjectMap<String, Asset<?>> assetMap = assetMaps.get(mapKey);
        // If it exists already, just return the old one
        if (assetMap.containsKey(assetID)) {
            return assetMap.get(assetID);
        }
        setAsset(asset, assetID, mapKey);
        return asset;
    }

    /**
     * Get an {@link Asset} by its {@link String} {@code assetID}
     * @param assetID The {@link String} ID of the {@link Asset}.
     * @return The {@link Asset}.
     */
    protected Asset getAsset(String assetID, String mapKey) {
        if (!assetMaps.containsKey(mapKey)) {
            return null;
        }
        return assetMaps.get(mapKey).get(assetID);
    }

    /**
     * Disposes all Assets in the program, using the Assets' dispose function.
     */
    public void dispose() {
        // textureAtlases.clear();
        // Loop through the Assets and call their dispose function
        for (ObjectMap<String, Asset<?>> assetMap : assetMaps.values()) {
            dispose(assetMap);
            // Clear the assetMap
            assetMap.clear();
        }
        // Clear all the assetMaps
        assetMaps.clear();
    }

    /**
     * Disposes all Assets in a specific mapKey, and removes it
     * from {@code assetMaps} if it exists.
     *
     * @param mapKey A {@link String} ID of the {@code mapKey}.
     */
    public void dispose(String mapKey) {
        // Check if assetMaps has it
        if (assetMaps.containsKey(mapKey)) {
            // If it does, loop through its assets and then dispose of them
            dispose(assetMaps.get(mapKey));
            // Remove it from the assetMaps
            assetMaps.remove(mapKey);
        }
    }

    /**
     * Disposes all Assets in the provided {@code assetMap}.
     *
     * @param assetMap A {@link ObjectMap<String, Asset>} of the Assets.
     */
    public void dispose(ObjectMap<String, Asset<?>> assetMap) {
        // If it does, loop through its assets and then dispose of them
        for (Asset asset : assetMap.values()) {
            asset.dispose();
        }
    }

    /**
     * A function that is called everytime a new {@link Asset} is
     * added to a map.
     */
    protected void newAsset(Asset asset, String mapKey) {
        // To override
    }

}
