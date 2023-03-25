package com.undercooked.game.food.interactions.steps;

import com.undercooked.game.Input.InputController;
import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.Cook;
import com.undercooked.game.food.interactions.InteractResult;
import com.undercooked.game.food.interactions.InteractionStep;

public class PressedStep extends InteractionStep {


    @Override
    public void update(IStep instance, float delta) {

    }

    @Override
    public InteractResult interact(IStep instance, Cook cook, String keyID, InputType inputType) {
        // If it uses the key set in value
        if (InputController.isKeyPressed(value)) {
            // Then move to the next instruction
            finished(instance, true);
            // Immediately check next input
            return InteractResult.RESTART;
        }
        // Otherwise, just ignore
        return InteractResult.NONE;
    }
}
