package com.undercooked.game.station;

public class StationData {
    String path, texturePath, defaultBase;
    int width, height;

    public StationData(String path, String texturePath, String defaultBase, int width, int height) {
        this.path = path;
        this.texturePath = texturePath;
        this.width = width;
        this.height = height;
        this.defaultBase = defaultBase;
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
}