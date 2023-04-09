package com.undercooked.game.screen.buttons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.undercooked.game.GameType;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.util.Listener;

public class ModeButton {
    GameType currentType;
    TextureManager textureManager;
    Button scenarioBtn;
    Button endlessBtn;
    TextureRegionDrawable scenarioBtnDrawable;
    TextureRegionDrawable endlessBtnDrawable;
    Listener<GameType> listener;
    final float defaultButtonWidth = 473f;
    final float defaultButtonHeight = 144f;
    float buttonWidth;
    float buttonHeight;
    float x, y;

    public ModeButton(TextureManager textureManager) {
        currentType = GameType.SCENARIO;
        setScale(1f);
        this.textureManager = textureManager;
    }

    public void load(String textureID) {
        textureManager.load(textureID, "uielements/scenario.png");
        textureManager.load(textureID, "uielements/scenario_off.png");
        textureManager.load(textureID, "uielements/endless.png");
        textureManager.load(textureID, "uielements/endless_off.png");
    }

    public void unload() {
        textureManager.unloadTexture("uielements/scenario.png");
        textureManager.unloadTexture("uielements/scenario_off.png");
        textureManager.unloadTexture("uielements/endless.png");
        textureManager.unloadTexture("uielements/endless_off.png");
    }

    public void postLoad() {
        scenarioBtnDrawable = new TextureRegionDrawable();
        scenarioBtn = new Button(scenarioBtnDrawable);
        endlessBtnDrawable = new TextureRegionDrawable();
        endlessBtn = new Button(endlessBtnDrawable);

        scenarioBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setCurrentType(GameType.SCENARIO, true);
            }
        });
        endlessBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setCurrentType(GameType.ENDLESS, true);
            }
        });

        updateButtonSize();
    }

    public void update() {
        // Don't continue if either of the drawables are null
        if (scenarioBtnDrawable == null || endlessBtnDrawable == null) return;

        switch (currentType) {
            case SCENARIO:
                // Scenario is active, Endless is not
                scenarioBtnDrawable.setRegion(new TextureRegion(textureManager.get("uielements/scenario.png")));
                endlessBtnDrawable.setRegion(new TextureRegion(textureManager.get("uielements/endless_off.png")));
                return;
            case ENDLESS:
                // Endless is active, Scenario is not
                endlessBtnDrawable.setRegion(new TextureRegion(textureManager.get("uielements/endless.png")));
                scenarioBtnDrawable.setRegion(new TextureRegion(textureManager.get("uielements/scenario_off.png")));
                return;
        }
        return;
    }

    public void addToStage(Stage stage) {
        stage.addActor(scenarioBtn);
        stage.addActor(endlessBtn);
    }

    public void setCurrentType(GameType gameType) {
        setCurrentType(gameType, false);
    }

    private void setCurrentType(GameType gameType, boolean tellListener) {
        GameType beforeType = this.currentType;
        this.currentType = gameType;
        // If the gameType is the same, no need to update the textures
        if (gameType != beforeType) {
            update();
        }
        // If listener is not null, tell it
        if (tellListener && listener != null) {
            listener.tell(gameType);
        }
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;

        updateButtonPosition();
    }

    private void updateButtonPosition() {
        if (scenarioBtn != null)
            scenarioBtn.setPosition(x-scenarioBtn.getWidth(),y);

        if (endlessBtn != null)
            endlessBtn.setPosition(x,y);
    }

    public void setListener(Listener<GameType> listener) {
        this.listener = listener;
    }

    private void updateButtonSize() {
        if (scenarioBtn != null)
            scenarioBtn.setSize(buttonWidth, buttonHeight);

        if (endlessBtn != null)
            endlessBtn.setSize(buttonWidth, buttonHeight);

        // Update their positions
        setPosition(x, y);
    }

    public void setScale(float scale) {
        buttonWidth = scale * defaultButtonWidth;
        buttonHeight = scale * defaultButtonHeight;

        updateButtonSize();
    }

    public float getButtonWidth() {
        return buttonWidth;
    }

    public float getButtonHeight() {
        return buttonHeight;
    }

    public GameType getCurrentType() {
        return currentType;
    }
}
