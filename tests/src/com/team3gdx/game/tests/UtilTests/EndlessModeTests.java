package com.team3gdx.game.tests.UtilTests;


import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import com.team3gdx.game.tests.GdxTestRunner;
import com.team3gdx.game.util.GameMode;
import com.team3gdx.game.util.EndlessMode;
import com.team3gdx.game.screen.*;


@RunWith(GdxTestRunner.class)
public class EndlessModeTests{
    long number = 60000;
    GameMode test = new EndlessMode(5,number);

    @Test
    public void getModeTimeTest(){
        assertEquals(test.getModeTime(),60000);
    }

    @Test
    public void getNumberOfChefsTest(){
        assertEquals(test.getNumberOfChefs(),5);
    }

    @Test
    public void getNumberOfCustmersInAWaveTest1(){
        assertEquals(test.getNumberOfCustmersInAWave(),1);
    }
    @Test
    public void getNumberOfCustmersInAWaveTest2(){
        GameScreen.currentWave = 4;
        assertEquals(test.getNumberOfCustmersInAWave(),2);
    }
    @Test
    public void getNumberOfCustmersInAWaveTest3(){
        GameScreen.currentWave = 6;
        assertEquals(test.getNumberOfCustmersInAWave(),3);
    }

    @Test
    public void TutorialModeTest(){
        assertEquals(test.getNumberOfWaves(),0);
    }

    @Test
    public void showTutorial(){
        assertEquals(test.showTutorial(),false);
    }
}
