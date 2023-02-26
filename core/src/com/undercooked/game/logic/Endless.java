package com.undercooked.game.logic;

import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.util.Constants;

public class Endless extends GameLogic {

    public Endless(GameScreen game) {
        super(game);
    }

    @Override
    public void update() {

    }

    @Override
    public void preLoad() {
        // Add all the game ingredients
        ingredients.addIngredient("burger");
        ingredients.addIngredient("burger_bun");
        ingredients.addIngredient("burger_bun_burned");
        ingredients.addIngredient("burger_bun_cooked");
        ingredients.addIngredient("lettuce");
        ingredients.addIngredient("lettuce_chopped");
        ingredients.addIngredient("meat");
        ingredients.addIngredient("meat_overcooked");
        ingredients.addIngredient("meat_undercooked");
        ingredients.addIngredient("onion");
        ingredients.addIngredient("onion_chopped");
        ingredients.addIngredient("patty");
        ingredients.addIngredient("patty_burned");
        ingredients.addIngredient("patty_cooked");
        ingredients.addIngredient("salad");
        ingredients.addIngredient("tomato");
        ingredients.addIngredient("tomato_burned");
        ingredients.addIngredient("tomato_cooked");
        ingredients.addIngredient("unformed_patty");
    }

    @Override
    public void load() {
        // Load all Cook and Customer sprites
        TextureManager textureManager = game.getTextureManager();
        // Cooks
        for (int i = 1; i <= Constants.NUM_COOK_TEXTURES; i++) {
            textureManager.load(Constants.GAME_TEXTURE_ID, "cook_walk_" + i + ".png");
            textureManager.load(Constants.GAME_TEXTURE_ID, "cook_walk_hands_" + i + ".png");
        }

        // Customers
        for (int i = 1 ; i <= Constants.NUM_CUSTOMER_TEXTURES ; i++) {
            textureManager.load(Constants.GAME_TEXTURE_ID, "cust" + i + "f.png");
            textureManager.load(Constants.GAME_TEXTURE_ID, "cust" + i + "b.png");
            textureManager.load(Constants.GAME_TEXTURE_ID, "cust" + i + "r.png");
            textureManager.load(Constants.GAME_TEXTURE_ID, "cust" + i + "l.png");
        }

        // Load all base ingredients
        ingredients.load(textureManager);
    }

    @Override
    public void unload() {
        game.getTextureManager().unload(Constants.GAME_TEXTURE_ID);
    }
}
