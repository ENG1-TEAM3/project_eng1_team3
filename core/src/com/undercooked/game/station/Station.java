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
	private int price;
	private boolean disabled;

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
		setPrice(stationData.getPrice());
	}

	public void update(float delta) {
		// Only continue if not disabled
		if (disabled) return;
		if (lockedCooks.size == 0) {
			interactControl.update(null);
			return;
		}
		interactControl.update(lockedCooks.get(0));
	}

	@Override
	public InteractResult interact(Cook cook, String keyID, InputType inputType) {
		// If disabled, stop here
		if (disabled) return InteractResult.NONE;

		if (interactControl == null) {
			// If it doesn't have an interaction control, then stop.
			return InteractResult.STOP;
		}
		return interactControl.interact(cook, keyID, inputType);
	}

	/**
	 * Called when the player is trying to buy the {@link Station}.
	 * @param money {@code int} : The money provided.
	 */
	public boolean buy(int money) {
		// If money < price, then the purchase has failed
		if (money < price) {
			return false;
		}
		// Otherwise, the purchase was a success and the station can
		// be enabled.
		disabled = false;
		return true;
	}

	public void create() {

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
		// If it's disabled, change the draw colour to be darker
		if (disabled) {
			batch.setColor(0.4f, 0.4f, 0.4f, 1f);
		}
		// Draw the Sprite
		super.draw(batch);
		// Then draw the items on top of the station
		for (int i = 0 ; i < items.size() ; i++) {
			Vector2 itemPos = itemPos(i);
			Item thisItem = items.get(i);
			thisItem.draw(batch, pos.x + sprite.getWidth()/2f + itemPos.x - thisItem.getWidth()/2f, pos.y + sprite.getHeight()/2f + itemPos.y - thisItem.getHeight()/2f);
		}
		// If it's disabled, reset the draw colour and stop
		if (disabled) {
			batch.setColor(1, 1, 1, 1);
			return;
		}
		// And then draw the interaction
		interactControl.draw(batch);
	}

	@Override
	public void drawPost(SpriteBatch batch) {
		// Do post drawing for the interaction
		interactControl.drawPost(batch);
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

	public void setPrice(int price) {
		this.price = price;
		// Set it to be disabled or not depending on the price
		this.disabled = (price > 0);
	}

	public int getPrice() {
		return price;
	}

	public boolean isDisabled() {
		return this.disabled;
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

	public void stop() {
		// Stop the interaction
		interactControl.stop();
	}
}
