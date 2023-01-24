package com.team3gdx.game.station;

import com.badlogic.gdx.math.Vector2;

public class ServingStation extends Station {

	// Allow only components to be placed at station (to give to customer!)

	public ServingStation(Vector2 pos) {
		super(pos, 1, false, null);
	}

}
