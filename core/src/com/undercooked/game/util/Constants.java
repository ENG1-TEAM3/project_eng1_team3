package com.undercooked.game.util;

import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.util.json.*;

/** A class filled with public static final variables so that they can be accessed from anywhere. */
public final class Constants {

    // Camera
    /** The ID of the World {@link com.badlogic.gdx.graphics.OrthographicCamera}. */
    public static final String WORLD_CAMERA_ID = "WORLD";
    /** The ID of the User Interface {@link com.badlogic.gdx.graphics.OrthographicCamera}. */
    public static final String UI_CAMERA_ID = "UI";

    // Window
    /** The width of the window video. */
    public static final int V_WIDTH = 1920;
    /** The height of the window video. */
    public static final int V_HEIGHT = 1080;

    // Screens
    /** The ID for the {@link com.undercooked.game.screen.MainScreen}. */
    public static final String MAIN_SCREEN_ID = "main";
    /** The ID for the {@link com.undercooked.game.screen.GameScreen}. */
    public static final String GAME_SCREEN_ID = "game";
    /** The ID for the {@link com.undercooked.game.screen.LeaderBoard}. */
    public static final String LEADERBOARD_SCREEN_ID = "leaderboard";
    /** The ID for the {@link com.undercooked.game.screen.PauseScreen}. */
    public static final String PAUSE_SCREEN_ID = "pause";
    /** The ID for the SettingsScreen. */
    public static final String SETTINGS_SCREEN_ID = "settings";

    // Files
    /** The folder to store the external data of the game into. */
    public static final String DATA_FILE = "PiazzaPanic_UnderCooked";
    /** The ID for the Menu's song. */
    public static final String MENU_SONG_ID = "musicSong";
    /** The ID for the Game's song. */
    public static final String GAME_SONG_ID = "gameSong";
    /** The texture ID for the textures on the {@link com.undercooked.game.screen.MainScreen} */
    public static final String MENU_TEXTURE_ID = "main";
    /** The texture ID for the textures on the {@link com.undercooked.game.screen.GameScreen} */
    public static final String GAME_TEXTURE_ID = "game";
    /** The texture ID for the textures on the {@link com.undercooked.game.screen.PauseScreen} */
    public static final String PAUSE_TEXTURE_ID = "pause";
    /** The texture ID for the textures on the {@link com.undercooked.game.screen.LeaderBoard} */
    public static final String LEADERBOARD_TEXTURE_ID = "leaderboard";

    // GameInfo, such as file names for maps, ingredients, stations, recipes
    // and other things.
    public static final class DefaultJson {
        // It creates these as to avoid creating them in memory and storing them
        // at all times

        /**
         * The formatting for a {@link com.undercooked.game.screen.LeaderBoard}'s score
         * {@link JsonValue}.
         * @return {@link JsonObject} : The Json formatting to use in {@link JsonFormat#formatJson(JsonValue, JsonObject)}.
         */
        public static JsonObject scoreFormat() {
            JsonObject root = new JsonObject();
            // Highscore variable set up
            JsonObject highscore = new JsonObject();
            highscore.addValue(new JsonString("name", null));
            highscore.addValue(new JsonFloat("time",-1F)); // time in scenario, number of customers in endless.
            JsonObjectArray highscores = new JsonObjectArray("scores", highscore);

            // Scenario scores
            JsonObject scenario = new JsonObject("scenario");
            scenario.addValue(new JsonString("id", null));
            scenario.addValue(highscores);
            root.addValue(new JsonObjectArray("scenarios", scenario));

            // Endless scores
            root.addValue(new JsonObjectArray("endless", highscore));
            return root;
        }

        /**
         * The formatting for the {@link com.undercooked.game.food.Item}'s
         * {@link JsonValue}.
         * @return {@link JsonObject} : The Json formatting to use in {@link JsonFormat#formatJson(JsonValue, JsonObject)}.
         */
        public static JsonObject itemFormat() {
            JsonObject root = new JsonObject();
            root.addValue(new JsonString("name",null));
            root.addValue(new JsonString("texture_path", null));
            root.addValue(new JsonInt("value", 0));
            return root;
        }

