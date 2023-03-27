package com.undercooked.game.interactions;

import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.audio.AudioSettings;
import com.undercooked.game.food.Items;
import com.undercooked.game.station.Station;
import com.undercooked.game.util.Listener;

public class IStep {
    // Holds the variables needed by the StationInteractControl to give to the InteractionSteps.
    // This makes it so only 1 InteractionStep has to exist at one point for each Interaction.

    /** The station to update */
    public final Station station;
    public final StationInteractControl interactControl;

    /** The items within the game. Useful for Interactions. */
    public Items gameItems;

    /** Listener to tell when the interaction is done */
    public Listener<Boolean> successListener;
    /** Listener for when the interaction should just stop completely */
    public Listener<Boolean> endListener;

    /** The elapsed time for the Interaction */
    public float elapsedTime;
    /** The sound the Interaction plays (looped) */
    public long soundID;
    /** The {@link AudioManager} to use to play the sound */

    private AudioManager audioManager;

    public IStep(Station station, StationInteractControl interactControl, AudioManager audioManager, Items gameItems) {
        this.station = station;
        this.gameItems = gameItems;
        this.interactControl = interactControl;
        this.audioManager = audioManager;
        this.elapsedTime = 0;
    }

    public void setSuccessListener(Listener listener) {
        this.successListener = listener;
    }

    public void load(TextureManager textureManager, AudioManager audioManager) {

    }

    public void updateInteractions() {
        // Then update the station
        station.updateStationInteractions();
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
