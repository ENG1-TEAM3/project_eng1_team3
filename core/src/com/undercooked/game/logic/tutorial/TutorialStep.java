package com.undercooked.game.logic.tutorial;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.Entity;
import com.undercooked.game.util.Listener;

public class TutorialStep {

    protected float x;
    protected float y;
    protected int textIndex;
    protected float sinceLastText;
    protected float textSpeed;
    protected String text;
    protected String displayText;
    protected boolean hasText;
    protected boolean processInput;
    protected boolean skippable;
    protected Listener<TutorialStep> endListener;
    private boolean started;

    /**
     * The constructor for the {@link TutorialStep}.
     * @param text {@link String} : The text to display
     * @param textSpeed {@link float} : The number of characters to add per second.
     */
    public TutorialStep(String text, float textSpeed) {
        this.textIndex = 0;
        this.text = text;
        this.displayText = "";
        this.sinceLastText = 0f;
        this.textSpeed = 1f / textSpeed;
        this.skippable = true;
        this.hasText = true;
        this.x = -1;
        this.y = -1;
    }

    public TutorialStep(String text, float textSpeed, float x, float y) {
        this(text, textSpeed);
        this.x = x;
        this.y = y;
    }

    public TutorialStep(String text, float textSpeed, boolean processInput) {
        this(text, textSpeed, -1, -1, processInput);
    }

    public TutorialStep(String text, float textSpeed, float x, float y, boolean processInput) {
        this(text, textSpeed, x, y);
        this.processInput = processInput;
    }

    /**
     * Function that is called when the {@link TutorialStep} is
     * first started.
     */
    public void start() {
        started = true;
    }

    public void update(float delta) {
        // Update display text
        if (textIndex < text.length()) {
            sinceLastText += delta;
            while (sinceLastText >= textSpeed) {
                sinceLastText -= textSpeed;
                displayText += text.charAt(textIndex);
                textIndex++;
                if (textIndex >= text.length()) {
                    break;
                }
            }
        }
    }

    public void finished() {
        endListener.tell(this);
    }

    public void load(TextureManager textureManager) {
        load(textureManager, "default");
    }

    public void load(TextureManager textureManager, String textureGroup) {

    }

    public void postLoad(TextureManager textureManager) {

    }

    public void unload() {

    }

    public void setTextSpeed(float textSpeed) {
        this.textSpeed = textSpeed;
    }
    public void setCanSkip(boolean skippable) {
        this.skippable = skippable;
    }

    public void setHasText(boolean hasText) {
        this.hasText = hasText;
    }

    public void setEndListener(Listener<TutorialStep> endListener) {
        this.endListener = endListener;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void render(SpriteBatch batch, Entity entity) {

    }

    public void render(ShapeRenderer shape, Entity entity) {

    }

    public boolean getProcessInput() {
        return processInput;
    }

    public boolean canSkip() {
        return skippable;
    }

    public boolean hasText() {
        return hasText;
    }
}
