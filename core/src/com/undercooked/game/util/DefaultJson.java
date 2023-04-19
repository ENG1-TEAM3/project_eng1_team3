package com.undercooked.game.util;

import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.util.json.*;
import com.undercooked.game.util.leaderboard.LeaderboardController;

/**
 * The JSON formatting for the different JSON files to be
 * loaded into the game.
 * <br>Use these as the second argument in
 * JsonFormat.formatJson(JsonValue, JsonObject);
 */
public final class DefaultJson {
    // It creates these as to avoid creating them in memory and storing them
    // at all times

    /**
     * The formatting for a {@link LeaderboardController}'s score
     * {@link JsonValue}.
     *
     * @return {@link JsonObject} : The Json formatting to use in {@link JsonFormat#formatJson(JsonValue, JsonObject)}.
     */
    public static JsonObject leaderboardFormat() {
        JsonObject root = new JsonObject();
        // Highscore variable set up
        JsonObject highscore = new JsonObject();
        highscore.addValue(new JsonString("name", "missing name", false));
        highscore.addValue(new JsonFloat("score", -1F)); // time in scenario, number of customers in endless.
        highscore.addValue(new JsonString("date", Constants.UNKNOWN_DATE, false)); // The date & time it was recorded
        JsonArray highscores = new JsonArray("scores", highscore);

        // Scenario scores
        JsonObject score = new JsonObject("scores");
        score.addValue(new JsonString("id", "missing id", false));
        score.addValue(new JsonString("name", "missing name", false));
        score.addValue(highscores);

        // Scenarios data

        root.addValue(new JsonArray("scenarios", score));

        // Endless scores
        root.addValue(new JsonArray("endless", score));
        return root;
    }

    /**
     * The formatting for the {@link com.undercooked.game.food.Item}'s
     * {@link JsonValue}.
     *
     * @return {@link JsonObject} : The Json formatting to use in {@link JsonFormat#formatJson(JsonValue, JsonObject)}.
     */
    public static JsonObject itemFormat() {
        JsonObject root = new JsonObject();
        root.addValue(new JsonString("name", null));
        root.addValue(new JsonString("texture_path", null));
        root.addValue(new JsonInt("value", 0));

        // The draw width / height of the food item
        root.addValue(new JsonFloat("width", -1f));
        root.addValue(new JsonFloat("height", -1f));
        return root;
    }

    /**
     * The formatting for the {@link com.undercooked.game.station.StationData}'s
     * {@link JsonValue}.
     *
     * @return {@link JsonObject} : The Json formatting to use in {@link JsonFormat#formatJson(JsonValue, JsonObject)}.
     */
    public static JsonObject stationFormat() {
        JsonObject root = new JsonObject();
        root.addValue(new JsonString("id", null));
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
        root.addValue(new JsonString("floor_tile", null));
        root.addValue(new JsonType("has_collision", JsonValue.ValueType.booleanValue));
        root.addValue(new JsonInt("holds", 0)); // How many items the station holds.
        root.addValue(new JsonInt("price", 0)); // The default price of the station, if not set on the map

        /*JsonObject interactions = new JsonObject();

        interactions.addValue(new JsonString("id", null));
        interactions.addValue(new JsonString("child_id", null)); // Used by the "call_interaction"

        root.addValue(new JsonObjectArray("interactions", interactions));*/
        return root;
    }

    /**
     * The formatting for the {@link com.undercooked.game.map.Map}'s
     * {@link JsonValue}.
     *
     * @return {@link JsonObject} : The Json formatting to use in {@link JsonFormat#formatJson(JsonValue, JsonObject)}.
     */
    public static JsonObject mapFormat() {
        JsonObject root = new JsonObject();

        // Map Stations (Map will not load them by default)
        JsonObject station = new JsonObject();
        station.addValue(new JsonString("id", null));
        station.addValue(new JsonInt("x", -1));
        station.addValue(new JsonInt("y", -1));
        station.addValue(new JsonString("base_texture", null));
        // Optional attributes:
        // has_collision: boolean
        // price: int

        root.addValue(new JsonArray("stations", station));

        // Other map variables
        root.addValue(new JsonInt("width", 16)); // The width of the Cook's area
        root.addValue(new JsonInt("height", 16)); // The height of the Cook's area
        return root;
    }

