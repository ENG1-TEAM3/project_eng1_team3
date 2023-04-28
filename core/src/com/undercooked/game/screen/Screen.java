package com.undercooked.game.screen;

import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.map.MapManager;
import com.undercooked.game.assets.TextureManager;

/**
 * Abstract class to be extended from that allows for the {@link Screen}
 * to be loaded, unloaded and so on.
 */
public abstract class Screen implements com.badlogic.gdx.Screen {

    /**
     * Using an int, the screen can avoid being unloaded if it's opened
     * multiple times in the ScreenController Stack.
     */
    private int loaded = 0;

    /**
     * The {@link MainGameClass}.
     */
    protected final MainGameClass game;

    /**
     * Constructor for the {@link Screen} that takes
     * the input of the {@link MainGameClass}.
     * @param game {@link MainGameClass} : The main game instance.
     */
    protected Screen(MainGameClass game) {
        this.game = game;
    }

    /**
     * Provides the {@link Screen} with the Screen that it was just loaded from.
     *
     * @param screen The {@link Screen} that opened this {@link Screen}.
     */
    public void fromScreen(Screen screen) { }

    /**
     * @return {@code int} : The number of times the {@link Screen}
     *                       is currently loaded.
     */
    public final int getLoaded() {
        return loaded;
    }

    /**
     * Changes the {@link #loaded} variable by +1 or -1.
     * @param negate {@code boolean} : {@code true} = -1, {@code false} = +1
     */
    public final void changeLoaded(boolean negate) {
        this.loaded += negate ? -1 : 1;
    }

    /**
     * Resets the {@link #loaded} variable to 0.
     */
    public final void resetLoaded() {
        this.loaded = 0;
    }

    /**
     * @return {@code boolean} : If the page is {@code true}
     *                           or not {@code false} loaded
     */
    public final boolean isLoaded() {
        return loaded > 0;
    }

    /**
     * Used for rendering the {@link Screen}. Should be called manually
     * in the {@link #render(float)} function, but by using this function
     * the screen can still be drawn while the {@link LoadScreen} is being used.
     * @param delta {@code float} : The time since the last frame.
     */
    public void renderScreen(float delta) { }

    /**
     * @return {@link AudioManager} : The game's {@link AudioManager}.
     */
    public final AudioManager getAudioManager() {
        return game.audioManager;
    }

    /**
     * @return {@link TextureManager} : The game's {@link TextureManager}.
     */
    public final TextureManager getTextureManager() {
        return game.textureManager;
    }

    /**
     * @return {@link ScreenController} : The game's {@link ScreenController}.
     */
    public final ScreenController getScreenController() {
        return game.screenController;
    }

    /**
     * @return {@link MapManager} : The game's {@link MapManager}.
     */
    public final MapManager getMapManager() {
        return game.mapManager;
    }

    /**
     * Function called before the {@link Screen} starts to load.
     */
    public void preLoad() { }

    /**
     * Should prepare all the {@link Screen}'s assets to be
     * loaded during the {@link LoadScreen}.
     * <br>Actual use of the loaded resources should be in the
     * {@link #postLoad()} function.
     */
    public abstract void load();

    /**
     * Function called after the {@link Screen} has loaded.
     */
    public void postLoad() { }

    /**
     * Should unload all of the {@link Screen}'s assets that
     * were loaded during the {@link #load()}.
     */
    public abstract void unload();
}
