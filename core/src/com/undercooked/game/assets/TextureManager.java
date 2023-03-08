package com.undercooked.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.util.Constants;

public class TextureManager {

    /**
     * An ObjectMap of the textures. The first is the groupID while
     * the second is the path.
     * This is to make it easier to unload ALL textures of a group (screen)
     * in one function call.
     */
    ObjectMap<String, Array<String>> textures;
    AssetManager assetManager;

    public TextureManager (AssetManager assetManager) {
        this.assetManager = assetManager;
        this.textures = new ObjectMap<>();
        load();
    }

    /**
     * Loads the default assets for the textures for if they
     * haven't been loaded, to prevent any crashes.
     */
    private void load() {
        try {
            assetManager.load(Constants.DEFAULT_TEXTURE, Texture.class);
        } catch (GdxRuntimeException e) {
            // Of course, make sure it actually doesn't crash if they can't load
            System.out.println("Couldn't load default texture.");
            e.printStackTrace();
        }
    }

    public Texture get(String path) {
        if (assetManager.isLoaded(path)) {
            return assetManager.get(path, Texture.class);
        } else {
            System.out.println(path + " not loaded.");
            // If the Texture isn't loaded, then return the default texture.
            // If it's not loaded, then just return null.
            if (!assetManager.isLoaded(Constants.DEFAULT_TEXTURE)) {
                System.out.println("Default path not loaded.");
                return null;
            }
            return assetManager.get(Constants.DEFAULT_TEXTURE, Texture.class);
        }
    }

    public Texture getAsset(String path) {
        path = "game/" + FileControl.toPath(path, "textures");
        if (assetManager.isLoaded(path)) {
            return assetManager.get(path, Texture.class);
        } else {
            System.out.println(path + " not loaded.");
            // If the Texture isn't loaded, then return the default texture.
            // If it's not loaded, then just return null.
            if (!assetManager.isLoaded(Constants.DEFAULT_TEXTURE)) {
                System.out.println("Default path not loaded.");
                return null;
            }
            return assetManager.get(Constants.DEFAULT_TEXTURE, Texture.class);
        }
    }

    public boolean load(String textureGroup, String path) {
        try {
            // Try to load the Texture
            assetManager.load(path, Texture.class);
        } catch (GdxRuntimeException e) {
            e.printStackTrace();
            // If the file doesn't exist, then return nothing
            return false;
        }

        // Add it to the textures map.
        if (!textures.containsKey(textureGroup)) {
            // Add the texture group first if it doesn't exist yet
            textures.put(textureGroup, new Array<String>());
        }
        // Add the path to the group, ignoring if it's already there or not.
        textures.get(textureGroup).add(path);
        return true;
    }

    public boolean loadAsset(String textureGroup, String path, String assetFolder) {
        return load(textureGroup, "game/" + FileControl.toPath(path, assetFolder));
    }

    public boolean loadAsset(String path, String assetFolder) {
        return loadAsset("default", path, assetFolder);
    }


    public boolean load(String path) {
        return load("default", path);
    }

    /**
     * Unloads all textures that have been loaded
     */
    public void unload() {

    }

    /**
     * Unload all textures of a group
     */
    public void unload(String textureGroup) {
        // If the textureGroup doesn't exist, just return
        if (!textures.containsKey(textureGroup)) {
            return;
        }
        // Loop through the paths and unload only the first instance of each path.
        // This allows for loading a page multiple times when using the "nextScreen"
        // function in the ScreenController
        // (under the possibility that multiple screens need the same texture)
        Array<String> paths = textures.get(textureGroup);
        Array<String> pathsRemoved = new Array<>();
        for (int i = paths.size-1 ; i >= 0 ; i--) {
            String path = paths.get(i);
            if (!pathsRemoved.contains(path, false)) {
                // Remove the index of the path from the paths array
                paths.removeIndex(i);
                // Add the path to removed paths.
                pathsRemoved.add(path);
                if (assetManager.isLoaded(path)) {
                    assetManager.unload(path);
                }
            }
        }
        // Then remove the textureGroup as it's no longer needed
        textures.remove(textureGroup);
    }

    /**
     * Unload a single texture from all groups, completely.
     */
    public void unloadTexture(String texture) {
        for (String key : textures.keys()) {
            Array<String> paths = textures.get(key);
            int pathIndex = paths.indexOf(key, false);
            if (pathIndex != -1) {
                paths.removeIndex(pathIndex);
            }
        }
    }



}
