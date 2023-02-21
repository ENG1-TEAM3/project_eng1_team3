package com.team3.gdx.tests;

import com.badlogic.gdx;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class AssetTests {

    @Test
    public void testChefAssetsExist(){
        assertTrue("Cook 1 standard asset exists test", Gdx.files.internal("cook_walk_1.png").exists);
    }
}