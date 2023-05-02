package com.team3gdx.game.save;

public class StationInfo {
    public StationInfo() {}

    public StationInfo(float x, float y, boolean active, StationType type) {

        this.x = x;
        this.y = y;
        this.active = active;
        this.type = type;
    }

    public float x;
    public float y;
    public boolean active;
    public StationType type;
}