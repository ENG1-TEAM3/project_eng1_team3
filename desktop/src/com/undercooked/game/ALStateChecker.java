package com.undercooked.game;

import com.undercooked.game.audio.SoundStateChecker;
import org.lwjgl.openal.AL10;

/**
 * A class used to check the ALState within the {@link AL10} class,
 * that can only be accessed in the desktop launcher folder.
 */
public class ALStateChecker implements SoundStateChecker {
    /**
     * Returns whether a sound id is playing by its id, or not.
     * @param soundID {@code int} : The id of the {@link com.badlogic.gdx.audio.Sound}.
     * @return {@code boolean} : {@code true} if the {@link com.badlogic.gdx.audio.Sound} is playing,
     *                           {@code false} if it is not.
     */
    @Override
    public boolean isPlaying(int soundID) {
        // System.out.println(AL10.alGetSourcei(soundID, AL10.AL_SOURCE_STATE));
        return AL10.alGetSourcei(soundID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }
}
