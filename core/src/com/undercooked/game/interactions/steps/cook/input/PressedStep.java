package com.undercooked.game.interactions.steps.cook.input;

import com.undercooked.game.Input.InputController;
import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.interactions.IStep;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionStep;

/**
 * Interaction step for taking an input from the {@link Cook}
 * and checking if it matches the key specified in {@link #value},
 * and that it's pressed.
 *
 * <br>Cannot fail, but will not continue from this step until the
 * input is given.
 */
public class PressedStep extends InteractionStep {
    @Override
    public InteractResult interact(IStep instance, Cook cook, String keyID, InputType inputType) {
        // If it uses the key set in value
        if (InputController.isKeyPressed(value)) {
            // Then move to the next instruction
            finished(instance, cook, true);
            // Immediately check next input
            return InteractResult.RESTART;
        }
        // Otherwise, just ignore
        return InteractResult.NONE;
    }
}
