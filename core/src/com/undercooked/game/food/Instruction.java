package com.undercooked.game.food;

import com.badlogic.gdx.graphics.Texture;
import com.undercooked.game.assets.TextureManager;

public class Instruction {
    private Texture texture;
    public String texturePath;
    public String text;

    public void postLoad(TextureManager textureManager) {
        texture = textureManager.getAsset(texturePath);
    }

    public void load(TextureManager textureManager, String textureID) {
        textureManager.loadAsset(textureID, texturePath, "textures");
    }

    public Texture getTexture() {
        return texture;
    }
}
