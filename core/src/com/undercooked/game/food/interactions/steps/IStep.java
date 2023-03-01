package com.undercooked.game.food.interactions.steps;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.audio.AudioSettings;
import com.undercooked.game.food.interactions.InteractionStep;
import com.undercooked.game.station.Station;
import com.undercooked.game.util.Listener;

public class IStep {
    // Holds the variables needed by the StationInteractControl to give to the InteractionSteps.
    // This makes it so only 1 InteractionStep has to exist at one point for each Interaction.

    /** The station to update */
    public Station station;

    /** Listener to tell when the interaction is done */
    public Listener<Boolean> successListener;

    /** The elapsed time for the Interaction */
    public float elapsedTime;
    /** The sound the Interaction plays (looped) */
    public long soundID;
    /** The {@link AudioManager} to use to play the sound */

    private AudioManager audioManager;

    public IStep(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    public void setSuccessListener(Listener listener) {
        this.successListener = listener;
    }

    public void load(TextureManager textureManager, AudioManager audioManager) {

    }

    public void playSound(String soundPath) {
        if (!audioManager.soundIsPlaying(soundID)) {
            soundID = audioManager.getSound(soundPath).play(AudioSettings.getGameVolume());
        }
    }

    public void stopSound(String soundPath) {
        audioManager.getSound(soundPath).stop(soundID);
    }

    public void reset() {
        elapsedTime = 0;
        soundID = -1;
    }
}
