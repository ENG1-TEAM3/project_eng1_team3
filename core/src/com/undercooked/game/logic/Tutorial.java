package com.undercooked.game.logic;

import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.screen.GameScreen;

public class Tutorial extends GameLogic {

    public Tutorial(GameScreen game, TextureManager textureManager, AudioManager audioManager) {
        super(game, textureManager, audioManager);
    }

    public Tutorial() {
        this(null, null, null);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }
}
