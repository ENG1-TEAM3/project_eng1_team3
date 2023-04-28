package com.undercooked.game.entity.customer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.food.Item;
import com.undercooked.game.food.Request;
import com.undercooked.game.map.MapManager;
import com.undercooked.game.map.Register;
import com.undercooked.game.util.Listener;

/**
 * The class used for the {@link Customer}s that appear on the map,
 * make a {@link Request} and then must be served before their
 * wait timer runs out.
 */
public class Customer {

	/** The x position of the {@link Customer}. */
	public float x;

	/** The x position of the {@link Customer}. */
	public float y;

	/** The front facing {@link Texture} of the {@link Customer}. */
	Texture textf;
	/** The back facing {@link Texture} of the {@link Customer}. */
	Texture textb;
	/** The right facing {@link Texture} of the {@link Customer}. */
	Texture textr;
	/** The left facing {@link Texture} of the {@link Customer}. */
	Texture textl;

	/** The current {@link Texture} that the {@link Customer} should draw. */
	Texture curTexture;

	/** How far the {@link Customer} moves per second. */
	float moveSpeed;

	/** The opacity that the {@link Customer} is drawn at. */
	float visibility;

	/** The {@link Register} that the {@link Customer} is linked to. */
	private Register register;

	/** The {@link Listener} to call when the {@link Customer} was served successfully. */
	Listener<Customer> servedListener;

	/** The {@link Listener} to call when the {@link Customer} wasn't served successfully. */
	Listener<Customer> failedListener;

	/** The {@link CustomerController} that is controlling the {@link Customer}. */
	CustomerController customerController;

	/** The {@link Customer}'s {@link Request}. */
	public Request order;

	/** The time that the {@link Customer} has left before they leave. */
	public float waitTimer;

	/** The speed that the {@link #waitTimer} decreases per second. */
	public float waitSpeed;

	/** Whether the {@link Customer} is waiting ({@code true}) or not ({@code false})*/
	public boolean waiting;

	/** Whether the {@link Customer} is leaving ({@code true}) or not ({@code false}). */
	public boolean leaving;

	/** The {@link Customer}'s {@link Texture} number. */
	private int custNo;

	/**
	 * Constructor for customer class
	 * 
	 * @param custno 			 {@code int} : The {@link Customer}'s {@link Texture} id.
	 * @param customerController {@link CustomerController} : The controller for the
	 *                                            {@link Customer}s, so it can be told
	 *                                            when this {@link Customer} has left.
	 * @param textureManager {@link TextureManager} : The {@link TextureManager} to use
	 *                       					  to load and get {@link Texture}s from.
	 */
	public Customer(int custno, CustomerController customerController, TextureManager textureManager) {
		TextureManager assetManager = textureManager;
		custNo = custno;
		textf = assetManager.get("entities/cust" + custno + "f.png");
		textb = assetManager.get("entities/cust" + custno + "b.png");
		textr = assetManager.get("entities/cust" + custno + "r.png");

		curTexture = textb;
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
		this.waitSpeed = 1F;
	}

