package com.team3gdx.game.tests;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.MainGameClass;
import com.team3gdx.game.entity.Cook;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Ingredients;

import com.team3gdx.game.station.CuttingStation;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.team3gdx.game.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class CuttingStationTests {
    @Test
    public void testAllowedIngredients() {
        CuttingStation CStation = new CuttingStation(new Vector2(0, 0), 1, true);
        Ingredient[] ALLOWED_INGREDIENTS = { Ingredients.lettuce, Ingredients.tomato, Ingredients.onion, Ingredients.cheese, Ingredients.cookedPotato };
        assertEquals(ALLOWED_INGREDIENTS, CuttingStation.ALLOWED_INGREDIENTS);
    }

//    @Test
//    public void testInteract(){
//        SpriteBatch batch = mock(SpriteBatch.class);
//        ShapeRenderer shapeRenderer = mock(ShapeRenderer.class);
//
//        MainGameClass game = new MainGameClass();
//        game.batch = batch;
//        game.shapeRenderer = shapeRenderer;
//
//        CuttingStation CStation = new CuttingStation(new Vector2(0, 0), 1, true);
////        Cook cook = new Cook(new Vector2(0,0), 1);
//        Ingredient lettuce = new Ingredient(null, 32, 32, "lettuce", 1, 0);
//        CStation.place(lettuce);
//        CStation.interact(game, 0);
//
//
//    }

//    @Test
//    public void testInteract(){
//        CuttingStation CStation = new CuttingStation(new Vector2(0, 0), 1);
//        //CStation.interact(new SpriteBatch());
//    }

//    @Test
//    public void testLockCook(){
//        Cook cook = new Cook(new Vector2(0,0), 1);
//        CuttingStation CStation = new CuttingStation(new Vector2(0, 0), 1, true);
//        Ingredient lettuce = new Ingredient(null, 32, 32, "lettuce", 1, 0);
//        CStation.place(lettuce);
//        MainGameClass game = new MainGameClass();
//        CStation.interact(game,);
//
//    }
}

