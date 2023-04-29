package com.team3gdx.game.save;

import com.team3gdx.game.util.EndlessMode;
import com.team3gdx.game.util.GameMode;

public class ModeInfo implements GameMode {

    public ModeInfo() {}

    public ModeInfo(GameMode gameMode) {
        numberOfWaves = gameMode.getNumberOfWaves();
        numberOfChefs = gameMode.getNumberOfChefs();
        numberOfCustomersInAWave = gameMode.getNumberOfCustmersInAWave();
        modeTime = gameMode.getModeTime();
        showTutorial = gameMode.showTutorial();

        name = gameMode instanceof EndlessMode ? "Endless" : "Scenario";
    }

    public String name;
    public int numberOfWaves;
    public int numberOfChefs;
    public int numberOfCustomersInAWave;
    public long modeTime;
    public boolean showTutorial;

    @Override
    public int getNumberOfWaves() {
        return numberOfWaves;
    }

    @Override
    public int getNumberOfChefs() {
        return numberOfChefs;
    }

    @Override
    public int getNumberOfCustmersInAWave() {
        return numberOfCustomersInAWave;
    }

    @Override
    public long getModeTime() {
        return modeTime;
    }

    @Override
    public boolean showTutorial() {
        return showTutorial;
    }
}
