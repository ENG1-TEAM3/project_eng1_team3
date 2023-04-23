package com.team3gdx.game.util;

public class ScenarioMode implements GameMode {
    private final int numberOfWaves;
    private final int numberOfChefs;
    private final int getNumberOfCustmersInAWave;

    public ScenarioMode(int numberOfWaves, int numberOfChefs,int getNumberOfCustmersInAWave) {

        this.numberOfWaves = numberOfWaves;
        this.numberOfChefs = numberOfChefs;
        this.getNumberOfCustmersInAWave = getNumberOfCustmersInAWave;
    }

    @Override
    public int getNumberOfWaves() {
        return numberOfWaves;
    }

    @Override
    public int getNumberOfCustmersInAWave() {
        return getNumberOfCustmersInAWave;
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
