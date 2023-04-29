package com.undercooked.game.entity.customer;

import java.util.Comparator;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.food.Item;
import com.undercooked.game.food.Request;
import com.undercooked.game.map.*;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.Listener;

/**
 * The class used to control the {@link Customer}s, their spawning, which
 * {@link Register}s they go to and the order they're drawn in.
 */
public class CustomerController {

	/** The {@link TextureManager} to load and get {@link com.badlogic.gdx.graphics.Texture}s from. */
	TextureManager textureManager;

	/** An array of all {@link Customer}s, sorted in the order they are spawned. */
	Array<Customer> customers;

	/** The {@link #customers} array, but it is sorted based on Y level. */
	Array<Customer> drawCustomers;

	/** An {@link Array} of the {@link Customer}s that need to be spawned.*/
	Array<Customer> toSpawn;

	/** An {@link Array} of the {@link Register}s on the {@link #map}*/
	Array<Register> registers;

	/** The {@link Map} to use for finding {@link Register}s. */
	Map map;

	/** The {@code x} position that the {@link Customer}s spawn at. */
	float spawnX;

	/** The {@code y} position that the {@link Customer}s spawn at. */
	float spawnY;

	/** The number of {@link MapCell}s that the {@link Customer}s move every second. */
	float customerSpeed;

	/** The speed at which the {@link Customer}'s {@link Customer#waitTimer} decreases. */
	float waitSpeed;

	/** The {@link Listener} that the {@link Customer}s call when they are served successfully. */
	Listener<Customer> getMoney;

	/** The {@link Listener} that the {@link Customer}s call when they are not served successfully. */
	Listener<Customer> loseReputation;

	/**
	 * The {@link Comparator} for sorting the {@link Customer}s in the
	 * {@link #drawCustomers} {@link Array} so that they can be drawn in
	 * the correct order.
	 */
	Comparator<Customer> customerDrawComparator;

	/** How the {@link Register}s are picked when finding an open one. */
	CustomerTarget targetType;

	/**
	 * The constructor for the {@link CustomerController}.
	 * <br>Sets up all the variables of the variables to be used.
	 * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
	 */
	public CustomerController(TextureManager textureManager, Map map) {
		this.textureManager = textureManager;
		this.map = map;
		this.customers = new Array<>();
		this.drawCustomers = new Array<>();
		this.toSpawn = new Array<>();
		this.registers = new Array<>();
		// computeCustomerZone(gameMap);

		this.spawnX = 3;
		this.spawnY = -1;

		this.customerDrawComparator = new Comparator<Customer>() {
			@Override
			public int compare(Customer o1, Customer o2) {
				if (o1.getY() > o2.getY()) {
					return -1;
				} else if (o2.getY() > o1.getY()) {
					return 1;
				}
				return 0;
			}
		};

		this.targetType = CustomerTarget.FARTHEST;
		this.customerSpeed = 1F;
		this.waitSpeed = 1F;
	}

	/**
	 * The constructor for the {@link CustomerController}, without the {@link Map}.
	 * @param textureManager {@link TextureManager} : The {@link TextureManager} to use.
	 */
	public CustomerController(TextureManager textureManager) {
		this(textureManager, null);
	}

	/**
	 * Set the map to be used.
	 * @param map {@link Map} : The {@link Map} to use.
	 */
	public void setMap(Map map) {
		this.map = map;
	}

	/**
	 * Load all of the {@link Customer}s, and their {@link com.badlogic.gdx.graphics.Texture}s.
	 * @param textureGroup {@link String} : The texture group to load to.
	 */
	public void load(String textureGroup) {
		// Finds the registers
		findRegisters();
		// Load all the customer textures
		for (int custNo = 1 ; custNo <= Constants.NUM_CUSTOMER_TEXTURES ; custNo++) {
			textureManager.load(textureGroup, "entities/cust" + custNo + "f.png");
			textureManager.load(textureGroup, "entities/cust" + custNo + "b.png");
			textureManager.load(textureGroup, "entities/cust" + custNo + "r.png");
			textureManager.load(textureGroup, "entities/cust" + custNo + "l.png");
		}
		// Load any customers that are already in the arrays
		for (Customer customer : customers) {
			customer.load(textureManager, textureGroup);
		}
		for (Customer customer : toSpawn) {
			customer.load(textureManager, textureGroup);
		}
	}