    /**
     * The formatting for the {@link com.undercooked.game.logic.ScenarioLogic}'s
     * {@link JsonValue}.
     *
     * @return {@link JsonObject} : The Json formatting to use in {@link JsonFormat#formatJson(JsonValue, JsonObject)}.
     */
    public static JsonObject scenarioFormat() {
        JsonObject root = new JsonObject();

        // Map ID of the map to use
        root.addValue((new JsonString("map_id", null)));

        // The name of the Scenario, displayed on the LeaderboardScreen and Scenario selection
        root.addValue(new JsonString("name", null));

        // The description of the Scenario, used in the Scenario selection
        root.addValue(new JsonString("description", "", false));

        // Where the Cooks should be placed (at the start)
        JsonObject cook = new JsonObject();
        cook.addValue(new JsonInt("x", -1));
        cook.addValue(new JsonInt("y", -1));
        cook.addValue(new JsonInt("num", -1)); // The cook texture number, -1 being random.

        root.addValue(new JsonArray("cooks", cook));

        // The interactions (these will automatically be sorted through to find the ingredient IDs)
        root.addValue(new JsonArray("interactions", new JsonString(null, null)));

        // Requests (What the customers could possibly request)
        // It is an array of arrays of strings, with the strings being item IDs.
        root.addValue(new JsonArray("requests", requestFormat(true)));

        // The amount of reputation that the player starts with
        root.addValue(new JsonInt("reputation", 3));

        // The speed at which the customers move at
        root.addValue(new JsonFloat("customer_speed", 1.5F));

        // The money needed to get another cook
        // Anything < 0 means they can't get another cook
        root.addValue(new JsonInt("cook_cost", -1));

        // The number of requests that need to be completed (in scenario mode)
        // If < 0, it will use all requests in the request array
        // If >= requests size, if duplicate_requests is...
        //              true: Will select that many requests
        //              false: Will select all requests once, but no more than that
        root.addValue(new JsonInt("num_of_requests", -1));

        // Whether a request can be selected once or multiple times
        root.addValue(new JsonBool("duplicate_requests", false));

        //// Power Ups
        JsonObject powerUps = new JsonObject("power_ups");

        // The time it takes for power ups to spawn
        powerUps.addValue(new JsonFloat("time", -1f));
        // Optionally, randomised timing
        powerUps.addValue(new JsonFloat("time_min", -1f));
        powerUps.addValue(new JsonFloat("time_max", -1f));
        // The time that the powerups will last
        powerUps.addValue(new JsonFloat("use_time", 0f));
        // The maximum number of power ups that can spawn at once
        powerUps.addValue(new JsonInt("max", 0));
        // The time it takes for a power up to despawn
        powerUps.addValue(new JsonFloat("despawn_time", 0f));

        root.addValue(powerUps);

        return root;
    }

    /**
     * The formatting for the requests {@link JsonValue}.
     *
     * @param asOr {@code boolean} : Whether it should return as a {@link JsonOr},
     *             allow a {@link JsonString} OR {@link JsonObject}
     *             or if it should only allow the {@link JsonObject}.
     * @return {@link JsonObject} : The Json formatting to use in {@link JsonFormat#formatJson(JsonValue, JsonObject)}.
     */
    public static JsonVal requestFormat(boolean asOr) {

        JsonObject asObject = new JsonObject();

        // The recipe information
        asObject.addValue(new JsonString("item_id", null));
        asObject.addValue(new JsonInt("value", -1)); // Value of the request. If < 0, then uses item's default value.


        asObject.addValue(new JsonFloat("time", -1F)); // The definite time that the request must be provided by.
        // If the above is not set, then the below two are checked
        asObject.addValue(new JsonFloat("time_min", -1F)); // The lowest time that the request must be provided by.
        asObject.addValue(new JsonFloat("time_max", -1F)); // The highest time that the request must be provided by.

        asObject.addValue(new JsonInt("number", 1)); // The number of times that the request can be made.

        asObject.addValue(new JsonInt("reputation_threat", 1)); // How much reputation is taken if the player fails to complete the request

        // The directions for how to make the item
        JsonObject recipe = new JsonObject("instruction");
        recipe.addValue(new JsonString("texture_path", "<main>:missing.png"));
        recipe.addValue(new JsonString("text", "missing"));

        JsonArray instructions = new JsonArray("instructions", recipe);

        asObject.addValue(instructions);

        // If it's as an Or, set it to use that
        if (asOr) {
            JsonString asString = new JsonString(null, null);

            return new JsonOr(null, new JsonVal[]{asString, asObject});
        }
        // Otherwise, just use the Object.
        return asObject;
    }

    /**
     * The formatting for the {@link com.undercooked.game.interactions.InteractionObject}'s
     * {@link JsonValue}.
     *
     * @return {@link JsonObject} : The Json formatting to use in {@link JsonFormat#formatJson(JsonValue, JsonObject)}.
     */
    public static JsonObject interactionFormat() {
        JsonObject root = new JsonObject();
        // Interaction ID
        root.addValue(new JsonString("id", null));

        // The interactions (these will automatically be sorted through to find the ingredient IDs)
        JsonObject interaction = new JsonObject();
        root.addValue(new JsonString("station_id", null)); // The station ID for the interaction
        root.addValue(new JsonArray("items", new JsonString(null, null))); // An array of item IDs. The exact same number are needed for the interaction to take place,
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
        interactionStep.addValue(new JsonArray("success", interactionStep)); // Step path to take if the interaction is a success
        interactionStep.addValue(new JsonArray("failure", interactionStep)); // Step path to take if the interaction is a failure (won't be needed for all interaction types)
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
        root.addValue(new JsonArray("steps", interactionStep));

        // Other variables

        return root;
    }

    /**
     * The formatting for the settings' {@link JsonValue}.
     *
     * @return {@link JsonObject} : The Json formatting to use in {@link JsonFormat#formatJson(JsonValue, JsonObject)}.
     */
    public static JsonObject settingsFormat() {
        JsonObject root = new JsonObject();

        // Volumes
        root.addValue(new JsonFloat("music_volume", 0.5F));
        root.addValue(new JsonFloat("game_volume", 0.5F));

        return root;
    }
}
