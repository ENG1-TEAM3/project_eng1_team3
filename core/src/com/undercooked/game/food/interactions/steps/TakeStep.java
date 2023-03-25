package com.undercooked.game.food.interactions.steps;

import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.Cook;
import com.undercooked.game.food.interactions.InteractResult;
import com.undercooked.game.food.interactions.InteractionStep;

public class TakeStep extends InteractionStep {

    @Override
    public void update(IStep instance, float delta) {

    }

    @Override
    public InteractResult interact(IStep instance, Cook cook, String keyID, InputType inputType) {
        return super.interact(instance, cook, keyID, inputType);
    }
}
