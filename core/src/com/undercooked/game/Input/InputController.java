package com.undercooked.game.Input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.util.StringUtil;
import com.undercooked.game.util.json.*;

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
            // Defaults to interaction
            inputs.put(keyID,new InputKey());
        }
        // Add the key
        inputs.get(keyID).addKey(newKey);
    }

    public static void setInteraction(String keyID, boolean interaction) {
        // First check that the key actually exists in the inputs map
        if (!inputs.containsKey(keyID)) {
            // If it doesn't, add it.
            inputs.put(keyID,new InputKey());
        }
        // Set the interaction value
        inputs.get(keyID).interaction = interaction;
    }

    public static boolean isInteraction(String keyID) {
        // First check that the key actually exists in the inputs map
        if (!inputs.containsKey(keyID)) {
            // If it doesn't, return false.
            return false;
        }
        // Return the interaction value
        return inputs.get(keyID).interaction;
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

    public static boolean isKeyReleased(String keyID) {
        if (!keyExists(keyID)) return false;

        return inputs.get(keyID).isReleased();
    }

    public static boolean isKeyJustReleased(String keyID) {
        if (!keyExists(keyID)) return false;

        return inputs.get(keyID).isJustReleased();
    }

    public static boolean isKey(String keyID, InputType inputType) {
        switch(inputType) {
            case PRESSED:
                return isKeyPressed(keyID);
            case JUST_PRESSED:
                return isKeyJustPressed(keyID);
            case RELEASED:
                return isKeyReleased(keyID);
            case JUST_RELEASED:
                return isKeyJustReleased(keyID);
            default:
                return false;
        }
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
        JsonValue defaultRoot = FileControl.loadJsonFile("defaults", "controls.json", true);
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
        } else {
            // File in Data folder already exists, so map any missing / incorrect values in
            // the default over the root.
            // If any keys are overwritten, save the current as a backup.
            // In any case of modification, overwrite.
            String backup = root.toJson(JsonWriter.OutputType.json);
            boolean changed = false;
            boolean needBackup = false;

            JsonObject keyFormat = new JsonObject();
            keyFormat.addValue(new JsonArray("keys", JsonValue.ValueType.stringValue));
            keyFormat.addValue(new JsonType("interaction", JsonValue.ValueType.booleanValue));

            // Loop through all the keys in the defaultRoot.
            for (JsonValue currentKey : defaultRoot.iterator()) {
                // Check if the save file already has it
                if (root.has(currentKey.name())) {
                    // If it has the key, then ensure that the value is valid. If not, overwrite and
                    // set "needBackup" to true.
                    JsonValue thisID = root.get(currentKey.name());
                    // First, value must be a list
                    if (!thisID.isObject()) {
                        // If it's not, then overwrite
                        root.remove(currentKey.name());
                        root.addChild(currentKey);
                        changed = true;
                        needBackup = true;
                        continue;
                    }

                    // Format it to make sure it's valid
                    JsonFormat.formatJson(thisID, keyFormat);

                    // Make sure it has the key
                    JsonValue thisKeys = thisID.get("keys");
                    // Make sure it's an array, otherwise overwrite
                    if (thisKeys.isArray()) {
                        // If it is, make sure all the "keys" children are strings
                        // This doesn't need a backup, as they can just be ignored
                        for (JsonValue keyVal : thisKeys.iterator()) {
                            if (!keyVal.isString()) {
                                thisKeys.remove(keyVal.name());
                            }
                        }

                        // If there are no items left, then just copy over the default one, and save.
                        if (thisKeys.size == 0 && currentKey.child().size != 0) {
                            thisID.remove("keys");
                            thisID.addChild(thisKeys);
                            changed = true;
                            needBackup = true;
                        }
                    }
                    // Interaction can be ignored.
                } else {
                    // If it doesn't have the key, then add it.
                    root.addChild(currentKey);
                }
            }

            // Now overwrite or backup if needed
            if (changed) {
                FileControl.saveJsonData("controls.json", root);
                // This can be in here as it should only need a backup if it has been changed.
                if (needBackup) {
                    FileControl.saveData("controls-backup.json", backup);
                }
            }

        }

        //// Load the controls into the inputs ObjectMap
        // First iterate through all keys
        for (JsonValue key : root.iterator()) {
            // Make sure it has the "keys" array
            if (!key.has("keys")) {
                // If not, then continue to next
                continue;
            }
            JsonValue keys = key.get("keys");
            // Check to make sure that the current key is an array
            if (!keys.isArray()) {
                // If not, then continue to next
                continue;
            }

            String keyID = key.name();
            // If it is an array, then loop through the values inside
            for (JsonValue keyName : keys.iterator()) {
                // Check to make sure the name is a String
                if (!keyName.isString()) {
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

            // Check if interaction exists
            if (key.has("interaction") && key.get("interaction").isBoolean()) {
                // If it does, set it the key to that value
                setInteraction(keyID, key.getBoolean("interaction"));
            } else {
                // If the conditional is false, just assume true
                setInteraction(keyID, true);
            }
        }
    }
}