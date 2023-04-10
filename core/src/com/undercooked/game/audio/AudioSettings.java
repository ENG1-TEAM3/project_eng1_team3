package com.undercooked.game.audio;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.util.Constants;
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
    public static MainGameClass game;


    public static final Listener MusicVolListener = new Listener<Float>() {
        @Override
        public void tell(Float value) {
            musicVolume = value;
            setMusicVolume(value,Constants.MUSIC_GROUP);
        }
    };

    public static final Listener GameVolListener = new Listener<Float>() {
        @Override
        public void tell(Float value) {
            gameVolume = value;
            setMusicVolume(value,Constants.GAME_SOUND_GROUP);
            setSoundVolume(value, Constants.GAME_SOUND_GROUP);
        }
    };
    public static final Listener SoundVolListener = new Listener<Float>() {
        @Override
        public void tell(Float value) {
            soundVolume = value;
            setSoundVolume(value);
        }
    };

    public static AudioSliders createAudioSliders(float x, float y, Stage stage, Texture backText, Texture buttonTex) {
        AudioSliders audioSliders = new AudioSliders(x,y,300,200, backText);
        audioSliders.setSliderWidth(0.18F);

        Slider musicSlider = audioSliders.addSlider(MusicVolListener, buttonTex);
        musicSlider.addToStage(stage);
        musicSlider.setValue(getMusicVolume());

        Slider gameSlider = audioSliders.addSlider(GameVolListener, buttonTex);
        gameSlider.addToStage(stage);
        gameSlider.setValue(getGameVolume());

        return audioSliders;
    }

    public static void setMusicVolume(float volume, String audioGroup) {
        game.audioManager.setMusicVolume(volume, audioGroup);
    }

    public static void setMusicVolume(float volume) {
        setMusicVolume(volume, "default");
    }

    public static void setSoundVolume(float volume, String audioGroup) {
        game.audioManager.setSoundVolume(volume, audioGroup);
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
