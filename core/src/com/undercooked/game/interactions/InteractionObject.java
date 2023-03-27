package com.undercooked.game.interactions;

import com.badlogic.gdx.utils.Array;

public class InteractionObject {
    Array<String> items;
    Array<InteractionStep> steps;

    public InteractionObject(Array<InteractionStep> steps, Array<String> ingredients) {
        this.steps = steps;
        this.items = ingredients;
    }

    public Array<String> getItems() {
        return items;
    }

    public Array<InteractionStep> getSteps() {
        return steps;
    }

    public void unload() {
        steps.clear();
    }
}