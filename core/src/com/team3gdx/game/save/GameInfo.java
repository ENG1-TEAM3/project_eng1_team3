package com.team3gdx.game.save;

import java.util.Date;
import java.util.List;

public class GameInfo {
    public GameInfo() {}

    public GameInfo(float money, float timer, ModeInfo gameMode, ChefInfo[] chefs) {

        this.money = money;
        this.timer = timer;
        this.gameMode = gameMode;
        this.chefs = chefs;
        this.createdAt = new Date();
    }

    private ModeInfo gameMode;
    public ChefInfo[] chefs;
    public float money;
    public float timer;
    public Date createdAt;
}
