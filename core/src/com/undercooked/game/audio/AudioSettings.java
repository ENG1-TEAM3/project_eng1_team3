package com.undercooked.game.audio;

import com.undercooked.game.util.Listener;

/**
 *
 * TODO: JavaDocs
 *
 */

public class AudioSettings {

    static float musicVolume;
    static float gameVolume;
    static float soundVolume;


    public static AudioSliders createAudioSliders(float x, float y, Stage stage) {
        AudioSliders audioSliders = new AudioSliders(x,y,300,200, Textures.getInstance().loadTexture("uielements/background.jpg", "background"));
        audioSliders.setSliderWidth(0.1F);

        Slider musicSlider = audioSliders.addSlider(MusicVolListener);
        musicSlider.addToStage(stage);
        musicSlider.setValue(getMusicVolume());
        audioSliders.setSliderWidth(0.1F);

        Slider gameSlider = audioSliders.addSlider(GameVolListener);
        gameSlider.addToStage(stage);
        gameSlider.setValue(getGameVolume());

        return audioSliders;
    }


    public static final Listener MusicVolListener = new Listener<Float>() {
        @Override
        public void tell(Float value) {
            musicVolume = value;
            setMusicVolume(value,"music");
        }
    };

    public static final Listener GameVolListener = new Listener<Float>() {
        @Override
        public void tell(Float value) {
            gameVolume = value;
            setMusicVolume(value,"game");
            setSoundVolume(value, "game");
        }
    };
    public static final Listener SoundVolListener = new Listener<Float>() {
        @Override
        public void tell(Float value) {
            soundVolume = value;
            setSoundVolume(value);
        }
    };

    public static void setMusicVolume(float volume, String audioGroup) {
        Audio.getInstance().setMusicVolume(volume, audioGroup);
    }

    public static void setMusicVolume(float volume) {
        setMusicVolume(volume, "default");
    }

    public static void setSoundVolume(float volume, String audioGroup) {
        Audio.getInstance().setSoundVolume(volume, audioGroup);
    }

    public static void setSoundVolume(float volume) {
        setSoundVolume(volume, "default");
    }

    public static float getSoundVolume() {
        return soundVolume;
    }

    public static float getMusicVolume() {
        return musicVolume;
    }
    public static float getGameVolume() {
        return gameVolume;
    }

}
