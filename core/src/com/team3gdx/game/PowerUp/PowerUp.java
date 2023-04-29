package com.team3gdx.game.PowerUp;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.entity.Entity;

/**
 * Represents an ingredient.
 * 
 */
public class PowerUp extends Entity {
	public final float duration;
	public String name;
	private float timeElapsed;

	/**
	 * Sets the appropriate properties.
	 * 
	 * @param pos             The (x, y) coordinates of the ingredient.
	 * @param width           The ingredient's texture width.
	 * @param height          The ingredient's texture height.
	 * @param name            The name of the ingredient and texture.
	 */
	public PowerUp(Vector2 pos, float width, float height, String name, float duration) {
		this.duration = duration;
		this.pos = pos;
		this.width = width;
		this.height = height;
		this.name = name;
		this.texture = new Texture("PowerUps/" + name + ".png");

	}

	/**
	 * Creates a new instance with identical properties.
	 *
	 * @param powerUp  The ingredient to clone.
	 */
	public PowerUp(PowerUp powerUp) {
		this.duration = powerUp.duration;
		this.pos = powerUp.pos;
		this.width = powerUp.width;
		this.height = powerUp.height;
		this.name = powerUp.name;
		this.texture = powerUp.texture;
	}

	public void addTime(float delta) {
		timeElapsed += delta;
	}

	public boolean isComplete() {
		return timeElapsed >= duration;
	}
}