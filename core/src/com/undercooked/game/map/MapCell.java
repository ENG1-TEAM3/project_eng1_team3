package com.undercooked.game.map;

public class MapCell {
    boolean collidable;
    boolean interactable;
    MapEntity mapEntity;

    public MapCell(boolean collidable, boolean interactable) {
        this.collidable = collidable;
        this.interactable = interactable;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public boolean isInteractable() {
        return interactable;
    }

    public MapEntity getMapEntity() {
        return mapEntity;
    }
}
