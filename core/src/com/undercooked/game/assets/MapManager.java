package com.undercooked.game.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.undercooked.game.util.Constants;

public class MapManager {

    AssetManager assetManager;
    String mapPath;
    OrthogonalTiledMapRenderer mapRenderer;
    public MapManager(AssetManager assetManager) {
        this.assetManager = assetManager;
        load();
    }

    private void load() {
        try {
            assetManager.load(Constants.DEFAULT_MAP, TiledMap.class);
        } catch (GdxRuntimeException e) {
            // Of course, make sure it actually doesn't crash if they can't load
            System.out.println("Couldn't load default TiledMap.");
            e.printStackTrace();
        }
    }

    public TiledMap get() {
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
    }

    public boolean load(String path) {
        // Load the TiledMap
        try {
            assetManager.load(path, TiledMap.class);
        } catch (GdxRuntimeException e) {
            // If it doesn't load, just return.
            return false;
        }
        // If the map loaded, then unload the previous map (if there is one) and save
        // the path to the new one.
        if (assetManager.isLoaded(mapPath)) {
            assetManager.unload(mapPath);
        }
        mapPath = path;
        return true;
    }

    public OrthogonalTiledMapRenderer createMapRenderer() {
        // If a renderer already exists, dispose it
        if (mapRenderer != null) {
            mapRenderer.dispose();
        }

        try {
            return new OrthogonalTiledMapRenderer(get());
        } catch (GdxRuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Unloads the map and renderer, if there is one of either.
     */
    public void unload() {
        if (assetManager.isLoaded(mapPath)) {
            assetManager.unload(mapPath);
        }
        if (mapRenderer != null) {
            mapRenderer.dispose();
        }
    }

    public static float gridToPos(float gridPos) {
        return gridPos * 64F;
    }

    public static float posToGrid(float pos) {
        return pos / 64F;
    }

}
