package com.undercooked.game.interactions;

import com.badlogic.gdx.utils.Array;
import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.Cook;

import java.util.Collections;

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

    public void stop(IStep instance) {
        // First unlock the Cooks
        instance.station.unlockCooks();
        // Then stop playing the sound
        instance.stopSound(sound);
    }

    public final void updateInteractions(IStep instance) {
        instance.updateInteractions();
    }

    /**
     * Function to update the station.
     * @param instance {@link IStep} : The interaction instance for a {@link com.undercooked.game.station.Station}.
     * @param cook {@link Cook} : The {@link Cook} locked to the {@link com.undercooked.game.station.Station},
     *                            or {@code null}.
     * @param delta {@link float} : The time since the last frame.
     */
    public void update(IStep instance, Cook cook, float delta) { }

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

    private void outputStep(InteractionStep cStep, int tabs) {
        String tabStr = String.join("", Collections.nCopies(tabs, "\t"));
        System.out.println(tabStr + cStep.getClass().getSimpleName());
        if (cStep.success != null) {
            System.out.println(tabStr + "\t" + "success:");
            for (InteractionStep nStep : cStep.success) {
                outputStep(nStep, tabs + 2);
            }
        }
        if (cStep.failure != null) {
            System.out.println(tabStr + "\t" + "failure:");
            for (InteractionStep nStep : cStep.failure) {
                outputStep(nStep, tabs+2);
            }
        }
    }

    public void output() {
        outputStep(this, 0);
    }

    public void output(int tabs) {
        outputStep(this, tabs);
    }
}
