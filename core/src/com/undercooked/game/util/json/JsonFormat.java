package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.util.Constants;

import javax.sound.midi.SysexMessage;

public class JsonFormat {

    public static JsonValue formatJson(JsonValue json, JsonObject ideal) {
        return formatJson(json, ideal, true);
    }

    /**
     * Takes a JsonValue and JsonObject input, and outputs a
     * formatted version where any missing values are added.
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

        System.out.println("Final Json: " + json);
        // Finally return the input Json, in case it's needed
        return json;
    }

    // TESTING
    public static void main(String[] args) {
        JsonValue jvalue = FileControl.loadJsonAsset("<main>:main", "scenarios");
        System.out.println(jvalue);
        formatJson(jvalue, Constants.DefaultJson.interactionFormat());
        System.out.println(jvalue);
    }

}
