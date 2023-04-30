package com.team3gdx.game.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class SaveService {

    ObjectMapper mapper;

    public SaveService() {
        mapper = new ObjectMapper();
    }

    public Array<GameInfo> getSavedGames() {

        Preferences prefs = Gdx.app.getPreferences("saves");

        Map<String, ?> games = prefs.get();

        Array<GameInfo> result = new Array<>();

        for (String id : games.keySet()) {
            if (games.get(id) instanceof String) {
                try {
                    result.add(mapper.readValue((String)games.get(id), GameInfo.class));
                } catch (JsonProcessingException ignored) {
                    System.out.println(ignored);
                }
            }
        }

        return result;
    }

    public void saveGame(GameInfo game) throws Exception {
        Preferences prefs = Gdx.app.getPreferences("saves");

        String id = UUID.randomUUID().toString();

        String json = null;
        try {
            json = mapper.writeValueAsString(game);
        } catch (JsonProcessingException e) {
            throw new Exception("Game data serialisation failed.");
        }

        prefs.putString(id, json);

        prefs.flush();
    }
}
