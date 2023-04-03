package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

public class JsonOr extends JsonVal<JsonVal[]> {

    public JsonOr(String ID, JsonVal[] value) {
        super(ID, value);
    }

    @Override
    public boolean isValue(JsonValue value) {
        // Make sure at least one value matches
        for (JsonVal jsonVal : this.value) {
            // System.out.println("TYPE: " + jsonVal.getType() + " VS: " + value.type());
            if (jsonVal.isValue(value)) {
                return true;
            }
        }
        // System.out.println("NO MATCH");
        // Otherwise return false.
        return false;
    }

    @Override
    public void setValue(JsonValue value, boolean existsBefore) {
        // Just set it to null if it tries to set here
        value.setType(JsonValue.ValueType.nullValue);
    }

    @Override
    public JsonValue.ValueType getType() {
        // If no or, return null
        if (this.value.length == 0) {
            return JsonValue.ValueType.nullValue;
        }
        // Otherwise, return the type of the first or
        return this.value[0].getType();
    }

    @Override
    public void addChild(JsonValue root) {
        // If no or, add a null value
        if (this.value.length == 0) {
            // Create the value
            JsonValue jValue = new JsonValue(JsonValue.ValueType.nullValue);
            // And then add it
            root.addChild(ID, jValue);
            return;
        }
        // Otherwise, add the first or as the child
        this.value[0].addChild(root);
    }

    @Override
    public void checkChild(JsonValue child, boolean existsBefore) {
        // Check if this has at least one or
        if (this.value.length == 0) {
            // If it doesn't, just set it
            setValue(child, existsBefore);
            // and then stop here
            return;
        }
        // When checking the child, check for a single valid one
        int valid = -1;
        for (int i = 0 ; i < this.value.length ; i++) {
            JsonVal jsonVal = this.value[i];
            // Check if it's valid
            if (jsonVal.isValue(child)) {
                // If it is, break from the loop, and set the
                // valid index
                valid = i;
                break;
            }
        }
        // If it's invalid, just set it to check the first child
        if (valid < 0) {
            this.value[0].checkChild(child, existsBefore);
            return;
        }
        System.out.println(this.value[valid]);
        // Otherwise, if it's valid, then use the valid child to set the value
        this.value[valid].checkChild(child, existsBefore);
    }
}
