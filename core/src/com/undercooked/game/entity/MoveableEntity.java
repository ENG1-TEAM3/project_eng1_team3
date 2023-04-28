package com.undercooked.game.entity;

import com.undercooked.game.map.Map;
import com.undercooked.game.map.MapManager;

public class MoveableEntity extends Entity {
    public float speed;

    public float dirX = 0;
    public float dirY = 0;
    protected boolean collidedX = false;
    protected boolean collidedY = false;

    public float moveCalc(float dir) {
        return dir * MapManager.gridToPos(speed);
    }

    public float moveCalc(float dir, float delta) {
        return moveCalc(dir) * delta;
    }

    public void move(float delta) {
        move(dirX, dirY, delta);
    }

    public void move(float x, float y, float delta) {
        pos.x += moveCalc(x, delta);
        pos.y += moveCalc(y, delta);
    }

    public void move(float x, float y) {
        pos.x += moveCalc(x);
        pos.y += moveCalc(y);
    }

    public void moveAndCollide(Map map, float delta) {
        moveAndCollide(map, dirX, dirY, delta);
    }
    public void moveAndCollide(Map map, float x, float y, float delta) {

        collidedX = false;
        collidedY = false;

        float moveX = Math.signum(x) * collision.width;
        float moveY = Math.signum(y) * collision.height;

        boolean xPositive = Math.signum(x) >= 0;
        boolean yPositive = Math.signum(y) >= 0;

        x = moveCalc(x, delta);
        y = moveCalc(y, delta);

        boolean xFinished = (x == 0);
        boolean yFinished = (y == 0);

        // Repeat until x and y are finished
        while (!xFinished || !yFinished) {

            // Change moveX and moveY depending on distance
            float nextX = x - moveX;
            if ((nextX <= 0 && xPositive) || (nextX >= 0 && !xPositive)) {
                moveX = x;
                nextX = 0;
            }
            float nextY = y - moveY;
            if ((nextY <= 0 && yPositive) || (nextY >= 0 && !yPositive)) {
                moveY = y;
                nextY = 0;
            }

            // Check collision
            // X
            if (!xFinished && map.checkCollision(this, collision.x + moveX, collision.y)) {
                float moveDist = 0.01F * Math.signum(moveX);
                // Move the player as close as possible on the x
                while (!map.checkCollision(this, collision.x + moveDist, collision.y)) {
                    collision.x += moveDist;
                }
                collision.x -= moveDist;
                pos.x = collision.x - offsetX;
                collidedX = true;
                moveX = 0;
            }

            // Y
            if (!yFinished && map.checkCollision(this, collision.x, collision.y + moveY)) {
                float moveDist = 0.01F * Math.signum(moveY);
                // Move the player as close as possible on the y
                while (!map.checkCollision(this, collision.x, collision.y + moveDist)) {
                    collision.y += moveDist;
                }
                collision.y -= moveDist;
                collidedY = true;
                moveY = 0;
            }

            // And then move
            collision.x += moveX;
            collision.y += moveY;

            // Update x and y, if the move distance isn't 0
            if (moveX != 0) x = nextX;
            if (moveY != 0) y = nextY;

            // Update xFinished and yFinished
            xFinished = (moveX==0);
            yFinished = (moveY==0);
        }
        pos.x = collision.x - offsetX;
        pos.y = collision.y - offsetY;
    }
}
