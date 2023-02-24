package com.undercooked.game.entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.undercooked.game.assets.TextureManager;

public class Entity {

	public Vector2 pos;

	public Texture texture;

	public float width;
	public float height;

	public float speed;

	float dirX = 0;
	float dirY = 0;

	public void draw(SpriteBatch batch) {
		batch.begin();
		batch.draw(texture, pos.x, pos.y, width, height);
		batch.end();
	}

	public void load(TextureManager textureManager) {
		// Load Texture
	}

	public void unload(TextureManager textureManager) {
		// Unload Texture
	}

}
