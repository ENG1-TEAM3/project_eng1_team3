package com.team3gdx.game.util;

import com.team3gdx.game.screen.GameScreen;

public class EndlessMode implements GameMode {
    private final int numberOfChefs;
    private final long modeTime;
    public EndlessMode(int numberOfChefs,long modeTime) {

        this.numberOfChefs = numberOfChefs;
        this.modeTime = modeTime;
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

    @Override
    public long getModeTime(){return modeTime;}
    @Override
    public int getNumberOfCustmersInAWave() {
        if (GameScreen.currentWave > 5){
            return 3;
        }
        else if (GameScreen.currentWave > 3){
            return 2;
        }
        else {
            return 1;
        }
    }
}
