package com.team3gdx.game.PowerUp;

import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class PowerUpService {
    private float timer;
    private final Array<PowerUp> activePowerUps = new Array<>();
    private final Random random = new Random();

    public PowerUpService() {

    }

    private boolean checkTimer(float delta) {
        timer += delta;

        float interval = 30;

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

        switch (random.nextInt(0, 6)) {
            case 1:
                activePowerUps.add(PowerUps.constructionCostReduce());
                break;
            case 2:
                activePowerUps.add(PowerUps.cookingSpeedReduce());
                break;
            case 3:
                activePowerUps.add(PowerUps.customerTimeIncrease());
                break;
            case 4:
                activePowerUps.add(PowerUps.increasePay());
                break;
            case 5:
                activePowerUps.add(PowerUps.speedBoost());
                break;
        }
    }

    public Array<PowerUp> getActivePowerUps() {
        return activePowerUps;
    }

}
