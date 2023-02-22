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

}