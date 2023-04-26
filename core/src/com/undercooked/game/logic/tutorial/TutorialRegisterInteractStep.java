package com.undercooked.game.logic.tutorial;

import com.undercooked.game.entity.cook.CookController;
import com.undercooked.game.util.Listener;

public class TutorialRegisterInteractStep extends TutorialFollowCurrentCookStep {
    public TutorialRegisterInteractStep(String text, float textSpeed, CookController cookController) {
        super(text, textSpeed, cookController);
        skippable = false;
    }
}
