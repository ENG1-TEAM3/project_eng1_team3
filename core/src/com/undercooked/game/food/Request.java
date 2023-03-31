package com.undercooked.game.food;

import com.badlogic.gdx.utils.Array;

public class Request {
    public String itemID;
    private int value;

    private int reputationThreat;
    private Array<Instruction> instructions;

    public Request(String itemID) {
        this.itemID = itemID;
        this.instructions = new Array<>();
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

    public void addInstruction() {

    }
}
