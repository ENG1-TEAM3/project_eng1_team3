package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;

public class JsonString extends JsonVal<String> {
    public JsonString(String ID, String value) {
        super(ID, value);
    }

    @Override
    public boolean isValue(JsonValue value) {
        return value.isString() || value.isNull();
    }

    @Override
    public void setValue(JsonValue value, boolean existsBefore) {
        // If it's null, then leave it alone
        if (value.type() == JsonValue.ValueType.nullValue) {
            return;
        }
        value.set(this.value);
    }

    @Override
    public JsonValue.ValueType getType() {
        return JsonValue.ValueType.stringValue;
    }
}
