package com.undercooked.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.util.Constants;

public class Entity {

	public Vector2 pos;
	protected String texturePath;
	protected Sprite sprite;

	public Rectangle collision;
	public float offsetX, offsetY;
	public Entity() {
		this.pos = new Vector2();
		this.collision = new Rectangle();
		this.offsetX = 0;
		this.offsetY = 0;
	}

	public void update(float delta) {
		this.collision.x = pos.x + offsetX;
		this.collision.y = pos.y + offsetY;
	}

	public void draw(SpriteBatch batch) {
		batch.draw(sprite, pos.x, pos.y, collision.width, collision.height);
	}

	public void draw(ShapeRenderer shape) {

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

	public float getX() {
		return pos.x;
	}

	public float getY() {
		return pos.y;
	}

	public void setX(float x) {
		this.pos.x = x;
		this.collision.x = x;
	}

	public void setY(float y) {
		this.pos.y = y;
		this.collision.y = y;
	}
	public void setWidth(float width) {
		this.collision.setWidth(width);
	}

	public void setHeight(float height) {
		this.collision.setWidth(height);
	}

	public void drawDebug(ShapeRenderer shape) {
		shape.setColor(Color.RED);
		shape.rect(collision.x,collision.y,collision.width,collision.height);
		shape.setColor(Color.WHITE);
	}

    public Sprite getSprite() {
		return sprite;
    }
}
