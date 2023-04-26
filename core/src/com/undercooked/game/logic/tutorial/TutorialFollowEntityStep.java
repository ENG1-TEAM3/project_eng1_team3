package com.undercooked.game.logic.tutorial;

import com.undercooked.game.entity.Entity;

public class TutorialFollowEntityStep extends TutorialStep {
    protected Entity targetEntity;

    public TutorialFollowEntityStep(String text, float textSpeed, Entity targetEntity) {
        super(text, textSpeed);
        this.targetEntity = targetEntity;
    }

    @Override
    public void update(float delta) {
        // Update x and y
        if (targetEntity != null) {
            x = targetEntity.getX();
            y = targetEntity.getY();
        }
        super.update(delta);
    }
}
