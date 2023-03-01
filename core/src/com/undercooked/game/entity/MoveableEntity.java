package com.undercooked.game.entity;

public class MoveableEntity extends Entity {
    public float speed;

    float dirX = 0;
    float dirY = 0;

    public void move (float delta) {
        pos.x += dirX * speed * delta;
        pos.y += dirY * speed * delta;
    }
}
