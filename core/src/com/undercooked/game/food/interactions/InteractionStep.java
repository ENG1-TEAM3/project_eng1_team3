package com.undercooked.game.food.interactions;

import com.badlogic.gdx.utils.Array;
import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.Cook;
import com.undercooked.game.food.interactions.steps.IStep;
import com.undercooked.game.station.Station;

public abstract class InteractionStep {

    /** The list of steps to take on success */
    protected Array<InteractionStep> success;
    /** The list of steps to take on failure */
    protected Array<InteractionStep> failure;
    /** Items required for the Interaction */
    protected Array<String> items;
    /** The time required for the Interaction */
    protected String sound;
    /** The ID of the sound as it's playing */
    protected float time;
    /** The value of the Interaction */
    protected String value;

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

    /**
     * Function to update the station.
     * @param instance
     * @param delta
     */
    public abstract void update(IStep instance, float delta);

    /** Function to override that is called when the task is done. */
    public void updateCookTarget(Cook cook) { }

    /**
     * Interaction betweek a {@link Cook} and the {@link InteractionStep}.
     * @param cook {@link Cook} : The Cook that interacted.
     * @param keyID {@link String} : The ID of the Key used.
     * @param inputType {@link InputType} : The type of input used.
     * @return {@link InteractResult} : The result of the {@link InteractionStep}.
     */
    public InteractResult interact(IStep instance, Cook cook, String keyID, InputType inputType) {
        return InteractResult.NONE;
    }

}
