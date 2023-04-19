package com.team3gdx.game.util;

public class EndlessMode implements GameMode {
    private final int numberOfChefs;

    public EndlessMode(int numberOfChefs) {

        this.numberOfChefs = numberOfChefs;
    }

    @Override
    public int getNumberOfWaves() {
        return -2;
    }

    @Override
    public int getNumberOfChefs() {
        return numberOfChefs;
    }

    @Override
    public boolean showTutorial() {
        return false;
    }
}
