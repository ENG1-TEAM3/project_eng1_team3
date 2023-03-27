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
        for (int i = (int) time ; i > 0 ; i--) {
            cook.addItem(instance.gameItems.getItem(value));
            System.out.println("Cook add (" + i + "): " + value);
        }
        // Finished with a success
        return finished(instance, cook, true);
    }
}