        /**
         * The formatting for the {@link com.undercooked.game.station.StationData}'s
         * {@link JsonValue}.
         * @return {@link JsonObject} : The Json formatting to use in {@link JsonFormat#formatJson(JsonValue, JsonObject)}.
         */
        public static JsonObject stationFormat() {
            JsonObject root = new JsonObject();
            root.addValue(new JsonString("id",null));
            root.addValue(new JsonString("texture_path", null));
            root.addValue(new JsonInt("texture_rotation", 0));
            root.addValue(new JsonInt("width", 1)); // How many x tiles it takes up
            root.addValue(new JsonInt("height", 1)); // How many y tiles it takes up
            // -1 of the below two defaults the collision to automatically calculate based on width / height.
            root.addValue(new JsonFloat("collision_width", -1F)); // The width of the collision box in pixels
            root.addValue(new JsonFloat("collision_height", -1F)); // The height of the collision box in pixels
            root.addValue(new JsonFloat("collision_offset_x", 0F)); // The z offset of the collision box in pixels
            root.addValue(new JsonFloat("collision_offset_y", 0F)); // The y offset of the collision box in pixels
            root.addValue(new JsonString("default_base", "<main>:station/blank.png"));
            root.addValue(new JsonType("has_collision", JsonValue.ValueType.booleanValue));
            root.addValue(new JsonInt("holds", 0)); // How many items the station holds.

            /*JsonObject interactions = new JsonObject();

            interactions.addValue(new JsonString("id", null));
            interactions.addValue(new JsonString("child_id", null)); // Used by the "call_interaction"

            root.addValue(new JsonObjectArray("interactions", interactions));*/
            return root;
        }

        /**
         * The formatting for the {@link com.undercooked.game.map.Map}'s
         * {@link JsonValue}.
         * @return {@link JsonObject} : The Json formatting to use in {@link JsonFormat#formatJson(JsonValue, JsonObject)}.
         */
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

        /**
         * The formatting for the {@link com.undercooked.game.logic.ScenarioLogic}'s
         * {@link JsonValue}.
         * @return {@link JsonObject} : The Json formatting to use in {@link JsonFormat#formatJson(JsonValue, JsonObject)}.
         */
        public static JsonObject scenarioFormat() {
            JsonObject root = new JsonObject();

            // Map ID of the map to use
            root.addValue((new JsonString("map_id", null)));

            // Where the Cooks should be placed (at the start)
            JsonObject cook = new JsonObject();
            cook.addValue(new JsonInt("x", -1));
            cook.addValue(new JsonInt("y", -1));
            cook.addValue(new JsonInt("num", -1)); // The cook texture number, -1 being random.

            root.addValue(new JsonObjectArray("cooks", cook));

            // The interactions (these will automatically be sorted through to find the ingredient IDs)
            root.addValue(new JsonArray("interactions", JsonValue.ValueType.stringValue));

            // Requests (What the customers could possibly request)
            // It is an array of arrays of strings, with the strings being item IDs.
            root.addValue(new JsonObjectArray("requests", requestFormat()));

            return root;
        }

        /**
         * The formatting for the requests {@link JsonValue}.
         * @return {@link JsonObject} : The Json formatting to use in {@link JsonFormat#formatJson(JsonValue, JsonObject)}.
         */
        public static JsonObject requestFormat() {
            JsonObject root = new JsonObject();

            root.addValue(new JsonString("item_id",null));
            root.addValue(new JsonInt("value", -1)); // Value of the request. If < 0, then uses item's default value.

            root.addValue(new JsonFloat("time_min", -1F)); // The lowest time that the request must be provided by.
            root.addValue(new JsonFloat("time_max", -1F)); // The highest time that the request must be provided by.

            root.addValue(new JsonInt("number", 1)); // The number of times that the request can be made.

            return root;
        }

