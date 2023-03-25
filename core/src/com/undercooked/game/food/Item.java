package com.undercooked.game.food;

public class Item {

    public String name;
    public String texturePath;
    private int value;

    public Item(String name, String texturePath, int value) {
        this.name = name;
        this.texturePath = texturePath;
        setValue(value);
    }

    public Item() {}

    public void setName(String name) {
        this.name = name;
    }

    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }

    public void setValue(int value) {
        this.value = Math.max(0, value);
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
