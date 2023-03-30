package com.undercooked.game.entity.customer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.entity.Entity;
import com.undercooked.game.food.Item;
import com.undercooked.game.map.MapManager;
import com.undercooked.game.map.Register;
import com.undercooked.game.util.Listener;

public class Customer extends Entity {
	int targetsquare;
	public float posx;
	public float posy;
	Texture textf;
	Texture textb;
	Texture textr;
	Texture textl;

	TextureRegion[][] custpartsf;
	TextureRegion[][] custpartsb;
	TextureRegion[][] custpartsr;
	TextureRegion[][] custpartsl;
	TextureRegion[][] currentcustparts;
	float startposx;
	int targetpixel;
	float moveSpeed;
	public boolean locked;
	public boolean leaving;

	private long arrivalTime;

	private float visibility;
	private int reputationThreat;
	private Register register;
	Listener<Integer> servedListener;
	Listener<Integer> failedListener;
	CustomerController customerController;

	public String order = "";
	public float waitTimer;
	public boolean waiting;

	/**
	 * Constructor for customer class
	 * @param x - x starting pixel coordinate
	 * @param y - y starting pixel coordinate
	 * @param custno - customer number - changes texture
	 * @param textureManager The {@link TextureManager} to use
	 *                       to load and get {@link Texture}s from.
	 */
	public Customer(int custno, CustomerController customerController, TextureManager textureManager) {
		TextureManager assetManager = textureManager;
		textf = assetManager.get("entities/cust" + custno + "f.png");
		// textb = assetManager.get("entities/cust" + custno + "b.png");
		// textr = assetManager.get("entities/cust" + custno + "r.png");
		// textl = assetManager.get("entities/cust" + custno + "l.png");

		// custpartsf = TextureRegion.split(textf, 32, 32);
		// custpartsb = TextureRegion.split(textb, 32, 32);
		// custpartsr = TextureRegion.split(textr, 32, 32);
		// custpartsl = TextureRegion.split(textl, 32, 32);

		// currentcustparts = custpartsf;

		// posx = MapManager.gridToPos(x);
		// posy = MapManager.gridToPos(y);
		startposx = posx;
		locked = false;
		this.visibility = 0F;
		this.waiting = false;
		this.customerController = customerController;
		this.moveSpeed = 2F;
		this.waitTimer = -1F;
	}

	public void update(float delta) {
		// If leaving, and y <= 0, then make invisible before deleting
		if (leaving) {
			posy -= moveSpeed;
			if (posy <= 0) {
				visibility -= 0.1F;
				if (visibility <= 0) {
					// Delete this customer
					customerController.deleteCustomer(this);
				}
			}
			return;
		}
		// If they're not visible, make them visible
		visibility = Math.min(1, visibility + 0.05F);
		// If this customer is waiting...
		if (waiting) {
			// If waitTimer is not already < 0
			if (waitTimer >= 0) {
				// Then decrease the wait timer
				waitTimer -= delta;
				// If waitTimer reaches 0, and the customer hasn't been served,
				// then tell the listener
				if (waitTimer <= 0) {
					if (failedListener != null) {
						failedListener.tell(reputationThreat);
					}
					leave();
				}
			}
			return;
		}

		// Finally, if it gets to here then the Customer is moving up
		// to a register

		// If it has a register to go to...
		if (register != null) {
			// Then just move until it reaches the register
			posy += moveSpeed;
			// If posy >= registerCell's y, + 0.5, then wait
			float targetY = register.registerCell.getDisplayY() - MapManager.gridToPos(0.3F);
			if (posy >= targetY) {
				waiting = true;
				posy = targetY;
			}
			return;
		}

		// If it doesn't have a register, then just move up if there's no other customer above.
		if (!customerController.customerInSquarePos(posx,posy+MapManager.gridToPos(0.5F), this)) {
			posy += moveSpeed;
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.setColor(1,1,1,visibility);
		batch.draw(textf, posx, posy, 64, 128);
		batch.setColor(1, 1, 1, 1);
	}

	public void serve(Item item) {
		if (item.getID() == this.order) {
			// Get the money for it
			servedListener.tell(item.getValue());
			// Then leave
			leave();
		}
	}

	protected void leave() {
		// Set the Customer to leave
		leaving = true;
		// If the Customer is at a register, then tell the
		// CustomerController that.
		customerController.customerOffRegister(register);
		// Move to the left path
		this.posx -= MapManager.gridToPos(1);
	}

	/**
	 * Set arrival time as cook has arrived
	 */
	public void arrived() {
		arrivalTime = System.currentTimeMillis();
	}

	/**
	 * Check amount of time waited in ms
	 * @return amount of time waited in ms
	 */
	public long waitTime() {
		return System.currentTimeMillis() - arrivalTime;
	}

	/**
	 * Render top of customer
	 * @param b - spritebatch to render with
	 */
	public void renderCustomersTop(Batch b) {
		b.draw(currentcustparts[0][0], posx, posy + 64, 64, 64);
	}

	/**
	 * Render bottom of customer
	 * @param b - spritebatch to render with
	 */
	public void renderCustomersBot(Batch b) {
		b.draw(currentcustparts[1][0], posx, posy, 64, 64);
	}

	/**
	 * Set target square location
	 * @param tg - tile y coordinate of target square
	 */
	public void setTargetsquare(int tg) {
		targetsquare = tg;
	}

	/**
	 * Move towards customer target tile
	 */
	public void stepTarget() {
		targetpixel = 32 + (targetsquare * 64);
		if (posy < targetpixel) {
			posy++;
		} else if (posy > targetpixel) {
			posy--;
			currentcustparts = custpartsf;
		}
		if (posy == targetpixel) {
			if (targetpixel > 0) {
				posx = startposx + 64;
				locked = true;
				currentcustparts = custpartsr;
			} else {
				leaving = true;
			}
		}
	}

	public void setRequest(String request) {
		this.order = request;
	}

	public void setRegister(Register register) {
		this.register = register;
	}

	public String getRequest() {
		return order;
	}

	public Register getRegister() {
		return register;
	}
}
