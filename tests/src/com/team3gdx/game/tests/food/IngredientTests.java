package com.team3gdx.game.tests.food;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Ingredients;
import com.team3gdx.game.tests.GdxTestRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class IngredientTests {

    // currently fails but does not throw exception
    @Test
    public void testIngredientFlip() {
        Ingredient bun = Ingredients.bun;

        bun.cook(.5f, mock(SpriteBatch.class), mock(ShapeRenderer.class)); // cook for stated ideal time

        bun.flip();

        assertTrue(bun.flipped);
    }
}
