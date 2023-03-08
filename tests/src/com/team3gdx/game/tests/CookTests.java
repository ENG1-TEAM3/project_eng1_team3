package com.team3gdx.game.tests;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.entity.Cook;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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

    @Test
    public void testGetCollideBoxAtPosition() {
        Cook cook = new Cook(new Vector2(64 * 5, 64 * 3), 1);
        Rectangle rec = new Rectangle(15, 15, 40, 25);
        Rectangle rec2 = cook.getCollideBoxAtPosition(3, 25);
        assertEquals(rec2, rec);
    }

    @Test
    public void testCheckCollision() {
        Cook cook = new Cook(new Vector2(64 * 5, 64 * 3), 1);
        Boolean result = cook.checkCollision(10, 1, null);
        assertFalse(result);
    }
}
