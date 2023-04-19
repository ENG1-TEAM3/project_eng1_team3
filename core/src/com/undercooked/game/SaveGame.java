package com.undercooked.game;

import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.entity.cook.CookController;
import com.undercooked.game.entity.customer.CustomerController;
import com.undercooked.game.station.StationController;

public class SaveGame {
	public static JsonValue serializeGame(CookController cookController, StationController stationController,
			CustomerController customerController) {
		JsonValue save = new JsonValue(JsonValue.ValueType.object);
		save.addChild("cooks", cookController.serializeCooks());
		save.addChild("stations", stationController.serializeStations());
		save.addChild("customers", customerController.serializeCustomers());
		return save;
	}
}
