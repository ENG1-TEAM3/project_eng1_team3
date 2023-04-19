package com.undercooked.game.util;

import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.entity.cook.CookController;
import com.undercooked.game.entity.customer.CustomerController;
import com.undercooked.game.files.FileControl;
import com.undercooked.game.station.StationController;

public class SaveLoadGame {
	public static JsonValue serializeGame(CookController cookController, StationController stationController,
			CustomerController customerController) {
		JsonValue save = new JsonValue(JsonValue.ValueType.object);
		save.addChild("cooks", cookController.serializeCooks());
		save.addChild("stations", stationController.serializeStations());
		save.addChild("customers", customerController.serializeCustomers());
		return save;
	}

	public static void saveGame(CookController cookController, StationController stationController,
			CustomerController customerController) {
		JsonValue save = serializeGame(cookController, stationController, customerController);
		FileControl.saveJsonData("save.json", save);
	}

	public static void loadGame(CookController cookController, StationController stationController,
			CustomerController customerController) {
		JsonValue save = FileControl.loadJsonData("save.json");
		cookController.deserializeCooks(save.get("cooks"));
		stationController.deserializeStations(save.get("stations"));
		customerController.deserializeCustomers(save.get("customers"));
	}
}
