package com.undercooked.game.entity.customer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.food.Item;
import com.undercooked.game.food.Request;
import com.undercooked.game.map.MapManager;
import com.undercooked.game.map.Register;
import com.undercooked.game.util.Listener;

public class Customer {
	public float x;
	public float y;
	Texture textf;
	Texture textb;
	Texture textr;
	Texture textl;

	TextureRegion[][] custpartsf;
	TextureRegion[][] custpartsb;
	TextureRegion[][] custpartsr;
	TextureRegion[][] custpartsl;
	TextureRegion[][] currentcustparts;
	float moveSpeed;
	public boolean leaving;

	private float visibility;
	private Register register;
	Listener<Customer> servedListener;
	Listener<Customer> failedListener;
	CustomerController customerController;

	public Request order;
	public float waitTimer;
	public boolean waiting;

	/**
	 * Constructor for customer class
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
		this.visibility = 0F;
		this.waiting = false;
		this.customerController = customerController;
		this.moveSpeed = 2F;
		this.waitTimer = -1F;
	}

	public void update(float delta) {
		// If leaving, and y <= 0, then make invisible before deleting
		if (leaving) {
			y -= moveSpeed * delta;
			if (y <= 0) {
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
					leave();
					if (failedListener != null) {
						failedListener.tell(this);
					}
				}
			}
			return;
		}

		// Finally, if it gets to here then the Customer is moving up
		// to a register

		// If it has a register to go to...
		if (register != null) {
			// Then just move until it reaches the register
			y += moveSpeed * delta;
			// If posy >= registerCell's y, + 0.5, then wait
			float targetY = register.getRegisterCell().getDisplayY() - MapManager.gridToPos(0.3F);
			if (y >= targetY) {
				waiting = true;
				y = targetY;
			}
			return;
		}

		// If it doesn't have a register, then just move up if there's no other customer above.
		if (!customerController.customerInSquarePos(x, y +MapManager.gridToPos(0.5F), this)) {
			y += moveSpeed * delta;
		}
	}

	public void draw(SpriteBatch batch) {
		batch.setColor(1,1,1,visibility);
		batch.draw(textf, x, y, 64, 128);
		batch.setColor(1, 1, 1, 1);
	}

	public void draw(ShapeRenderer shape) {
		// Only continue if waiting, as this is used to
		// draw the wait timer
		if (!waiting) return;

		// Draw base rectangle
		float width = 20;
		float padding = 5;
		float height = 60;
		shape.setColor(Color.GRAY);
		shape.rect(x, y+16, width, height);
		float percentFilled = waitTimer / getRequest().getTime();
		shape.setColor(Color.BLACK);
		shape.rect(x+padding, y+16+padding, width-padding*2, height-padding*2);
		shape.setColor(1f-percentFilled,percentFilled,0,1);
		shape.rect(x+padding, y+16+padding, width-padding*2, (height-padding*2)* percentFilled);
		shape.setColor(Color.WHITE);
	}

	public boolean serve(Item item) {
		if (item.getID().equals(this.order.itemID)) {
			// Remove this customer from the register
			customerController.customerOffRegister(register);
			// Then leave
			leave();
			// Finally, get the money for it
			servedListener.tell(this);
			return true;
		}
		return false;
	}

	protected void leave() {
		// Set the Customer to leave
		leaving = true;
		// And that they are no longer waiting
		waiting = false;
		// If the Customer is at a register, then tell the
		// CustomerController that.
		customerController.customerOffRegister(register);
		// Move to the left path
		this.x -= MapManager.gridToPos(1);
	}

	public void setRequest(Request request) {
		this.order = request;
		this.waitTimer = request.getTime();
	}

	public void setRegister(Register register) {
		this.register = register;
	}

	public void setMoveSpeed(float moveSpeed) {
		// Convert it to cells / second
		moveSpeed = MapManager.gridToPos(moveSpeed);
		// Minimum of 0.001
		this.moveSpeed = Math.max(1F, moveSpeed);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public Request getRequest() {
		return order;
	}

	public Register getRegister() {
		return register;
	}

	public boolean isWaiting() {
		return waiting;
	}
}
