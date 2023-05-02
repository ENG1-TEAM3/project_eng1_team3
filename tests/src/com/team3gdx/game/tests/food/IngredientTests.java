package com.team3gdx.game.tests.food;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.MainGameClass;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Ingredients;
import com.team3gdx.game.screen.GameScreen;
import com.team3gdx.game.tests.GdxTestRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class IngredientTests {
    private Ingredient getIngredient() {
        return new Ingredient(new Vector2(0, 0), 32, 32, "burger_bun", 0, 1);
    }
    private Ingredient getIngredient2() {
        return new Ingredient(new Vector2(0, 0), 32, 32, "onion", 0, 1);
    }
    @Test
    public void testCookDrawsFlipText() {
        Ingredient sut = getIngredient();

        SpriteBatch batch = mock(SpriteBatch.class);
        ShapeRenderer shapeRenderer = mock(ShapeRenderer.class);

        MainGameClass game = new MainGameClass();
        game.batch = batch;
        game.shapeRenderer = shapeRenderer;

        sut.cook(.65f, game);

        verify(batch, atLeastOnce()).begin();
        verify(batch, atLeastOnce()).end();
    }

    @Test
    public void testIngredientFlipsWithCorrectCookingTime() {
        GameScreen.state1 = GameScreen.STATE.Continue;

        Ingredient sut = getIngredient();

        SpriteBatch batch = mock(SpriteBatch.class);
        ShapeRenderer shapeRenderer = mock(ShapeRenderer.class);

        MainGameClass game = new MainGameClass();
        game.batch = batch;
        game.shapeRenderer = shapeRenderer;

        sut.cook(1, game);

        sut.flip();

        assertTrue(sut.flipped);
    }

    @Test
    public void testIngredientDoesNotFlipWithNoCookingTime() {
        GameScreen.state1 = GameScreen.STATE.Continue;

        Ingredient sut = getIngredient();

        sut.flip();

        assertFalse(sut.flipped);
    }

    @Test
    public void testIngredientDoesNotFlipWithNotEnoughCookingTime() {
        GameScreen.state1 = GameScreen.STATE.Continue;

        Ingredient sut = getIngredient();

        SpriteBatch batch = mock(SpriteBatch.class);
        ShapeRenderer shapeRenderer = mock(ShapeRenderer.class);

        MainGameClass game = new MainGameClass();
        game.batch = batch;
        game.shapeRenderer = shapeRenderer;

        sut.cook(0.3f, game);

        sut.flip();

        assertFalse(sut.flipped);
    }

    @Test
    public void testSlice() {
        Ingredient ing = getIngredient2();

        SpriteBatch batch = mock(SpriteBatch.class);
        ShapeRenderer shapeRenderer = mock(ShapeRenderer.class);

        MainGameClass game = new MainGameClass();
        game.batch = batch;
        game.shapeRenderer = shapeRenderer;

        // Turn chopped
        assertTrue(ing.slice(game, 500));
        assertEquals(1, ing.slices);

        // Turned mushy
        assertTrue(ing.slice(game, 500));
        assertEquals(3, ing.slices);
    }
}
