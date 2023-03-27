package com.undercooked.game.interactions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.Input.InputType;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.entity.Cook;
import com.undercooked.game.food.Item;
import com.undercooked.game.food.ItemStack;
import com.undercooked.game.food.Items;
import com.undercooked.game.station.Station;
import com.undercooked.game.util.Listener;

public class StationInteractControl {

    Station station;
    IStep interactionInstance;
    Interactions interactions;

    /**
     * Possible {@link InteractionStep}s that can be started.
     */
    Array<InteractionObject> possibleInteractions;
    Array<InteractionStep> completeSteps;
    InteractionStep currentInteraction;
    Array<InteractionStep> stepsToFollow;
    AudioManager audioManager;
    float lastDeltaCheck;

    public StationInteractControl(Station station, AudioManager audioManager, Items items) {
        this.station = station;
        this.audioManager = audioManager;
        this.interactionInstance = new IStep(station, this, audioManager, items);
        this.lastDeltaCheck = 0;

        this.possibleInteractions = new Array<>();
        this.completeSteps = new Array<>();
        this.stepsToFollow = new Array<>();
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
        if (interactions == null) {
            return;
        }
        for (int i = interactions.size-1 ; i >= 0 ; i--) {
            stepsToFollow.insert(0,interactions.get(i));
        }
    }

    public void setInteractions(Array<InteractionStep> interactions) {
        clear();
        if (interactions == null || interactions.size == 0) {
            return;
        }
        // Make a copy of the array
        Array<InteractionStep> arrayCopy = new Array<>();
        for (InteractionStep interaction : interactions) {
            arrayCopy.add(interaction);
        }
        // Set the interaction to the first index
        setInteraction(arrayCopy.first());
        // Remove the first index
        arrayCopy.removeIndex(0);
        // Then add the rest
        addFollowingInteractions(arrayCopy);
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
        // System.out.println("current Interaction null");
        return InteractResult.NONE;
    }

    public InteractResult finished(Cook cook, String keyID, InputType inputType, boolean success) {
        // First add the following Interactions
        if (success) {
            addFollowingInteractions(currentInteraction.success);
        } else {
            addFollowingInteractions(currentInteraction.failure);
        }
        // Then move to the next Interaction
        nextInteraction();
        float curDelta = Gdx.graphics.getDeltaTime();
        lastDeltaCheck = curDelta;
        // If currentInteraction is null, update.
        if (currentInteraction == null) {
            station.updateStationInteractions();
            return InteractResult.STOP;
        } else {
            System.out.println("Doing next: " + currentInteraction.getClass());
            // Otherwise call the finished last of the previous
            return currentInteraction.finishedLast(interactionInstance, cook, keyID, inputType);
        }
    }

    public void setInteractions(Interactions interactions) {
        this.interactions = interactions;
    }

    public void updatePossibleInteractions(String stationID) {
        // If the interactions instance is null, clear the possibleInteractions and
        // return
        if (interactions == null) {
            possibleInteractions.clear();
            return;
        }
        Array<String> stationInteractions = interactions.getStationInteractions(stationID);
        // System.out.println(stationID + ": " + stationInteractions);

        // If it's null, then clear and return
        if (stationInteractions == null) {
            // System.out.println("NO INTERACTION :(");
            possibleInteractions.clear();
            return;
        }

        // Load all the possible interactions for this station
        for (String stationInteraction : stationInteractions) {
            // System.out.println("POSSIBLE INTERACTION!!!");
            possibleInteractions.add(interactions.getInteractionSteps(stationInteraction));
        }
    }

    public InteractionObject findValidInteraction(ItemStack items) {
        // Find an interaction with a matching item requirement.
        // Loop through possibleInteractions
        for (InteractionObject possibleInt : possibleInteractions) {
            // First make sure the stack sizes are the same
            if (possibleInt.items.size != items.size()) {
                // System.out.println(String.format("%d vs. %d", possibleInt.items.size, items.size()));
                // If they aren't, then it's invalid
                break;
            }

            // Check if, for every item in the ItemStack, that there is matching item
            // in the possibleInteraction items.
            Array<Item> checkArray = items.asArray();

            // Loop through the item IDs in the possibleInteractions
            for (String itemID : possibleInt.items) {
                // Check for if the checkArray has the ID.
                int itemInd = -1;
                for (int i = 0 ; i < items.size() ; i++) {
                    Item item = items.get(i);
                    if (item.getID() == itemID) {
                        itemInd = i;
                        break;
                    }
                }
                // If it doesn't, this is an invalid interaction
                if (itemInd == -1) {
                    break;
                }
                // If it does, then remove the ingredient from the
                // items list
                checkArray.removeIndex(itemInd);
            }

            // Finally, if the checkArray is empty, then it's valid
            if (checkArray.size <= 0) {
                return possibleInt;
            }
        }
        // Nothing matches, so return null.
        return null;
    }

    public void setCurrentInteraction(InteractionObject interaction) {
        // Set the interaction, if it's not null
        if (interaction != null) {
            setInteractions(interaction.steps);
            System.out.println("INTERACTION VALID: " + interaction.items);
        }
    }
}
