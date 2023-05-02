package com.team3gdx.game.tests;

import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Ingredients;

import com.team3gdx.game.station.CuttingStation;


import static org.junit.Assert.assertEquals;

import com.team3gdx.game.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class CuttingStationTests {
    @Test
    public void testAllowedIngredients() {
        CuttingStation CStation = new CuttingStation(new Vector2(0, 0), 1);
        Ingredient[] ALLOWED_INGREDIENTS = {Ingredients.lettuce, Ingredients.tomato, Ingredients.onion};
        assertEquals(ALLOWED_INGREDIENTS, CuttingStation.ALLOWED_INGREDIENTS);
    }

//    @Test
//    public void testInteract(){
//        CuttingStation CStation = new CuttingStation(new Vector2(0, 0), 1);
//        //CStation.interact(new SpriteBatch());
//    }

    @Test
    public void testLockCook(){
        CuttingStation CStation = new CuttingStation(new Vector2(0, 0), 1);
        Ingredient lettuce = new Ingredient(null, 32, 32, "lettuce", 1, 0);
        CStation.place(lettuce);
        CStation.lockCook();

    }
}
}
