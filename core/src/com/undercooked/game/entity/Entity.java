package com.undercooked.game.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.util.Constants;

public class Entity {

	public Vector2 pos;

	protected String texturePath;
	protected Sprite sprite;

	public float width;
	public float height;
	public Rectangle collision;
	public Entity() {
		this.pos = new Vector2();
	}

	public void draw(SpriteBatch batch) {
		batch.begin();
		batch.draw(sprite, pos.x, pos.y, width, height);
		batch.end();
	}

	public void load(TextureManager textureManager, String textureID) {
		// Load Texture
		textureManager.loadAsset(textureID, texturePath, "textures");
	}

	public void load(TextureManager textureManager) {
		// Load Texture
		textureManager.loadAsset(Constants.GAME_TEXTURE_ID, texturePath, "textures");
	}

	public void unload(TextureManager textureManager) {
		// Unload Texture
		textureManager.unload(texturePath);
	}

	/**
	 * Function called after the screen has been loaded.
	 */
	public void postLoad(TextureManager textureManager) {
		// Get texture
		this.sprite = new Sprite(textureManager.getAsset(texturePath));
	}

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
