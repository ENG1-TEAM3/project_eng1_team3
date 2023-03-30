package com.undercooked.game.interactions.steps;

import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.interactions.IStep;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionStep;

/**
 * Interaction step which removes the topmost item of
 * the {@link com.undercooked.game.station.Station}.
 *
 * <br>Fails if there is no item to remove from the
 * {@link com.undercooked.game.station.Station}.
 */
public class RemoveStep extends InteractionStep {
    @Override
    public InteractResult finishedLast(IStep instance, Cook cook, String keyID, InputType inputType) {
        if (!instance.station.hasItem()) {
            return finished(instance, cook, keyID, inputType, false);
        }
        // Remove the item from the station
        instance.station.takeItem();
        // Finish step
        return finished(instance, cook, keyID, inputType, true);
    }

    @Override
    public void update(IStep instance, Cook cook, float delta) {
        finishedLast(instance,cook,null,null);
    }
}
