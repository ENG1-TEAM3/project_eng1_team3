package com.undercooked.game.station;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.undercooked.game.interactions.StationInteractControl;

/**
 * Represents a station.
 * 
 */
public class UniqueStation extends Station {
	StationInteractControl interactControl;

	public UniqueStation(StationData stationData) {
		super(stationData);
	}

	public void update(float delta) {
		interactControl.update(null, delta);
	}

	public void drawText(SpriteBatch batch, String text, Vector2 pos) {
		batch.begin();
		(new BitmapFont()).draw(batch, text, pos.x, pos.y);
		batch.end();
	}

}
