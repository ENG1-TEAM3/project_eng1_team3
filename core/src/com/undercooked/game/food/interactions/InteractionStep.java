package com.undercooked.game.food.interactions;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.audio.AudioSettings;
import com.undercooked.game.food.interactions.steps.IStep;
import com.undercooked.game.util.Listener;
import com.undercooked.game.util.ListenerController;
import com.undercooked.game.station.Station;

public abstract class InteractionStep implements IInteractionStep {

    /** The list of steps to take on success */
    public Array<InteractionStep> success;
    /** The list of steps to take on failure */
    public Array<InteractionStep> failure;
    /** Items required for the Interaction */
    public Array<String> items;
    /** The time required for the Interaction */
    public String sound;
    /** The ID of the sound as it's playing */
    public float time;

    public void updateTime(IStep instance, float delta) {
        instance.elapsedTime += delta;
    }

    public void playSound(IStep instance) {
        instance.playSound(sound);
    }

    /**
     * Tell listeners that we're finished.
     */
    public void finished(IStep instance, boolean success) {
        // Stop playing the sound
        instance.stopSound(sound);
        // Tell the listener it's done, so it can move on to the next Interaction
        instance.successListener.tell(success);
    }

    /** Function to override that is called when the task is done. */
    public void updateStation(Station station) { }

}
