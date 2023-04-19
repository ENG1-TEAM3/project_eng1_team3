package com.undercooked.game.interactions.steps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.interactions.InteractionInstance;
import com.undercooked.game.interactions.InteractionStep;

/**
 * Interaction step for waiting a number of times specified in the
 * {@link #time} value.
 *
 * <br>Cannot fail, but will not continue from this step until the
 * wait is finished.
 */
public class WaitStep extends InteractionStep {

    static final float width = 50F;
    static final float height = 10F;
    static final float padding = 5F;
    static final float paddingDouble = padding*2;
    static final float offsetY = 32F;

    @Override
    public void update(InteractionInstance instance, Cook cook, float delta, float powerUpMultiplier) {
        updateTime(instance, delta, powerUpMultiplier);
        // Check if elapsed time is >= time
        if (instance.elapsedTime >= time) {
            // If it is, then the wait is finished.
            waitFinished(instance, cook);
        }
    }

    public void waitFinished(InteractionInstance instance, Cook cook) {
        finished(instance, cook, null, null, true);
    }

    public float getDrawPercent(InteractionInstance instance) {
        return Math.min(1f, instance.elapsedTime / time);
    }

    public void setBarColor(InteractionInstance instance, ShapeRenderer shape) {
        shape.setColor(Color.GREEN);
    }

    @Override
    public void draw(InteractionInstance instance, ShapeRenderer shape) {
        // Draw a box above the station, and then inside that a progress bar
        float drawY = instance.station.getY() + instance.station.getInteractBox().height/2 + offsetY;
        float drawX = instance.station.getX() + instance.station.getInteractBox().width/2 - (width+paddingDouble)/2;

        // Draw back rectangle
        shape.setColor(Color.GRAY);
        shape.rect(drawX, drawY, width + paddingDouble, height + paddingDouble);

        // Draw progress bar
        shape.setColor(Color.BLACK);
        shape.rect(drawX+padding, drawY+padding, width, height);
        setBarColor(instance, shape);
        shape.rect(drawX+padding, drawY+padding, width * getDrawPercent(instance), height);

        // Reset colour
        shape.setColor(Color.WHITE);
    }
}
