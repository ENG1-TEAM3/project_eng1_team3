package com.undercooked.game.entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.undercooked.game.MainGameClass;

public class CookController {

    public void load() {
        AssetManager assetManager = MainGameClass.assetManager;
        assetManager.load("entities/cook_walk_1.png", Texture.class);
        assetManager.load("entities/cook_walk_2.png", Texture.class);
    }

    public void unload() {
        AssetManager assetManager = MainGameClass.assetManager;
        assetManager.unload("entities/cook_walk_1.png");
        assetManager.unload("entities/cook_walk_2.png");
    }

}
