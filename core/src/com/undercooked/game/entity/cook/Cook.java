package com.undercooked.game.entity.cook;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.undercooked.game.Input.InputController;
import com.undercooked.game.Input.InputType;
import com.undercooked.game.Input.Keys;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.MoveableEntity;
import com.undercooked.game.food.Item;
import com.undercooked.game.food.ItemStack;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.map.Map;
import com.undercooked.game.map.MapCell;
import com.undercooked.game.station.Station;
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
	private Station stationTarget;
	private int holdLimit;
	public Station lockedTo = null;
	public boolean holding = false;
	public ItemStack heldItems;
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

		this.heldItems = new ItemStack();
		this.holdLimit = 5;

		this.interactTarget = null;
		this.stationTarget = null;
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
		updateTexture();
		// setWalkTexture("entities/cook_walk_" + cookno + ".png");
	}

	public void checkInput(float delta) {
		// Check inputs, and change things based on the result
		dirX = 0;
		dirY = 0;
		// Only check movement if not locked to a station
		if (lockedTo == null) {
			movementCheck();
		}

		// Interact input check
		interactInputCheck();
	}

	private void movementCheck() {
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
	}

	private void interactInputCheck() {
		if (interactTarget == null || stationTarget == null) {
			return;
		}
		// Check for custom interactions
		if (InputController.isKeyJustPressed("take")) {
			// If the station has an item, take it
			if (stationTarget.items.size() > 0) {
				// If the Cook can also take it...
				if (canAddItem()) {
					// Then take the top item on the Station's stack
					addItem(stationTarget.takeItem());
					// If it succeeds, stop here
					return;
				}
			}
		}


		if (InputController.isKeyJustPressed("drop")) {
			// If the cook has an item, drop it
			if (heldItems.size() > 0) {
				// If the station can hold it...
				if (stationTarget.canHoldItem()) {
					// Then add it
					stationTarget.addItem(takeItem());
					// If it succeeds, stop here
					return;
				}
			}
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
					if (interactTarget.getMapEntity() == null) {
						return;
					}
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
			if (interactResult == InteractResult.STOP) {
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
		if (interactTarget != null) {
			if (interactTarget.getMapEntity() != null) {
				Station asStation = (Station) interactTarget.getMapEntity();
				// Only set stationTarget if another cook isn't locked to it
				if (!asStation.hasCookLocked() || asStation.hasCookLocked(this)) {
					// If it doesn't have a cook, then it's the target.
					stationTarget = asStation;
				} else {
					// Otherwise, it can't be the target, as it's already
					// the target of another cook.
					stationTarget = null;
				}
			} else {
				stationTarget = null;
			}
		} else {
			stationTarget = null;
		}

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
		for (Item item : heldItems) {
			//if (ingredient.sprite.getTexture() != null) {
			batch.draw(item.sprite, pos.x + 16, pos.y + 112 + itemIndex * 8, 32, 32);
			itemIndex++;
			//}
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

	public Station getStationTarget() {
		return stationTarget;
	}

	public boolean canAddItems(int number) {
		if (number <= 0) {
			return true;
		}
		// Return whether adding it goes above the limit or not
		return !(heldItems.size() + number > holdLimit);
	}

	public boolean canAddItem() {
		return canAddItems(1);
	}

	public boolean addItem(Item item) {
		// Continue only if it can add it
		if (!canAddItem()) {
			return false;
		}
		// Add the item
		heldItems.add(item);
		updateTexture();
		return true;
	}

	public Item takeItem() {
		// Continue only if the Cook has an item to remove
		if (heldItems.size() <= 0) {
			return null;
		}
		// Pop the item and return it
		Item poppedItem = heldItems.pop();
		updateTexture();
		return poppedItem;
	}

	public void lockToStation(Station station) {
		// If not already locked to a Station
		if (lockedTo != null) {
			return;
		}
		// Then lock to a station.
		lockedTo = station;
		// And tell the Station to lock this Cook
		station.lockCook(this);
	}

	public void unlock() {
		// If lockedTo is null, then just return
		if (lockedTo == null) {
			return;
		}
		// Save the station temporarily, as it needs to be forgotten to prevent a loop
		Station stationTemp = lockedTo;
		lockedTo = null;
		// If locked to a station, then unlock
		stationTemp.unlockCook(this);
	}

	public void updateTexture() {
		if (heldItems.size() > 0) {
			setWalkTexture("entities/cook_walk_hands_" + cookno + ".png");
		} else {
			setWalkTexture("entities/cook_walk_" + cookno + ".png");
		}
	}
}