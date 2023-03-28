package com.undercooked.game.interactions.steps.cook;

import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.Cook;
import com.undercooked.game.interactions.IStep;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionStep;

public class TakeStep extends InteractionStep {
    @Override
    public InteractResult interact(IStep instance, Cook cook, String keyID, InputType inputType) {
        // Only continue if it's the take input, and pressed

        // If there is an item, then take it. Finish this interaction with
        // a success
        if (instance.station.hasItem()) {
            cook.addItem(instance.station.items.pop());
            return finished(instance, cook, keyID, inputType, true);
        }
        // If it doesn't have an item, then finish with a failure, and
        // return
        return finished(instance, cook, keyID, inputType, false);
    }
}
