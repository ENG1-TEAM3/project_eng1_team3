package com.team3gdx.game.save;

public class ChefInfo {
    public ChefInfo() {}

    public ChefInfo(float x, float y, int cookNum, IngredientInfo[] ingredients) {

        this.x = x;
        this.y = y;
        this.cookNum = cookNum;
        this.ingredients = ingredients;
    }

    public float x;
    public float y;
    public int cookNum;

    public IngredientInfo[] ingredients;
}
