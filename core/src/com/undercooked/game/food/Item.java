package com.undercooked.game.food;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * A class that holds the information for a specific item,
 * identified by the itemID, which is the assetPath to its
 * stored location.
 */
public class Item {
    /** The ID of the {@link Item}. */
    private String itemID;
    /** The display name of the {@link Item}. */
    public String name;
    /** The path to the {@link Texture}. */
    public String texturePath;
    /** The value as an {@code int}. 1 to £0.01. */
    private int value;
    /** The {@link Sprite} to draw the {@link Item} using. */
    public Sprite sprite;

    /**
     * The full constructor for the {@link Item}.
     *
     * @param itemID {@link String} : The ID of the {@link Item}.
     * @param name {@link String} : The display name.
     * @param texturePath {@link String} : The path to the {@link Texture}.
     * @param value {@code int} : The value of the {@link Item}.
     */
    public Item(String itemID, String name, String texturePath, int value) {
        this(itemID);
        this.name = name;
        this.texturePath = texturePath;
        this.sprite = new Sprite();
        setValue(value);
    }

    /**
     * Simpler constructor for the {@link Item} that only accepts the
     * ID of the {@link Item}.
     * <br>The setters should be used if this constructor is used instead.
     *
     * @param itemID {@link String} : The ID of the {@link Item}.
     */
    public Item(String itemID) {
        this.itemID = itemID;
    }

    /**
     * Updates the {@link Sprite} to use the {@link Texture} provided.
     *
     * @param texture {@link Texture} : The Texture to use.
     */
    public void updateSprite(Texture texture) {
        this.sprite = new Sprite(texture);
    }

    /**
     * Set the display name of the {@link Item}.
     *
     * @param name {@link String} : The display name to set to.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the texturePath of the {@link Item}.
     *
     * @param texturePath {@link String} : The texturePath to set to.
     */
    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }

    /**
     * Set the value of the {@link Item}.
     * <br>Forces it to a minimum of 0.
     *
     * @param value {@link int} : The value to set to.
     */
    public void setValue(int value) {
        this.value = Math.max(0, value);
    }

    /**
     * Returns the ID of the {@link Item}.
     *
     * @return {@link String} : The ID.
     */
    public String getID() {
        return itemID;
    }

    /**
     * Returns the display name of the {@link Item}.
     *
     * @return {@link String} : The display name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the texturePath of the {@link Item}.
     *
     * @return {@link String} : The texturePath.
     */
    public String getTexturePath() {
        return texturePath;
    }

    /**
     * Returns the value of the {@link Item}.
     *
     * @return {@code int} : The value of the {@link Item}.
     */
    public int getValue() {
        return value;
    }
}
