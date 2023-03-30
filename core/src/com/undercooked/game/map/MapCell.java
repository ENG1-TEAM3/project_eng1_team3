package com.undercooked.game.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.undercooked.game.assets.TextureManager;

public class MapCell {
    boolean collidable;
    boolean interactable;
    private int x, y;
    private float displayX, displayY;
    float width;
    float height;
    boolean base;
    String belowTilePath;
    /** The floor tile to draw underneath. */
    Texture belowTile;
    MapEntity mapEntity;

    public MapCell(boolean collidable, boolean interactable, boolean base) {
        this.collidable = collidable;
        this.interactable = interactable;
        this.base = base;
        this.x = 0;
        this.y = 0;
        this.width = 64;
        this.height = 64;
    }

    public MapCell() {
        this(false, false, false);
    }


    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    public void setInteractable(boolean interactable) {
        this.interactable = interactable;
    }

    public void setBase(boolean base) {
        this.base = base;
    }

    public void setMapEntity(MapEntity mapEntity) {
        this.mapEntity = mapEntity;
        if (mapEntity != null) {
            mapEntity.setX(this.displayX);
            mapEntity.setY(this.displayY);
            mapEntity.setWidth(this.width);
            mapEntity.setHeight(this.height);
        }
    }

    public void setX(int x) {
        this.x = x;
        setDisplayX(MapManager.gridToPos(x));
    }

    protected void setDisplayX(float x) {
        if (this.mapEntity != null) {
            mapEntity.setX(x);
        }
        this.displayX = x;
    }

    public void setY(int y) {
        this.y = y;
        setDisplayY(MapManager.gridToPos(y));
    }

    protected void setDisplayY(float y) {
        if (this.mapEntity != null) {
            mapEntity.setY(y);
        }
        this.displayY = y;
    }

    public void setWidth(int width) {
        float newWidth = MapManager.gridToPos(width);
        if (this.mapEntity != null) {
            mapEntity.setWidth(width);
        }
        this.width = newWidth;
    }

    public void setHeight(int height) {
        float newHeight = MapManager.gridToPos(height);
        if (this.mapEntity != null) {
            mapEntity.setHeight(height);
        }
        this.height = newHeight;
    }

    public int getX() {
        return x;
    }

    public float getDisplayX() {
        return displayX;
    }

    public int getY() {
        return y;
    }

    public float getDisplayY() {
        return displayY;
    }

    public void updateBelowTile(TextureManager textureManager) {
        if (belowTilePath == null) {
            this.belowTile = null;
            return;
        }
        this.belowTile = textureManager.getAsset(belowTilePath);
    }

    public void setBelowTile(String texturePath) {
        this.belowTilePath = texturePath;
    }

    public void load(TextureManager textureManager, String textureID) {
        // Load the floor tile
        loadFloor(textureManager, textureID);
        // And then load the map entity
        mapEntity.load(textureManager, textureID);
    }

    public void loadFloor(TextureManager textureManager, String textureID) {
        textureManager.loadAsset(textureID, belowTilePath, "textures");
    }

    public void unload(TextureManager textureManager) {
        // Unload the floor tile
        textureManager.unloadTexture(belowTilePath);
        // And unload the map entity
        mapEntity.unload(textureManager);
    }

    public boolean isCollidable() {
        return collidable;
    }

    public boolean isInteractable() {
        return interactable;
    }

    public boolean isBase() {
        return base;
    }

    public MapEntity getMapEntity() {
        return mapEntity;
    }

    public void drawBelow(SpriteBatch batch) {
        if (belowTile != null) {
            batch.draw(belowTile, displayX, displayY, width, height);
        }
    }

    public void draw(SpriteBatch batch) {
        drawBelow(batch);
        mapEntity.draw(batch);
    }

    public Rectangle getCollision() {
        if (mapEntity == null) {
            return new Rectangle(x,y, 0, 0);
        }
        return mapEntity.collision;
    }
}