package com.undercooked.game.logic.tutorial;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.Entity;

public class TutorialHighlightEntityStep extends TutorialFollowEntityStep {
    protected Entity highlightEntity;
    protected Texture highlightTexture;

    public TutorialHighlightEntityStep(String text, float textSpeed, Entity highlightEntity) {
        super(text, textSpeed, highlightEntity);
        this.highlightEntity = highlightEntity;
    }

    @Override
    public void load(TextureManager textureManager, String textureGroup) {
        // Load the select box sprite
        textureManager.load(textureGroup, "interactions/select_box.png");
    }

    @Override
    public void postLoad(TextureManager textureManager) {
        super.postLoad(textureManager);
        highlightTexture = textureManager.get("interactions/select_box.png");
    }

    @Override
    public void render(SpriteBatch batch, Entity entity) {
        super.render(batch, entity);
        // If it's not the highlight entity, ignore
        if (entity != highlightEntity) return;

        if (highlightTexture == null) return;
        // Render the highlight texture over the entity's sprite
        Sprite entitySprite = highlightEntity.getSprite();
        batch.setColor(Color.PINK);
        batch.draw(highlightTexture, entity.getX(), entity.getY(), entitySprite.getWidth(), entitySprite.getHeight());
        batch.setColor(Color.WHITE);
    }
}