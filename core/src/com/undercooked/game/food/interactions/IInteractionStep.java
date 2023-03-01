package com.undercooked.game.food.interactions;

import com.undercooked.game.food.interactions.steps.IStep;

public interface IInteractionStep {

    void update(IStep instance, float delta);

}
