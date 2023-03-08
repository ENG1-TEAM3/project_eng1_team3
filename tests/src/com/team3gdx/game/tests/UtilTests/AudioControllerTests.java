package com.team3gdx.game.tests.UtilTests;

import com.badlogic.gdx.Gdx;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import com.team3gdx.game.tests.GdxTestRunner;
import com.team3gdx.game.util.AudioController;

@RunWith(GdxTestRunner.class)
public class AudioControllerTests {

    @Test
    public void testmusicExists() {
        assertTrue(Gdx.files.internal("./bin/main/audio/music").exists());
    }
    @Test
    public void testsoudFXExists() {
        assertTrue(Gdx.files.internal("./bin/main/audio/soundFX").exists());
    }
    @Test
    public void testAddMusic() {
        
    }
}