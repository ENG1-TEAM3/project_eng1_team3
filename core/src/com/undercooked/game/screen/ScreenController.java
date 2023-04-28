package com.undercooked.game.screen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.MainGameClass;

public class ScreenController {

    LoadScreen loadScreen;
    ObjectMap<String, Screen> screens;
    MainGameClass game;
    Array<Screen> screenStack;

    public ScreenController(MainGameClass game, AssetManager assetManager) {
        this.screens = new ObjectMap<>();
        this.loadScreen = new LoadScreen(assetManager, game);
        this.game = game;
        this.screenStack = new Array<>();
    }

    private void startLoading(Screen lastScreen, Screen nextScreen) {
        nextScreen.preLoad();
        loadScreen.setScreens(lastScreen, nextScreen);
        loadScreen.start(game);
    }

    public void backScreen() {
        // If there are no screens beyond the current, then go to the MissingScreen
        if (screenStack.size <= 1) {
            setScreen(new MissingScreen(game));
            return;
        }
        // Get the current screen.
        Screen current = screenStack.pop();
        // Unload the current Screen
        unload(current);
        // Move to the previous Screen (it will already be loaded)
        game.setScreen(screenStack.peek());
    }

    public void setScreen(String ID) {
        // First make sure that the screen ID exists
        if (!screens.containsKey(ID)) {
            // If it doesn't throw an error.
            throw new RuntimeException(String.format("Screen with ID %s does not exist.",ID));
        }
        setScreen(screens.get(ID));
    }

    public void setScreen(Screen screen) {
        // First, make sure that the screenStack is unloaded
        unloadStack();

        // When it's all unloaded, then load the new screen
        // It won't be loaded before this point, so no need to check.
        screen.load();
        screen.changeLoaded(false);

        // Then open the load screen
        startLoading(game.getScreen(), screen);

        // Add it to the screen stack
        screenStack.add(screen);
    }

    public void nextScreen(String ID) {
        // First make sure that the screen ID exists
        if (!screens.containsKey(ID)) {
            // If it doesn't throw an error.
            throw new RuntimeException(String.format("Screen with ID %s does not exist.",ID));
        }
        nextScreen(screens.get(ID));
    }

    public void nextScreen(Screen screen) {
        // Load the screen, if it isn't already.
        screen.load();
        screen.changeLoaded(false);

        // Then start loading
        startLoading(game.getScreen(), screen);

        // Add it to the screen stack
        screenStack.add(screen);
    }

    public void addScreen(Screen screen, String ID) {
        // If the ID is already in there, don't do anything.
        if (screens.containsKey(ID)) {
            return;
        }
        // If it doesn't already exist, add it.
        setScreen(screen, ID);
    }

    public void unload(Screen screen) {
        // If it matches, lower loaded by 1
        screen.changeLoaded(true);
        // If it's no longer loaded, unload it
        if (!screen.isLoaded()) {
            screen.unload();
        }
    }

    public void unloadStack() {
        // If the stack is empty, just return
        if (screenStack.size == 0) return;

        // Loop through the screen stack and unload the screens.
        while (screenStack.size > 0) {
            Screen thisScreen = screenStack.pop();

            // Only unload it if it's loaded.
            if (thisScreen.isLoaded()) {
                thisScreen.unload();
                thisScreen.resetLoaded();
            }
        }
        // Clear the stack
        screenStack.clear();
    }

    public void setScreen(Screen screen, String ID) {
        screens.put(ID, screen);
    }

    public Screen getScreen(String ID) {
        return screens.get(ID);
    }

    public void removeScreen(String ID) {
        screens.remove(ID);
    }

    public boolean onScreen(String ID) {
        if (!screens.containsKey(ID)) {
            return false;
        }
        return screenStack.peek() == screens.get(ID);
    }

    public boolean onScreen(Screen screen) {
        return screenStack.peek() == screen;
    }
}
