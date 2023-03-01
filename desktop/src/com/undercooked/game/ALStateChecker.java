package com.undercooked.game;

import com.undercooked.game.audio.SoundStateChecker;
import org.lwjgl.openal.AL10;

public class ALStateChecker implements SoundStateChecker {
    @Override
    public boolean isPlaying(int soundID) {
        return AL10.alGetSourcei(soundID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }
}
