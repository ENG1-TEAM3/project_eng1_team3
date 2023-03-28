package com.undercooked.game.interactions.steps;

import com.undercooked.game.entity.Cook;
import com.undercooked.game.interactions.IStep;
import com.undercooked.game.interactions.InteractionStep;

/**
 * Interaction step for waiting a number of times specified in the
 * {@link #time} value.
 *
 * <br>Cannot fail, but will not continue from this step until the
 * wait is finished.
 */
public class WaitStep extends InteractionStep {
    @Override
    public void update(IStep instance, Cook cook, float delta) {
        updateTime(instance, delta);
        // Check if elapsed time is >= time
        // System.out.println(instance.elapsedTime);
        if (instance.elapsedTime >= time) {
            // If it is, then the wait is finished.
            finished(instance, cook, null, null, true);
        }
    }
}
