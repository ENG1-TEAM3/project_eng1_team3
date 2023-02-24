package com.undercooked.game.entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.util.Constants;

public class CookController {

    TextureManager textureManager;

    public CookController(TextureManager textureManager) {
        this.textureManager = textureManager;
    }

    public void load(String textureGroup) {
        textureManager.load(textureGroup, "entities/cook_walk_1.png");
        textureManager.load(textureGroup, "entities/cook_walk_2.png");
    }

    public void unload(String textureGroup) {
        textureManager.load(textureGroup, "entities/cook_walk_1.png");
        textureManager.load(textureGroup, "entities/cook_walk_2.png");
    }

}
