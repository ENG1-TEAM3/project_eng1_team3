package com.undercooked.game.interactions.steps.cook;

import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.interactions.IStep;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionStep;

/**
 * Interaction step which removes the topmost item of
 * the {@link Cook}.
 *
 * <br>Fails if the {@link Cook} is null, or if they
 * have no items.
 */
public class CookRemoveStep extends InteractionStep {
    @Override
    public InteractResult finishedLast(IStep instance, Cook cook, String keyID, InputType inputType) {
        if (cook == null) {
            return finished(instance, null, keyID, inputType, false);
        }
        if (cook.heldItems.size() <= 0) {
            return finished(instance, cook, keyID, inputType, false);
        }
        // Remove the item from the cook
        cook.takeItem();
        // Finish step
        return finished(instance, cook, keyID, inputType, true);
    }

    @Override
    public void update(IStep instance, Cook cook, float delta) {
        finishedLast(instance,cook,null,null);
    }
}
