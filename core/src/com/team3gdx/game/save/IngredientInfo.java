package com.team3gdx.game.save;

import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Status;

public class IngredientInfo {
    public IngredientInfo() {}

    public IngredientInfo(Ingredient ingredient) {
        slices = ingredient.slices;
        idealSlices = ingredient.getIdealSlices();
        cookedTime = ingredient.getCookedTime();
        idealCookedTime = ingredient.getIdealCookedTime();
        usable = ingredient.isUsable();
        status = ingredient.status;
        cooking = ingredient.cooking;
        slicing = ingredient.slicing;
        flipped = ingredient.flipped;
        rolling = ingredient.rolling;
        name = ingredient.name;
        x = ingredient.pos.x;
        y = ingredient.pos.y;
        width = ingredient.width;
        height = ingredient.height;
    }

    public int slices;
    public int idealSlices;
    public float cookedTime;
    public float idealCookedTime;
    public boolean usable;
    public Status status;

    public boolean cooking;
    public boolean slicing;
    public boolean flipped;
    public boolean rolling;

    public String name;

    public float x;
    public float y;

    public float width;
    public float height;
}
