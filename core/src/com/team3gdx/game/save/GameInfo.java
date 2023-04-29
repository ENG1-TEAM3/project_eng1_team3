package com.team3gdx.game.save;

import java.util.Date;
import java.util.List;

public class GameInfo {
    public GameInfo() {}

    public GameInfo(float money, float timer, ChefInfo[] chefs) {

        this.money = money;
        this.timer = timer;
        this.chefs = chefs;
        this.createdAt = new Date();
    }

    public ChefInfo[] chefs;
    public float money;
    public float timer;
    public Date createdAt;
}
