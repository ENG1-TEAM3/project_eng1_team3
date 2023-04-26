package com.team3gdx.game.PowerUp;

import com.badlogic.gdx.utils.Array;
import com.team3gdx.game.util.GameMode;

public class PowerUpService {
    private float timer;
    private final Array<PowerUp> activePowerUps = new Array<>();

    public PowerUpService() {

    }

    private boolean checkTimer(float delta) {
        timer += delta;

        float interval = 5000;

        if (timer < interval) {
            return false;
        }

        timer -= interval;

        return true;
    }

    public void render(float delta) {

        for (PowerUp powerup : activePowerUps) {
            powerup.addTime(delta);

            if (powerup.isComplete()) {
                activePowerUps.removeValue(powerup, true);
            }
        }

        if (!checkTimer(delta)) {
            return;
        }


    }



}