	/**
	 * Post load all of the {@link Customer}s.
	 */
	public void postLoad() {
		// Post load any customers that are already in the arrays
		for (Customer customer : customers) {
			customer.postLoad(textureManager);
		}
		for (Customer customer : toSpawn) {
			customer.postLoad(textureManager);
		}
	}

	/**
	 * Unload all of the {@link Customer}s {@link com.badlogic.gdx.graphics.Texture}s.
	 */
	public void unload() {
		// Unload the textures
		for (int custNo = 1 ; custNo <= Constants.NUM_CUSTOMER_TEXTURES ; custNo++) {
			textureManager.unloadTexture("entities/cust" + custNo + "f.png");
			textureManager.unloadTexture("entities/cust" + custNo + "b.png");
			textureManager.unloadTexture("entities/cust" + custNo + "r.png");
			textureManager.unloadTexture("entities/cust" + custNo + "l.png");
		}
	}

	/**
	 * Update all of the {@link Customer}s.
	 * @param delta {@code float} : The time since the last frame.
	 */
	public void update(float delta) {
		// Check if there is a Customer waiting for
		// an open space
		Customer waitingCustomer = customerWaiting();
		if (waitingCustomer != null) {
			// If there is, then set them to use the first
			// available register.
			// This will be null if there is none open.
			customerOnRegister(waitingCustomer, getOpenRegister());
		}
		// Update the Customers
		for (int index = 0; index < customers.size; index++) {
			customers.get(index).update(delta);
		}
		// If there are customers to spawn...
		if (toSpawn.size > 0) {
			// And one can be spawned...
			if (canSpawn()) {
				// then move it over to the customers array
				customers.add(toSpawn.removeIndex(0));
			}
		}
	}


	/**
	 * Draw all of the {@link Customer}s' {@link com.badlogic.gdx.graphics.Texture}s.
	 * @param batch {@link SpriteBatch} : The {@link SpriteBatch} to use.
	 */
	public void draw(SpriteBatch batch) {
		// First sort the draw array
		drawCustomers.sort(customerDrawComparator);
		// Draw all the Customers
		for (Customer customer : drawCustomers) {
			customer.draw(batch);
		}
	}

	/**
	 * Draw all of the {@link Customer}s' shapes.
	 * @param shape {@link ShapeRenderer} : The {@link ShapeRenderer} to use.
	 */
	public void draw(ShapeRenderer shape) {
		// First sort the draw array
		drawCustomers.sort(customerDrawComparator);
		// Draw all the Customers
		for (Customer customer : drawCustomers) {
			customer.draw(shape);
		}
	}

	/**
	 * Returns the first {@link Customer} that does not have a
	 * {@link Register} linked to them.
	 * @return {@link Customer} : The first {@link Customer} not linked to
	 * 							  a {@link Register}, or {@code null}.
	 */
	public Customer customerWaiting() {
		// Check through the customers, and if there is one
		// that doesn't have a register set, then that means
		// that they're waiting.
		for (Customer customer : customers) {
			if (customer.getRegister() == null) {
				return customer;
			}
		}
		return null;
	}

	/**
	 * Returns whether a {@link Customer} can currently be spawned or not.
	 * @return {@code boolean} : {@code true} if a {@link Customer} can be spawned,
	 * 							 {@code false} if not.
	 */
	public boolean canSpawn() {
		return !(customerInSquareGrid(spawnX, spawnY) || customerInSquareGrid(spawnX, spawnY + 1));
	}

