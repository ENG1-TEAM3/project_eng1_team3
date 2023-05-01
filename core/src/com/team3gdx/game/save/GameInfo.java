package com.team3gdx.game.save;

import java.util.Date;

public class GameInfo {
    public GameInfo() {}

    public GameInfo(int money, float timer, int currentWave, int reputation, ModeInfo gameMode, ChefInfo[] chefs, CustomerInfo[] customers) {

        this.money = money;
        this.timer = timer;
        this.currentWave = currentWave;
        this.reputation = reputation;
        this.gameMode = gameMode;
        this.chefs = chefs;
        this.customers = customers;
        this.createdAt = new Date();
    }

    public ModeInfo gameMode;
    public ChefInfo[] chefs;
    public CustomerInfo[] customers;
    public int money;
    public float timer;
    public int currentWave;
    public int reputation;
    public Date createdAt;
}
