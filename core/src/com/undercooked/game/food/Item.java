package com.undercooked.game.food;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Item {
    private String itemID;
    public String name;
    public String texturePath;
    private int value;
    public Sprite sprite;

    public Item(String itemID, String name, String texturePath, int value) {
        this(itemID);
        this.name = name;
        this.texturePath = texturePath;
        this.sprite = new Sprite();
        setValue(value);
    }

    public Item(String itemID) {
        this.itemID = itemID;
    }

    public void updateSprite(Texture texture) {
        this.sprite = new Sprite(texture);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }

    public void setValue(int value) {
        this.value = Math.max(0, value);
    }

    public String getID() {
        return itemID;
    }

    public String getName() {
        return name;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public int getValue() {
        return value;
    }
}
