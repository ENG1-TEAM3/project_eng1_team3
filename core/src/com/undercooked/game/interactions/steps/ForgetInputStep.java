package com.undercooked.game.interactions.steps;

import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.interactions.IStep;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionStep;

/**
 * An interaction step that continues to the next interaction step after
 * forgetting the last interaction provided.
 *
 * <br>Similar to {@link StopInputStep}, but continues the interactions.
 * <br>Fails if no interaction is being passed down.
 */
public class ForgetInputStep extends InteractionStep {

    @Override
    public InteractResult finishedLast(IStep instance, Cook cook, String keyID, InputType inputType) {
        return interact(instance, cook, keyID, inputType);
    }

    @Override
    public void update(IStep instance, Cook cook, float delta) {
        finished(instance, cook, true);
    }

    @Override
    public InteractResult interact(IStep instance, Cook cook, String keyID, InputType inputType) {
        return finished(instance, cook, true);
    }
}