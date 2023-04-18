package com.team3gdx.game.tests.food;

import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Ingredients;
import com.team3gdx.game.food.Recipe;
import com.team3gdx.game.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

@RunWith(GdxTestRunner.class)
public class RecipeTests {

    @Test
    public void testContains() {
        // Does not contain
        Map<Ingredient, String> BURGER_STEPS = new HashMap<Ingredient, String>();
        BURGER_STEPS.put(Ingredients.cooked_bun, "Toast");
        BURGER_STEPS.put(Ingredients.cookedPatty, "Fry");
        Recipe recipe = new Recipe("Form patty", Ingredients.unformedPatty, BURGER_STEPS, "serve together",
                "burger", false, null, 32, 32, 0);
        ArrayList<Ingredient> toCheck = new ArrayList<Ingredient>(recipe.ingredientInstructions.keySet());
        Ingredient ingredient = new Ingredient(null, 32, 32, "unformed_patty", 0, .5f);
        recipe.contains(ingredient, toCheck);
        assertFalse(recipe.contains(ingredient, toCheck));

        // Does contain
        Map<Ingredient, String> SALAD_STEPS = new HashMap<Ingredient, String>();
        SALAD_STEPS.put(Ingredients.lettuceChopped, "Cut");
        SALAD_STEPS.put(Ingredients.tomatoChopped, "Cut");
        SALAD_STEPS.put(Ingredients.onionChopped, "Cut");
        Recipe recipe2 = new Recipe("", null, SALAD_STEPS, "serve together", "salad", false, null, 32, 32, 0);
        ArrayList<Ingredient> toCheck2 = new ArrayList<Ingredient>(recipe2.ingredientInstructions.keySet());
        assertTrue(recipe2.contains(Ingredients.lettuceChopped, toCheck2));
    }


    @Test
    public void testMatches() {
        Map<Ingredient, String> SALAD_STEPS = new HashMap<Ingredient, String>();
        SALAD_STEPS.put(Ingredients.tomatoChopped, "Cut");
        SALAD_STEPS.put(Ingredients.onionChopped, "Cut");
        SALAD_STEPS.put(Ingredients.lettuceChopped, "Cut");
        Recipe recipe = new Recipe("", null, SALAD_STEPS, "serve together", "salad", false, null, 32, 32, 0);
        Stack<Ingredient> stack = new Stack<Ingredient>();
        assertFalse(recipe.matches(stack));

        // We have a problem here as the order of the SALAD_STEPS changes every time
        //recipe.shouldBeOrdered = true;
        //stack.push(Ingredients.onionChopped);
        //stack.push(Ingredients.tomatoChopped);
        //stack.push(Ingredients.lettuceChopped);
        //assertTrue(recipe.matches(stack));

        stack.push(Ingredients.onionChopped);
        stack.push(Ingredients.tomatoChopped);
        stack.push(Ingredients.lettuceChopped);
        assertTrue(recipe.matches(stack));

        stack.push(Ingredients.onionChopped);
        stack.push(Ingredients.tomatoChopped);
        stack.push(Ingredients.lettuceChopped);
        stack.push(Ingredients.lettuce);
        assertFalse(recipe.matches(stack));

    }


     //   for (Ingredient ingredient : givenIngredients) {
     //       if (contains(ingredient, toCheck)) {
     //           toCheck.remove(ingredient);
     //       }
     //   }
     //   if (toCheck.isEmpty())
      //      return true;

       // return false;

}
