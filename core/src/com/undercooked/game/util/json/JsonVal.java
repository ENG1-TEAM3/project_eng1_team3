package com.undercooked.game.util.json;

public abstract class JsonVal<T> {
    protected String ID;
    protected T value;

    public JsonVal(String ID, T value) {
        if (ID == null) {
            ID = "";
        }
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
        // System.out.println("Set type: " + getType());
        value.setType(getType());
    }
    public abstract JsonValue.ValueType getType();


    public void overrideValue(JsonValue value) {
        // Set the type
        setType(value);
        // And then update the value
        setValue(value);
    }

    public void addChild(JsonValue root) {
        // Create the value
        JsonValue jValue = new JsonValue(getType());
        // Set it to use the default value
        setValue(jValue, false);
        // And then add it
        root.addChild(ID, jValue);
    }

    public void checkChild(JsonValue child, boolean existsBefore) {
        // If it does, check if the value type is correct
        if (!isValue(child)) {
            overrideValue(child);
        }
        // If type matches, then just try and call
        // the "format" function, in case it's something
        // like a JsonObject.
        format(child, existsBefore);
    }
}
