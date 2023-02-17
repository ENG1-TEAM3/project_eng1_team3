package com.undercooked.game.audio;

import com.undercooked.game.util.Listener;

/**
 *
 * TODO: JavaDocs
 *
 */

public class AudioSettings {

    static float musicVolume;
    static float soundVolume;

    public static final Listener MusicVolListener = new Listener<Float>() {
        @Override
        public void tell(Float value) {
            setMusicVolume(value,"music");
        }
    };
    public static final Listener SoundVolListener = new Listener<Float>() {
        @Override
        public void tell(Float value) {
            setSoundVolume(value);
        }
    };

    public static void setMusicVolume(float volume, String audioGroup) {
        musicVolume = volume;
        Audio.getInstance().setMusicVolume(volume, audioGroup);
    }

    public static void setMusicVolume(float volume) {
        setMusicVolume(volume, "default");
    }

    public static float getMusicVolume() {
        return musicVolume;
    }

    public static void setSoundVolume(float volume, String audioGroup) {
        soundVolume = volume;
        Audio.getInstance().setSoundVolume(volume, audioGroup);
    }

    public static void setSoundVolume(float volume) {
        setSoundVolume(volume, "default");
    }

    public static float getSoundVolume() {
        return soundVolume;
    }

}
