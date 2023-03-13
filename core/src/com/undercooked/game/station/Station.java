package com.undercooked.game.station;

import java.util.Stack;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.entity.Cook;
import com.undercooked.game.food.Ingredient;
import com.undercooked.game.food.interactions.StationInteractControl;
import com.undercooked.game.map.MapEntity;
import com.undercooked.game.map.MapManager;

import static com.undercooked.game.MainGameClass.shapeRenderer;

/**
 * Represents a station.
 * 
 */
public class Station extends MapEntity {

	StationInteractControl interactControl;
	StationData stationData;

	public Station(StationData stationData) {
		super();
		this.stationData = stationData;
		this.texturePath = stationData.getTexturePath();
		setWidth(stationData.getWidth());
		setHeight(stationData.getHeight());
	}

	public void update(float delta) {

	}

	public void interact() {

	}

	public void create() {

	}

	public void drawStatusBar(SpriteBatch batch) {
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(collision.x * 64, collision.y * 64 + 64 + 64 / 10, 64, 64 / 8);
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.rect(collision.x * 64, collision.y * 64 + 64 + 64 / 10, 64, 64 / 10);
		shapeRenderer.end();
	}

	/**
	 *
	 * @param text Text to be drawn.
	 * @param pos  Position to draw at.
	 */
	public void drawText(SpriteBatch batch, String text, Vector2 pos) {
		batch.begin();
		(new BitmapFont()).draw(batch, text, pos.x, pos.y);
		batch.end();
	}

}