	/**
	 * Spawns a new {@link Customer} with the {@link Request} provided.
	 * @param request {@link Request} : The {@link Request} for the
	 * 									{@link Customer} to have.
	 * @return {@link Customer} : The new {@link Customer} spawned.
	 */
	public Customer spawnCustomer(Request request) {
		// Randomly choose a customer number
		Random random = new Random();
		int custNo = random.nextInt(1,Constants.NUM_CUSTOMER_TEXTURES+1);

		// Create the new customer
		Customer newCustomer = new Customer(custNo, this, textureManager);

		// Add the customer if it's valid. If not, add to the "to spawn" array
		// If toSpawn already has something in it, then add it to the end
		// of that, as it can't be added before.
		if (!(toSpawn.size > 0) && canSpawn()) {
			customers.add(newCustomer);
		} else {
			toSpawn.add(newCustomer);
		}
		// Either way, add it to the draw array
		drawCustomers.add(newCustomer);

		// Set position
		newCustomer.x = MapManager.gridToPos(spawnX);
		newCustomer.y = MapManager.gridToPos(spawnY);

		// Set speed
		newCustomer.setMoveSpeed(customerSpeed);
		newCustomer.setWaitSpeed(waitSpeed);

		// Set their listeners
		setCustomersListeners(newCustomer);
		newCustomer.setRequest(request);
		// Try to put the customer on a register
		customerOnRegister(newCustomer, getOpenRegister());
		return newCustomer;
	}

	/**
	 * Set the {@link Listener}s of a {@link Customer}.
	 * @param customer {@link Customer} : The {@link Customer} to set the
	 *                                    {@link Listener}s for.
	 */
	protected void setCustomersListeners(Customer customer) {
		customer.servedListener = getMoney;
		customer.failedListener = loseReputation;
	}

	/**
	 * A {@code y} comparator for the {@link Register}s, to be used
	 * to sort them in {@link #getOpenRegister()} for the
	 * {@link CustomerTarget#CLOSEST} and {@link CustomerTarget#FARTHEST}
	 * targeting types.
	 */
	Comparator<Register> registerYComparator = new Comparator<Register>() {
		@Override
		public int compare(Register o1, Register o2) {
			MapCell mapCell1 = o1.getRegisterCell();
			MapCell mapCell2 = o2.getRegisterCell();
			// If higher, return 1
			if (mapCell1.getDisplayY() > mapCell1.getDisplayY()) {
				return 1;
			}
			// If lower, return -1
			if (mapCell2.getDisplayY() < mapCell2.getDisplayY()) {
				return -1;
			}
			// Return 0 if they're the same.
			return 0;
		}
	};

	/**
	 * Gets a {@link Register} that has no {@link Customer} on it,
	 * changing which one is selected depending on the {@link #targetType}.
	 * @return {@link Register} : A {@link Register} with no {@link Customer},
	 * 							  or null if there isn't one.
	 */
	public Register getOpenRegister() {
		// Loop through the registers, and add them to an array
		// if they have no customer on them
		Array<Register> openRegisters = new Array<>();
		for (Register register : registers) {
			// Return it if the Customer is null
			if (register.getCustomer() == null) {
				openRegisters.add(register);
			}
		}
		// If it doesn't find one, return null.
		if (openRegisters.size == 0)
			return null;

		// If it did find one, depending on the CustomerTarget, select a register
		switch (targetType) {
			case FARTHEST:
				openRegisters.sort(registerYComparator);
				return openRegisters.first();
			case CLOSEST:
				openRegisters.sort(registerYComparator);
				openRegisters.reverse();
				return openRegisters.first();
			case RANDOM:
				// Get a random index
				return openRegisters.random();
		}
		return null;
	}

