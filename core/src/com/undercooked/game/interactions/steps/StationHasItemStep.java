package com.undercooked.game.interactions.steps;

import com.undercooked.game.entity.Cook;
import com.undercooked.game.interactions.IStep;
import com.undercooked.game.interactions.InteractionStep;

public class StationHasItemStep extends InteractionStep {
    @Override
    public void update(IStep instance, Cook cook, float delta) {
        // Continue if the station has an item.
        finished(instance, cook, instance.station.hasItem(value, (int) time));
    }
}
