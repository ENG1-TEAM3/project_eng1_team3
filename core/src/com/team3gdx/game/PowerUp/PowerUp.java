package com.team3gdx.game.PowerUp;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.entity.Entity;
import com.team3gdx.game.screen.GameScreen;
import com.team3gdx.game.screen.GameScreen.STATE;

/**
 * Represents an ingredient.
 * 
 */
public class PowerUp extends Entity {


	public String name;

	/**
	 * Sets the appropriate properties.
	 * 
	 * @param pos             The (x, y) coordinates of the ingredient.
	 * @param width           The ingredient's texture width.
	 * @param height          The ingredient's texture height.
	 * @param name            The name of the ingredient and texture.
	 */
	public PowerUp(Vector2 pos, float width, float height, String name) {
		this.pos = pos;
		this.width = width;
		this.height = height;
		this.name = name;
		this.texture = new Texture("powerUp/" + name + ".png");

	}

	/**
	 * Creates a new instance with identical properties.
	 * 
	 * @param ingredient The ingredient to clone.
	 */
	public PowerUp(PowerUp powerUp) {
		this.pos = powerUp.pos;
		this.width = powerUp.width;
		this.height = powerUp.height;
		this.name = powerUp.name;
		this.texture = powerUp.texture;
	}
}