	/**
	 * Update function of the {@link Customer}.
	 * <br>Updates their position if they're entering or leaving, or
	 * lowers their wait timer if they have one.
	 * @param delta {@code float} : The time since the last frame.
	 */
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
				waitTimer -= delta * waitSpeed;
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
				curTexture = textr;
				y = targetY;
			}
			return;
		}

		// If it doesn't have a register, then just move up if there's no other customer
		// above.
		if (!customerController.customerInSquarePos(x, y + MapManager.gridToPos(0.5F), this)) {
			y += moveSpeed * delta;
		}
	}

	/**
	 * Load the {@link Request} of the {@link Customer} if
	 * they have one.
	 * @param textureManager {@link TextureManager} : The {@link TextureManager} to
	 *                                                use
	 * @param textureGroup {@link String} : The texture group to load to.
	 */
	public void load(TextureManager textureManager, String textureGroup) {
		if (getRequest() != null) {
			getRequest().load(textureManager, textureGroup);
		}
	}

	/**
	 * Post load which loads the {@link Request} of the {@link Customer} if
	 * they have one.
	 * @param textureManager {@link TextureManager} : The {@link TextureManager} to
	 *                                                use
	 */
	public void postLoad(TextureManager textureManager) {
		if (getRequest() != null) {
			getRequest().postLoad(textureManager);
		}
	}

	/**
	 * Draw the {@link Customer}'s {@link Texture}.
	 * @param batch {@link SpriteBatch} : The {@link SpriteBatch} to use.
	 */
	public void draw(SpriteBatch batch) {
		batch.setColor(1, 1, 1, visibility);
		// Draw a different texture depending on which direction the Customer is facing
		batch.draw(curTexture, x, y, 64, 128);
		batch.setColor(1, 1, 1, 1);
	}

	/**
	 * Draw the {@link Customer}'s shapes.
	 * @param shape {@link ShapeRenderer} : The {@link ShapeRenderer} to use.
	 */
	public void draw(ShapeRenderer shape) {
		// Only continue if waiting, as this is used to
		// draw the wait timer
		if (!waiting)
			return;

		// Draw base rectangle
		float width = 20;
		float padding = 5;
		float height = 60;
		shape.setColor(Color.GRAY);
		shape.rect(x, y + 16, width, height);
		float percentFilled = waitTimer / getRequest().getTime();
		shape.setColor(Color.BLACK);
		shape.rect(x + padding, y + 16 + padding, width - padding * 2, height - padding * 2);
		shape.setColor(1f - percentFilled, percentFilled, 0, 1);
		shape.rect(x + padding, y + 16 + padding, width - padding * 2, (height - padding * 2) * percentFilled);
		shape.setColor(Color.WHITE);
	}

	/**
	 * Called when the {@link Customer} is served an item.
	 * Returns {@code true} or {@code false} whether the {@link Customer}
	 * was served the correct item or not.
	 * <br>If served the correct item, it will leave.
	 * @param item {@link Item} : The {@link Item} that the {@link Customer}
	 *                            was served.
	 * @return {@code boolean} : {@code true} if the {@link Item} was correct,
	 * 							 {@code false} if not.
	 */
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

	/**
	 * Called when the {@link Customer} should leave the map.
	 */
	protected void leave() {
		// If it's already leaving, return
		if (leaving) return;
		// Set the Customer to leave
		leaving = true;
		// And that they are no longer waiting
		waiting = false;
		// If the Customer is at a register, then tell the
		// CustomerController that.
		customerController.customerOffRegister(register);
		// Move to the left path
		this.x -= MapManager.gridToPos(1);
		// Update the texture
		curTexture = textb;
	}

	/**
	 * Set the {@link Customer}'s {@link Request}.
	 * @param request {@link Request} : The {@link Request} to set to.
	 */
	public void setRequest(Request request) {
		this.order = request;
		this.waitTimer = request.getTime();
	}

	/**
	 * Set the {@link Register} that the {@link Customer} is
	 * linked to.
	 * @param register {@link Register} : The {@link Register} that
	 *                                 the {@link Customer} should be linked
	 *                                 to.
	 */
	public void setRegister(Register register) {
		this.register = register;
	}

	/**
	 * Set the move speed of the {@link Customer} as how many
	 * grid spaces on the {@link com.undercooked.game.map.Map} it moves per second.
	 * @param moveSpeed {@code float} : The speed to move at.
	 */
	public void setMoveSpeed(float moveSpeed) {
		// Convert it to cells / second
		moveSpeed = MapManager.gridToPos(moveSpeed);
		// Minimum of 0.001
		this.moveSpeed = Math.max(1F, moveSpeed);
	}

	/**
	 * Returns the X position of the {@link Customer}.
	 * @return {@code float} : The x position.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Returns the Y position of the {@link Customer}.
	 * @return {@code float} : The y position.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Returns the {@link Request} of the {@link Customer}.
	 * @return {@link Register} : The {@link Customer}'s {@link Request}.
	 */
	public Request getRequest() {
		return order;
	}

	/**
	 * Returns the {@link Register} that the {@link Customer}
	 * is linked to.
	 * @return {@link Register} : The {@link Register}.
	 */
	public Register getRegister() {
		return register;
	}

	/**
	 * Returns if the {@link Customer} is waiting or not.
 	 * @return {@code boolean} : {@code true} if the {@link Customer} is waiting,
	 * 							 {@code false} if not.
	 */
	public boolean isWaiting() {
		return waiting;
	}

	/**
	 * Set the speed at which a customer will wait. It is the amount
	 * that the {@link #waitTimer} is lowered per second.
	 * <br>Default is 1.
	 * @param waitSpeed {@code float} : The speed to wait at.
	 */
	public void setWaitSpeed(float waitSpeed) {
		this.waitSpeed = Math.max(0, waitSpeed);
	}

	/**
	 * Serialise the {@link Customer} into a {@link JsonValue}.
	 * @return {@link JsonValue} : The {@link Customer}'s data in Json form.
	 */
	public JsonValue serial() {
		if (leaving) return null;
		// Return JsonValue
		JsonValue customerRoot = new JsonValue(JsonValue.ValueType.object);
		customerRoot.addChild("x", new JsonValue(x));
		customerRoot.addChild("y", new JsonValue(y));
		if (register != null) {
			customerRoot.addChild("reg_x", new JsonValue(register.getRegisterCell().getX()));
			customerRoot.addChild("reg_y", new JsonValue(register.getRegisterCell().getY()));
		}
		customerRoot.addChild("custno", new JsonValue(custNo));
		customerRoot.addChild("wait_timer", new JsonValue(waitTimer));
		customerRoot.addChild("move_speed", new JsonValue(moveSpeed));
		customerRoot.addChild("request", order.serial());
		return customerRoot;
	}
}
