package com.team3gdx.game.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class SaveService {

    ObjectMapper mapper;

    public SaveService() {
        mapper = new ObjectMapper();
    }

    public List<GameInfo> getSavedGames() {

        Preferences prefs = Gdx.app.getPreferences("saves");

        Map<String, ?> games = prefs.get();

        ArrayList<GameInfo> result = new ArrayList<>();

        for (String id : games.keySet()) {
            if (games.get(id) instanceof String) {
                try {
                    result.add(mapper.readValue((String)games.get(id), GameInfo.class));
                } catch (JsonProcessingException ignored) {

                }
            }
        }

        return result;
    }

    public void saveGame(GameInfo game) {
        Preferences prefs = Gdx.app.getPreferences("saves");

        String id = UUID.randomUUID().toString();

        String json = mapper.writeValueAsString(game);

        prefs.putString(id, json);
    }
}
