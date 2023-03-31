package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

public class JsonArray extends JsonVal<JsonVal> {
    public JsonArray(String ID, JsonVal valueType) {
        super(ID, valueType);
    }

    @Override
    public boolean isValue(JsonValue value) {
        return value.isArray();
    }

    @Override
    public void setValue(JsonValue value, boolean existsBefore) {
        // Format all the values within
        for (int i = value.size-1 ; i >= 0 ; i--) {
            JsonValue val = value.get(i);
            if (!this.value.isValue(val)) {
                // If it's not valid, remove it
                value.remove(i);
            } else {
                // If the value is valid, format it
                this.value.format(val, true);
            }
        }
    }

    @Override
    public JsonValue.ValueType getType() {
        return JsonValue.ValueType.array;
    }
}
