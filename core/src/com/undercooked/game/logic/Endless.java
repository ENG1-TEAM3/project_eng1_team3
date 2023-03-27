package com.undercooked.game.logic;

import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.screen.GameScreen;
import com.undercooked.game.util.Constants;

public class Endless extends ScenarioLogic {

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
        items.addItem("burger");
        items.addItem("burger_bun");
        items.addItem("burger_bun_burned");
        items.addItem("burger_bun_cooked");
        items.addItem("lettuce");
        items.addItem("lettuce_chopped");
        items.addItem("meat");
        items.addItem("meat_overcooked");
        items.addItem("meat_undercooked");
        items.addItem("onion");
        items.addItem("onion_chopped");
        items.addItem("patty");
        items.addItem("patty_burned");
        items.addItem("patty_cooked");
        items.addItem("salad");
        items.addItem("tomato");
        items.addItem("tomato_burned");
        items.addItem("tomato_cooked");
        items.addItem("unformed_patty");
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
