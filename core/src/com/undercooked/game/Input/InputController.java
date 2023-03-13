package com.undercooked.game.Input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.util.StringUtil;

public class InputController {

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

    public static void setKey(String keyID, int newKey) {
        // First check that the key actually exists in the inputs map
        if (!inputs.containsKey(keyID)) {
            // If it doesn't, add it.
            inputs.put(keyID,new InputKey());
        }
        // Add the key
        inputs.get(keyID).addKey(newKey);
    }

    public static void updateKeys() {
        for (InputKey key : inputs.values()) {
            key.update();
        }
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

    public static int getKeyVal(String keyName) {
        // Convert to title case, as that is what LibGDX uses
        keyName = StringUtil.convertToTitleCase(keyName);
        // Return the value of the key name
        return Input.Keys.valueOf(keyName);
    }

    /**
     * Loads inputs from the controls.json.
     * <br><br>
     * If it does not exist in the data path, then it will copy from
     *  the defaults folder and then save to the data path.
     */
    public static void loadControls() {
        // Try to load the external json file
        JsonValue root = FileControl.loadJsonData("controls.json");
        // If the json hasn't loaded...
        if (root == null) {
            System.out.println("No controls.json exists. It will be created from the default.");
            // Load the json from the defaults folder internally instead, and save it to the data path
            root = FileControl.loadJsonFile("defaults", "controls.json", true);
            if (root == null) {
                throw new RuntimeException("Default controls file not found.");
            }
            // Save it to the data path
            FileControl.saveJsonData("controls.json", root);
        }

        //// Load the controls into the inputs ObjectMap
        // First iterate through all keys
        for (JsonValue key : root.iterator()) {
            // Check to make sure that the current key is an array
            if (key.type() != JsonValue.ValueType.array) {
                // If not, then continue to next
                continue;
            }

            String keyID = key.name();

            // If it is an array, then loop through the values inside
            for (JsonValue keyName : key.iterator()) {
                // Check to make sure the name is a String
                if (keyName.type() != JsonValue.ValueType.stringValue) {
                    // If not, then continue to next
                    continue;
                }

                // Get the value of the key
                int keyVal = getKeyVal(keyName.toString());

                // If it's -1, then it's invalid
                if (keyVal == -1) {
                    // If it's invalid, skip to next
                    continue;
                }

                // If it's valid, add it to the inputs map
                addKey(keyID, keyVal);
            }
        }
    }

}