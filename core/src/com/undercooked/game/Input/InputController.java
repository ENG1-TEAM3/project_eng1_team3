package com.undercooked.game.Input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class InputController {

    /** Helper class to store keys better in the inputs map. */
    private static class InputKey {
        static Array<Integer> keys;
        static boolean keyPressed, keyJustPressed, keyReleased, keyJustReleased;

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

        public static int size() {
            return keys.size;
        }

        public static int getKey(int index) {
            return keys.get(index);
        }

        public static void addKey(int key) {
            // Don't add the key if it's already there
            if (keys.contains(key,true)) {
                return;
            }
            // Add the key
            keys.add(key);
        }

        public static boolean isPressed() {
            return keyPressed;
        }

        public static boolean isJustPressed() {
            return keyJustPressed;
        }

        public static boolean isReleased() {
            return keyReleased;
        }

        public static boolean isJustReleased() {
            return keyJustReleased;
        }
    }

    static final ObjectMap<String, InputKey> inputs = new ObjectMap<>();

    /**
     * Function used for Saving the inputs.
     *
     * @return The {@link ObjectMap} of the inputs.
     */
    public static ObjectMap getInputs() {
        return inputs;
    }


    public static void addKey(String keyID, int newKey) {
        // First check that the key actually exists in the inputs map
        if (!inputs.containsKey(keyID)) {
            // If it doesn't, add it.
            inputs.put(keyID,new InputKey());
        }
        // Add the key
        inputs.get(keyID).addKey(newKey);
    }

    public static void setKey(Key key, int newKey) {

    }

    public static void setToDefaultKeys() {
        inputs.clear();
        for (Key key : Key.values()) {
            addKey(key.id, key.key);
        }
    }

    public static ObjectMap defaultKeyMap() {
        ObjectMap<String, InputKey> output = new ObjectMap<>();
        for (Key key : Key.values()) {
            // First check that the key actually exists in the inputs map
            if (!output.containsKey(key.id)) {
                // If it doesn't, add it.
                output.put(key.id,new InputKey());
            }
            // Add the key
            output.get(key.id).addKey(key.key);
        }
        return output;
    }

    public static boolean isKeyPressed(String keyID) {
        if (!keyExists(keyID)) return false;

        return inputs.get(keyID).isPressed();
    }

    public static boolean isKeyJustPressed(String keyID) {
        if (!keyExists(keyID)) return false;

        return inputs.get(keyID).isJustPressed();
    }

    public static boolean keyExists(String keyID) {
        return inputs.containsKey(keyID);
    }

    public static String keyID(Key key) {
        return key.id;
    }

}

enum Key {
    COOK_UP("c_up", Input.Keys.W),
    COOK_LEFT("c_left", Input.Keys.A),
    COOK_DOWN("c_down", Input.Keys.S),
    COOK_RIGHT("c_right", Input.Keys.D);
    final String id;
    final int key;
    Key(String id, int key) {
        this.id = id;
        this.key = key;
    }
}
