package com.undercooked.game.logic.tutorial;

import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.entity.cook.CookController;

public class TutorialCookHoldingStep extends TutorialFollowCookStep {
    String itemID;
    public TutorialCookHoldingStep(String text, float textSpeed, CookController cookController, String itemID) {
        super(text, textSpeed, cookController);
        skippable = false;
        this.itemID = itemID;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        // Check if the current cook is holding the item
        Cook currentCook = cookController.getCurrentCook();
        if (currentCook == null || currentCook.heldItems.hasID(itemID)) {
            finished();
        }
    }
}
