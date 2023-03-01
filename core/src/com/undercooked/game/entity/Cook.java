package com.undercooked.game.entity;

import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.undercooked.game.Input.InputController;
import com.undercooked.game.Input.Keys;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.food.Ingredient;
import com.undercooked.game.map.Map;
import com.undercooked.game.map.MapEntity;
import com.undercooked.game.util.CollisionTile;
import com.undercooked.game.util.Control;

public class Cook extends MoveableEntity {

	private static final int MAX_STACK_SIZE = 5;
	private static final int FRAME_COLS = 8, FRAME_ROWS = 4;

	private Vector2 direction;
	private int cookno;

	private Texture walkSheet;
	private TextureManager textureManager;
	private Animation<TextureRegion> walkAnimation;
	private TextureRegion[][] spriteSheet;
	private TextureRegion[][] currentFrame;
	private TextureRegion[] walkFrames;
	private float stateTime = 0;

	public boolean locked = false;
	public boolean holding = false;
	public Stack<Ingredient> heldItems = new Stack<>();
	Map map;

	/**
	 * Cook entity constructor
	 * @param pos - x y position vector in pixels
	 * @param cookNum - cook number, changes texture
	 */
	public Cook(Vector2 pos, int cookNum, TextureManager textureManager, Map map) {
		this.pos = pos;
		this.cookno = cookNum;

		width = 64;
		height = 128;
		speed = 0.25f;
		direction = new Vector2(0, -1);

		this.textureManager = textureManager;
		this.map = map;

		setWalkTexture("entities/cook_walk_" + cookno + ".png");
	}

	public void checkInput(float delta) {
		// Check inputs, and change things based on the result
		dirX = 0;
		dirY = 0;
		if (InputController.isKeyPressed(Keys.cook_down)) {
			dirY -= 1;
			setWalkFrames(2);
			direction = new Vector2(0, 1);
		} else if (InputController.isKeyPressed(Keys.cook_up)) {
			dirY += 1;
			setWalkFrames(0);
			direction = new Vector2(0, -1);
		}
		if (InputController.isKeyPressed(Keys.cook_left)) {
			dirX -= 1;
			setWalkFrames(1);
			direction = new Vector2(-1, 0);
		} else if (InputController.isKeyPressed(Keys.cook_right)) {
			dirY -= 1;
			setWalkFrames(3);
			direction = new Vector2(1, 0);
		}

		// X
		if (map.checkCollision(this, pos.x + (speed * delta), pos.y)) {
			dirX = 0;
		}

		// Y
		if (map.checkCollision(this, pos.x + (dirX * speed * delta), pos.y)) {
			dirX = 0;
		}

		currentFrame = walkAnimation.getKeyFrame(stateTime, true).split(32, 32);
		if (dirX != 0 || dirY != 0) {
			stateTime += Gdx.graphics.getDeltaTime();
		} else {
			stateTime = 0;
		}
	}

	/**
	 * Update cook using user input
	 * @param delta - The time difference from the last frame
	 */
	public void update(float delta) {
		// Move
		pos.x += dirX * speed * delta;
		pos.y += dirY * speed * delta;

		// Reset move speed
		dirX = 0;
		dirY = 0;
	}

	/**
	 * Pick up an item
	 * @param item - item to pick up
	 */
	public void pickUpItem(Ingredient item) {
		if (!holding) {
			holding = true;
			setWalkTexture("entities/cook_walk_hands_" + cookno + ".png");
		}

		if (!full())
			heldItems.push(item);
	}

	/**
	 * Put down the item on the top of the stack
	 */
	public void dropItem() {
		if (heldItems.size() == 1) {
			holding = false;
			setWalkTexture("entities/cook_walk_" + cookno + ".png");
		}
		if (heldItems.size() > 0) {
			heldItems.pop();
		}
		if (heldItems.size() == 0) {
			holding = false;
			setWalkTexture("entities/cook_walk_" + cookno + ".png");
		}
	}

	public boolean full() {
		return heldItems.size() >= MAX_STACK_SIZE;
	}

	/**
	 * Draw bottom of cook
	 * @param batch - spritebatch to draw with
	 */
	public void draw_bot(SpriteBatch batch) {
		batch.draw(currentFrame[1][0], pos.x, pos.y, 64, 64);
	}

	/**
	 * Draw top of cook
	 * @param batch - spritebatch to draw with
	 */
	public void draw_top(SpriteBatch batch) {
		batch.draw(currentFrame[0][0], pos.x, pos.y + 64, 64, 64);
	}

	/**
	 * Draw top of cook at a certain position - used for the display in the top right
	 * @param batch - spritebatch to draw with
	 * @param position - position in pixels to draw at
	 */
	public void draw_top(SpriteBatch batch, Vector2 position) {
		batch.draw(currentFrame[0][0], position.x, position.y + 128, 128, 128);
	}

	/**
	 * Set the texture to draw with
	 * @param path - Filepath to texture
	 */
	private void setWalkTexture(String path) {
		walkSheet = textureManager.get(path);
		spriteSheet = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS,
				walkSheet.getHeight() / FRAME_ROWS);
		walkFrames = new TextureRegion[FRAME_COLS];
		setWalkFrames(0);
	}

	/**
	 * Set specific walk frames
	 * @param row - row on the sprite sheet to draw
	 */
	private void setWalkFrames(int row) {
		for (int i = 0; i < FRAME_COLS; i++) {
			walkFrames[i] = spriteSheet[row][i];
		}
		walkAnimation = new Animation<>(0.09f, walkFrames);
		currentFrame = walkAnimation.getKeyFrame(stateTime, true).split(32, 32);
	}

	/**
	 * Check collision with collide tiles at a certain coordinate
	 * @param cookx - cook x pixel coordinate
	 * @param cooky - cook y pixel coordinate
	 * @param cltiles - 2d Collision tiles array
	 * @return True if the cook can move, false if they cant
	 */
	public Boolean checkCollision(float cookx, float cooky, CollisionTile[][] cltiles) {
		if (cooky - 10 < 0) {
			return false;
		}
		int wid = cltiles.length;
		int hi = cltiles[0].length;
		for (int x = 0; x < wid; x++) {
			for (int y = 0; y < hi; y++) {
				if (cltiles[x][y] != null) {
					if (Intersector.overlaps(cltiles[x][y].returnRect(), this.getCollideBoxAtPosition(cookx, cooky))) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Return a rectangle which is the hitbox of the cook at a certain coordinate
	 * @param x - x pixel coordinate
	 * @param y - y pixel coordinate
	 * @return Rectangle object of the cook hitbox
	 */
	Rectangle getCollideBoxAtPosition(float x, float y) {
		return new Rectangle(x + 12, y - 10, 40, 25);
	}

	/**
	 * Return cook x pixel coordinate
	 * @return cook x pixel coordinate
	 */
	public float getX() {
		return pos.x;
	}

	/**
	 * Return cook y pixel coordinate
	 * @return cook y pixel coordinate
	 */
	public float getY() {
		return pos.y;
	}

	/**
	 * Return cook width
	 * @return cook width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * return cook height
	 * @return cook height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * Return cook direction vector
	 * @return cook direction vector
	 */
	public Vector2 getDirection() {
		return direction;
	}

	public int getCookNo() {
		return cookno;
	}
}