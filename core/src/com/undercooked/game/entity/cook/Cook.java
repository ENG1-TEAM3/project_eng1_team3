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
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
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
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.Listener;
import com.undercooked.game.util.Observer;
import com.undercooked.game.util.json.JsonArray;
import com.undercooked.game.util.json.JsonObject;
import com.undercooked.game.util.json.JsonVal;

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
	protected Listener<Cook> serveListener;
	protected Listener<MapCell> interactRegisterListener;
	protected Listener<MapCell> interactPhoneListener;
	protected Listener<Integer> moneyUsedListener;
	protected Observer<Integer> moneyObserver;

	/**
	 * Cook entity constructor
	 * 
	 * @param pos     - x y position vector in pixels
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
		this.interactCollision = new Rectangle(collision.x, collision.y, 16, 16);

		collision.width = 40;
		offsetX = (64 - collision.getWidth()) / 2;
		collision.height = 10;
		offsetY = -collision.height / 2;
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
		// If they're not null, make sure they're not disabled
		if (stationTarget.isDisabled()) {
			// If they're disabled, then try to buy the station if
			// interact is pressed
			if (InputController.isKeyJustPressed("interact")) {
				if (moneyUsedListener != null && moneyObserver != null) {
					if (stationTarget.buy(moneyObserver.observe())) {
						moneyUsedListener.tell(stationTarget.getPrice());
					}
				}
			}
			return;
		}

		/// Custom station interactions
		// If it's a phone...
		if (stationTarget.getID().equals(Constants.PHONE_ID)) {
			// Then check if the player is interacting with it
			if (InputController.isKeyJustPressed("interact")) {
				// If they are, and the listener for it isn't null, then tell
				// the phone interaction listener that it has been interacted with
				if (interactPhoneListener != null) {
					interactPhoneListener.tell(interactTarget);
				}
				return;
			}

			// Phones can not have any other interactions
			return;
		}

		// If it's a Register...
		if (stationTarget.getID().equals(Constants.REGISTER_ID)) {
			// Then do a few custom checks
			// If they're trying to interact, call the Interact listener
			// if it exists
			if (InputController.isKeyJustPressed("interact")) {
				if (interactRegisterListener != null) {
					interactRegisterListener.tell(interactTarget);
				}
				return;
			}

			// If the above doesn't apply, then check if they're trying to
			// put down their item
			if (InputController.isKeyJustPressed("drop")) {
				// If they are, check if the serve listener exists
				if (serveListener != null) {
					// If it does, then tell it that the Cook is trying to serve
					serveListener.tell(this);
				}
				return;
			}

			// Registers can not have any other interactions.
			return;
		}

		// Station interactions
		// Check for station interactions
		if (InputController.isKeyJustPressed("take")) {
			// If the station has an item, take it
			if (stationTarget.items.size() > 0) {
				// If the Cook can also take it...
				if (canAddItem()) {
					// Then take the top item on the Station's stack
					addItem(stationTarget.takeItem());
					// If the cook is locked, also unlock them
					if (stationTarget.hasCookLocked()) {
						unlock();
					}
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

		interactionsCheck();

	}

	private void interactionsCheck() {
		// Make sure the target is still valid
		if (interactTarget == null || stationTarget == null) {
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
					if (interactTarget.getMapEntity() == null) {
						return;
					}
					interactResult = interactTarget.getMapEntity().interact(this, keyID, inputType);
					// If it's repeat, then repeat the check
					if (interactResult == InteractResult.RESTART) {
						// Start the check over again
						interactionsCheck();
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
	 * 
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
			pos.x = collision.x - offsetX;
			dirX = 0;
		}

		// Y
		if (map.checkCollision(this, collision.x, collision.y + (moveCalc(dirY, delta)))) {
			// Move the player as close as possible on the y
			while (!map.checkCollision(this, collision.x, collision.y + 0.01F * dirY)) {
				collision.y += 0.01F * dirY;
			}
			collision.y -= 0.01F * dirY;
			pos.y = collision.y - offsetY;
			dirY = 0;
		}

		// Move
		move(delta);
		interactCollision.x = collision.x + collision.width / 2 + (direction.x * 32) - interactCollision.width / 2;
		interactCollision.y = 64 + collision.y + collision.height / 2 + (direction.y * 32)
				- interactCollision.width / 2;

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
	 * Draw the {@link Cook}.
	 * 
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
		shape.rect(interactCollision.x, interactCollision.y, interactCollision.width, interactCollision.height);
		shape.setColor(Color.WHITE);
	}

	/**
	 * Draw the top half of the {@link Cook} at the location provided.
	 * 
	 * @param batch {@link SpriteBatch} : The {@link SpriteBatch} to draw with.
	 * @param x     {@code float} : The {@code x} position to draw at.
	 * @param y     {@code float} : The {@code y} position to draw at.
	 */
	public void draw_top(SpriteBatch batch, int x, int y) {
		TextureRegion chef_top = new TextureRegion(currentFrame, 0, 0, currentFrame.getRegionWidth(),
				currentFrame.getRegionHeight() / 2);
		batch.draw(chef_top, x, y, 128, 128);
	}

	public void drawHeldItems(SpriteBatch batch) {
		int itemIndex = 0;
		int currentOffset = 0;
		for (Item item : heldItems) {
			// if (ingredient.sprite.getTexture() != null) {
			item.draw(batch, pos.x + 32 - item.getWidth() / 2f, pos.y + 112 + currentOffset);
			// batch.draw(item.sprite, pos.x + 16, pos.y + 112 + itemIndex * 8, 32, 32);
			currentOffset += item.getHeight() / 2f;
			itemIndex++;
			// }
		}
	}

	/**
	 * Set the texture to draw with
	 * 
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
	 * 
	 * @param row - row on the sprite sheet to draw
	 */
	private void setWalkFrames(int row) {
		/*
		 * for (int i = 0; i < FRAME_COLS; i++) {
		 * walkFrames[i] = spriteSheet[row][i];
		 * }
		 */
		walkAnimation = new Animation<>(0.09f, spriteSheet[row]);
		currentFrame = walkAnimation.getKeyFrame(stateTime, true);
	}

	/**
	 * Return cook x pixel coordinate
	 * 
	 * @return cook x pixel coordinate
	 */
	public float getX() {
		return pos.x;
	}

	/**
	 * Return cook y pixel coordinate
	 * 
	 * @return cook y pixel coordinate
	 */
	public float getY() {
		return pos.y;
	}

	/**
	 * Return cook width
	 * 
	 * @return cook width
	 */
	public float getWidth() {
		return collision.width;
	}

	/**
	 * return cook height
	 * 
	 * @return cook height
	 */
	public float getHeight() {
		return collision.height;
	}

	/**
	 * Return cook direction vector
	 * 
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

	public void clear() {
		heldItems.clear();
		updateTexture();
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

	/**
	 * Return the {@link Cook} as a {@link String}
	 */
	public JsonValue serial() {
		// Return JsonValue
		JsonValue cookRoot = new JsonValue(JsonValue.ValueType.object);
		cookRoot.addChild("cookno", new JsonValue(cookno));
		cookRoot.addChild("x", new JsonValue(pos.x));
		cookRoot.addChild("y", new JsonValue(pos.y));
		cookRoot.addChild("items", heldItems.serial());
		return cookRoot;
	}

}
