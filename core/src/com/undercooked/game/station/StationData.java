package com.undercooked.game.station;

public class StationData {
    private String path, texturePath,
            defaultBase, floorTile;
    private final String id;
    private int width, height;
    private int holds;
    private int price;
    private float collisionWidth, collisionHeight,
          collisionOffsetX, collisionOffsetY;
    private boolean hasCollision;

    public StationData(String path, String texturePath, String defaultBase,
                       String id,
                       int width, int height,
                       float collisionWidth, float collisionHeight,
                       float collisionOffsetX, float collisionOffsetY,
                       boolean hasCollision) {
        this(id);
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

    public StationData(String id) {
        this.id = id;
        this.holds = 0;
        this.price = 0;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }

    public void setDefaultBase(String defaultBase) {
        this.defaultBase = defaultBase;
    }

    public void setFloorTile(String floorTile) {
        this.floorTile = floorTile;
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

    public void setHoldCount(int holds) {
        this.holds = Math.max(0,holds);
    }

    public void setPrice(int price) {
        this.price = Math.max(0,price);
    }

    public String getPath() {
        return path;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public String getDefaultBase() {
        return defaultBase;
    }

    public String getFloorTile() {
        return floorTile;
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

    public int getHoldCount() {
        return holds;
    }

    public int getPrice() {
        return price;
    }

    public String getID() {
        return id;
    }
}