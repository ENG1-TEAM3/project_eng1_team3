package com.undercooked.game.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.util.Constants;

/**
 * A class to manage the {@link Texture}s within the game.
 */
public class TextureManager {

    /**
     * An ObjectMap of the textures. The first is the groupID while
     * the second is the path.
     * This is to make it easier to unload ALL textures of a group (screen)
     * in one function call.
     */
    ObjectMap<String, Array<String>> textures;

    /** The {@link AssetManager} that will be used to load, get and unload the {@link Texture}s. */
    AssetManager assetManager;

    /**
     * Constructor to set up the {@link ObjectMap} for the {@link #textures}.
     * @param assetManager {@link AssetManager} : The {@link AssetManager} to use.
     */
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

    /**
     * Returns the {@link Texture} at the path provided, if it has been loaded.
     * <br>If not loaded, it returns the default texture.
     * @param path {@link String} : The path of the {@link Texture}.
     * @return {@link Texture} : The {@link Texture} loaded from the path.
     */
    public Texture get(String path) {
        System.out.println("Getting Texture: " + path);
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

    /**
     * Gets a {@link Texture} from an asset path.
     * @param path {@link String} : The asset path of the {@link Texture}.
     * @return {@link Texture} : The {@link Texture} loaded from the path.
     */
    public Texture getAsset(String path) {
        // Only try if it's not null
        if (path == null) {
            return get(null);
        }
        path = FileControl.getAssetPath(path, "textures");
        return get(path);
    }

    /**
     * Loads a {@link Texture} from a path.
     * @param textureGroup {@link String} : The texture group to load it to.
     * @param path {@link String} : The path of the {@link Texture}.
     * @return {@code boolean} : {@code true} if it was able to add it to be loaded
     *                           by the {@link AssetManager},
     *                           {@code false} if not.
     */
    public boolean load(String textureGroup, String path) {
        System.out.println("Loading Texture: " + path);
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

    /**
     * Loads a {@link Texture} from an asset path.
     * @param textureGroup {@link String} : The texture group to load it to.
     * @param path {@link String} : The asset path of the {@link Texture}.
     * @param assetFolder {@link String} : The folder the asset is in.
     * @return {@code boolean} : {@code true} if it was able to add it to be loaded
     *                           by the {@link AssetManager},
     *                           {@code false} if not.
     */
    public boolean loadAsset(String textureGroup, String path, String assetFolder) {
        // Only try if it's not null
        if (path == null) {
            return false;
        }
        return load(textureGroup, FileControl.getAssetPath(path, assetFolder));
    }


    /**
     * Loads a {@link Texture} from an asset path.
     * @param textureGroup {@link String} : The texture group to load it to.
     * @param path {@link String} : The asset path of the {@link Texture}.
     * @return {@code boolean} : {@code true} if it was able to add it to be loaded
     *                           by the {@link AssetManager},
     *                           {@code false} if not.
     */
    public boolean loadAsset(String textureGroup, String path) {
        return loadAsset(textureGroup, path, "textures");
    }

    /**
     * Loads a {@link Texture} from a path, putting it in a default group.
     * @param path {@link String} : The path of the {@link Texture}.
     * @return {@code boolean} : {@code true} if it was able to add it to be loaded
     *                           by the {@link AssetManager},
     *                           {@code false} if not.
     */
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
    public void unload(String textureGroup, boolean unloadRepeats) {
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
            if (unloadRepeats || !pathsRemoved.contains(path, false)) {
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
     * Unload all textures of a group, by default only unloading
     * each file once.
     */
    public void unload(String textureGroup) {
        unload(textureGroup, false);
    }

    /**
     * Unload a single texture from all groups, completely.
     */
    public void unloadTexture(String texture) {
        for (String key : textures.keys()) {
            Array<String> paths = textures.get(key);
            int pathIndex = paths.indexOf(texture, false);
            if (pathIndex != -1) {
                paths.removeIndex(pathIndex);
            }
        }
    }
}
