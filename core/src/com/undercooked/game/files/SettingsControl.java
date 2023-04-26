package com.undercooked.game.files;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.util.DefaultJson;
import com.undercooked.game.util.json.JsonFormat;

/**
 * A class to control the settings of the game.
 */
public class SettingsControl {

    /** loaded = True if this class has loaded its data. False otherwise. */
    boolean loaded;
    /** Location of settings file. */
    String fileLoc;
    /** json containing the settings. */
    JsonValue settingsData;

    public SettingsControl(String fileLoc) {
        this.fileLoc = fileLoc;
        this.loaded = false;
    }

    public boolean loadData() {
        // it's now loaded
        loaded = true;
        // Try to load the file
        settingsData = FileControl.loadJsonData(fileLoc);
        // If it failed to load...
        if (settingsData == null) {
            // Make new json and save it
            settingsData = new JsonValue(JsonValue.ValueType.object);
            JsonFormat.formatJson(settingsData, DefaultJson.settingsFormat());
            // And save the data
            saveData();
        } else {
            // Otherwise just format the json
            JsonFormat.formatJson(settingsData, DefaultJson.settingsFormat());
        }
        return loaded;
    }

    public void unload() {
        // Forget the json
        settingsData = null;
        // and it's not loaded
        loaded = false;
    }

    public boolean loadIfNotLoaded() {
        // Only load if it's not loaded.
        if (loaded)
            return true;
        return loadData();
    }

    public void saveData() {
        // Only save if it's loaded
        if (!loaded)
            return;
        FileControl.saveJsonData(fileLoc, settingsData);
    }

    private void setFloatValue(String key, float val) {
        // If the json is not null
        if (settingsData == null)
            return;
        // Then set the value
        settingsData.get(key).set(val, key);
    }

    public void setMusicVolume(float volume) {
        setFloatValue("music_volume", volume);
    }

    public void setGameVolume(float volume) {
        setFloatValue("game_volume", volume);
    }

    public float getMusicVolume() {
        return settingsData.getFloat("music_volume");
    }

    public float getGameVolume() {
        return settingsData.getFloat("game_volume");
    }
}
