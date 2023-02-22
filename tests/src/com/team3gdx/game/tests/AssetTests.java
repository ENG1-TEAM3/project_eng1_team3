package com.team3gdx.game.tests;

import com.badlogic.gdx.Gdx;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class AssetTests {

    @Test
    public void testBurgerAssetExists() {
        // Check burger exists
        assertTrue(Gdx.files.internal("items/burger.png").exists());
    }

    @Test
    public void testChefAssetExists() {
        // Check chef walking
        assertTrue(Gdx.files.internal("entities/cook_walk_1.png").exists());
        assertTrue(Gdx.files.internal("entities/cook_walk_2.png").exists());
        assertTrue(Gdx.files.internal("entities/cook_walk_3.png").exists());
        // Check chef with hands in the air
        assertTrue(Gdx.files.internal("entities/cook_walk_1.png").exists());
        assertTrue(Gdx.files.internal("entities/cook_walk_2.png").exists());
        assertTrue(Gdx.files.internal("entities/cook_walk_3.png").exists());
    }

    //Check whether customer 1 assets exists
    @Test
    public void testCust1AssetExists() {
        assertTrue(Gdx.files.internal("entities/cust1b.png").exists());
        assertTrue(Gdx.files.internal("entities/cust1f.png").exists());
        assertTrue(Gdx.files.internal("entities/cust1l.png").exists());
        assertTrue(Gdx.files.internal("entities/cust1r.png").exists());
    }
    //Check whether customer 2 assets exists
    @Test
    public void testCust2AssetExists() {
        assertTrue(Gdx.files.internal("entities/cust2b.png").exists());
        assertTrue(Gdx.files.internal("entities/cust2f.png").exists());
        assertTrue(Gdx.files.internal("entities/cust2l.png").exists());
        assertTrue(Gdx.files.internal("entities/cust2r.png").exists());
    }
    //Check whether customer 3 assets exists
    @Test
    public void testCust3AssetExists() {
        assertTrue(Gdx.files.internal("entities/cust3b.png").exists());
        assertTrue(Gdx.files.internal("entities/cust3f.png").exists());
        assertTrue(Gdx.files.internal("entities/cust3l.png").exists());
        assertTrue(Gdx.files.internal("entities/cust3r.png").exists());
    }
    //Check whether customer 4 assets exists
    @Test
    public void testCust4AssetExists() {
        assertTrue(Gdx.files.internal("entities/cust4b.png").exists());
        assertTrue(Gdx.files.internal("entities/cust4f.png").exists());
        assertTrue(Gdx.files.internal("entities/cust4l.png").exists());
        assertTrue(Gdx.files.internal("entities/cust4r.png").exists());
    }

    //Check whether customer 5 assets exists
    @Test
    public void testCust5AssetExists() {
        assertTrue(Gdx.files.internal("entities/cust5b.png").exists());
        assertTrue(Gdx.files.internal("entities/cust5f.png").exists());
        assertTrue(Gdx.files.internal("entities/cust5l.png").exists());
        assertTrue(Gdx.files.internal("entities/cust5r.png").exists());
    }
}
