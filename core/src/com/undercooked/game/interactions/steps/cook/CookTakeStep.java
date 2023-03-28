package com.undercooked.game.interactions.steps.cook;

import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.Cook;
import com.undercooked.game.interactions.IStep;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionStep;

public class CookTakeStep extends InteractionStep {

    @Override
    public InteractResult finishedLast(IStep instance, Cook cook, String keyID, InputType inputType) {
        // Try to take the Cook's item
        if (cook.heldItems.size() > 0) {
            // Take the top item and add it to the Station
            instance.station.addItem(cook.takeItem());
            // Return a success
            return finished(instance, cook, keyID, inputType, true);
        }
        // Otherwise it failed
        return finished(instance, cook, keyID, inputType, false);
    }
}
