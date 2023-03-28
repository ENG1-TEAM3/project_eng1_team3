package com.undercooked.game.interactions.steps;

import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.Cook;
import com.undercooked.game.interactions.IStep;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionStep;

public class GiveStep extends InteractionStep {
    @Override
    public InteractResult interact(IStep instance, Cook cook, String keyID, InputType inputType) {
        // Give the item to the Cook, time number of times
        System.out.println(String.format("Given Cook %d '%s'", (int) time, value));
        for (int i = (int) time ; i > 0 ; i--) {
            cook.addItem(instance.gameItems.getItem(value));
        }
        // Finished with a success
        return finished(instance, cook, true);
    }
}
