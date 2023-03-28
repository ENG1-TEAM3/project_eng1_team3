package com.undercooked.game.interactions.steps;

import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.Cook;
import com.undercooked.game.food.Item;
import com.undercooked.game.interactions.IStep;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionStep;

public class SetStep extends InteractionStep {

    @Override
    public InteractResult finishedLast(IStep instance, Cook cook, String keyID, InputType inputType) {
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
    public void update(IStep instance, Cook cook, float delta) {
        finishedLast(instance, cook, null, null);
    }

    @Override
    public void updateCookTarget(Cook cook) {
        // Set the items on the stack.

    }
}
