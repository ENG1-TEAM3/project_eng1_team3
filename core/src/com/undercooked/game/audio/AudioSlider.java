package com.undercooked.game.audio;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.util.Listener;
import com.undercooked.game.util.ListenerController;

/**
 *
 * TODO: JavaDocs
 *
 */

public class AudioSlider {

    Sprite sliderSprite;
    Button sliderButton;
    float percent, minValue, maxValue;
    ListenerController<Float> listenerController;
    String audioGroup;

    public AudioSlider(float x, float y, float value, float minValue, float maxValue, Texture sliderTex, String audioGroup) {
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
    public AudioSlider(float x, float y, float value, float minValue, float maxValue, Texture sliderTex) {
        this(x,y,value,minValue,maxValue,sliderTex,"default");
    }

    /*public setBodySprite(Texture sprite) {
        bodySprite = sprite;
    }*/

    public void interact(float x, float y) {
        percent = Math.max(Math.min(x / sliderButton.getWidth(), 1F), 0F);
        listenerController.tellListeners(percent);
        // System.out.println(String.format("x: %f, y: %f", x, y));
        // System.out.println(String.format("sliderButton.getWidth(): %f, percent: %f", sliderButton.getWidth(), percent));
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

    public void setSliderSize(float size) {

    }

    public float getValue() {
        return Math.max(Math.min(minValue + percent * (maxValue - minValue),maxValue),minValue);
    }

    public void setSliderSprite(Sprite sprite) {
        sliderSprite = sprite;
    }

    public void render(SpriteBatch batch) {
        // sliderButton.draw(batch, 1F);
        sliderSprite.setPosition(sliderButton.getX() + sliderButton.getWidth()*percent - sliderSprite.getWidth()/2,
                sliderButton.getY()+sliderButton.getHeight()/2-sliderSprite.getHeight()/2);
        sliderSprite.draw(batch);
    }

    public void addToStage(Stage stage) {
        stage.addActor(this.sliderButton);
    }

}
