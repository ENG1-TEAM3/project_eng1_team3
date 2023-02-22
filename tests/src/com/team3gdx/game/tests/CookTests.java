package com.team3gdx.game.tests;

import com.badlogic.gdx.Gdx;

import static com.team3gdx.game.food.Ingredients.tomato;
import static org.junit.Assert.*;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.entity.Cook;
import com.team3gdx.game.food.Ingredient;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class CookTests {

    @Test
    public void testPosX() {
        Cook cook = new Cook(new Vector2(64 * 5, 64 * 3), 1);
        assertEquals(cook.getX(), cook.pos.x, 0.001);
    }

    @Test
    public void testPosY() {
        Cook cook = new Cook(new Vector2(64 * 5, 64 * 3), 1);
        assertEquals(cook.getY(), cook.pos.y, 0.001);
    }

    @Test
    public void testGetWidth() {
        Cook cook = new Cook(new Vector2(64 * 5, 64 * 3), 1);
        assertEquals(cook.getWidth(), cook.width, 0.001);
    }

    @Test
    public void TestGetHeight() {
        Cook cook = new Cook(new Vector2(64 * 5, 64 * 3), 1);
        assertEquals(cook.getHeight(), cook.height, 0.001);
    }

    @Test
    public void testGetDirection() {
        Cook cook = new Cook(new Vector2(64 * 5, 64 * 3), 1);
        assertEquals(cook.getDirection(), cook.direction);
    }


    //Currently not working
    @Test
    public void testMaxStack() {
          Cook cook = new Cook(new Vector2(64 * 5, 64 * 3), 1);
          System.out.println(cook.heldItems.size());
          cook.pickUpItem(new Ingredient(null, 32, 32, "unformed_patty", 0, .5f));
          System.out.println(cook.heldItems.size());
          assertEquals(1, cook.heldItems.size(), 0.001);

    }

}
