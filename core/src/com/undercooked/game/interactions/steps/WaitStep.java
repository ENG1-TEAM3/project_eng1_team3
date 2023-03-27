package com.undercooked.game.interactions.steps;

import com.undercooked.game.entity.Cook;
import com.undercooked.game.interactions.IStep;
import com.undercooked.game.interactions.InteractionStep;

public class WaitStep extends InteractionStep {
    @Override
    public void update(IStep instance, float delta) {
        updateTime(instance, delta);
        // Check if elapsed time is >= time
        if (instance.elapsedTime >= time) {
            // If it is, then the wait is finished.
            finished(instance, true);
        }
    }
}
