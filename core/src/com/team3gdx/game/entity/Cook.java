package com.team3gdx.game.entity;

import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.food.Ingredient;
import com.team3gdx.game.util.CollisionTile;
import com.team3gdx.game.util.Control;

public class Cook extends Entity {

	private static final int MAX_STACK_SIZE = 5;
	private static final int FRAME_COLS = 5, FRAME_ROWS = 4;

	private Vector2 direction;
	private int cookno;

	private Texture walkSheet;
	private Animation<TextureRegion> walkAnimation;
	private TextureRegion[][] spriteSheet;
	private TextureRegion[][] currentFrame;
	private TextureRegion[] walkFrames;
	private float stateTime = 0;

	public boolean locked = false;
	public boolean holding = false;
	public Stack<Ingredient> heldItems = new Stack<Ingredient>();

	/**
	 * Cook entity constructor
	 * @param pos - x y position vector in pixels
	 * @param cooknum - cook number, changes texture
	 */
	public Cook(Vector2 pos, int cooknum) {
		this.pos = pos;
		this.cookno = cooknum;

		width = 64;
		height = 128;
		speed = 0.25f;
		direction = new Vector2(0, -1);

		setWalkTexture("entities/cook_walk_" + String.valueOf(cookno) + ".png");
	}

	/**
	 * Update cook using user input
	 * @param control - Control input handling object
	 * @param dt - some change in time
	 * @param cl - 2d array of collision tiles for collision detection
	 */
	public void update(Control control, float dt, CollisionTile[][] cl) {
		currentFrame = walkAnimation.getKeyFrame(stateTime, true).split(32, 32);

		dirX = 0;
		dirY = 0;
		if (control.up && !control.down) {
			if (this.checkCollision(pos.x, pos.y + (speed * dt), cl)) {
				dirY = 1;
			}
			setWalkFrames(2);
			direction = new Vector2(0, 1);
		} else if (control.down && !control.up) {
			if (this.checkCollision(pos.x, pos.y - (speed * dt), cl)) {
				dirY = -1;
			}
			setWalkFrames(0);
			direction = new Vector2(0, -1);
		}
		if (control.left && !control.right) {
			if (this.checkCollision(pos.x - (speed * dt), pos.y, cl)) {
				dirX = -1;
			}
			setWalkFrames(1);
			direction = new Vector2(-1, 0);
		} else if (control.right && !control.left) {
			if (this.checkCollision(pos.x + (speed * dt), pos.y, cl)) {
				dirX = +1;
			}
			setWalkFrames(3);
			direction.x = 1;
			direction.y = 1;
			direction = new Vector2(1, 0);
		}
		if (dirX != 0 || dirY != 0) {
			stateTime += Gdx.graphics.getDeltaTime() / 8;
		} else {
			stateTime = 0;
		}
		pos.x += dirX * speed * dt;
		pos.y += dirY * speed * dt;

	}

	/**
	 * Pick up an item
	 * @param item - item to pick up
	 */
	public void pickUpItem(Ingredient item) {
		item.cooking = false;
		item.slicing = false;
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
		walkSheet = new Texture(path);
		spriteSheet = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS,
				walkSheet.getHeight() / FRAME_ROWS);
		walkFrames = new TextureRegion[FRAME_ROWS];
		setWalkFrames(0);
	}

	/**
	 * Set specific walk frames
	 * @param row - row on the sprite sheet to draw
	 */
	private void setWalkFrames(int row) {
		for (int i = 0; i < FRAME_ROWS; i++) {
			walkFrames[i] = spriteSheet[row][i];
		}
		walkAnimation = new Animation<TextureRegion>(0.025f, walkFrames);
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

}