package com.undercooked.game.logic;

import com.undercooked.game.entity.Cook;
import com.undercooked.game.screen.GameScreen;

public class Scenario extends GameLogic {

    int cookCount;

    public Scenario(GameScreen game) {
        super(game);
        cookCount = 1;
    }

    @Override
    public void update() {

    }

    @Override
    public void preLoad() {
        super.preLoad();
    }

    @Override
    public void load() {
        // Load cookCount number of cooks.
        //addCook(new Cook());
    }

    @Override
    public void unload() {
        ingredients.unload(game.getTextureManager());
    }

    public void addIngredient() {
       ingredients.load(game.getTextureManager());
    }

    public void setCookCount(int cookCount) {
        this.cookCount = cookCount;
    }
}
