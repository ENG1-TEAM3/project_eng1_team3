package com.team3gdx.game.save;

import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.entity.Cook;
import com.team3gdx.game.util.GameMode;

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

    public ModeInfo gameMode;
    public ChefInfo[] chefs;
    public float money;
    public float timer;
    public Date createdAt;
}
