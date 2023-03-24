package com.undercooked.game.map;

import com.undercooked.game.entity.Entity;

public class MapEntity extends Entity {

    protected int width;
    protected int height;
    String basePath;

    public void setWidth(int width) {
        this.collision.width = Math.max(MapManager.gridToPos(1),MapManager.gridToPos(width));
        this.width = Math.max(1,width);
        // this.sprite.setSize(width*MapManager.gridToPos(height), this.sprite.getHeight());
    }

    public int getCellWidth() {
        return this.width;
    }

    public void setHeight(int height) {
        this.collision.height = Math.max(MapManager.gridToPos(1),MapManager.gridToPos(height));
        this.height = Math.max(1,height);
        // this.sprite.setSize(this.sprite.getWidth(), height*MapManager.gridToPos(height));
    }

    public int getCellHeight() {
        return this.height;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    /*public void draw(SpriteBatch batch) {
        sprite.setPosition(collision.x,collision.y);
        sprite.setSize(collision.width, collision.height);
        sprite.draw(MainGameClass.batch);
    }*/

}