	/**
	 * Returns whether there is a {@link Customer} in the {@link MapCell} at
	 * the position provided, ignoring the {@link Customer} provided.
	 * @param x {@code float} : The x position.
	 * @param y {@code float} : The y position.
	 * @param ignoredCustomer {@link Customer} : The {@link Customer} to ignore.
	 * @return {@code boolean} : {@code true} if there is a {@link Customer},
	 * 						     {@code false} if there is not.
	 */
	public boolean customerInSquareGrid(float x, float y, Customer ignoredCustomer) {
		x = (int) x;
		y = (int) y;
		// Check for all customers
		for (Customer customer : customers) {
			if (customer == ignoredCustomer) {
				continue;
			}
			int cX = MapManager.posToGridFloor(customer.x);
			int cY = MapManager.posToGridFloor(customer.y);
			if (x == cX & y == cY) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns whether there is a {@link Customer} in the {@link MapCell} at the position
	 * provided.
	 * @param x {@code float} : The x position.
	 * @param y {@code float} : The y position.
	 * @return {@code boolean} : {@code true} if there is a {@link Customer},
	 * 						     {@code false} if there is not.
	 */
	public boolean customerInSquareGrid(float x, float y) {
		return customerInSquareGrid(x, y, null);
	}


	/**
	 * Returns whether there is a {@link Customer} in the {@link MapCell} that the
	 * float values match to.
	 * @param x {@code float} : The x position.
	 * @param y {@code float} : The y position.
	 * @return {@code boolean} : {@code true} if there is a {@link Customer},
	 * 						     {@code false} if there is not.
	 */
	public boolean customerInSquarePos(float x, float y) {
		return customerInSquareGrid(MapManager.posToGrid(x), MapManager.posToGrid(y), null);
	}

	/**
	 * Returns whether there is a {@link Customer} in the {@link MapCell} that the
	 * float values match to, ignoring the {@link Customer} provided.
	 * @param x {@code float} : The x position.
	 * @param y {@code float} : The y position.
	 * @param ignoredCustomer {@link Customer} : The {@link Customer} to ignore.
	 * @return {@code boolean} : {@code true} if there is a {@link Customer},
	 * 						     {@code false} if there is not.
	 */
	public boolean customerInSquarePos(float x, float y, Customer ignoredCustomer) {
		return customerInSquareGrid(MapManager.posToGrid(x), MapManager.posToGrid(y), ignoredCustomer);
	}

	/**
	 * Links a {@link Customer} to a {@link Register}.
	 * @param customer {@link Customer} : The {@link Customer} to link.
	 * @param register {@link Register} : The {@link Register} to link.
	 */
	public void customerOnRegister(Customer customer, Register register) {
		// Make sure the register is not null
		if (register == null) {
			return;
		}
		// Make sure this Register doesn't have a customer
		if (register.hasCustomer()) {
			return;
		}
		// Make sure the Customer is not null
		if (customer == null) {
			return;
		}
		// Add the customer to the register
		register.setCustomer(customer);
		customer.setRegister(register);
	}

	/**
	 * Removes a {@link Customer} from a {@link Register}.
	 * @param register {@link Register} : The {@link Register} to remove
	 *                                    the {@link Customer} from.
	 */
	public void customerOffRegister(Register register) {
		// Make sure the register is not null
		if (register == null) {
			return;
		}
		// Make sure this Register has a customer
		if (!register.hasCustomer()) {
			return;
		}
		// Remove the Customer from the register
		register.setCustomer(null);
	}

	/**
	 * Deletes a {@link Customer} from all {@link Array}s.
	 * @param customer {@link Customer} : The {@link Customer} to remove.
	 */
	protected void deleteCustomer(Customer customer) {
		customers.removeValue(customer, true);
		toSpawn.removeValue(customer, true);
		drawCustomers.removeValue(customer, true);
	}

	/**
	 * Set the {@link Customer}s' {@link Customer#servedListener}, which
	 * they call when they not have been served successfully.
	 * @param reputationListener {@link Listener<Customer>} : The {@link Listener}
	 *                                         for the {@link Customer}s to use.
	 */
	public void setReputationListener(Listener<Customer> reputationListener) {
		this.loseReputation = reputationListener;
	}

	/**
	 * Set the {@link Customer}s' {@link Customer#servedListener}, which
	 * they call when they have been served successfully.
	 * @param servedListener {@link Listener<Customer>} : The {@link Listener}
	 *                                         for the {@link Customer}s to use.
	 */
	public void setServedListener(Listener<Customer> servedListener) {
		this.getMoney = servedListener;
	}

	/**
	 * Set what type of {@link CustomerTarget} should be used when spawning
	 * the {@link Customer}s. Depending on the {@link CustomerTarget}, they
	 * may head towards different {@link Register}s before others are used.
	 * @param targetType {@link CustomerTarget} : The type of targeting method
	 *                                            to use.
	 */
	public void setTargetType(CustomerTarget targetType) {
		this.targetType = targetType;
	}

	/**
	 * Set the movement speed of all of the {@link Customer}s.
	 * @param customerSpeed {@code float} : The number of cells to move
	 *                                   	per second.
	 */
	public void setCustomerSpeed(float customerSpeed) {
		this.customerSpeed = customerSpeed;
	}

	/**
	 * Set the wait speed of all of the {@link Customer}s.
	 * @param multiplier {@code float} : The speed to wait at.
	 */
	public void setCustomerWaitSpeed(float multiplier) {
		this.waitSpeed = multiplier;
		// Update for all Customers
		for (Customer customer : customers) {
			customer.setWaitSpeed(multiplier);
		}
		for (Customer customer : toSpawn) {
			customer.setWaitSpeed(multiplier);
		}
	}

	/**
	 * Finds all {@link Register} {@link MapCell}s on the {@link Map},
	 * and adds them to the {@link #registers} array.
	 */
	public void findRegisters() {
		// Clear the register array
		registers.clear();
		// Loop through all the map cells on the left wall.
		// All valid registers have to be placed there.
		// Loop from top to bottom, so that they are ordered
		// top to bottom.
		for (int y = map.getHeight() - 1; y >= 0; y--) {
			MapCell thisCell = map.getCell(0, y);
			// Make sure it's not null
			if (thisCell == null)
				continue;
			// Then get the MapEntity.
			MapEntity thisEntity = thisCell.getMapEntity();
			// Make sure it's not null
			if (thisEntity == null)
				continue;

			// If it gets here, make sure it's the register
			if (!thisEntity.getID().equals(Constants.REGISTER_ID))
				continue;

			// If it's a register, then add the MapCell to the array
			registers.add(new Register(thisCell));
		}
	}

	/**
	 * Attempts to serve an {@link Item} to a {@link Customer} that
	 * is waiting at a {@link Register} {@link MapCell}.
	 * @param registerCell {@link MapCell} : The {@link MapCell} interacted
	 *                                    	 with.
	 * @param item {@link Item} : The {@link Item} served.
	 * @return {@code boolean} : {@code true} if the {@link Item} was served successfully,
	 * 							 {@code false} if not.
	 */
	public boolean serve(MapCell registerCell, Item item) {
		// Make sure that the target is a valid register
		// by getting the register, and making sure the
		// result isn't null.
		Register register = getRegisterFromCell(registerCell);
		if (register == null)
			return false;

		// Then get the customer of that register
		Customer customer = register.getCustomer();
		// If it's null, then return false
		if (customer == null)
			return false;
		// Customer must be waiting
		if (!customer.isWaiting())
			return false;

		// Otherwise, check if the order and item matches
		return customer.serve(item);
	}

	/**
	 * Returns whether a {@link Register} is at the {@code x} and
	 * {@code y} specified.
	 * @param x {@code int} : The {@code x} to check.
	 * @param y {@code int} : The {@code y} to check.
	 * @return {@code boolean} : {@code true} if there is a {@link Register},
	 * 							 {@code false} if there is not.
	 */
	public Register getRegisterAtPos(int x, int y) {
		// Check through all registers
		for (Register register : registers) {
			// If the Cell position matches, return the register
			if (register.getRegisterCell().getX() == x && register.getRegisterCell().getY() == y) {
				return register;
			}
		}
		// If none found, return null
		return null;
	}

	/**
	 * Get a {@link Register} from a {@link MapCell}.
	 * <br>If there is no {@link Register} on that {@link MapCell}, then
	 * it will return {@code null}.
	 * @param mapCell {@link MapCell} : The {@link MapCell} to use.
	 * @return {@link Register} : The {@link Register} on that cell,
	 * 						      or {@code null} if there isn't one.
 	 */
	public Register getRegisterFromCell(MapCell mapCell) {
		// Check through all registers
		for (Register register : registers) {
			// If the Cell matches, return the register
			if (register.getRegisterCell() == mapCell) {
				return register;
			}
		}
		// If none found, return null
		return null;
	}

	/**
	 * Reset the {@link CustomerController} by removing all spawned
	 * {@link Customer}s.
	 */
	public void reset() {
		// Clear the customer arrays
		customers.clear();
		toSpawn.clear();
		drawCustomers.clear();

		// Remove all customers from the registers
		for (Register register : registers) {
			register.setCustomer(null);
		}
	}

	/**
	 * Clear all variables that should be disposed.
	 */
	public void dispose() {
		customers.clear();
		toSpawn.clear();
		drawCustomers.clear();
		registers.clear();
		getMoney = null;
		loseReputation = null;
		map = null;
	}

	/**
	 * Serialize all of the {@link Customer}s in {@link JsonValue}s,
	 * apart from {@link Customer}s who are leaving, and return them
	 * as a {@link JsonValue} array.
	 * @return {@link JsonValue} : The serialized data.
	 */
	public JsonValue serializeCustomers() {
		// Create the cooks JsonValue
		JsonValue customersArrayRoot = new JsonValue(JsonValue.ValueType.array);

		// For each Cook, add it to the cooks JsonValue
		for (Customer customer : customers) {
			JsonValue customerData = customer.serial();
			if (customerData != null) {
				customerData.addChild("to_spawn", new JsonValue(toSpawn.contains(customer, true)));
				customersArrayRoot.addChild(customerData);
			}
		}

		// JsonValue customersRoot = new JsonValue(JsonValue.ValueType.object);
		// // Add the cooks JsonValue to the root JsonValue
		// customersRoot.addChild("customers", customersRoot);

		// return customersRoot;
		return customersArrayRoot;
	}

	/**
	 * Deserialize a {@link JsonValue} for {@link Customer}s, and add them
	 * to everything they need to be.
	 * @param jsonValue {@link JsonValue} : The {@link JsonValue} to deserialize.
	 */
	public void deserializeCustomers(JsonValue jsonValue) {
		// Clear the customers
		customers.clear();
		toSpawn.clear();
		drawCustomers.clear();

		// Loop through the cooks JsonValue
		for (JsonValue customerObject : jsonValue) {
			// Create a new Cook-
			Customer customer = new Customer(customerObject.getInt("custno"), this, textureManager);
			customer.x = customerObject.getFloat("x");
			customer.y = customerObject.getFloat("y");
			customer.setRequest(new Request(customerObject.get("request")));
			customer.waitTimer = customerObject.getFloat("wait_timer");
			customer.moveSpeed = customerObject.getFloat("move_speed");
			customer.visibility = 1f;
			setCustomersListeners(customer);
			if (customerObject.getBoolean("to_spawn")) {
				// Add the Cook to the toSpawn array
				toSpawn.add(customer);
			} else {
				Register registerAtPos = getRegisterAtPos(customerObject.getInt("reg_x"), customerObject.getInt("reg_y"));
				if (registerAtPos == null) {
					toSpawn.add(customer);
					continue;
				}

				customerOnRegister(customer,registerAtPos);

				// Add the Cook to the cooks array
				customers.add(customer);
				// Add the Cook to the drawCustomers array
				drawCustomers.add(customer);

				if (customer.y >= MapManager.gridToPos(customerObject.getInt("reg_y"))) {
					customer.waiting = true;
					customer.y = MapManager.gridToPos(customerObject.getInt("reg_y"));
				}
			}
		}
	}
}
