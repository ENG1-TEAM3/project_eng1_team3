package com.undercooked.game.interactions.steps;

import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.food.Item;
import com.undercooked.game.interactions.InteractionInstance;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionStep;

/**
 * Interaction step that deletes all items currently on
 * the {@link com.undercooked.game.station.Station}, and
 * then sets to the value provided, ignoring if the
 * {@link com.undercooked.game.station.Station} can hold
 * it or not.
 *
 * <br>Does not fail.
 */
public class SetStep extends InteractionStep {

    @Override
    public InteractResult finishedLast(InteractionInstance instance, Cook cook, String keyID, InputType inputType) {
        // Clear the station's items
        instance.station.items.clear();
        // Get the new item
        Item newItem = instance.gameItems.getItem(value);
        // No need to check if it's null, as it was already done when
        // loading the interaction
        // Add to the station's items
        instance.station.items.add(newItem);
        // It is finished immediately
        return finished(instance, cook,true);
    }

    @Override
    public void update(InteractionInstance instance, Cook cook, float delta, float powerUpMultiplier) {
        finishedLast(instance, cook, null, null);
    }
}
