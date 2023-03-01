package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

public class JsonArray extends JsonVal<JsonValue.ValueType> {
    public JsonArray(String ID, JsonValue.ValueType valueType) {
        super(ID, valueType);
    }
}
