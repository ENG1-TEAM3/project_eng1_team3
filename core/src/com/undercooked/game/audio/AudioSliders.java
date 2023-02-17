package com.undercooked.game.audio;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.textures.Textures;
import com.undercooked.game.util.Listener;

/**
 * A class to easily add the {@link Slider}s for
 * setting the volume of the {@link AudioSettings}
 * to be drawn.
 */
public class AudioSliders {

    /** The background's variables */
    private float x, y, width, height;
    /** Percentage of height a {@link Slider} uses for its area */
    private float slideHeight;
    /** Percent padding for the {@link Slider}s */
    private float paddingSides, paddingVertical;
    /** Percentage of the {@link Slider} used for the button. */
    private float slideButtonWidth;
    Sprite backSprite;
    Array<Slider> sliders;

    /**
     * Constructor for the AudioSliders
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public AudioSliders(float x, float y, float width, float height, Texture backgroundTex) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.backSprite = new Sprite(backgroundTex);
        this.paddingSides = 0.1F;
        this.paddingVertical = 0.05F;
        this.slideHeight = 0.5F; // Use up half the area.
        this.slideButtonWidth = 0.05F;

        this.sliders = new Array<>();
    }

    /**
     * Calculates the new position, width and height of the
     * {@link Slider}s.
     */
    public void update() {
        // Resize the sprite to fit the width / height
        backSprite.setSize(width, height);
        backSprite.setPosition(x, y);

        // Convert the percentage padding to the amount they
        // are on width / height
        // It is /2 as it's for one side only.
        float paddingX = (width*paddingSides)/2F,
              paddingY = (height*paddingVertical)/2F;

        // Calculate the corners of the Sliders' area.
        float bottomX = paddingX,
              bottomY = paddingY,
              topX = width-paddingX,
              topY = height-paddingY;

        // Calculate the width of the sliders.
        float slideWidth = topX - bottomX;

        // Calculate the vertical area given to each slider.
        float percentSlide = 1F / sliders.size;
        float slideYRoom = (topY - bottomY) * percentSlide;

        // Calculate the height of the sliders.
        float slideHeight = this.slideHeight * slideYRoom;

        System.out.println(slideHeight);

        // Finally, update the Sliders
        for (int i = sliders.size-1 ; i >= 0 ; i--) {
            Slider slider = sliders.get(i);
            slider.setWidth(slideWidth);
            slider.setHeight(slideHeight);
            slider.setSliderSize(slideButtonWidth*slideWidth,slideHeight);
            slider.setX(x+bottomX);
            slider.setY(y+bottomY + (slideYRoom - slideHeight)/2 + slideYRoom * (sliders.size-1-i));
        }
    }

    public Slider addSlider(Listener<Float> listener, String audioGroup) {
        Slider newSlider = new Slider(x,y,0.5F,0F,1F,
                Textures.getInstance().loadTexture("uielements/vButton.jpg", "vButton"), audioGroup);
        newSlider.addListener(listener);
        sliders.add(newSlider);
        update();
        return newSlider;
    }

    public Slider addSlider(Listener<Float> listener) {
        return addSlider(listener, "default");
    }

    public void removeSliderInd(int index) {
        sliders.removeIndex(index);
        update();
    }

    public void removeSlider(Slider slider) {
        removeSliderInd(sliders.indexOf(slider, true));
    }

    public void render(SpriteBatch batch) {

        // Draw the background sprite behind.
        backSprite.draw(batch);

        // Draw the Sliders
        for (Slider slider : sliders) {
            slider.render(batch);
        }
    }

    //region Getters

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getPaddingSides() {
        return paddingSides;
    }

    public float getPaddingVertical() {
        return paddingVertical;
    }

    public Slider getSlider(int index) {
        return sliders.get(index);
    }

    // endregion

    //region Setters

    public void setX(float x) {
        this.x = x;
        update();
    }

    public void setY(float y) {
        this.y = y;
        update();
    }

    public void setWidth(float width) {
        this.width = width;
        update();
    }

    public void setHeight(float height) {
        this.height = height;
        update();
    }

    public void setPaddingSides(float paddingSides) {
        this.paddingSides = paddingSides;
        update();
    }

    public void setPaddingVertical(float paddingVertical) {
        this.paddingVertical = paddingVertical;
        update();
    }

    public void setSliderWidth(float sliderWidthPercent) {
        this.slideButtonWidth = sliderWidthPercent;
        update();
    }

    //endregion
}
