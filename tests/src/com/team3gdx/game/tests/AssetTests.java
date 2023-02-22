package com.team3gdx.game.tests;

import com.badlogic.gdx.Gdx;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class AssetTests {

    @Test
    public void testBurgerAssetExists() {
        assertTrue(Gdx.files.internal("items/burger.png").exists());
    }

    @Test
    public void testTests() {
        assertTrue(true);
    }
}
