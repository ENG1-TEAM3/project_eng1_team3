package com.undercooked.game.food;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonValue;
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

    public JsonValue serial() {
        JsonValue instructionRoot = new JsonValue(JsonValue.ValueType.object);
        instructionRoot.addChild("texturePath", new JsonValue(texturePath));
        instructionRoot.addChild("text", new JsonValue(text));
        return instructionRoot;
    }
}
