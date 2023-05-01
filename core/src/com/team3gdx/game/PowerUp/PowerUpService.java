package com.team3gdx.game.PowerUp;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class PowerUpService {
    private final TiledMap map;
    private float timer;
    private final Array<PowerUp> activePowerUps = new Array<>();
    private final Random random = new Random();

    public PowerUpService(TiledMap map) {

        this.map = map;
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

        switch (random.nextInt(50)) {
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

    private void spawnPowerUp(PowerUp powerUp) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);

        int x, y;
        MapProperties mapProperties;

        do {
            x = random.nextInt(layer.getWidth());
            y = random.nextInt(layer.getHeight());
            mapProperties = layer.getCell(x, y).getTile().getProperties();
        } while (mapProperties.get("Station") != null || mapProperties.get("PowerUp") != null);

        mapProperties.put("PowerUp", powerUp);
    }

    public void interact(int x, int y) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);

        Object tile = layer.getCell(x, y).getTile().getProperties().get("PowerUp");

        if (tile == null) {
            return;
        }

        PowerUp powerUp = (PowerUp) tile;

        // draw take text
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
