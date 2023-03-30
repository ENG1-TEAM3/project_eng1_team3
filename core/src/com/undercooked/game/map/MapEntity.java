package com.undercooked.game.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.undercooked.game.Input.InputType;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.entity.Entity;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.InteractionStep;

public class MapEntity extends Entity {

    private int width;
    private int height;
    Rectangle interactBox;
    String basePath;
    protected String id;

    public MapEntity() {
        this.interactBox = new Rectangle();
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        interactBox.x = x;
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        interactBox.y = y;
    }

    public void setWidth(int width) {
        this.width = Math.max(1,width);
        this.interactBox.width = Math.max(MapManager.gridToPos(1), MapManager.gridToPos(width));
        this.collision.width = interactBox.width;
        // this.sprite.setSize(width*MapManager.gridToPos(height), this.sprite.getHeight());
    }

    public void setHeight(int height) {
        this.height = Math.max(1,height);
        this.interactBox.height = Math.max(MapManager.gridToPos(1),MapManager.gridToPos(height));
        this.collision.height = interactBox.height;
        // this.sprite.setSize(this.sprite.getWidth(), height*MapManager.gridToPos(height));
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public int getCellWidth() {
        return this.width;
    }

    public int getCellHeight() {
        return this.height;
    }

    public String getID() {
        return id;
    }

    public boolean isInteracting(Rectangle rect) {
        return interactBox.overlaps(rect);
    }

    public Rectangle getInteractBox() {
        return interactBox;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(sprite, pos.x, pos.y, interactBox.width, interactBox.height);
    }

    @Override
    public void drawDebug(ShapeRenderer shape) {
        super.drawDebug(shape);
        /*shape.setColor(Color.YELLOW);
        shape.rect(interactBox.x,interactBox.y,interactBox.width,interactBox.height);
        shape.setColor(Color.WHITE);*/
    }

    /**
     * Class to be Override by children
     * @param keyID {@link String} : The key's ID.
     * @param inputType {@link InputType} : The type of input of interaction.
     * @return {@link InteractResult} : The result of the {@link InteractionStep}.
     */
    public InteractResult interact(Cook cook, String keyID, InputType inputType) {
        return InteractResult.NONE;
    }
}
