package com.team3gdx.game.tests.food;



import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.food.Ingredients;
import com.team3gdx.game.food.Menu;
import com.team3gdx.game.food.Recipe;
import com.team3gdx.game.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class MenuTests {

    @Test
    public void testMappingIngredientsSteps() {
        Menu menu = new Menu();
        Map<Ingredient, String> burgerSteps = menu.BURGER_STEPS;

        // Test keys
        ArrayList<Ingredient> listOfKeys = new ArrayList<Ingredient>(burgerSteps.keySet());
        ArrayList<Ingredient> toCheckK = new ArrayList<Ingredient>(Arrays.asList(Ingredients.cooked_bun, Ingredients.cookedPatty));
        assertTrue(toCheckK.containsAll(listOfKeys) && listOfKeys.containsAll(toCheckK));

        //Test values
        ArrayList<String> listOfValues = new ArrayList<String>(burgerSteps.values());
        ArrayList<String> toCheckV = new ArrayList<String>(Arrays.asList("Toast", "Fry"));
        assertTrue(toCheckV.containsAll(listOfValues) && listOfValues.containsAll(toCheckV));
    }

    @Test
    public void testMappingRecipes() {
        Menu menu = new Menu();
        Map<String, Recipe> recipe = menu.RECIPES;
        // Test keys
        ArrayList<String> listOfKeys = new ArrayList<String>(recipe.keySet());
        String toCheckK = "Burger";
        assertEquals(toCheckK, listOfKeys.get(0));

        // Test Values
        Recipe toCheckV = new Recipe("Form patty", Ingredients.unformedPatty, menu.BURGER_STEPS, "serve together",
                "burger", null, 32, 32, 0);
        ArrayList<Recipe> listOfValues = new ArrayList<Recipe>(recipe.values());
        assertEquals(toCheckV, listOfValues.get(0));
    }

    @Test
    public void testMappingIngredientsTransformations() {
        Menu menu = new Menu();
        Map<Ingredient, Ingredient> ingredientSteps = menu.INGREDIENT_PREP;

        // Test keys
        ArrayList<Ingredient> listOfKeys = new ArrayList<Ingredient>(ingredientSteps.keySet());
        ArrayList<Ingredient> toCheckK = new ArrayList<Ingredient>(Arrays.asList(Ingredients.unformedPatty, Ingredients.unformedDough));
        assertTrue(toCheckK.containsAll(listOfKeys) && listOfKeys.containsAll(toCheckK));

        //Test values
        ArrayList<Ingredient> listOfValues = new ArrayList<Ingredient>(ingredientSteps.values());
        ArrayList<Ingredient> toCheckV = new ArrayList<Ingredient>(Arrays.asList(Ingredients.formedPatty, Ingredients.formedDough));
        assertTrue(toCheckV.containsAll(listOfValues) && listOfValues.containsAll(toCheckV));
    }
}
