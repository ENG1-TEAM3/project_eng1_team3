package com.team3.gdx.tests;

import com.badlogic.gdx.Gdx;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class AssetTests {

    @Test
    public void testChefAssetsExist(){
        assertTrue("Cook 1 standard asset exists test", Gdx.files.internal("entities/cook_walk_1.png").exists());
        assertTrue("Cook 2 standard asset exists test", Gdx.files.internal("entities/cook_walk_2.png").exists());
        assertTrue("Cook 3 standard asset exists test", Gdx.files.internal("entities/cook_walk_3.png").exists());
        assertTrue("Cook 1 hand asset exists test", Gdx.files.internal("entities/cook_walk_hands_1.png").exists());
        assertTrue("Cook 2 hand asset exists test", Gdx.files.internal("entities/cook_walk_hands_1.png").exists());
        assertTrue("Cook 3 hand asset exists test", Gdx.files.internal("entities/cook_walk_hands_1.png").exists());
    }
    @Test
    public void testCustAssetsExist(){
        //customer 1
        assertTrue("Customer 1 front", Gdx.files.internal("entities/cust1f.png").exists());
        assertTrue("Customer 1 back", Gdx.files.internal("entities/cust1b.png").exists());
        assertTrue("Customer 1 left", Gdx.files.internal("entities/cust1l.png").exists());
        assertTrue("Customer 2 right", Gdx.files.internal("entities/cust2r.png").exists());
        //customer 2
        assertTrue("Customer 2 front", Gdx.files.internal("entities/cust2f.png").exists());
        assertTrue("Customer 2 back", Gdx.files.internal("entities/cust2b.png").exists());
        assertTrue("Customer 2 left", Gdx.files.internal("entities/cust2l.png").exists());
        assertTrue("Customer 2 right", Gdx.files.internal("entities/cust2r.png").exists());
        //customer 3
        assertTrue("Customer 3 front", Gdx.files.internal("entities/cust3f.png").exists());
        assertTrue("Customer 3 back", Gdx.files.internal("entities/cust3b.png").exists());
        assertTrue("Customer 3 left", Gdx.files.internal("entities/cust3l.png").exists());
        assertTrue("Customer 3 right", Gdx.files.internal("entities/cust3r.png").exists());
        //customer 3
        assertTrue("Customer 4 front", Gdx.files.internal("entities/cust4f.png").exists());
        assertTrue("Customer 4 back", Gdx.files.internal("entities/cust4b.png").exists());
        assertTrue("Customer 4 left", Gdx.files.internal("entities/cust4l.png").exists());
        assertTrue("Customer 4 right", Gdx.files.internal("entities/cust4r.png").exists());
        //customer 3
        assertTrue("Customer 5 front", Gdx.files.internal("entities/cust5f.png").exists());
        assertTrue("Customer 5 back", Gdx.files.internal("entities/cust5b.png").exists());
        assertTrue("Customer 5 left", Gdx.files.internal("entities/cust5l.png").exists());
        assertTrue("Customer 5 right", Gdx.files.internal("entities/cust5r.png").exists());




    }

}