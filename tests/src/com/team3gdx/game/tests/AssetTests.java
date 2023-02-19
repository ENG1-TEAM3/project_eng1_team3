package com.team3gdx.game.tests;

import com.badlogic.gdx.Gdx;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class AssetTests {

    @Test
    public void testBurgerAssetExists() {
        Assert.assertTrue(Gdx.files.internal("items/burger.png").exists());
    }

    @Test
    public void testTests() {
        Assert.assertTrue(true);
    }
}
