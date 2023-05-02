package com.team3gdx.game.tests;

import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Ingredients;
import com.team3gdx.game.station.BakingStation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Stack;

@RunWith(GdxTestRunner.class)
public class BakingStationTests {
    @Test
    public void testAllowedIngredients() {
        BakingStation BStation = new BakingStation(new Vector2(0, 0));
        Ingredient bun = new Ingredient(new Vector2(0, 0), 32, 32, "burger_bun", 0, .5f);
        Ingredient cooked_bun = new Ingredient(new Vector2(0, 0), 32, 32, "burger_bun", 0, .5f);
        Ingredient[] ALLOWED_INGREDIENTS = {Ingredients.bun, Ingredients.cooked_bun};
        assertEquals(ALLOWED_INGREDIENTS, BakingStation.ALLOWED_INGREDIENTS);
    }

    @Test
    public void testPlaceBun() {
        Ingredient bun = new Ingredient(new Vector2(0, 0), 32, 32, "burger_bun", 0, 1);
        BakingStation BStation = new BakingStation(new Vector2(0, 0));
        Stack<Ingredient> BSList = new Stack<Ingredient>();
        BSList.push(bun);
        BStation.place(bun);
        assertEquals(BSList.peek(), BStation.slots.peek());
    }

    @Test
    public void testFullStationPlacement() {
        Ingredient bun = new Ingredient(new Vector2(0, 0), 32, 32, "burger_bun", 0, 1);
        BakingStation BStation = new BakingStation(new Vector2(0, 0));
        Stack<Ingredient> BSList = new Stack<Ingredient>();
        BStation.place(bun);
        BStation.place(bun);
        BStation.place(bun);
        BStation.place(bun);
        boolean validPlace = BStation.place(bun);
        assertEquals(validPlace, false);
    }

}