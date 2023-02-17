package com.undercooked.game.files;


import com.badlogic.gdx.utils.*;
import com.undercooked.game.Input.InputController;

/**
 *
 * TODO: NEEDS TO BE COMPLETED
 *
 */

public class SaveManager {

    private static Json json;
    static {
        Json json = new Json();
    }

    public static ObjectMap<String, JsonValue> saveSettings;
    static {
        for (SaveFiles key : SaveFiles.values()) {
            loadSave(key);
        }
    }

    public static JsonValue loadSave(SaveFiles saveID) {
        String dir = FileControl.getDataPath();
        String fileData = FileControl.loadFile(dir, saveID.fileName, saveID.defaultData);
        JsonValue root = new JsonReader().parse(fileData);
        return root;
    }

    public static void loadKeyData() {

    }

    public static void saveKeyData() {



    }

    /*

    File Testing

    public static void main(String[] args) {
        // loadSave(SaveFiles.SETTINGS);
        Json json = new Json();
        ObjectMap defaultMap = InputController.defaultKeyMap();
        System.out.println(defaultMap);
        System.out.println(json.toJson(defaultMap, ObjectMap.class));
    }*/

}

enum SaveFiles {
    /** Contains settings and controls for the game. */
    SETTINGS("settings.json",new Json().toJson(InputController.defaultKeyMap())),
    /** Contains data on the leaderboard for the game. */
    LEADERBOARD("leaderboard.json",new Json().toJson(new JsonValue("")));
    final String fileName;
    final String defaultData;

    SaveFiles(String fileName, String defaultData) {
        this.fileName = fileName;
        this.defaultData = defaultData;
    }
}