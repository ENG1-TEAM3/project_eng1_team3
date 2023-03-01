package com.undercooked.game.food.interactions;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.food.interactions.steps.IStep;
import com.undercooked.game.util.Listener;

public class StationInteractControl {

    IStep interactionInstance;

    Array<InteractionStep> completeSteps;
    InteractionStep currentInteraction;
    Array<InteractionStep> stepsToFollow;
    AudioManager audioManager;

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
                float time = currentInteraction.time;
                // Then move to the next Interaction
                nextInteraction();
                // If the time <= 0, then immediately update the next interaction.
                update(Gdx.graphics.getDeltaTime());
            }
        };
    }

    public void update(float delta) {
        // Only update if there's an interaction currently
        if (currentInteraction != null) {
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
    }


}
