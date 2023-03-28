package com.undercooked.game.interactions.steps;

import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.Cook;
import com.undercooked.game.interactions.IStep;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionStep;

public class UnlockCookStep extends InteractionStep {
    @Override
    public InteractResult finishedLast(IStep instance, Cook cook, String keyID, InputType inputType) {
        if (cook != null) {
            // Lock the cook to the station
            cook.unlock();
            return finished(instance, cook, keyID, inputType, true);
        } else {
            return finished(instance, null, keyID, inputType, false);
        }
    }
}