        /**
         * The formatting for the {@link com.undercooked.game.interactions.InteractionObject}'s
         * {@link JsonValue}.
         * @return {@link JsonObject} : The Json formatting to use in {@link JsonFormat#formatJson(JsonValue, JsonObject)}.
         */
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
            interactionStep.addValue(new JsonObjectArray("success", interactionStep)); // Step path to take if the interaction is a success
            interactionStep.addValue(new JsonObjectArray("failure", interactionStep)); // Step path to take if the interaction is a failure (won't be needed for all interaction types)
            interactionStep.addValue(new JsonFloat("time", -1F)); // The time (in seconds) that this step takes.
            interactionStep.addValue(new JsonString("sound", null)); // The sound this step makes.
            /* An example format would be
            {
                "station_id": "<main>:cutting",
                "items": ["<main>:lettuce"],
                "steps": [{
                    "type": "wait",
                    "value": null,
                    "success": null,
                    "failure": null,
                    "time": 3.0,
                    "sound": "<main>:chopping.mp3"
                },
                {
                    "type": "input",
                    "value": "interact",
                    "success": [{
                        "type": "set",
                        "value": ["<main>:chopped_lettuce"],
                        "success": null,
                        "failure": null,
                        "time": 0,
                        "sound": null
                    }],
                    "failure": [{
                        "type": "set",
                        "value": ["<main>:bad_chopped_lettuce"],
                        "success": null,
                        "failure": null,
                        "time": 0,
                        "sound": "<main>:buzz.mp3"
                    }],
                    "time": 4.0,
                    "sound": "<main>:heavy_chopping.mp3"
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
              "station_id": "<main>:pantry/tomato_pantry",
              "steps": [{
                "type": "just_pressed",
                "value": "take",
                "success": [{
                  "type": "give",
                  "value": "<main>:tomato",
                  "time": 1 // The number of 'time's to give
                }]
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

    // Sounds
    /** {@link com.undercooked.game.assets.AudioManager} group for the game's Music. */
    public static final String MUSIC_GROUP = "music";
    /** {@link com.undercooked.game.assets.AudioManager} group for the game's music audio. */
    public static final String GAME_GROUP = "game";
    /** {@link com.undercooked.game.assets.AudioManager} group for the game's sound audio. */
    public static final String GAME_SOUND_GROUP = "game_sound";

    // Textures
    /**
     * The number of {@link com.undercooked.game.entity.Cook} {@link com.badlogic.gdx.graphics.Texture}s
     * in the asset folder.
     */
    public static final int NUM_COOK_TEXTURES = 3;
    /**
     * The number of {@link com.undercooked.game.entity.Customer} {@link com.badlogic.gdx.graphics.Texture}s
     * in the asset folder.
     */
    public static int NUM_CUSTOMER_TEXTURES = 5;

    // Defaults
    /** The default texture for the Music. */
    public static final float DEFAULT_MUSIC_VOLUME = 0.5F;
    /** The default volume for the Sound. */
    public static final float DEFAULT_SOUND_VOLUME = 0.5F;
    /** The default {@link com.undercooked.game.map.Map} to load if one fails to load. */
    public static final String DEFAULT_MAP = "<main>:main";
    /** The default {@link com.badlogic.gdx.graphics.Texture} to load if one fails to load. */
    public static final String DEFAULT_TEXTURE = "items/missing.png";
    /** The default {@link com.badlogic.gdx.audio.Music} to load if one fails to load. */
    public static final String DEFAULT_MUSIC = "game/sounds/frying.mp3";
    /** The default {@link com.badlogic.gdx.audio.Sound} to load if one fails to load. */
    public static final String DEFAULT_SOUND = "game/sounds/cutting.mp3";

    // public static final ... DEFAULT_CONTROLS = ...;

    // public static final... DEFAULT_SETTINGS = ...;
}
