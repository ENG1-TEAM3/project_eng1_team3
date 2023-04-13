package com.undercooked.game.interactions.steps.cook;

import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.interactions.InteractionInstance;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionStep;

/**
 * Interaction step for taking the {@link com.undercooked.game.station.Station}'s topmost
 * item and adding it to the {@link Cook}.
 *
 * <br>Fails in the condition that the {@link com.undercooked.game.station.Station}
 * isn't holding an item, or if it can't add the item to the {@link Cook}.
 */
public class TakeStep extends InteractionStep {
    @Override
    public InteractResult interact(InteractionInstance instance, Cook cook, String keyID, InputType inputType) {
        // Only continue if it's the take input, and pressed

        // If there is an item, then take it. Finish this interaction with
        // a success
        if (instance.station.hasItem()) {
            if (!cook.canAddItem()) {
                return finished(instance, cook, keyID, inputType, false);
            }
            cook.addItem(instance.station.items.pop());
            return finished(instance, cook, keyID, inputType, true);
        }
        // If it doesn't have an item, then finish with a failure, and
        // return
        return finished(instance, cook, keyID, inputType, false);
    }
}
