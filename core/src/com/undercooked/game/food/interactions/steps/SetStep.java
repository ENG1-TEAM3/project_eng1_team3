package com.undercooked.game.food.interactions.steps;

import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.food.interactions.InteractionStep;
import com.undercooked.game.station.Station;

public class SetStep extends InteractionStep {

    @Override
    public void update(IStep instance, float delta) {
        // It is finished immediately
        finished(instance, true);
    }

    @Override
    public void updateStation(Station station) {
        // Set the items on the stack.

    }
}
