package com.undercooked.game.food.interactions;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.Input.InputType;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.entity.Cook;
import com.undercooked.game.food.interactions.steps.IStep;
import com.undercooked.game.util.Listener;

public class StationInteractControl {

    IStep interactionInstance;

    Array<InteractionStep> completeSteps;
    InteractionStep currentInteraction;
    Array<InteractionStep> stepsToFollow;
    AudioManager audioManager;
    float lastDeltaCheck;

    public StationInteractControl(AudioManager audioManager) {
        this.audioManager = audioManager;
        this.interactionInstance = new IStep(audioManager);
        this.completeSteps = new Array<>();
        this.stepsToFollow = new Array<>();
        this.interactionInstance.successListener = new Listener<Boolean>() {
            @Override
            public void tell(Boolean success) {
                // First add the following Interactions
                if (success) {
                    addFollowingInteractions(currentInteraction.success);
                } else {
                    addFollowingInteractions(currentInteraction.failure);
                }
                // Then move to the next Interaction
                nextInteraction();
                float curDelta = Gdx.graphics.getDeltaTime();
                // Update the next instruction
                update(curDelta - lastDeltaCheck);
                lastDeltaCheck = curDelta;
            }
        };
        this.lastDeltaCheck = 0;
    }

    public void update(float delta) {
        // Only update if there's an interaction currently
        if (currentInteraction != null) {
            lastDeltaCheck = delta;
            currentInteraction.playSound(interactionInstance);
            currentInteraction.update(interactionInstance, delta);
        }
    }

    public void nextInteraction() {
        InteractionStep nextStep = null;
        if (stepsToFollow.size > 0) {
            nextStep = stepsToFollow.first();
            stepsToFollow.removeIndex(0);
        }
        currentInteraction = nextStep;
    }

    public void setInteraction(InteractionStep interaction) {
        interactionInstance.reset();
        completeSteps.clear();
        stepsToFollow.clear();

        currentInteraction = interaction;
    }

    public void addFollowingInteractions(Array<InteractionStep> interactions) {
        for (int i = interactions.size-1 ; i >= 0 ; i--) {
            stepsToFollow.insert(0,interactions.get(i));
        }
    }

    public void setInteractions(Array<InteractionStep> interactions) {
        setInteraction(interactions.first());
        addFollowingInteractions(interactions);
    }

    public void clear() {
        currentInteraction = null;
        stepsToFollow.clear();
        completeSteps.clear();
    }

    public InteractResult interact(Cook cook, String keyID, InputType inputType) {
        if (currentInteraction != null) {
            return currentInteraction.interact(interactionInstance, cook, keyID, inputType);
        }
        return InteractResult.NONE;
    }
}
