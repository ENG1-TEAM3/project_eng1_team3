package com.undercooked.game.map;

import com.undercooked.game.entity.Entity;

public class MapEntity extends Entity {

    public int width;
    public int height;

    public void setWidth(int width) {
        this.width = Math.max(1,width);
    }

    public void setHeight(int height) {
        this.height = Math.max(1,height);
    }

}
