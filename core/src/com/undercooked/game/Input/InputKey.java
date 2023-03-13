package com.undercooked.game.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

/** Helper class to store keys better in the inputs map. */
public class InputKey {
    Array<Integer> keys;
    boolean keyPressed, keyJustPressed, keyReleased, keyJustReleased;

    public InputKey() {
        keys = new Array<>();
    }

    public InputKey(int key) {
        this();
        keys.add(key);
    }

    public InputKey(Array<Integer> keys) {
        this();
        for (int key : keys) {
            this.keys.add(key);
        }
    }

    public int size() {
        return keys.size;
    }

    public int getKey(int index) {
        return keys.get(index);
    }

    public void addKey(int key) {
        // Don't add the key if it's already there
        if (keys.contains(key,true)) {
            return;
        }
        // Add the key
        keys.add(key);
    }

    public void clear() {
        keys.clear();
    }

    public boolean isPressed() {
        return keyPressed;
    }

    public boolean isJustPressed() {
        return keyJustPressed;
    }

    public boolean isReleased() {
        return keyReleased;
    }

    public boolean isJustReleased() {
        return keyJustReleased;
    }

    public void resetKeys() {
        keyPressed = false;
        keyJustPressed = false;
        keyReleased = false;
        keyJustReleased = false;
    }

    public void update() {
        boolean beforePressed = keyPressed,
                beforeJustPressed = keyJustPressed;
        resetKeys();
        for (int key : keys) {
            keyPressed = keyPressed || Gdx.input.isKeyPressed(key);
            keyJustPressed = keyJustPressed || Gdx.input.isKeyJustPressed(key);
        }
        // Check for released
        if (!keyPressed) {
            keyReleased = true;
            // Only just pressed if pressed before
            if (beforePressed) {
                keyJustReleased = true;
            }
        }
    }
}