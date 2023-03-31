package com.undercooked.game.food;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.assets.TextureManager;

public class Request {
    public String itemID;
    private int value;

    private int reputationThreat;
    private Texture texture;
    private Array<Instruction> instructions;

    public Request(String itemID) {
        this.itemID = itemID;
        this.instructions = new Array<>();
    }

    public Instruction addInstruction(String texturePath, String text) {
        Instruction newInstruction = new Instruction();

        newInstruction.texturePath = texturePath;
        newInstruction.text = text;
        instructions.add(newInstruction);

        return newInstruction;
    }

    public void setValue(int value) {
        this.value = Math.max(0, value);
    }

    public void setReputationThreat(int value) {
        this.reputationThreat = Math.max(0, value);
    }

    public int getValue() {
        return value;
    }

    public int getReputationThreat() {
        return reputationThreat;
    }

    public Array<Instruction> getInstructions() {
        return instructions;
    }
    public Sprite getSprite(Items items) {
        return items.getItem(itemID).sprite;
    }

    public void postLoad(TextureManager textureManager) {
        for (Instruction instruction : instructions) {
            instruction.postLoad(textureManager);
        }
    }
}
