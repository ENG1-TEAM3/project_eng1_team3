package com.team3gdx.game.tests;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.entity.Cook;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Ingredients;
import com.team3gdx.game.station.CookingStation;
import com.team3gdx.game.tests.GdxTestRunner;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class CookingStationTests {
    @Test
    public void testInstantiation(){
        Vector2 pos = new Vector2(0,0);
        Ingredient[] allowed =  { Ingredients.formedPatty };
        CookingStation CoStation = new CookingStation(pos, 4, allowed, "particles/flames.party", "audio/soundFX/frying.mp3");
    }

//    @Test
//    public void testDrawParticles(){
//        SpriteBatch batch = new SpriteBatch();
//        Vector2 pos = new Vector2(0,0);
//        Ingredient[] allowed =  { Ingredients.formedPatty };
//        CookingStation CoStation = new CookingStation(pos, 4, allowed, "particles/flames.party", "audio/soundFX/frying.mp3");
//        CoStation.drawParticles(batch, 0);
//    }

    @Test
    public void testLockCook() {
        Cook cook = new Cook(new Vector2(0,0), 1);
        Vector2 pos = new Vector2(0,0);
        Ingredient[] allowed =  { Ingredients.formedPatty };
        CookingStation CoStation = new CookingStation(pos, 4, allowed, "particles/flames.party", "audio/soundFX/frying.mp3");
        assertFalse(CoStation.lockCook());
    }
}
