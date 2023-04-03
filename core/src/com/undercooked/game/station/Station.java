package com.undercooked.game.station;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.Input.InputType;
import com.undercooked.game.MainGameClass;
import com.undercooked.game.assets.AudioManager;
import com.undercooked.game.entity.cook.Cook;
import com.undercooked.game.food.Item;
import com.undercooked.game.food.ItemStack;
import com.undercooked.game.food.Items;
import com.undercooked.game.interactions.InteractResult;
import com.undercooked.game.interactions.Interactions;
import com.undercooked.game.interactions.StationInteractControl;
import com.undercooked.game.map.MapEntity;

import static com.undercooked.game.MainGameClass.shapeRenderer;

/**
 * Represents a station.
 * 
 */
public class Station extends MapEntity {

	StationInteractControl interactControl;
	private StationData stationData;
	public ItemStack items;
	private Array<Cook> lockedCooks;

	public Station(StationData stationData) {
		super();
		setStationData(stationData);
		this.texturePath = stationData.getTexturePath();
		this.items = new ItemStack();
		this.lockedCooks = new Array<>();
		this.setBasePath(stationData.getDefaultBase());
		setWidth(stationData.getWidth());
		setHeight(stationData.getHeight());
	}

	public void setStationData(StationData stationData) {
		this.stationData = stationData;
		this.id = stationData.getID();
	}

	public void update(float delta) {
		if (lockedCooks.size == 0) {
			interactControl.update(null);
			return;
		}
		interactControl.update(lockedCooks.get(0));
	}

	@Override
	public InteractResult interact(Cook cook, String keyID, InputType inputType) {
		// System.out.println(keyID + ": " + inputType.toString());
		if (interactControl == null) {
			// If it doesn't have an interaction control, then stop.
			return InteractResult.STOP;
		}
		return interactControl.interact(cook, keyID, inputType);
	}

	public void create() {

	}

	public void drawStatusBar(SpriteBatch batch) {
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(collision.x * 64, collision.y * 64 + 64 + 64 / 10, 64, 64 / 8);
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.rect(collision.x * 64, collision.y * 64 + 64 + 64 / 10, 64, 64 / 10);
		shapeRenderer.end();
	}

