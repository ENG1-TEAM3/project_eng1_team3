package com.team3gdx.game.save;

import com.team3gdx.game.PowerUp.PowerUp;

public class PowerUpInfo {
    public PowerUpInfo() {}

    public PowerUpInfo(PowerUp powerUp) {
        duration = powerUp.duration;
        name = powerUp.name;
        timeElapsed = powerUp.timeElapsed;
        x = powerUp.pos.x;
        y = powerUp.pos.y;
        width = powerUp.width;
        height = powerUp.height;
    }

    public float duration;
    public String name;
    public float timeElapsed;
    public float x;
    public float y;
    public float width;
    public float height;

}
