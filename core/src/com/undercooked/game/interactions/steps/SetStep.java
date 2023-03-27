package com.undercooked.game.interactions.steps;

import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.Cook;
import com.undercooked.game.interactions.IStep;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionStep;

public class SetStep extends InteractionStep {

    @Override
    public InteractResult finishedLast(IStep instance, Cook cook, String keyID, InputType inputType) {
        // It is finished immediately
        return finished(instance, cook,true);
    }

    @Override
    public void updateCookTarget(Cook cook) {
        // Set the items on the stack.

    }
}
