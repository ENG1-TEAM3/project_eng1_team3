package com.undercooked.game.screen;

import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.map.MapManager;
import com.undercooked.game.assets.TextureManager;

public abstract class Screen implements IScreen {

    /**
     * Using an int, the screen can avoid being unloaded if it's opened
     * multiple times in the ScreenController Stack.
     */
    private int loaded = 0;
    protected final MainGameClass game;

    protected Screen(MainGameClass game) {
        this.game = game;
    }

    /**
     * Function called before the {@link Screen} starts to load.
     */
    public void preLoad() { }

    /**
     * Function called after the {@link Screen} has loaded.
     */
    public void postLoad() { }

    /**
     * Provides the {@link Screen} with the Screen that it was just loaded from.
     *
     * @param screen The {@link Screen} that opened this {@link Screen}.
     */
    public void fromScreen(Screen screen) { }

    public final int getLoaded() {
        return loaded;
    }

    public final void changeLoaded(boolean negate) {
        this.loaded += negate ? -1 : 1;
    }

    public final void resetLoaded() {
        this.loaded = 0;
    }

    public final boolean isLoaded() {
        return loaded > 0;
    }
    public void renderScreen(float delta) { }

    public final AudioManager getAudioManager() {
        return game.audioManager;
    }

    public final TextureManager getTextureManager() {
        return game.textureManager;
    }

    public final ScreenController getScreenController() {
        return game.screenController;
    }

    public final MapManager getMapManager() {
        return game.mapManager;
    }
}
