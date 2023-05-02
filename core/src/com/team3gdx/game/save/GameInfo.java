package com.team3gdx.game.save;

import java.util.Date;

public class GameInfo {
    public GameInfo() {}

    public GameInfo(int money, long timer, int currentWave, int reputation, ModeInfo gameMode, ChefInfo[] chefs, CustomerInfo[] customers, StationInfo[] stations, PowerUpInfo[] activePowerUps, PowerUpInfo[] spawnedPowerUps) {

        this.money = money;
        this.timerOffset = timer;
        this.currentWave = currentWave;
        this.reputation = reputation;
        this.gameMode = gameMode;
        this.chefs = chefs;
        this.customers = customers;
        this.stations = stations;
        this.activePowerUps = activePowerUps;
        this.spawnedPowerUps = spawnedPowerUps;
        this.createdAt = new Date();
    }

    public ModeInfo gameMode;
    public ChefInfo[] chefs;
    public CustomerInfo[] customers;
    public StationInfo[] stations;
    public PowerUpInfo[] activePowerUps;
    public PowerUpInfo[] spawnedPowerUps;
    public int money;
    public long timerOffset;
    public int currentWave;
    public int reputation;
    public Date createdAt;
}
