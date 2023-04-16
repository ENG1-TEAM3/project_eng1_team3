package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;

public class JsonBool extends JsonVal<Boolean> {
    public JsonBool(String ID, Boolean value) {
        super(ID, value);
    }

    @Override
    public boolean isValue(JsonValue value) {
        return value.isBoolean();
    }

    @Override
    public void setValue(JsonValue value, boolean existsBefore) {
        value.set(this.value);
    }

    @Override
    public JsonValue.ValueType getType() {
        return JsonValue.ValueType.booleanValue;
    }
}
