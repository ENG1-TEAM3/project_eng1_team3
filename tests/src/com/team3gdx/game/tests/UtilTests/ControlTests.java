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
        // one key down
        keyDown(Keys.DOWN);
        assertTrue(down);
        assertFalse(up);
        assertFalse(right);
        assertFalse(left);

        // multiple keys down
        keyDown(Keys.UP);
        keyDown(Keys.A);
        assertTrue(down);
        assertTrue(up);
        assertFalse(right);
        assertTrue(left);
    }

    @Test
    public void testKeyUp(){
        // Put keys down first
        testKeyDown();
        // Raise one key
        keyUp(Keys.DOWN);
        assertFalse(down);
        assertTrue(up);
        assertFalse(right);
        assertTrue(left);

        // Raise all keys
        keyUp(Keys.UP);
        keyUp(Keys.A);
        assertFalse(down);
        assertFalse(up);
        assertFalse(right);
        assertFalse(left);
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
