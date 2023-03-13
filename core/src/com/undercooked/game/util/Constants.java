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
            highscore.addValue(new JsonString("name", null));
            highscore.addValue(new JsonInt("value",-1)); // time in scenario, number of customers in endless.
            JsonObjectArray highscores = new JsonObjectArray("highscores", highscore);

            // Scenario scores
            JsonObject scenario = new JsonObject("scenario");
            scenario.addValue(new JsonString("id", null));
            scenario.addValue(highscores);
            root.addValue(new JsonObjectArray("scenarios", scenario));

            // Endless scores
            root.addValue(new JsonObjectArray("endless", highscore));
            return root;
        }
        public static JsonObject itemFormat() {
            JsonObject root = new JsonObject();
            root.addValue(new JsonString("id",null));
            root.addValue(new JsonString("texture_path", null));
            root.addValue(new JsonInt("value", null));
            return root;
        }
        public static JsonObject stationFormat() {
            JsonObject root = new JsonObject();
            root.addValue(new JsonString("id",null));
            root.addValue(new JsonString("texture_path", null));
            root.addValue(new JsonInt("texture_rotation", 0));
            root.addValue(new JsonInt("width", 1)); // How many x tiles it takes up
            root.addValue(new JsonInt("height", 1)); // How many y tiles it takes up
            root.addValue(new JsonString("default_base", "<main>:blank.png"));
            return root;
        }
        public static JsonObject mapFormat() {
            JsonObject root = new JsonObject();
            // Map ID
            root.addValue(new JsonString("id",null));

            // Map Stations (Map will not load them by default)
            JsonObject station = new JsonObject();
            station.addValue(new JsonString("id", null));
            station.addValue(new JsonInt("x", -1));
            station.addValue(new JsonInt("y", -1));
            station.addValue(new JsonString("base_texture", null));

            root.addValue(new JsonObjectArray("stations", station));

            // Other map variables
            root.addValue(new JsonInt("width", 16)); // The width of the Cook's area
            root.addValue(new JsonInt("height", 16)); // The height of the Cook's area
            return root;
        }
        public static JsonObject scenarioFormat() {
            JsonObject root = new JsonObject();
            // Map ID
            root.addValue(new JsonString("id",null));

            // Map ID (The map to use)
            root.addValue((new JsonString("map_id", null)));

            // Where the Cooks should be placed (at the start)
            JsonObject cook = new JsonObject();
            cook.addValue(new JsonInt("x", -1));
            cook.addValue(new JsonInt("y", -1));
            cook.addValue(new JsonInt("num", -1)); // The cook texture number, -1 being random.

            root.addValue(new JsonObjectArray("cooks", cook));

            // The interactions (these will automatically be sorted through to find the ingredient IDs)
            root.addValue(new JsonType("interactions", JsonValue.ValueType.array));

            // Requests (What the customers could possibly request)
            // It is an array of arrays of strings, with the strings being item IDs.
            root.addValue(new JsonType("requests", JsonValue.ValueType.array));

            // Other variables

            return root;
        }
        public static JsonObject interactionFormat() {
            JsonObject root = new JsonObject();
            // Interaction ID
            root.addValue(new JsonString("id",null));

            // The interactions (these will automatically be sorted through to find the ingredient IDs)
            JsonObject interaction = new JsonObject();
            root.addValue(new JsonString("station_id", null)); // The station ID for the interaction
            root.addValue(new JsonArray("items", JsonValue.ValueType.stringValue)); // An array of item IDs. The exact same number are needed for the interaction to take place,
            // the interaction only starting once a valid interaction is found.

            // A step in the interaction
            JsonObject interactionStep = new JsonObject("intStep");
            interactionStep.addValue(new JsonString("type", null)); // The type (e.g: Wait, wait (with cook locked to the station), set item, interaction input, etc.)
            /*
                Examples of types:
                - Wait : How long it takes for the next step.
                - Wait, Cook : How long it takes for the next step (requiring a cook to advance the timer)
                - Wait, Cook timed : Two timers. One for Wait, Cook and another than runs while Cook is not locked.
                        If Cook timer reaches full, it's a success. If second timer reaches full, it's a failure.
                - Set : Sets the items on the station to the list provided. Ignores time and moves to next step.
                - Input : Requires an input of id value to move to the next step.
                - Input timed : Requires an input within a time limit.
                - Colour : Colours the bar the colour provided in value. Ignores time and moves to next step.
             */
            interactionStep.addValue(new JsonString("value", null)); // Can be anything, relying on the type.
            /** NOTE: THE BELOW LIKELY, CURRENTLY, CAUSES AN INFINITE LOOP. CAN'T TEST AS THERE ARE CODE ERRORS. */
            interactionStep.addValue(new JsonObjectArray("success", interactionStep)); // Step path to take if the interaction is a success
            interactionStep.addValue(new JsonObjectArray("failure", interactionStep)); // Step path to take if the interaction is a failure (won't be needed for all interaction types)
            interactionStep.addValue(new JsonFloat("time", null)); // The time (in seconds) that this step takes.
            interactionStep.addValue(new JsonString("sound", null)); // The sound this step makes.
            /* An example format would be
            {
                "id": "lettuce_chop",
                "station_id": "chopping",
                "items": ["lettuce"],
                "steps": [{
                    "type": "wait",
                    "value": null,
                    "success": null,
                    "failure": null,
                    "time": 3.0,
                    "sound": "chopping.mp3"
                },
                {
                    "type": "input",
                    "value": "interact",
                    "success": [{
                        "type": "set",
                        "value": ["chopped_lettuce"],
                        "success": null,
                        "failure": null,
                        "time": 0,
                        "sound": null
                    }],
                    "failure": [{
                        "type": "set",
                        "value": ["bad_chopped_lettuce"],
                        "success": null,
                        "failure": null,
                        "time": 0,
                        "sound": "buzz.mp3"
                    }],
                    "time": 4.0,
                    "sound": "heavy_chopping.mp3"
                }, ...
                *something here would happen after EITHER both success or failure.
                For example, cooking meat successfully, but not taking it off quick enough
                would mean it's ruined whether you flipped it correctly or not.*
                ]
            }
            */ // Visually, this could be shown as one main bar, and a faded bar above. If you fail, the main bar disappears, and the faded bar replaces it.

            /*
            Pantries will also be created using this. E.g:
            {
                "ID": "take_tomato",
                "station_ID": "tomato_pantry",
                "items": [], // When it sets itself to a tomato again, it will no longer apply to this.
                "steps": [{
                    "type": "set",
                    "value": "tomato",
                    "success": null,
                    "failure": null,
                    "time": 0,
                    "sound": null
                }]
            }
             */

            // interaction.addValue(new JsonObjectArray("steps_arr", interactionStep));
            root.addValue(new JsonObjectArray("steps", interactionStep));

            // Other variables

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
    public static final String DEFAULT_MAP = "<main>:art_map/customertest.tmx";
    public static final String DEFAULT_TEXTURE = "items/missing.png";
    public static final String DEFAULT_MUSIC = "audio/music/GameMusic.mp3";
    public static final String DEFAULT_SOUND = "uielements/testsound.mp3";

    // public static final ... DEFAULT_CONTROLS = ...;

    // public static final... DEFAULT_SETTINGS = ...;
}
