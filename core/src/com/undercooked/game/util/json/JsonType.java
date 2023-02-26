package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;

public class JsonType extends JsonVal<JsonValue.ValueType> {
    public JsonType(String ID, JsonValue.ValueType value) {
        super(ID, value);
    }
}
