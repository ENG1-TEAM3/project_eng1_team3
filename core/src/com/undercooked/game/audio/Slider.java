package com.undercooked.game.audio;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.undercooked.game.util.Listener;
import com.undercooked.game.util.ListenerController;

/**
 *
 * TODO: JavaDocs
 *
 */

public class Slider {

    Sprite sliderSprite;
    Button sliderButton;
    float percent, minValue, maxValue;
    ListenerController<Float> listenerController;
    String audioGroup;

    public Slider(float x, float y, float value, float minValue, float maxValue, Texture sliderTex, String audioGroup) {
        this.sliderButton = new Button(new Button.ButtonStyle());
        this.sliderButton.setPosition(x,y);
        this.sliderSprite = new Sprite(sliderTex);

        this.percent = (value - minValue) / (maxValue - minValue);
        this.minValue = minValue;
        this.maxValue = maxValue;

        this.listenerController = new ListenerController();

        this.sliderButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                interact(x,y);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                interact(x, y);
                super.touchDragged(event, x, y, pointer);
            }
        });

        this.audioGroup = audioGroup;
    }
    public Slider(float x, float y, float value, float minValue, float maxValue, Texture sliderTex) {
        this(x,y,value,minValue,maxValue,sliderTex,"default");
    }

    /*public setBodySprite(Texture sprite) {
        bodySprite = sprite;
    }*/

    public void interact(float x, float y) {
        setPercent((x-sliderSprite.getWidth()/2) / (sliderButton.getWidth() - sliderSprite.getWidth()));
        listenerController.tellListeners(percent);
    }

    public void addListener(Listener<Float> listener) {
        listenerController.addListener(listener);
    }

    public void removeListener(Listener<Float> listener) {
        listenerController.removeListener(listener);
    }

    public void setSize(float width, float height) {
        sliderButton.setSize(width, height);
    }

    public void setSliderSize(float width, float height) {
        // Limit the width to the button size.
        if (sliderButton.getWidth() < width) {
            width = sliderButton.getWidth();
        }
        sliderSprite.setSize(width, height);
    }

    public float getValue() {
        return Math.max(Math.min(minValue + percent * (maxValue - minValue),maxValue),minValue);
    }

    public void setSliderSprite(Sprite sprite) {
        sliderSprite = sprite;
    }

    public void render(SpriteBatch batch) {
        // sliderButton.draw(batch, 1F);
        // Position of sliderSprite on the Slider so that it visually touches the border of
        // the Slider (so that anywhere that the sliderSprite is pressed will count as pressing it.
        sliderSprite.setPosition(sliderButton.getX() + (sliderButton.getWidth()-sliderSprite.getWidth())*percent,
                sliderButton.getY()+sliderButton.getHeight()/2-sliderSprite.getHeight()/2);
        sliderSprite.draw(batch);
    }

    public void addToStage(Stage stage) {
        stage.addActor(this.sliderButton);
    }

    // region Setters

    public void setX(float x) {
        sliderButton.setX(x);
    }

    public void setY(float y) {
        sliderButton.setY(y);
    }

    public void setWidth(float width) {
        sliderButton.setWidth(width);
    }

    public void setHeight(float height) {
        sliderButton.setHeight(height);
    }

    public float setValue(float value) {
        float percent = value / (maxValue - minValue);
        return setPercent(percent);
    }

    public float setPercent(float percent) {
        this.percent = Math.max(Math.min(percent, 1F), 0F);
        return this.percent;
    }

    public void setTouchable(Touchable touchable) {
        this.sliderButton.setTouchable(touchable);
    }

    // endregion

}
