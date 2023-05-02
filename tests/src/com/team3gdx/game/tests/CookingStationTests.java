package com.team3gdx.game.tests;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.team3gdx.game.MainGameClass;
import com.team3gdx.game.entity.Cook;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Ingredients;
import com.team3gdx.game.station.CookingStation;
import com.team3gdx.game.station.CuttingStation;
import com.team3gdx.game.station.FryingStation;
import com.team3gdx.game.tests.GdxTestRunner;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class CookingStationTests {

    @Test
    public void testDrawParticles(){
        SpriteBatch batch = mock(SpriteBatch.class);
        Vector2 pos = new Vector2(0,0);
        Ingredient[] allowed =  { Ingredients.formedPatty };
        CookingStation CoStation = new CookingStation(pos, 4, allowed, "particles/flames.party", "audio/soundFX/frying.mp3", true);
        CoStation.drawParticles(batch, 0);
        verify(batch, atLeastOnce()).begin();
        verify(batch, atLeastOnce()).end();
    }

//    @Test
//    public void testLockCook() {
//        Cook cook = new Cook(new Vector2(0,0), 1);
//        Vector2 pos = new Vector2(0,0);
////        Ingredient[] allowed =  { Ingredients.lettuce };
////        CookingStation CoStation = new CookingStation(pos, 4, allowed, "particles/flames.party", "audio/soundFX/frying.mp3", true);
//        CuttingStation CStation = new CuttingStation(new Vector2(0, 0), 1, true);
//        Ingredient lettuce = new Ingredient(null, 32, 32, "lettuce", 1, 0);
//        CStation.place(lettuce);
//
//        boolean locked = cook.locked;
//        assertFalse(CStation.lockCook());
//    }

//    @Test
//    public void testCheckCookingStation(){
//        Vector2 pos = new Vector2(0,0);
//        Array<Cook> cooks = new Array<>();
//
//        Ingredient[] allowed =  { Ingredients.formedPatty, Ingredients.cookedPatty };
//        CookingStation CoStation = new CookingStation(pos, 4, allowed, "particles/flames.party", "audio/soundFX/frying.mp3", true);
//        Ingredient cookedPatty = new Ingredient(null, 32, 32, "patty", 0, .5f);
//        CoStation.place(cookedPatty);
//        SpriteBatch batch = mock(SpriteBatch.class);
//        ShapeRenderer shapeRenderer = mock(ShapeRenderer.class);
//        MainGameClass game = new MainGameClass();
//
//        game.batch = batch;
//        game.shapeRenderer = shapeRenderer;
//        cooks.add(new Cook(new Vector2(0,0), 1);
//        Cook cook =cooks.get(currentCookIndex);
//        CoStation.checkCookingStation(game);
//    }

}
