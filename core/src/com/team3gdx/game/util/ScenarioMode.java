package com.team3gdx.game.util;

public class ScenarioMode implements GameMode {
    private final int numberOfWaves;
    private final int numberOfChefs;

    public ScenarioMode(int numberOfWaves, int numberOfChefs) {

        this.numberOfWaves = numberOfWaves;
        this.numberOfChefs = numberOfChefs;
    }

    @Override
    public int getNumberOfWaves() {
        return numberOfWaves;
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
