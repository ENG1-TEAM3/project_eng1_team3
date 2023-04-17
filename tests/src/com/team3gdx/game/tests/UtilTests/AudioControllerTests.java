package com.team3gdx.game.tests.UtilTests;

import com.badlogic.gdx.Gdx;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import static org.junit.Assert.*;

import com.team3gdx.game.tests.GdxTestRunner;
import com.team3gdx.game.util.AudioController;

@RunWith(GdxTestRunner.class)
public class AudioControllerTests extends AudioController {

    @Test
    public void testmusicExists() {
        assertTrue(Gdx.files.internal("./bin/main/audio/music").exists());
    }
    @Test
    public void testsoundFXExists() {
        assertTrue(Gdx.files.internal("./bin/main/audio/soundFX").exists());
    }
    @Test
    public void testAddMusic() {
        
        addMusic("testMusic.mp3");
        assertTrue(Gdx.files.internal("./bin/main/audio/music/testMusic.mp3").exists());

    }
    @Test
    public void testAddSoundfx() {
        
        addSoundFX("testSoundFX.mp3");
        assertTrue(Gdx.files.internal("./bin/main/audio/soundFX/testSoundFX.mp3").exists());
    }
    //@Test
    //public void testVolume() {
    //    setVolume("testMusic.mp3",6);
    //    float testVolume = getVolume("testMusic.mp3");
    //    assertEquals(testVolume, 6);
    //}

}