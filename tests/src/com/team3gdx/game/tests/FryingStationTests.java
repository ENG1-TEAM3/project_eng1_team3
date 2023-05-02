package com.team3gdx.game.tests;

import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Ingredients;
import com.team3gdx.game.station.BakingStation;
import com.team3gdx.game.station.CookingStation;
import com.team3gdx.game.station.FryingStation;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class FryingStationTests {
    @Test
    public void testAllowedIngredients() {
        FryingStation FStation = new FryingStation(new Vector2(0, 0), true);
        Ingredient[] ALLOWED_INGREDIENTS = {Ingredients.formedPatty};
        assertEquals(ALLOWED_INGREDIENTS, FryingStation.ALLOWED_INGREDIENTS);
    }
}
