package com.undercooked.game.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.Entity;
import com.undercooked.game.entity.cook.CookController;
import com.undercooked.game.logic.GameLogic;
import com.undercooked.game.logic.TutorialLogic;
import com.undercooked.game.logic.tutorial.TutorialStep;
import com.undercooked.game.util.CameraController;
import com.undercooked.game.util.Constants;

public class TutorialRenderer extends GameRenderer {

    TutorialLogic logic;
    public TutorialRenderer(TutorialLogic logic, SpriteBatch batch, ShapeRenderer shape, BitmapFont font, Array renderEntities) {
        super(logic, batch, shape, font, renderEntities);
        this.logic = logic;
    }

    public TutorialRenderer(TutorialLogic logic) {
        super();
        this.logic = logic;
    }

    @Override
    public void renderEntity(Entity entity) {
        // Render the entity for the current tutorial step (if there is one)
        TutorialStep currentStep = logic.getCurrentStep();
        if (currentStep == null) return;

        batch.begin();
        currentStep.render(batch, entity);
        batch.end();
    }

    @Override
    public void renderUI(float delta) {
        // Render the base
        super.renderUI(delta);

        // Render tutorial information
        TutorialStep tutorialStep = logic.getCurrentStep();
        // If there is no step to render, stop here
        if (tutorialStep == null) return;

        // Only if it has text, draw it to the screen
        if (!tutorialStep.hasText()) return;

        float boxWidth = 800;
        float boxHeight = 260;
        float boxLeft = 600;
        text.setText(font, tutorialStep.getDisplayText(), 0, tutorialStep.getDisplayText().length(), Color.WHITE, boxWidth -20, Align.left, true, null);

        shape.begin(ShapeRenderer.ShapeType.Filled);
        // Draw the background
        shape.rect(boxLeft,30, boxWidth,boxHeight);
        shape.end();
        // The the text
        batch.begin();
        font.draw(batch, text, boxLeft+10, boxHeight+5);
        batch.end();

    }
}
