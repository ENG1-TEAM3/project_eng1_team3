package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;

public class JsonLeaf<T> extends JsonVal<JsonValue.ValueType> {
    T value;
    public JsonLeaf(String ID, JsonValue.ValueType type, T value) {
        super(ID, type);
        this.value = value;
    }
}
