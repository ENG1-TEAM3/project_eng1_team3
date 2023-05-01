package com.team3gdx.game.tests.UtilTests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import com.team3gdx.game.tests.GdxTestRunner;
import com.team3gdx.game.util.TutorialMode;
import com.team3gdx.game.util.GameMode;
import com.team3gdx.game.util.ScenarioMode;


@RunWith(GdxTestRunner.class)
public class ScenarioModeTests{
    long number = 60000;
    GameMode test = new ScenarioMode(5,5,5,number);

    @Test
    public void getModeTimeTest(){
        assertEquals(test.getModeTime(),60000);
    }

    @Test
    public void getNumberOfChefsTest(){
        assertEquals(test.getNumberOfChefs(),5);
    }

    @Test
    public void getNumberOfCustmersInAWaveTest(){
        assertEquals(test.getNumberOfCustmersInAWave(),5);
    }

    @Test
    public void TutorialModeTest(){
        assertEquals(test.getNumberOfWaves(),5);
    }

    @Test
    public void showTutorial(){
        assertEquals(test.showTutorial(),false);
    }
}

