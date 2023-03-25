package com.undercooked.game.station;

public class StationData {
    String path, texturePath, defaultBase;
    int width, height;
    float collisionWidth, collisionHeight,
          collisionOffsetX, collisionOffsetY;
    boolean hasCollision;

    public StationData(String path, String texturePath, String defaultBase,
                       int width, int height,
                       float collisionWidth, float collisionHeight,
                       float collisionOffsetX, float collisionOffsetY,
                       boolean hasCollision) {
        this.path = path;
        this.texturePath = texturePath;
        this.width = width;
        this.height = height;
        this.collisionWidth = collisionWidth;
        this.collisionHeight = collisionHeight;
        this.collisionOffsetX = collisionOffsetX;
        this.collisionOffsetY = collisionOffsetY;
        this.defaultBase = defaultBase;
        this.hasCollision = hasCollision;
    }

    public StationData() { }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }

    public void setDefaultBase(String defaultBase) {
        this.defaultBase = defaultBase;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setCollisionWidth(float collisionWidth) {
        this.collisionWidth = collisionWidth;
    }

    public void setCollisionHeight(float collisionHeight) {
        this.collisionHeight = collisionHeight;
    }

    public void setCollisionOffsetX(float collisionOffsetX) {
        this.collisionOffsetX = collisionOffsetX;
    }

    public void setCollisionOffsetY(float collisionOffsetY) {
        this.collisionOffsetY = collisionOffsetY;
    }

    public void setCollidable(boolean hasCollision) {
        this.hasCollision = hasCollision;
    }

    public String getPath() {
        return path;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getCollisionWidth() {
        return collisionWidth;
    }

    public float getCollisionHeight() {
        return collisionHeight;
    }

    public float getCollisionOffsetX() {
        return collisionOffsetX;
    }

    public float getCollisionOffsetY() {
        return collisionOffsetY;
    }

    public boolean isCollidable() {
        return hasCollision;
    }
}