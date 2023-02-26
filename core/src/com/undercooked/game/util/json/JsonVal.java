package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.JsonValue;

public abstract class JsonVal<T> {
    protected String ID;
    protected T value;
    public JsonVal(String ID, T value) {
        this.ID = ID;
        this.value = value;
    }
    public String getID() {
        return ID;
    }
    public T getValue() {
        return value;
    }
}
