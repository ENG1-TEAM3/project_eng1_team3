package com.undercooked.game.audio;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.files.SettingsControl;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.Listener;

/**
 *
 * TODO: JavaDocs
 *
 */

public class AudioSettings {

    float musicVolume;
    float gameVolume;
    float soundVolume;
    MainGameClass game;
    SettingsControl settingsControl;

    public AudioSettings(MainGameClass game, SettingsControl settingsControl) {
        this.game = game;
        this.settingsControl = settingsControl;
    }

    public final Listener musicVolListener = new Listener<Float>() {
        @Override
        public void tell(Float value) {
            setMusicVol(value);
        }
    };

    public final Listener gameVolListener = new Listener<Float>() {
        @Override
        public void tell(Float value) {
            setGameVol(value);
        }
    };
    public final Listener saveListener = new Listener<Float>() {
        @Override
        public void tell(Float change) {
            // Only save if there was a change
            if (change == 0) return;
            saveVolumes();
        }
    };

    public AudioSliders createAudioSliders(float x, float y, Stage stage, Texture backText, Texture buttonTex) {
        AudioSliders audioSliders = new AudioSliders(x,y,300,200, backText);
        audioSliders.setSliderWidth(0.18F);

        Slider musicSlider = audioSliders.addSlider(musicVolListener, buttonTex);
        musicSlider.addToStage(stage);
        musicSlider.setValue(getMusicVolume());
        musicSlider.addReleaseListener(saveListener);

        Slider gameSlider = audioSliders.addSlider(gameVolListener, buttonTex);
        gameSlider.addToStage(stage);
        gameSlider.setValue(getGameVolume());
        gameSlider.addReleaseListener(saveListener);

        return audioSliders;
    }

    public void setMusicVolume(float volume, String audioGroup) {
        game.audioManager.setMusicVolume(volume, audioGroup);
    }

    public void setMusicVolume(float volume) {
        setMusicVolume(volume, "default");
    }

    public void setMusicVol(float volume) {
        musicVolume = volume;
        setMusicVolume(volume, Constants.MUSIC_GROUP);
    }

    public void setGameVol(float volume) {
        gameVolume = volume;
        setMusicVolume(volume, Constants.GAME_GROUP);
        setSoundVolume(volume, Constants.GAME_GROUP);
    }

    public void saveVolumes() {
        if (settingsControl == null) return;
        settingsControl.setMusicVolume(musicVolume);
        settingsControl.setGameVolume(gameVolume);
        settingsControl.saveData();
    }

    public void loadVolumes() {
        if (settingsControl == null) return;
        settingsControl.loadIfNotLoaded();
        musicVolume = settingsControl.getMusicVolume();
        gameVolume = settingsControl.getGameVolume();
    }

    public void setSoundVolume(float volume, String audioGroup) {
        game.audioManager.setSoundVolume(volume, audioGroup);
    }

    public void setSoundVolume(float volume) {
        setSoundVolume(volume, "default");
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public float getMusicVolume() {
        return musicVolume;
    }
    public float getGameVolume() {
        return gameVolume;
    }

}
