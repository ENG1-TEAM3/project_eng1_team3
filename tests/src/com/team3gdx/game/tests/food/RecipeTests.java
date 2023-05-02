package com.team3gdx.game.tests.food;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.MainGameClass;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Ingredients;
import com.team3gdx.game.food.Recipe;
import com.team3gdx.game.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

@RunWith(GdxTestRunner.class)
public class RecipeTests {

    @Test
    public void testCost() {
        // Create Recipe
        Map<Ingredient, String> BURGER_STEPS = new HashMap<Ingredient, String>();
        BURGER_STEPS.put(Ingredients.cooked_bun, "Toast");
        BURGER_STEPS.put(Ingredients.cookedPatty, "Fry");
        Recipe recipe = new Recipe("Form patty", Ingredients.unformedPatty, BURGER_STEPS, "serve together",
                "burger",  null, 32, 32, 20);
        // Is cost recorded right
        assertEquals(20, recipe.cost(), 0.001);
    }
    @Test
    public void testContains() {
        // Create Recipe
        Map<Ingredient, String> BURGER_STEPS = new HashMap<Ingredient, String>();
        BURGER_STEPS.put(Ingredients.cooked_bun, "Toast");
        BURGER_STEPS.put(Ingredients.cookedPatty, "Fry");
        Recipe recipe = new Recipe("Form patty", Ingredients.unformedPatty, BURGER_STEPS, "serve together",
                "burger",  null, 32, 32, 0);
        // Does not contain
        ArrayList<Ingredient> toCheck = new ArrayList<Ingredient>(recipe.ingredientInstructions.keySet());
        Ingredient ingredient = new Ingredient(null, 32, 32, "unformed_patty", 0, .5f);
        recipe.contains(ingredient, toCheck);
        assertFalse(recipe.contains(ingredient, toCheck));
        // Does contain
        Map<Ingredient, String> SALAD_STEPS = new HashMap<Ingredient, String>();
        SALAD_STEPS.put(Ingredients.lettuceChopped, "Cut");
        SALAD_STEPS.put(Ingredients.tomatoChopped, "Cut");
        SALAD_STEPS.put(Ingredients.onionChopped, "Cut");
        Recipe recipe2 = new Recipe("", null, SALAD_STEPS, "serve together", "salad",  null, 32, 32, 0);
        ArrayList<Ingredient> toCheck2 = new ArrayList<Ingredient>(recipe2.ingredientInstructions.keySet());
        Ingredient ingredient2 = Ingredients.lettuceChopped;
        // Not usable
        assertFalse(recipe2.contains(ingredient2, toCheck2));
        // Usable
        ingredient2.idealSlices = 2;
        ingredient2.slices = 1;
        assertTrue(recipe2.contains(ingredient2, toCheck2));
    }

    @Test
    public void testMatches() {
        // Different sizes
        Map<Ingredient, String> SALAD_STEPS = new HashMap<Ingredient, String>();
        SALAD_STEPS.put(Ingredients.tomatoChopped, "Cut");
        SALAD_STEPS.put(Ingredients.onionChopped, "Cut");
        SALAD_STEPS.put(Ingredients.lettuceChopped, "Cut");
        Recipe recipe = new Recipe("", null, SALAD_STEPS, "serve together", "salad", null, 32, 32, 0);
        Stack<Ingredient> stack = new Stack<Ingredient>();
        assertFalse(recipe.matches(stack));

        //Ingredients not usable
        Ingredient ing1 = Ingredients.onionChopped;
        Ingredient ing2 = Ingredients.tomatoChopped;
        Ingredient ing3 = Ingredients.lettuceChopped;
        stack.push(ing1);
        stack.push(ing2);
        stack.push(ing3);
        assertFalse(recipe.matches(stack));

        //Ingredients are usable

        ing1.slices = 1;
        ing1.idealSlices = 2;
        ing2.slices = 1;
        ing2.idealSlices = 2;
        ing3.slices = 1;
        ing3.idealSlices = 2;
        assertTrue(recipe.matches(stack));

        stack.push(Ingredients.lettuce);
        assertFalse(recipe.matches(stack));

    }
}
