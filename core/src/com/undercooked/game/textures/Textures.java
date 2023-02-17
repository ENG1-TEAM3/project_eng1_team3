package com.undercooked.game.textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.files.assets.Asset;
import com.undercooked.game.files.assets.AssetController;

/** A class which loads and manages all assets of the game.
 * It allows for each class to have their own instance
 * of each sprite, as well as being able to dispose of them.
 */
public class Textures extends AssetController {

    /** Make Singleton Class */
    protected Textures() { }

    private static Textures INSTANCE;

    public static Textures getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Textures();
        }
        return INSTANCE;
    }

    /** This class is used for storing the assets of a class requesting it */
    private class InstanceAssets {
        ObjectMap<String, Sprite> sprites; // The string matches the textureID used for the Sprite.

        public InstanceAssets() {
            sprites = new ObjectMap<>();
        }

        public void setSprite(String spriteID, Sprite sprite) {
            // If the sprite already exists, then dispose it
            sprites.put(spriteID, sprite);
        }

        public ObjectMap.Keys<String> getSpriteIDs() {
            return sprites.keys();
        }

        public void dispose() {
            // Clear the sprites array, so that there's nothing directing
            // to them so that they will be deleted
            sprites.clear();
        }
    }

    /**
     * An {@link ObjectMap} of the different Objects' {@link Sprite}s,
     * so that they each have a unique {@link Sprite} instance.
     */
    private ObjectMap<String, InstanceAssets> instanceMap = new ObjectMap<>();
    private int ID = 0; // Instance ID

    /**
     * Unused as of right now, as
     *
     * Puts together the {@link #textureMap} using the {@link TextureAtlas}es
     * provided in the {@link #textureAtlases} {@link ObjectMap}.
     *
     * It creates the {@link Sprite}s here, so they
     * only ever have to be created once.
     */
    /*public static void createResources() {
        for (String spriteID : textureAtlases.keys()) {
            TextureAtlas thisAtlas = textureAtlases.get(spriteID);
            for (TextureAtlas.AtlasRegion spriteRegion : thisAtlas.getRegions()) {
                spriteMap.put(spriteKey(spriteID,spriteRegion.name),thisAtlas.createSprite(spriteRegion.name));
            }
        }
    }*/

    /**
     * Get a {@link Texture} from memory and store it in the {@code assetMap}.
     *
     * @param path The path of the {@link Texture}
     * @param textureID The {@link String} ID to use for the {@link Texture}.
     * @return {@link Texture} : The {@link Texture} from the file.
     */
    public Texture loadTextureFile(String path, String textureID) {
        return (Texture) setAsset(textureFile(path), textureID, "texture").getAsset();
    }

    /**
     * Similar to {@link #loadTextureFile(String, String)}, but instead of
     * loading from the file no matter what, it instead first checks if the
     * {@code textureID} already exists or not.
     *
     * @param path The path of the {@link Texture}
     * @param textureID The {@link String} ID to use for the {@link Texture}.
     * @return {@link Texture} : The {@link Texture} from the file, or matching the ID.
     */
    public Texture loadTexture(String path, String textureID) {
        return (Texture) loadAsset(textureFile(path), textureID, "texture").getAsset();
    }

    /**
     * Load a {@link Texture} from memory and convert it into a
     * {@link TextureAsset}.
     *
     * @param path The path to the {@link Texture}
     * @return {@link Asset} : The {@link TextureAsset}.
     */
    protected Asset textureFile(String path) {
        return new TextureAsset(new Texture(Gdx.files.internal(path)));
    }

    /**
     * Gets the {@link Texture} mapped to the {@link String} {@code textureID}.
     *
     * @param textureID {@link String} ID for the {@link Texture}.
     * @return {@link Texture} : The {@link Texture} requested.
     *                           null if no {@link Texture} is mapped.
     */
    public Texture getTexture(String textureID) {
        return (Texture) getAsset(textureID, "texture").getAsset();
    }

    /**
     * Creates a {@link Sprite} from a {@link Texture}.
     * <br> A function on its own, so it can easily be modified
     * if needed.
     *
     * @param texture The {@link Texture} of the {@link Sprite}.
     * @return The {@link Sprite} created.
     */
    private Sprite createSprite(Texture texture) {
        return new Sprite(texture);
    }

    /**
     * Creates a new {@link InstanceAssets} which stores {@link Sprite}s.
     * <br><br>
     * By separating them like this, one {@link Object} can create and modify
     * a {@link Sprite} without affecting another.
     * <br><br>
     * Return needs to be cast to {@code int} or {@link InstanceAssets}.
     *
     * @param returnID Whether the function should return the {@code int} ID
     *                 given to the {@link InstanceAssets} (true), or the
     *                 {@link InstanceAssets} itself (false).
     * @return {@link Object} : {@code int} ID or {@link InstanceAssets}.
     */
    private Object createInstance(boolean returnID) {
        // Convert the current ID number into a String, and that is the ID for the instance
        InstanceAssets newInstance = new InstanceAssets();
        String thisID = Integer.toString(ID);
        instanceMap.put(thisID,newInstance);
        // Move to the next ID.
        ID += 1;
        // Finally, return the instance, or the ID.
        if (returnID) {
            return thisID;
        }
        return newInstance;
    }

    /**
     * Creates an instance that can be used to collect sprites from.
     *
     * @return {@link InstanceAssets} : The instance created.
     */
    public String newInstanceID() {
        return (String) createInstance(false);
    }

    /**
     * Creates an instance that can be used to collect sprites from.
     *
     * @return {@link InstanceAssets} : The instance created.
     */
    public InstanceAssets newInstance() {
        return (InstanceAssets) createInstance(false);
    }

    /**
     * Disposes all the texture atlases, sprites and forgets all the class sprites.
     */
    @Override
    public void dispose() {
        super.dispose();
        // Loop through the asset instances
        for (InstanceAssets instance : instanceMap.values()) {
            // Dispose of the instances
            instance.dispose();
        }
        // Clear the instanceMap so the Instances will be deleted
        instanceMap.clear();
    }
}
