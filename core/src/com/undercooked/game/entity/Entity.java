package com.undercooked.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.util.Constants;

/**
 * A class which provides the basic functionality of creating an {@link Entity}
 * that can be given a position, check for collision and be drawn to a
 * {@link com.badlogic.gdx.Screen}.
 */
public class Entity {

	/**
	 * The current position of the {@link Entity}.
	 */
	public Vector2 pos;

	/**
	 * The path to the {@link Entity}'s texture, as an asset path.
	 */
	protected String texturePath;

	/**
	 * The {@link Sprite} that is used to draw the {@link Entity}'s
	 * {@link com.badlogic.gdx.graphics.Texture}.
	 */
	protected Sprite sprite;

	/**
	 * The collision {@link Rectangle} of the {@link Entity}.
	 */
	public Rectangle collision;

	/**
	 * The difference in location for {@link #pos} in comparison
	 * to the {@link #collision}.
	 */
	public float offsetX, offsetY;

	/**
	 * The constructor for the {@link Entity}.
	 * <br>Sets up {@link #pos} and {@link #collision}.
	 */
	public Entity() {
		this.pos = new Vector2();
		this.collision = new Rectangle();
		this.offsetX = 0;
		this.offsetY = 0;
	}

	/**
	 * The update function of the {@link Entity}. Should be called
	 * when the {@link Entity} needs to be updated, usually every frame.
	 * @param delta {@code float} : The time since the last frame.
	 */
	public void update(float delta) {
		this.collision.x = pos.x + offsetX;
		this.collision.y = pos.y + offsetY;
	}

	/**
	 * Draw the {@link Entity} using sprites.
	 * @param batch {@link SpriteBatch} : The Sprite Batch to use.
	 */
	public void draw(SpriteBatch batch) {
		batch.draw(sprite, pos.x, pos.y, collision.width, collision.height);
	}

	/**
	 * Draw the {@link Entity} using shapes.
	 * @param shape {@link ShapeRenderer} : The Shape Renderer to use.
	 */
	public void draw(ShapeRenderer shape) {

	}

	/**
	 * Draw post, drawn after the two {@link #draw(SpriteBatch)} and
	 * {@link #draw(ShapeRenderer)} functions.
	 * @param batch {@link SpriteBatch} : The Sprite Batch to use.
	 */
	public void drawPost(SpriteBatch batch) {

	}

	/**
	 * Called to load the {@link Entity}.
	 * @param textureManager {@link TextureManager} : The Texture Manager to load
	 * 								{@link com.badlogic.gdx.graphics.Texture}s.
	 * @param textureGroup {@link String} : The texture group to load to.
	 */
	public void load(TextureManager textureManager, String textureGroup) {
		// Load Texture
		textureManager.loadAsset(textureGroup, texturePath, "textures");
	}

	/**
	 * Called to load the {@link Entity}.
	 * @param textureManager {@link TextureManager} : The Texture Manager to load
	 * 								{@link com.badlogic.gdx.graphics.Texture}s.
	 */
	public void load(TextureManager textureManager) {
		// Load Texture
		textureManager.loadAsset(Constants.GAME_TEXTURE_ID, texturePath, "textures");
	}

	/**
	 * Called to unload the {@link Entity}.
	 * @param textureManager {@link TextureManager} : The Texture Manager to unload
	 * 								{@link com.badlogic.gdx.graphics.Texture}s.
	 */
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

	/**
	 * Sets the texture path of the {@link Entity}, used when
	 * {@link #postLoad(TextureManager)} is called.
	 * <br>The format is that of an asset path.
	 * @param texturePath {@link String} : The path to the {@link com.badlogic.gdx.graphics.Texture}.
	 */
	public void setTexture(String texturePath) {
		this.texturePath = texturePath;
	}

	/**
	 * Returns whether the collision {@link Rectangle} of this {@link Entity}
	 * is overlapping another {@link Entity}'s collision {@link Rectangle}.
	 * @param entity {@link Entity} : The entity to compare against.
	 * @return {@code boolean} : Whether it is (true) or not (false) overlapping.
	 */
	public boolean isColliding(Entity entity) {
		return collision.overlaps(entity.collision);
	}

	/**
	 * Returns whether the collision {@link Rectangle} of this {@link Entity}
	 * is overlapping another {@link Rectangle}.
	 * @param rect {@link Rectangle} : The rectangle to compare against.
	 * @return {@code boolean} : Whether it is (true) or not (false) overlapping.
	 */
	public boolean isColliding(Rectangle rect) {
		return collision.overlaps(rect);
	}


	/**
	 * Get the X position.
	 * @return {@code float} : The x position of the {@link Entity}.
	 */
	public float getX() {
		return pos.x;
	}

	/**
	 * Get the Y position.
	 * @return {@code float} : The y position of the {@link Entity}.
	 */
	public float getY() {
		return pos.y;
	}

	/**
	 * Set the X position
	 * @param x {@code float} : The new x position of the {@link Entity}.
	 */
	public void setX(float x) {
		this.pos.x = x;
		this.collision.x = x;
	}

	/**
	 * Set the Y position
	 * @param y {@code float} : The new y position of the {@link Entity}.
	 */
	public void setY(float y) {
		this.pos.y = y;
		this.collision.y = y;
	}

	/**
	 * Set the collision width.
	 * @param width {@code float} : The new width of the {@link Entity}.
	 */
	public void setWidth(float width) {
		this.collision.setWidth(width);
	}

	/**
	 * Set the collision height.
	 * @param height {@code float} : The new height of the {@link Entity}.
	 */
	public void setHeight(float height) {
		this.collision.setWidth(height);
	}

	/**
	 * Debug drawing using the {@link ShapeRenderer}.
	 * @param shape {@link ShapeRenderer} : The renderer to use.
	 */
	public void drawDebug(ShapeRenderer shape) {
		shape.setColor(Color.RED);
		shape.rect(collision.x,collision.y,collision.width,collision.height);
		shape.setColor(Color.WHITE);
	}

	/**
	 * Get the Sprite.
	 * @return {@link Sprite} : The {@link Sprite} of the {@link Entity}.
	 */
    public Sprite getSprite() {
		return sprite;
    }
}
