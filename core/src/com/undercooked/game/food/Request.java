package com.undercooked.game.food;

public class Request {
    public String itemID;
    private int value;

    public void setValue(int value) {
        this.value = Math.max(0, value);
    }

    public int getValue() {
        return value;
    }
}
