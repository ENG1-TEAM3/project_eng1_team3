package com.team3gdx.game.tests.UtilTests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import com.team3gdx.game.tests.GdxTestRunner;
import com.team3gdx.game.util.TutorialMode;
import com.team3gdx.game.util.GameMode;

@RunWith(GdxTestRunner.class)
public class TutorialModeTests {
    public TutorialMode test;

    @Test
    public void getModeTimeTest(){
        assertEquals(test.getModeTime(),60000);
    }

    @Test
    public void getNumberOfChefsTest(){
        assertEquals(test.getNumberOfChefs(),2);
    }

    @Test
    public void getNumberOfCustmersInAWaveTest(){
        assertEquals(test.getNumberOfCustmersInAWave(),1);
    }

    @Test
    public void TutorialModeTest(){
        assertEquals(test.getNumberOfWaves(),1);
    }

    @Test
    public void showTutorial(){
        assertEquals(test.showTutorial(),true);
    }
}
