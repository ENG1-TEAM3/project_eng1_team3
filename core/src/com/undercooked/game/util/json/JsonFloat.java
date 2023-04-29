package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;

public class JsonFloat extends JsonVal<Float> {
    public JsonFloat(String ID, Float value) {
        super(ID, value);
    }

    @Override
    public boolean isValue(JsonValue value) {
        return value.isNumber();
    }

    @Override
    public void setValue(JsonValue value, boolean existsBefore) {
        value.set(this.value, null);
    }

    @Override
    public JsonValue.ValueType getType() {
        return JsonValue.ValueType.doubleValue;
    }
}
