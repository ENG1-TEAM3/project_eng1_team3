package com.undercooked.game.interactions.steps;

import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.interactions.InteractionInstance;
import com.undercooked.game.interactions.InteractionStep;

/**
 * Interaction step that goes to the success or failure steps if
 * the {@link com.undercooked.game.station.Station} has the
 * {@link com.undercooked.game.food.Item} specified in {@link #value}
 * a number of times specified in {@link #value}.
 *
 * <br>Succeeds if it does have the {@link com.undercooked.game.food.Item}
 * the number of times specified, and fails if it does not.
 */
public class StationHasItemStep extends InteractionStep {
    @Override
    public void update(InteractionInstance instance, Cook cook, float delta) {
        // Continue if the station has an item.
        finished(instance, cook, instance.station.hasItem(value, (int) time));
    }
}
