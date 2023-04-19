package com.undercooked.game.food;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.assets.TextureManager;

public class Request {
    public String itemID;
    private int value;
    private float time;

    private int reputationThreat;
    private Texture texture;
    private Array<Instruction> instructions;

    public Request(String itemID) {
        this.itemID = itemID;
        this.instructions = new Array<>();
        this.time = -1;
    }

    public void postLoad(TextureManager textureManager) {
        for (Instruction instruction : instructions) {
            instruction.postLoad(textureManager);
        }
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

    public void setTime(float time) {
        this.time = time;
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

    public float getTime() {
        return this.time;
    }

    public JsonValue serial() {
        JsonValue requestRoot = new JsonValue(JsonValue.ValueType.object);
        requestRoot.addChild("itemID", new JsonValue(itemID));
        requestRoot.addChild("value", new JsonValue(value));
        requestRoot.addChild("time", new JsonValue(time));

        // * Caveat: As instructions is an Array<Instruction>, I cannot create a serial
        // function for it.
        // * meaning, its serialization will be done here.
        JsonValue instructionsRoot = new JsonValue(JsonValue.ValueType.array);
        for (Instruction instruction : instructions) {
            instructionsRoot.addChild(instruction.serial());
        }
        requestRoot.addChild("instructions", instructionsRoot);
        return requestRoot;
    }
}
