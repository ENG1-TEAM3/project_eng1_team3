package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.util.Constants;

public class JsonFormat {

    public static JsonValue formatJson(JsonValue json, JsonObject ideal) {
        return formatJson(json, ideal, false);
    }

    /**
     * Takes a JsonValue and JsonObject input, and outputs a
     * formatted version where any missing values are added.
     * @return The formatted {@link JsonValue}.
     */
    private static JsonValue formatJson(JsonValue json, JsonObject ideal, boolean didNotExist) {
        // If json is null, return null
        if (json == null) {
            return null;
        }
        // Create a copy of the json
        // Loop through the values of the JsonObject
        for (JsonVal jsonVal : ideal.getValues()) {
            boolean exists = true;
            // System.out.println("Checking: " + jsonVal.ID);
            Class jsonClass = jsonVal.getClass();
            // System.out.println("Class: " + jsonClass);
            // Check that the value exists in json
            if (!json.hasChild(jsonVal.ID)) {
                exists = false;
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
                else if (jsonClass == JsonArray.class) {
                    jvalue = new JsonValue(JsonValue.ValueType.array);
                }
                else {
                    // System.out.println(jsonVal.ID + ": Null");
                    jvalue = new JsonValue(JsonValue.ValueType.nullValue);
                }
                json.addChild(jsonVal.ID, jvalue);
            } else {
                JsonValue.ValueType jsonType = json.get(jsonVal.ID).type();
                JsonValue valToSet = json.get(jsonVal.ID);
                // If it's there, make sure it's the right type.
                if (jsonClass == JsonObject.class && jsonType != JsonValue.ValueType.object) {
                    // Set it to an empty object
                    valToSet.setType(JsonValue.ValueType.object);
                }
                else if (jsonClass == JsonString.class && jsonType != JsonValue.ValueType.stringValue) {
                    valToSet.set((String) jsonVal.value);
                }
                else if (jsonClass == JsonFloat.class && jsonType != JsonValue.ValueType.doubleValue) {
                    // System.out.println(jsonVal.ID + ": Float");
                    valToSet.set((Double) jsonVal.value, null);
                }
                else if (jsonClass == JsonInt.class && jsonType != JsonValue.ValueType.doubleValue) {
                    // System.out.println(jsonVal.ID + ": Int");
                    valToSet.set((Double) jsonVal.value, null);
                }
                else if (jsonClass == JsonObjectArray.class && jsonType != JsonValue.ValueType.array) {
                    // System.out.println(jsonVal.ID + ": Object Array");
                    valToSet.setType(JsonValue.ValueType.array);
                }
                else if (jsonClass == JsonType.class && jsonType != jsonVal.value) {
                    // System.out.println(jsonVal.ID + ": " + jsonVal.value);
                    valToSet.setType((JsonValue.ValueType) jsonVal.value);
                }
                else if (jsonClass == JsonArray.class) {
                    if (jsonType != JsonValue.ValueType.array) {
                        valToSet.setType(JsonValue.ValueType.array);
                    } else {
                        // If it's an array, you have to make sure that all the variables
                        // are of the correct type.
                        // Remove them if they aren't.
                        JsonValue jsonArray = json.get(jsonVal.ID);
                        for (int i = jsonArray.size-1 ; i >= 0 ; i--) {
                            JsonValue val = jsonArray.get(i);
                            if (val.type() != jsonVal.value) {
                                jsonArray.remove(i);
                            }
                        }
                    }
                }
                else {
                    // System.out.println(jsonVal.ID + ": Null");
                    valToSet.setType(JsonValue.ValueType.nullValue);
                }
            }
            // If the current json did not exist before this, then skip these two following ifs.
            // This is to prevent infinite loops where the json doesn't have any values (to avoid checking where it doesn't need to)
            if (!didNotExist) {
                // Check the class type
                if (jsonClass == JsonObject.class) {
                    // System.out.println(jsonVal.ID + ": Recurse");
                    // If it's a JsonObject, recurse.
                    // But only recurse if the value is actually an object
                    if (json.type() == JsonValue.ValueType.object) {
                        formatJson(json.get(jsonVal.ID), (JsonObject) jsonVal, exists);
                    }
                }
                // If it's an Object Array, make sure all objects within follow the
                // correct formatting
                else if (jsonClass == JsonObjectArray.class) {
                    for (JsonValue jval : json.get(jsonVal.ID).iterator()) {
                        // Only recurse if the value is an object
                        if (jval.type() == JsonValue.ValueType.object) {
                            formatJson(jval, (JsonObject) jsonVal.value, exists);
                        }
                    }
                }
            }
            // If the above doesn't apply, then it should already be formatted.
        }
        // Finally return the input Json, in case it's needed
        return json;
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
        formatJson(jvalue, Constants.DefaultJson.mapFormat());
        // System.out.println(jvalue);
        // jvalue.get("scenarios").addChild("a", new JsonValue("a"));
        System.out.println(jvalue);
    }

}
