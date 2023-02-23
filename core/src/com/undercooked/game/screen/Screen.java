package com.undercooked.game.screen;

public abstract class Screen implements com.badlogic.gdx.Screen {

    /**
     * Using an int, the screen can avoid being unloaded if it's opened
     * multiple times in the ScreenController Stack.
     */
    private int loaded = 0;

    public int getLoaded() {
        return loaded;
    }

    public void changeLoaded(boolean negate) {
        this.loaded += negate ? -1 : 1;
    }

    public void resetLoaded() {
        this.loaded = 0;
    }

    public boolean isLoaded() {
        return loaded > 0;
    }

    public void load() {}
    public void unload() {}
    public void renderScreen() { }

}
