package com.team3gdx.game.tests.UtilTests;

import com.badlogic.gdx.Gdx;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import com.badlogic.gdx.Input.Keys;


import com.team3gdx.game.tests.GdxTestRunner;
import com.team3gdx.game.util.Control;

@RunWith(GdxTestRunner.class)
public class ControlTests extends Control {

    @Test
    public void testKeyDown(){
        keyDown(Keys.DOWN);
        assertTrue(down);
    }

    @Test
    public void testKeyUp(){
        keyUp(Keys.DOWN);
        assertFalse(down);
    }
    @Test
    public void testKeyTyped(){
        assertFalse(keyTyped('f'));
    }

    @Test
    public void testTouchDown(){
        assertFalse(touchDown(5,5,5,5));
    }

    @Test
    public void testTouchUp(){
        assertFalse(touchUp(5,5,5,5));
    }

    @Test
    public void testTouchDragged(){
        assertFalse(touchDragged(5,5,5));
    }

    @Test
    public void testMouseMoved(){
        assertFalse(mouseMoved(5,5));
    }

    @Test
    public void testScrollled(){
        assertFalse(scrolled(0));
    }
}
