package com.team3gdx.game.save;

public class StationInfo {
    public StationInfo() {}

    public StationInfo(float x, float y, boolean active, StationType type, IngredientInfo[] ingredients) {

        this.x = x;
        this.y = y;
        this.active = active;
        this.type = type;
        this.ingredients = ingredients;
    }

    public float x;
    public float y;
    public boolean active;
    public StationType type;
    public IngredientInfo[] ingredients;
}