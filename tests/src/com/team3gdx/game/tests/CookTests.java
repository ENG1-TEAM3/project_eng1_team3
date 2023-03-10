package com.team3gdx.game.tests;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.entity.Cook;

import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Ingredients;
import com.team3gdx.game.screen.GameScreen;
import com.team3gdx.game.util.Control;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.team3gdx.game.screen.GameScreen.CLTiles;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


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
        TiledMap map1 = new TmxMapLoader().load("map/art_map/customertest.tmx");
        GameScreen.constructCollisionData(map1);
        Boolean result = cook.checkCollision(10, 1, CLTiles);
        assertFalse(result);
        Boolean result2 = cook.checkCollision(10, 20, CLTiles);
        assertTrue(result2);
    }

    @Test
    public void testingPickUpItem() {
        Cook cook = new Cook(new Vector2(64 * 5, 64 * 3), 1);
        Ingredient newIngr = Ingredients.unformedPatty;
        cook.pickUpItem(newIngr);
        assertEquals(cook.heldItems.size(), 1, 0.0001);
        assertFalse(newIngr.cooking);
        assertFalse(newIngr.slicing);
    }

    @Test
    public void testDropItem() {
        Cook cook = new Cook(new Vector2(64 * 5, 64 * 3), 1);
        Ingredient newIngr = Ingredients.unformedPatty;
        // One ingredient
        cook.pickUpItem(newIngr);
        assertEquals(cook.heldItems.size(), 1, 0.0001);
        cook.dropItem();
        assertFalse(cook.holding);
        assertEquals(cook.heldItems.size(), 0, 0.0001);
        // No Ingredient
        assertFalse(cook.holding);
        assertEquals(cook.heldItems.size(), 0, 0.0001);
        // Multiple Ingredient
        Ingredient newIngr2 = Ingredients.lettuce;
        cook.pickUpItem(newIngr);
        cook.pickUpItem(newIngr2);
        assertEquals(cook.heldItems.size(), 2, 0.0001);
        Boolean prevHold = cook.holding;
        cook.dropItem();
        assertEquals(cook.heldItems.size(), 1, 0.0001);
        assertEquals(cook.holding, prevHold);
    }

    @Test
    public void testFull() {
        Cook cook = new Cook(new Vector2(64 * 5, 64 * 3), 1);
        Ingredient newIngr = Ingredients.unformedPatty;
        cook.pickUpItem(newIngr);
        cook.pickUpItem(newIngr);
        cook.pickUpItem(newIngr);
        cook.pickUpItem(newIngr);
        cook.pickUpItem(newIngr);
        assertEquals(cook.heldItems.size(), 5, 0.0001);
        cook.pickUpItem(newIngr);
        assertEquals(cook.heldItems.size(), 5, 0.0001);
        assertTrue(cook.full());
    }

    @Test
    public void testUpdate() {
        // Setup
        Cook cook = new Cook(new Vector2(64 * 5, 64 * 3), 1);
        Control control = new Control();
        TiledMap map1 = new TmxMapLoader().load("map/art_map/customertest.tmx");
        GameScreen.constructCollisionData(map1);
        // Move up
        control.up = true;
        control.down = false;
        cook.update(control, System.currentTimeMillis(), GameScreen.CLTiles);
        assertEquals(cook.getDirection(), new Vector2(0, 1));
        // Move Down
        control.up = false;
        control.down = true;
        cook.update(control, System.currentTimeMillis(), GameScreen.CLTiles);
        assertEquals(cook.getDirection(), new Vector2(0, -1));
        // Move Left
        control.right = false;
        control.left = true;
        cook.update(control, System.currentTimeMillis(), GameScreen.CLTiles);
        assertEquals(cook.getDirection(), new Vector2(-1, 0));
        // Move Right
        control.right = true;
        control.left = false;
        cook.update(control, System.currentTimeMillis(), GameScreen.CLTiles);
        assertEquals(cook.getDirection(), new Vector2(1, 0));
    }
}
