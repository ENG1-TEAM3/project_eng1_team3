package com.undercooked.game.interactions.steps.cook.input;

import com.undercooked.game.Input.InputController;
import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.Cook;
import com.undercooked.game.interactions.IStep;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionStep;

public class ReleasedStep extends InteractionStep {
    @Override
    public InteractResult interact(IStep instance, Cook cook, String keyID, InputType inputType) {
        // If it uses the key set in value
        if (InputController.isKeyReleased(value)) {
            // Then move to the next instruction
            finished(instance, cook, true);
            // Immediately check next input
            return InteractResult.RESTART;
        }
        // Otherwise, just ignore
        return InteractResult.NONE;
    }
}