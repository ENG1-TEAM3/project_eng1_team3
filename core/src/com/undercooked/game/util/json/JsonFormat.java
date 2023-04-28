package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.util.DefaultJson;

/**
 * A class which allows for the formatting of a {@link JsonValue}
 * so that checks for the {@link JsonValue} having certain children
 * do not have to be made, as they will already exist.
 */
public class JsonFormat {

    /**
     * Takes a JsonValue and JsonObject input, and outputs a
     * formatted version where any missing values are added.
     * @param json {@link JsonValue} : The {@link JsonValue} to format.
     * @param ideal {@link JsonObject} : The ideal Json formatting.
     * @return The formatted {@link JsonValue}.
     */
    public static JsonValue formatJson(JsonValue json, JsonObject ideal) {
        return formatJson(json, ideal, true);
    }

    /**
     * Takes a JsonValue and JsonObject input, and outputs a
     * formatted version where any missing values are added.
     * @param json {@link JsonValue} : The {@link JsonValue} to format.
     * @param ideal {@link JsonObject} : The ideal Json formatting.
     * @param existsBefore {@code boolean} : Whether the {@code ideal} argument
     *                                    existed in the previous recursion or not.
     * @return The formatted {@link JsonValue}.
     */
    protected static JsonValue formatJson(JsonValue json, JsonObject ideal, boolean existsBefore) {
        // If json is null, return null
        if (json == null) {
            return null;
        }
        // Make sure ideal is not null too
        if (ideal == null) {
            return null;
        }

        Array<JsonVal> values = ideal.getValues();

        for (int i = 0 ; i < values.size ; i++) {
            JsonVal jsonVal = values.get(i);
            // Check that it has the ID
            if (!json.has(jsonVal.ID)) {
                // Add the child
                jsonVal.addChild(json);
            } else {
                // Get the child
                JsonValue thisJson = json.get(jsonVal.ID);
                // And then check if
                jsonVal.checkChild(thisJson, existsBefore);
            }
        }

        // Finally return the input Json, in case it's needed
        return json;
    }
}