	public Vector2 itemPos(int num) {
		// Make sure num is in range
		num = Math.abs(num);
		num %= 8;
		switch (num) {
			case 0:
				return new Vector2(-16, -16);
			case 1:
				return new Vector2(16, -16);
			case 2:
				return new Vector2(-16, 16);
			case 3:
				return new Vector2(16, 16);
			case 4:
				return new Vector2(0, -16);
			case 5:
				return new Vector2(-16, 0);
			case 6:
				return new Vector2(16, 0);
			case 7:
				return new Vector2(0, 16);
			default:
				return new Vector2(0, 0);
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		// Draw the Sprite
		super.draw(batch);
		// Then draw the items on top of the station
		for (int i = 0 ; i < items.size() ; i++) {
			Vector2 itemPos = itemPos(i);
			batch.draw(items.get(i).sprite, pos.x + sprite.getWidth()/4 + itemPos.x, pos.y + sprite.getHeight()/4 + itemPos.y,
					32, 32);
		}
	}

	public void draw(ShapeRenderer shape) {
		// Draw the interaction
		interactControl.draw(shape);
	}

	/**
	 *
	 * @param text Text to be drawn.
	 * @param pos  Position to draw at.
	 */
	public void drawText(SpriteBatch batch, String text, Vector2 pos) {
		batch.begin();
		MainGameClass.font.draw(batch, text, pos.x, pos.y);
		batch.end();
	}

	public void makeInteractionController(AudioManager audioManager, Items gameItems) {
		this.interactControl = new StationInteractControl(this, audioManager, gameItems);
		updateStationInteractions();
	}


	/**
	 * Returns whether the {@link Station} has a specific {@link Item}
	 * (or any {@link Item}) a number of times.
	 * @param itemID {@link String} : The itemID to check for.
	 * @param number {@code int} : The number of {@link Item}s needed.
	 * @return {@code boolean} : {@code true} if it does,
	 * 							 {@code false} if it does not.
	 */
	public boolean hasItem(String itemID, int number) {
		// If itemID is null, just compare the size and number
		if (itemID == null) {
			return items.size() >= number;
		}
		// If it's <= 0, then return true
		if (number <= 0) {
			return true;
		}
		// Otherwise, if itemID is not null then check the number
		// of times that itemID occurs.
		// Check all items
		int numFound = 0;
		for (Item item : items) {
			// If item's ID and itemID match...
			if (item.getID() == itemID) {
				// Increase numFound
				numFound += 1;
			}
		}
		// Return comparison of numFound and number
		return numFound >= number;
	}

	public boolean canHoldItems(int number) {
		if (number <= 0) {
			return true;
		}
		// Return whether adding it goes above the limit or not
		return !(items.size() + number > stationData.getHoldCount());
	}

	public boolean canHoldItem() {
		return canHoldItems(1);
	}

	public boolean addItem(Item item) {
		// Only continue if it CAN add an item
		if (!canHoldItem()) {
			return false;
		}
		// Add the item
		items.add(item);
		// Update Station Interactions.
		updateStationInteractions();
		return true;
	}

	public Item takeItem() {
		// Only continue if it has an item
		if (items.size() <= 0) {
			return null;
		}
		// Return the item that was popped.
		Item returnItem = items.pop();
		// Update Station Interactions.
		updateStationInteractions();
		// Return the item that was taken.
		return returnItem;
	}

	public void clear() {
		// Clear all items
		items.clear();
		// Update station interactions
		updateStationInteractions();
	}

	public void updateStationInteractions() {
		interactControl.setCurrentInteraction(interactControl.findValidInteraction(items));
	}

	/**
	 * Returns {@code true} or {@code false} depending on if the
	 * {@link Station} has that number of items or not.
	 * @param number {@code int} : The number of {@link Item}s.
	 * @return {@code boolean} : {@code true} if it has that many {@link Item}s,
	 * 							 {@code false} if it does not.
	 */
	public boolean hasItem(int number) {
		return hasItem(null, number);
	}

	/**
	 * Returns a {@code boolean} on whether the {@link Station}
	 * has an item or not.
	 * @return {@code boolean} : {@code true} if it has an {@link Item},
	 * 	 * 					     {@code false} if it does not.
	 */
	public boolean hasItem() {
		return hasItem(null, 1);
	}

	public void updateInteractions() {
		this.interactControl.updatePossibleInteractions(stationData.getID());
	}

	public void setInteractions(Interactions interactions) {
		this.interactControl.setInteractions(interactions);
	}

	public boolean hasCookLocked() {
		return this.lockedCooks.size > 0;
	}

	public boolean hasCookLocked(Cook cook) {
		return this.lockedCooks.contains(cook, true);
	}

	public void lockCook(Cook cook) {
		// If the cook isn't already locked...
		if (hasCookLocked(cook)) {
			return;
		}
		// Then add the cook.
		this.lockedCooks.add(cook);
		// Then tell the cook to lock
		cook.lockToStation(this);
	}

	public void unlockCook(Cook cook) {
		// If the cook is locked...
		if (!hasCookLocked(cook)) {
			return;
		}
		// Remove the Cook
		this.lockedCooks.removeValue(cook, true);
		// And then tell the cook to remove its lock
		cook.unlock();
	}

	public void unlockCooks() {
		for (int i = lockedCooks.size-1 ; i >= 0 ; i--) {
			unlockCook(lockedCooks.get(i));
		}
	}

    public void reset() {
		// Clear the station
		clear();

		// If any cooks are locked, then unlock them
		if (hasCookLocked()) {
			unlockCooks();
		}
    }
}
