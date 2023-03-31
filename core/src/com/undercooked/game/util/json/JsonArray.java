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
        value.setType(getType());
    }

    @Override
    public void checkChild(JsonValue child, boolean existsBefore) {
        // For all of them, check child
        for (int i = child.size-1 ; i >= 0 ; i--) {
            JsonValue val = child.get(i);
            if (!this.value.isValue(val)) {
                // If it's not valid, remove it
                overrideValue(child.get(i));
            } else {
                // Otherwise, if it's valid, check the child
                this.value.checkChild(child.get(i), existsBefore);
            }
        }
    }

    @Override
    public JsonValue.ValueType getType() {
        return JsonValue.ValueType.array;
    }
}
