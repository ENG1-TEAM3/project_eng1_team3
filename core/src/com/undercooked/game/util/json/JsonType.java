package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;

public class JsonType extends JsonVal<JsonValue.ValueType> {
    public JsonType(String ID, JsonValue.ValueType value) {
        super(ID, value);
    }

    @Override
    public boolean isValue(JsonValue value) {
        return value.type().equals(this.value);
    }

    @Override
    public void setValue(JsonValue value, boolean existsBefore) {
        setType(value);
    }

    @Override
    public JsonValue.ValueType getType() {
        return this.value;
    }
}
