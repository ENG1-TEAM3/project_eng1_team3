package com.undercooked.game.entity;

import com.undercooked.game.map.MapManager;

public class MoveableEntity extends Entity {
    public float speed;

    public float dirX = 0;
    public float dirY = 0;

    public float moveCalc(float dir, float delta) {
        return dir * MapManager.gridToPos(speed) * delta;
    }

    public void move(float delta) {
        move(dirX, dirY, delta);
    }

    public void move(float x, float y, float delta) {
        pos.x += moveCalc(x, delta);
        pos.y += moveCalc(y, delta);
    }
}
