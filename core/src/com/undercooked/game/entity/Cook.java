package com.undercooked.game.entity;

import java.util.Stack;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.undercooked.game.Input.InputController;
import com.undercooked.game.Input.InputType;
import com.undercooked.game.Input.Keys;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.food.Ingredient;
import com.undercooked.game.food.interactions.InteractResult;
import com.undercooked.game.map.Map;
import com.undercooked.game.map.MapCell;
import com.undercooked.game.util.CollisionTile;

public class Cook extends MoveableEntity {

	private static final int MAX_STACK_SIZE = 5;
	private static final int FRAME_COLS = 5, FRAME_ROWS = 4;

	private Vector2 direction;
	private final int cookno;
	private final TextureManager textureManager;
	private Animation<TextureRegion> walkAnimation;
	private TextureRegion[][] spriteSheet;
	private TextureRegion currentFrame;
	private float stateTime = 0;

	private Rectangle interactCollision;
	private MapCell interactTarget;
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
		super();
		this.pos = pos;
		this.collision.x = pos.x;
		this.collision.y = pos.y;
		this.cookno = cookNum;

		this.interactTarget = null;
		this.interactCollision = new Rectangle(collision.x, collision.y, 16,16);

		collision.width = 40;
		offsetX = (64 - collision.getWidth())/2;
		collision.height = 10;
		offsetY = -collision.height/2;
		speed = 2.5f;
		direction = new Vector2(0, -1);

