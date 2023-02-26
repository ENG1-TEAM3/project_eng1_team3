package com.undercooked.game.util;

import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.util.json.*;

/** A class filled with public static final variables so that they can be accessed from anywhere. */
public final class Constants {

    // Camera
    public static final String WORLD_CAMERA_ID = "WORLD";
    public static final String UI_CAMERA_ID = "UI";

    // Window
    public static final int V_WIDTH = 1920;
    public static final int V_HEIGHT = 1080;

    // Screens
    public static final String MAIN_SCREEN_ID = "main";
    public static final String GAME_SCREEN_ID = "game";
    public static final String LEADERBOARD_SCREEN_ID = "leaderboard";
    public static final String PAUSE_SCREEN_ID = "pause";
    public static final String SETTINGS_SCREEN_ID = "settings";

    // Files
    public static final String DATA_FILE = "PiazzaPanic_UnderCooked";
    public static final String MENU_SONG_ID = "musicSong";
    public static final String GAME_SONG_ID = "gameSong";
    public static final String MENU_TEXTURE_ID = "main";
    public static final String GAME_TEXTURE_ID = "game";
    public static final String PAUSE_TEXTURE_ID = "pause";
    public static final String LEADERBOARD_TEXTURE_ID = "leaderboard";

    // GameInfo, such as file names for maps, ingredients, stations, recipes
    // and other things.
    public static final class DefaultJson {
        // It creates these as to avoid creating them in memory and storing them
        // at all times
        public static JsonObject scoreFormat() {
            JsonObject root = new JsonObject();
            // Highscore variable set up
            JsonObject highscore = new JsonObject();
            highscore.addValue(new JsonType("Name", JsonValue.ValueType.stringValue));
            highscore.addValue(new JsonInt("Time",-1));
            JsonObjectArray highscores = new JsonObjectArray("highscores", highscore);

            // Scenario scores
            JsonObject scenario = new JsonObject("scenario");
            scenario.addValue(new JsonString("ID", "undefined"));
            scenario.addValue(highscores);
            JsonObjectArray scenarios = new JsonObjectArray("scenarios", scenario);

            // Endless scores
            JsonObjectArray endless = new JsonObjectArray("endless", highscore);
            root.addValue(endless);
            root.addValue(scenarios);
            return root;
        }
        public static JsonObject ingredientFormat() {
            JsonObject root = new JsonObject();
            root.addValue(new JsonString("ID","undefined"));
            root.addValue(new JsonString("path", "undefined"));
            return root;
        }
        // public static JsonValue
    }

    // Preferences
    public static final class Preferences {
        // Preference IDs
        public static final String SETTINGS = "SETTINGS";

        // Preferences names
        public static final String MUSIC_VOLUME = "music_vol";
        public static final String GAME_VOLUME = "game_vol";
    }

    // Sounds
    public static final String MUSIC_GROUP = "music";
    public static final String GAME_GROUP = "game";

    // Textures
    public static final int NUM_COOK_TEXTURES = 3;
    public static int NUM_CUSTOMER_TEXTURES = 5;

    // Defaults
    public static final float DEFAULT_MUSIC_VOLUME = 0.5F;
    public static final float DEFAULT_SOUND_VOLUME = 0.5F;
    public static final String DEFAULT_MAP = "map/art_map/customertest.tmx";
    public static final String DEFAULT_TEXTURE = "items/missing.png";
    public static final String DEFAULT_MUSIC = "audio/music/GameMusic.mp3";
    public static final String DEFAULT_SOUND = "uielements/testsound.mp3";

    // public static final ... DEFAULT_CONTROLS = ...;

    // public static final... DEFAULT_SETTINGS = ...;
}
