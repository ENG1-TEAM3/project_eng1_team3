package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;

public class JsonString extends JsonVal<String> {
    boolean allowNull;
    public JsonString(String ID, String value) {
        super(ID, value);
        // By default, allow null
        this.allowNull = true;
    }

    public JsonString(String ID, String value, boolean allowNull) {
        super(ID, value);
        this.allowNull = allowNull;
        // If not allowed null, and default value is null, then
        // instead change it to an empty string.
        if (!allowNull && value == null) {
            this.value = "";
        }
    }

    @Override
    public boolean isValue(JsonValue value) {
        return value.isString() || (allowNull && value.isNull());
    }

    @Override
    public void setValue(JsonValue value, boolean existsBefore) {
        // If null is allowed, then leave it alone
        if (allowNull && value.isNull()) {
            return;
        }
        value.set(this.value);
    }

    @Override
    public JsonValue.ValueType getType() {
        return JsonValue.ValueType.stringValue;
    }
}
