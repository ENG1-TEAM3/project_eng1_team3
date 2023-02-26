package com.undercooked.game.util.json;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class JsonObject extends JsonVal<ObjectMap<String, JsonVal>> {

    public JsonObject(String ID) {
        super(ID, new ObjectMap<String, JsonVal>());
    }
    public JsonObject() {
        this("");
    }

    public void addValue(JsonVal jsonVal) {
        value.put(jsonVal.getID(), jsonVal);
    }

    public Array<JsonVal> getValues() {
        return value.values().toArray();
    }
}
