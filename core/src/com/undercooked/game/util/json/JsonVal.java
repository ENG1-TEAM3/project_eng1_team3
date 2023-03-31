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

    public abstract boolean isValue(JsonValue value);
    public void format(JsonValue value, boolean existsBefore) {

    }
    public abstract void setValue(JsonValue value, boolean existsBefore);
    public void setValue(JsonValue value) {
        setValue(value, true);
    }
    public void setType(JsonValue value) {
        System.out.println("Set type:" + getType());
        value.setType(getType());
    }
    public abstract JsonValue.ValueType getType();
}
