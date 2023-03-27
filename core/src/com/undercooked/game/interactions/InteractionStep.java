package com.undercooked.game.interactions;

import com.badlogic.gdx.utils.Array;
import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.Cook;

public abstract class InteractionStep {

    /** The list of steps to take on success */
    protected Array<InteractionStep> success;
    /** The list of steps to take on failure */
    protected Array<InteractionStep> failure;
    /** The time required for the Interaction */
    protected String sound;
    /** The ID of the sound as it's playing */
    protected float time;
    /** The value of the Interaction */
    protected String value;
    /** The last {@link Cook} to interact. */
    protected Cook lastInteractedCook;

    /** Update the time value of the instance. */
    public final void updateTime(IStep instance, float delta) {
        instance.elapsedTime += delta;
    }

    /** Play the sound of the instance. */
    public final void playSound(IStep instance) {
        instance.playSound(sound);
    }

    /**
     * Tell listeners that we're finished.
     */
    public final InteractResult finished(IStep instance, Cook cook, String keyID, InputType inputType, boolean success) {
        // Stop playing the sound
        instance.stopSound(sound);
        // Tell the Interact Controller it's done, so it can move on to the next Interaction
        return instance.interactControl.finished(cook, keyID, inputType, success);
    }

    public final InteractResult finished(IStep instance, Cook cook, boolean success) {
        return finished(instance, cook, null, null, true);
    }

    public final InteractResult finished(IStep instance, Cook cook, String keyID, InputType inputType) {
        return finished(instance, cook, keyID, inputType, true);
    }

    public final InteractResult finished(IStep instance, boolean success) {
        return finished(instance, null, null, null, success);
    }

    public final void updateInteractions(IStep instance) {
        instance.updateInteractions();
    }

    /**
     * Function to update the station.
     * @param instance
     * @param delta
     */
    public void update(IStep instance, float delta) { }

    /** Function to override that is called when the task is done. */
    public void updateCookTarget(Cook cook) { }

    /** Calls for the next {@link InteractionStep}. */
    public InteractResult finishedLast(IStep instance, Cook cook, String keyID, InputType inputType) {
        return InteractResult.NONE;
    }

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