package com.undercooked.game.interactions.steps;

import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.interactions.IStep;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionStep;

/**
 * An interaction step that stops checking for interactions for
 * the selected {@link Cook} on the current frame.
 *
 * <br>Similar to {@link StopInputStep}, but stops the interactions.
 * <br>Fails if it does not continue from an interact.
 */
public class StopInputStep extends InteractionStep {
    @Override
    public InteractResult finishedLast(IStep instance, Cook cook, String keyID, InputType inputType) {
        return interact(instance, cook, keyID, inputType);
    }

    @Override
    public void update(IStep instance, Cook cook, float delta) {
        finished(instance, cook, false);
    }

    @Override
    public InteractResult interact(IStep instance, Cook cook, String keyID, InputType inputType) {
        return InteractResult.STOP;
    }
}
