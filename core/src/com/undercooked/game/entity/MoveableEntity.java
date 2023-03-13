package com.undercooked.game.entity;

import com.undercooked.game.map.MapManager;

public class MoveableEntity extends Entity {
    public float speed;

    float dirX = 0;
    float dirY = 0;

    public float moveCalc(float dir, float delta) {
        return dir * MapManager.gridToPos(speed) * delta;
    }

    public void move (float delta) {
        pos.x += moveCalc(dirX, delta);
        pos.y += moveCalc(dirY, delta);
    }
}
