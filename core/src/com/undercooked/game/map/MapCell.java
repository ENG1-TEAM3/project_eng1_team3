package com.undercooked.game.map;

public class MapCell {
    boolean collidable;
    boolean interactable;
    boolean base;
    /** If there was / is a base that was placed b. */
    boolean baseUnder;
    MapEntity mapEntity;

    public MapCell(boolean collidable, boolean interactable, boolean base) {
        this.collidable = collidable;
        this.interactable = interactable;
        this.base = base;
    }


    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    public void setInteractable(boolean interactable) {
        this.interactable = interactable;
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
}