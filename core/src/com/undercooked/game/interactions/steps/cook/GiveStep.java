package com.undercooked.game.interactions.steps.cook;

import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.interactions.IStep;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionStep;

/**
 * Interaction step that gives the {@link Cook} the item provided in
 * {@link #value} a number of times, specified by {@link #time}.
 *
 * <br>Fails in the condition that the {@link Cook} can't hold the number
 * specified in {@link #time}, or if {@link Cook} is null.
 */
public class GiveStep extends InteractionStep {
    @Override
    public InteractResult finishedLast(IStep instance, Cook cook, String keyID, InputType inputType) {
        if (cook == null) {
            return finished(instance, false);
        }
        if (!cook.canAddItems((int) time)) {
            return finished(instance, cook, false);
        }
        // Give the item to the Cook, time number of times
        // System.out.println(String.format("Given Cook %d '%s'", (int) time, value));
        for (int i = (int) time ; i > 0 ; i--) {
            cook.addItem(instance.gameItems.getItem(value));
        }
        // Finished with a success
        return finished(instance, cook, true);
    }
}
