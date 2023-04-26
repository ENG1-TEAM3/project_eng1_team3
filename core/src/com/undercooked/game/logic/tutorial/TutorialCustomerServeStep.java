package com.undercooked.game.logic.tutorial;

import com.undercooked.game.entity.cook.CookController;

public class TutorialCustomerServeStep extends TutorialFollowCurrentCookStep {
    public TutorialCustomerServeStep(String text, float textSpeed, CookController cookController) {
        super(text, textSpeed, cookController);
        skippable = false;
    }
}
