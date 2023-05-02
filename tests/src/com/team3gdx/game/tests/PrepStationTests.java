package com.team3gdx.game.tests;

import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Ingredients;
import com.team3gdx.game.station.PrepStation;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Stack;
import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class PrepStationTests {

    @Test
    public void testSlotsToRecipe(){
        PrepStation PStation = new PrepStation(new Vector2(0,0), true);
        Ingredient cookedPatty = new Ingredient(null, 32, 32, "patty", 0, .5f);
        Ingredient cooked_bun = new Ingredient(new Vector2(0, 0), 32, 32, "burger_bun", 0, .5f);
        PStation.place(cookedPatty);
        PStation.place(cooked_bun);
        // held = PStation.slots.peek();
        assertEquals("burger", PStation.slots.peek());

    }
}
