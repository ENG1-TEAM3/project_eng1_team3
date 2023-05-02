package com.team3gdx.game.PowerUp;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.team3gdx.game.util.Control;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PowerUpService {
    private final TiledMap map;
    private final Control control;
    private float timer;
    private final Array<PowerUp> activePowerUps = new Array<>();
    private final Map<Vector2, PowerUp> spawnedPowerUps = new HashMap<>();
    private final Random random = new Random();

    public void addActivePowerUp(PowerUp powerUp) {
        activePowerUps.add(powerUp);
    }

    public void addSpawnedPowerUp(PowerUp powerUp) {
        spawnedPowerUps.put(powerUp.pos, powerUp);
    }

    public Array<PowerUp> getActivePowerUps() {
        return activePowerUps;
    }

    public Array<PowerUp> getSpawnedPowerUps () {
        return new Array<>(spawnedPowerUps.values().toArray(new PowerUp[0]));
    }
    public Array<Vector2> getSpawnedPowerUpsPos () {
        return new Array<>(spawnedPowerUps.keySet().toArray(new Vector2[0]));
    }

    public PowerUpService(TiledMap map, Control control) {

        this.map = map;
        this.control = control;
    }

    public boolean checkTimer(float delta) {
        timer += delta;

        float interval = 30;

        if (timer < interval) {
            return false;
        }

        timer -= interval;

        return true;
    }

    public void spawnPowerUps(float delta) {

        for (PowerUp powerup : activePowerUps) {
            powerup.addTime(delta);

            if (powerup.isComplete()) {
                activePowerUps.removeValue(powerup, true);
            }
        }

        if (!checkTimer(delta)) {
            return;
        }

        switch (random.nextInt(6)) {
            case 1:
                spawnPowerUp(PowerUps.constructionCostReduce());
                break;
            case 2:
                spawnPowerUp(PowerUps.cookingSpeedReduce());
                break;
            case 3:
                spawnPowerUp(PowerUps.customerTimeIncrease());
                break;
            case 4:
                spawnPowerUp(PowerUps.increasePay());
                break;
            case 5:
                spawnPowerUp(PowerUps.speedBoost());
                break;
        }
    }

    public void spawnPowerUp(PowerUp powerUp) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);

        int x, y;
        Vector2 pos = new Vector2();
        MapProperties mapProperties;

        do {
            x = random.nextInt(13) + 3;
            y = random.nextInt(16);

            pos.x = x;
            pos.y = y;

            TiledMapTileLayer.Cell cell = layer.getCell(x, y);

            if (cell == null) {
                break;
            }

            mapProperties = cell.getTile().getProperties();

        } while (mapProperties != null && (mapProperties.get("Station") != null || spawnedPowerUps.containsKey(pos)));

        powerUp.pos = new Vector2(pos.x * 64, pos.y * 64);
        spawnedPowerUps.put(pos, powerUp);
    }

    public boolean interact(int x, int y) {
        Vector2 pos = new Vector2(x, y);

        if (!spawnedPowerUps.containsKey(pos)) {
            return false;
        }

        PowerUp powerUp = spawnedPowerUps.get(pos);

        if (control.interact) {
            activePowerUps.add(powerUp);
            spawnedPowerUps.remove(pos);
        }

        return true;
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
