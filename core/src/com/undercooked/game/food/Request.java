package com.undercooked.game.food;

public class Request {
    public String itemID;
    private int value;

    private int reputationThreat;

    public Request(String itemID) {
        this.itemID = itemID;
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
}
