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
                // If it doesn't create it.
                JsonValue jValue = new JsonValue(jsonVal.getType());
                // Set it to use the default value
                jsonVal.setValue(jValue, false);
                // And then add it
                json.addChild(jsonVal.ID, jValue);
            } else {
                JsonValue thisJson = json.get(jsonVal.ID);
                // If it does, check if the value type is correct
                if (!jsonVal.isValue(thisJson)) {
                    // If it isn't, then set the type
                    jsonVal.setType(thisJson);
                    // And then update the value
                    jsonVal.setValue(thisJson);
                }
                // If type matches, then just try and call
                // the "format" function, in case it's something
                // like a JsonObject.
                jsonVal.format(thisJson, existsBefore);
            }
        }

        System.out.println("Final Json: " + json);
        // Finally return the input Json, in case it's needed
        return json;
    }

    // TESTING
    public static void main(String[] args) {
        JsonValue jvalue = FileControl.loadJsonAsset("<main>:pantry/tomato_pantry.json", "interactions");
        //System.out.println(FileControl.toPath("<main>:burger.json", "interactions"));
        System.out.println(jvalue);
        formatJson(jvalue, Constants.DefaultJson.interactionFormat());
        // System.out.println(jvalue);
        // jvalue.get("scenarios").addChild("a", new JsonValue("a"));
        System.out.println(jvalue);
    }

}
