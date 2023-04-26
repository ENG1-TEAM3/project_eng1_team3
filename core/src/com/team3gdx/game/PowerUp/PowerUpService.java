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

        activePowerUps.add(PowerUps.cookingSpeedReduce());

        switch (random.nextInt(6, 7)) {
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

    public float totalConstructionCost(float initial) {
        for (PowerUp powerup : activePowerUps) {
            if (powerup.name == "construction_cost_reduce") {
                initial *= 0.85;
            }
        }

        return initial;
    }

    public float totalCookingSpeed(float initial) {
        for (PowerUp powerup : activePowerUps) {
            if (powerup.name == "cooking_speed_reduce") {
                initial *= 1.5;
            }
        }

        return initial;
    }

    public float getPriceMultiplier() {
        float multiplier = 1;

        for (PowerUp powerup : activePowerUps) {
            if (powerup.name == "Increase_pay") {
                multiplier *= 2;
            }
        }

        return multiplier;
    }

    public float totalSpeed(float initial) {
        for (PowerUp powerup : activePowerUps) {
            if (powerup.name == "speed_boost") {
                initial *= 2;
            }
        }

        return initial;
    }
}
