package com.undercooked.game.interactions.steps.cook;

import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.interactions.InteractionInstance;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionStep;

/**
 * Interaction step for taking the {@link Cook}'s topmost
 * item and adding it to the {@link com.undercooked.game.station.Station}.
 *
 * <br>Fails in the condition that the {@link Cook} isn't holding an item, or
 * if it can't add the item to the {@link com.undercooked.game.station.Station}.
 */
public class CookTakeStep extends InteractionStep {

    @Override
    public InteractResult finishedLast(InteractionInstance instance, Cook cook, String keyID, InputType inputType) {
        // Try to take the Cook's item
        if (cook.heldItems.size() > 0) {
            if (!instance.station.canHoldItem()) {
                return finished(instance, cook, keyID, inputType, false);
            }
            // Take the top item and add it to the Station
            instance.station.addItem(cook.takeItem());
            // Return a success
            return finished(instance, cook, keyID, inputType, true);
        }
        // Otherwise it failed
        return finished(instance, cook, keyID, inputType, false);
    }
}
