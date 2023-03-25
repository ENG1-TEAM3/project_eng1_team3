package com.undercooked.game.logic;

import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.util.Constants;

public class Endless extends GameLogic {

    public Endless(GameScreen game, TextureManager textureManager) {
        super(game, textureManager);
    }

    public Endless() {
        this(null, null);
    }


    @Override
    public void update(float delta) {

    }

    @Override
    public void preLoad() {
        // Add all the game ingredients
        items.addIngredient("burger");
        items.addIngredient("burger_bun");
        items.addIngredient("burger_bun_burned");
        items.addIngredient("burger_bun_cooked");
        items.addIngredient("lettuce");
        items.addIngredient("lettuce_chopped");
        items.addIngredient("meat");
        items.addIngredient("meat_overcooked");
        items.addIngredient("meat_undercooked");
        items.addIngredient("onion");
        items.addIngredient("onion_chopped");
        items.addIngredient("patty");
        items.addIngredient("patty_burned");
        items.addIngredient("patty_cooked");
        items.addIngredient("salad");
        items.addIngredient("tomato");
        items.addIngredient("tomato_burned");
        items.addIngredient("tomato_cooked");
        items.addIngredient("unformed_patty");
    }

    @Override
    public void load() {
        // Load all Cook and Customer sprites
        TextureManager textureManager = gameScreen.getTextureManager();
        // Cooks
        for (int i = 1; i <= Constants.NUM_COOK_TEXTURES; i++) {
            textureManager.load(Constants.GAME_TEXTURE_ID, "cook_walk_" + i + ".png");
            textureManager.load(Constants.GAME_TEXTURE_ID, "cook_walk_hands_" + i + ".png");
        }

        // Customers
        for (int i = 1; i <= Constants.NUM_CUSTOMER_TEXTURES; i++) {
            textureManager.load(Constants.GAME_TEXTURE_ID, "cust" + i + "f.png");
            textureManager.load(Constants.GAME_TEXTURE_ID, "cust" + i + "b.png");
            textureManager.load(Constants.GAME_TEXTURE_ID, "cust" + i + "r.png");
            textureManager.load(Constants.GAME_TEXTURE_ID, "cust" + i + "l.png");
        }

        // Load all base ingredients
        items.load(textureManager);
    }

    @Override
    public void unload() {
        gameScreen.getTextureManager().unload(Constants.GAME_TEXTURE_ID);
    }
}
