package com.undercooked.game.interactions.steps.cook;

import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.interactions.InteractionInstance;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionStep;

/**
 * Interaction step for locking the {@link Cook} to the
 * {@link com.undercooked.game.station.Station}.
 *
 * <br>Fails in the condition that {@link Cook} is {@code null}.
 */
public class LockCookStep extends InteractionStep {
    @Override
    public InteractResult finishedLast(InteractionInstance instance, Cook cook, String keyID, InputType inputType) {
        if (cook != null) {
            // Lock the cook to the station
            cook.lockToStation(instance.station);
            return finished(instance, cook, keyID, inputType, true);
        } else {
            return finished(instance, null, keyID, inputType, false);
        }
    }
}
