package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.util.Constants;

public class JsonFormat {

    /**
     * Takes a JsonValue and JsonObject input, and outputs a
     * formatted version where any missing values are added.
     * @return The formatted {@link JsonValue}.
     */
    public static void formatJson(JsonValue json, JsonObject ideal) {
        // Create a copy of the json
        // Loop through the values of the JsonObject
        for (JsonVal jsonVal : ideal.getValues()) {
            // System.out.println("Checking: " + jsonVal.ID);
            Class jsonClass = jsonVal.getClass();
            // System.out.println("Class: " + jsonClass);
            // Check that the value exists in json
            if (!json.hasChild(jsonVal.ID)) {
                // If it doesn't, then add it.
                JsonValue jvalue;
                // All cases of JsonObject
                if (jsonClass == JsonObject.class) {
                    // System.out.println(jsonVal.ID + ": Object");
                    jvalue = new JsonValue(JsonValue.ValueType.object);
                }
                else if (jsonClass == JsonString.class) {
                    // System.out.println(jsonVal.ID + ": Object");
                    jvalue = new JsonValue(JsonValue.ValueType.stringValue);
                    jvalue.set((String) jsonVal.value);
                }
                else if (jsonClass == JsonFloat.class) {
                    // System.out.println(jsonVal.ID + ": Float");
                    jvalue = new JsonValue(JsonValue.ValueType.doubleValue);
                    jvalue.set((Double) jsonVal.value, null);
                }
                else if (jsonClass == JsonInt.class) {
                    // System.out.println(jsonVal.ID + ": Int");
                    jvalue = new JsonValue(JsonValue.ValueType.doubleValue);
                    jvalue.set((Integer) jsonVal.value, null);
                }
                else if (jsonClass == JsonObjectArray.class) {
                    // System.out.println(jsonVal.ID + ": Object Array");
                    jvalue = new JsonValue(JsonValue.ValueType.array);
                }
                else if (jsonClass == JsonType.class) {
                    // System.out.println(jsonVal.ID + ": " + jsonVal.value);
                    jvalue = new JsonValue((JsonValue.ValueType) jsonVal.value);
                }
                else {
                    // System.out.println(jsonVal.ID + ": Null");
                    jvalue = new JsonValue(JsonValue.ValueType.nullValue);
                }
                json.addChild(jsonVal.ID, jvalue);
            }
            // Check the class type
            if (jsonClass == JsonObject.class) {
                // System.out.println(jsonVal.ID + ": Recurse");
                // If it's a JsonObject, recurse.
                // But only recurse if the value is actually an object
                if (json.type() == JsonValue.ValueType.object) {
                    formatJson(json.get(jsonVal.ID), (JsonObject) jsonVal);
                }
            }
            // If it's an Object Array, make sure all objects within follow the
            // correct formatting
            else if (jsonClass == JsonObjectArray.class) {
                for (JsonValue jval : json.get(jsonVal.ID).iterator()) {
                    // Only recurse if the value is an object
                    if (jval.type() == JsonValue.ValueType.object) {
                        formatJson(jval, (JsonObject) jsonVal.value);
                    }
                }
            }
            // If the above doesn't apply, then it should already be formatted.
        }
    }

    // TESTING
    public static void main(String[] args) {
        JsonValue jvalue = new JsonValue(JsonValue.ValueType.object); // FileControl.loadJsonData("settings.json");
        JsonValue test1 = new JsonValue(JsonValue.ValueType.object);
        JsonValue test2 = new JsonValue(JsonValue.ValueType.object);
        JsonValue test = new JsonValue(JsonValue.ValueType.array);
        JsonValue endless = new JsonValue(JsonValue.ValueType.array);
        jvalue.addChild("scenarios", test);
        jvalue.addChild("endless", endless);
        test.addChild(test1);
        test.addChild(test2);
        endless.addChild(new JsonValue(JsonValue.ValueType.object));
        formatJson(jvalue, Constants.DefaultJson.scoreFormat());
        // System.out.println(jvalue);
        // jvalue.get("scenarios").addChild("a", new JsonValue("a"));
        System.out.println(jvalue);
    }

}
