package com.undercooked.game.logic.tutorial;

import com.undercooked.game.entity.Entity;
import com.undercooked.game.entity.cook.CookController;

public class TutorialFollowCurrentCookStep extends TutorialFollowEntityStep {
    CookController cookController;
    public TutorialFollowCurrentCookStep(String text, float textSpeed, CookController cookController) {
        super(text, textSpeed, null);
        this.cookController = cookController;
        this.processInput = true;
    }

    @Override
    public void update(float delta) {
        targetEntity = cookController.getCurrentCook();
        super.update(delta);
    }

    @Override
    public void start() {
        super.start();
        targetEntity = cookController.getCurrentCook();
    }
}
