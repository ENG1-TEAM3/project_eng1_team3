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
    protected ObjectMap<String, ObjectMap<String, Asset>> assetMaps = new ObjectMap<>();

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
        ObjectMap<String, Asset> assetMap = assetMaps.get(mapKey);
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
            assetMaps.put(mapKey, new ObjectMap<String, Asset>());
        }
        ObjectMap<String, Asset> assetMap = assetMaps.get(mapKey);
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
     * Disposes all the texture atlases, sprites and forgets all the class sprites.
     */
    public void dispose() {
        // textureAtlases.clear();
        assetMaps.clear();
    }

    /**
     * A function that is called everytime a new {@link Asset} is
     * added to a map.
     */
    protected void newAsset(Asset asset, String mapKey) {
        // To override
    }

}
