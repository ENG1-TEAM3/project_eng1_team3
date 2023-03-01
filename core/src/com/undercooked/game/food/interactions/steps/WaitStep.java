package com.undercooked.game.food.interactions.steps;

import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.food.interactions.InteractionStep;

public class WaitStep extends InteractionStep {
    @Override
    public void update(IStep instance, float delta) {
        // Check if elapsed time is >= time
        if (instance.elapsedTime >= time) {
            // If it is, then the wait is finished.
            finished(instance, true);
        }
    }
}