		this.textureManager = textureManager;
		this.map = map;
	}

	@Override
	public void load(TextureManager textureManager, String textureGroup) {
		textureManager.load(textureGroup, "entities/cook_walk_" + cookno + ".png");
		textureManager.load(textureGroup, "entities/cook_walk_hands_" + cookno + ".png");
	}

	@Override
	public void postLoad(TextureManager textureManager) {
		setWalkTexture("entities/cook_walk_" + cookno + ".png");
	}

	public void checkInput(float delta) {
		// Check inputs, and change things based on the result
		dirX = 0;
		dirY = 0;
		if (InputController.isKeyPressed(Keys.cook_down)) {
			dirY -= 1;
			setWalkFrames(0);
			direction = new Vector2(0, -1);
		}
		if (InputController.isKeyPressed(Keys.cook_up)) {
			dirY += 1;
			setWalkFrames(2);
			direction = new Vector2(0, 1);
		}
		if (InputController.isKeyPressed(Keys.cook_left)) {
			dirX -= 1;
			setWalkFrames(1);
			direction = new Vector2(-1, 0);
		}
		if (InputController.isKeyPressed(Keys.cook_right)) {
			dirX += 1;
			setWalkFrames(3);
			direction = new Vector2(1, 0);
		}

		// Interact input check
		interactInputCheck();
	}

	private void interactInputCheck() {
		if (interactTarget == null) {
			return;
		}
		// Check for every input
		for (Object curKey : InputController.getInputs().keys().iterator()) {
			String keyID = (String) curKey;
			// If it's not an interaction key, then ignore and skip
			if (!InputController.isInteraction(keyID)) {
				continue;
			}
			InteractResult interactResult = InteractResult.NONE;
			// Loop through the InputTypes
			for (InputType inputType : InputType.values()) {
				if (InputController.isKey(keyID, inputType)) {
					interactResult = interactTarget.getMapEntity().interact(this, keyID, inputType);
					// If it's repeat, then repeat the check
					if (interactResult == InteractResult.RESTART) {
						// Start the check over again
						interactInputCheck();
						// And then stop this check
						interactResult = InteractResult.STOP;
					}
					if (interactResult == InteractResult.STOP) {
						break;
					}
				}
			}
			// If it's not null for interaction result, then just ignore other keys.
			if (interactResult != InteractResult.STOP) {
				break;
			}
		}
	}

	/**
	 * Update cook using user input
	 * @param delta - The time difference from the last frame
	 */
	public void update(float delta) {
		// Update super
		super.update(delta);

		// Check collision
		// X
		if (map.checkCollision(this, collision.x + (moveCalc(dirX, delta)), collision.y)) {
			// Move the player as close as possible on the x
			while (!map.checkCollision(this, collision.x + 0.01F * dirX, collision.y)) {
				collision.x += 0.01F * dirX;
			}
			collision.x -= 0.01F * dirX;
			pos.x = collision.x-offsetX;
			dirX = 0;
		}

		// Y
		if (map.checkCollision(this, collision.x, collision.y + (moveCalc(dirY, delta)))) {
			// Move the player as close as possible on the y
			while (!map.checkCollision(this, collision.x, collision.y + 0.01F * dirY)) {
				collision.y += 0.01F * dirY;
			}
			collision.y -= 0.01F * dirY;
			pos.y = collision.y-offsetY;
			dirY = 0;
		}

		// Move
		move(delta);
		interactCollision.x = collision.x + collision.width/2 + (direction.x * 32) - interactCollision.width/2;
		interactCollision.y = 64 + collision.y + collision.height/2 + (direction.y * 32) - interactCollision.width/2;

		// Update the cell that is currently being looked at
		interactTarget = map.getCollision(interactCollision, true, Map.CollisionType.INTERACTABLE);

		// Update animation
		currentFrame = walkAnimation.getKeyFrame(stateTime, true);
		if (dirX != 0 || dirY != 0) {
			stateTime += delta;
		} else {
			stateTime = 0;
		}

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
	/*public void draw_bot(SpriteBatch batch) {
		batch.draw(currentFrame[1][0], pos.x, pos.y, 64, 64);
	}*/

	/**
	 * Draw top of cook
	 * @param batch - spritebatch to draw with
	 */
	/*public void draw_top(SpriteBatch batch) {
		batch.draw(currentFrame[0][0], pos.x, pos.y + 64, 64, 64);
	}*/

	/**
	 * Draw top of cook at a certain position - used for the display in the top right
	 * @param batch - spritebatch to draw with
	 * @param position - position in pixels to draw at
	 */
	/*public void draw_top(SpriteBatch batch, Vector2 position) {
		batch.draw(currentFrame[0][0], position.x, position.y + 128, 128, 128);
	}*/

	/**
	 * Draw the {@link Cook}.
	 * @param batch
	 */
	@Override
	public void draw(SpriteBatch batch) {
		batch.draw(currentFrame, pos.x, pos.y, 64, 128);
		drawHeldItems(batch);
	}

	@Override
	public void drawDebug(ShapeRenderer shape) {
		super.drawDebug(shape);
		shape.setColor(Color.GREEN);
		shape.rect(interactCollision.x,interactCollision.y,interactCollision.width,interactCollision.height);
		shape.setColor(Color.WHITE);
	}

	/**
	 * Draw the top half of the {@link Cook} at the location provided.
	 * @param batch {@link SpriteBatch} : The {@link SpriteBatch} to draw with.
	 * @param x {@code float} : The {@code x} position to draw at.
	 * @param y {@code float} : The {@code y} position to draw at.
	 */
	public void draw_top(SpriteBatch batch, int x, int y) {
		TextureRegion chef_top = new TextureRegion(currentFrame, 0, 0, currentFrame.getRegionWidth(), currentFrame.getRegionHeight()/2);
		batch.draw(chef_top, x, y, 128, 128);
	}

	public void drawHeldItems(SpriteBatch batch) {
		int itemIndex = 0;
		for (Entity ingredient : heldItems) {
			ingredient.pos.x = pos.x + 16;
			ingredient.pos.y = pos.y + 112 + itemIndex * 8;
			ingredient.draw(MainGameClass.batch);
			itemIndex++;
		}
	}

	/**
	 * Set the texture to draw with
	 * @param path - Filepath to texture
	 */
	private void setWalkTexture(String path) {
		Texture walkSheet = textureManager.get(path);
		spriteSheet = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS,
				walkSheet.getHeight() / FRAME_ROWS);
		// walkFrames = new TextureRegion[FRAME_COLS];
		setWalkFrames(0);
	}

	/**
	 * Set specific walk frames
	 * @param row - row on the sprite sheet to draw
	 */
	private void setWalkFrames(int row) {
		/*for (int i = 0; i < FRAME_COLS; i++) {
			walkFrames[i] = spriteSheet[row][i];
		}*/
		walkAnimation = new Animation<>(0.09f, spriteSheet[row]);
		currentFrame = walkAnimation.getKeyFrame(stateTime, true);
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
		return collision.width;
	}

	/**
	 * return cook height
	 * @return cook height
	 */
	public float getHeight() {
		return collision.height;
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

	public MapCell getInteractTarget() {
		return interactTarget;
	}
}