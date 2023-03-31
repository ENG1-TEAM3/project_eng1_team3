package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;

public class JsonObjectArray extends JsonVal<JsonObject> {

    public JsonObjectArray(String ID, JsonObject jsonObject) {
        super(ID, jsonObject);
    }

    @Override
    public boolean isValue(JsonValue value) {
        // Return if it's an array or not
        return value.isArray();
    }

    @Override
    public void setValue(JsonValue value, boolean existsBefore) {
        // Format all the JsonValues in the array
        for (JsonValue val : value) {
            JsonFormat.formatJson(val, this.value, existsBefore);
        }
    }

    @Override
    public void format(JsonValue value, boolean existsBefore) {
        // Only continue if it existed before
        if (existsBefore) {
            // Try to format
            setValue(value, true);
        }
    }

    @Override
    public JsonValue.ValueType getType() {
        return JsonValue.ValueType.array;
    }
}
