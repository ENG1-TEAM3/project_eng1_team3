package com.undercooked.game.util;

import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.GameType;
import com.undercooked.game.entity.cook.CookController;
import com.undercooked.game.entity.customer.CustomerController;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.food.Items;
import com.undercooked.game.logic.EndlessLogic;
import com.undercooked.game.logic.GameLogic;
import com.undercooked.game.logic.ScenarioLogic;
import com.undercooked.game.map.Map;
import com.undercooked.game.station.StationController;

public class SaveLoadGame {

	public static void saveGame(GameLogic gameLogic) {
		JsonValue save = gameLogic.serialise();
		FileControl.saveJsonData("save.json", save);
	}

	public static JsonValue loadGameJson() {
		return FileControl.loadJsonData("save.json");
	}

	public static void loadGame(GameLogic gameLogic, JsonValue saveData) {
		gameLogic.deserialise(saveData);
	}
}
