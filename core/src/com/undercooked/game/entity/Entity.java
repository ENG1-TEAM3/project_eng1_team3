package com.undercooked.game.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.util.Constants;

public class Entity {

	public Vector2 pos;

	String texturePath;
	public Sprite sprite;

	public float width;
	public float height;
	public Rectangle collision;

	public void draw(SpriteBatch batch) {
		batch.begin();
		batch.draw(sprite, pos.x, pos.y, width, height);
		batch.end();
	}

	public void load(TextureManager textureManager, String textureID) {
		// Load Texture
		textureManager.load(textureID, texturePath);
	}

	public void load(TextureManager textureManager) {
		// Load Texture
		textureManager.load(Constants.GAME_TEXTURE_ID, texturePath);
	}

	public void unload(TextureManager textureManager) {
		// Unload Texture
		textureManager.unload(texturePath);
	}

	/**
	 * Function called after the screen has been loaded.
	 */
	public void postLoad() {}

	public void setTexture(String texturePath) {
		this.texturePath = texturePath;
	}

	public boolean isColliding(Entity entity) {
		return collision.overlaps(entity.collision);
	}

	public boolean isColliding(Rectangle rect) {
		return collision.overlaps(rect);
	}

}
