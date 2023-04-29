package com.team3gdx.game.util;

public interface GameMode {

    int getNumberOfWaves();

    int getNumberOfChefs();

    int getNumberOfCustmersInAWave();

    long getModeTime();

    boolean showTutorial();
}
