package com.undercooked.game.logic.tutorial;

import com.undercooked.game.entity.Entity;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.entity.cook.CookController;

public class TutorialFollowCookStep extends TutorialFollowEntityStep {
    protected CookController cookController;
    public TutorialFollowCookStep(String text, float textSpeed, CookController cookController) {
        super(text, textSpeed, null);
        this.cookController = cookController;
        this.processInput = true;
    }

    @Override
    public void update(float delta) {
        targetEntity = cookController.getCurrentCook();
        super.update(delta);
    }
}
