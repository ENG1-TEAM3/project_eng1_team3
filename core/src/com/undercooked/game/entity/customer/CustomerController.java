package com.undercooked.game.entity.customer;

import java.util.ArrayList;
import java.util.Comparator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.food.Item;
import com.undercooked.game.food.Request;
import com.undercooked.game.logic.GameLogic;
import com.undercooked.game.map.*;
import com.undercooked.game.util.Constants;
import com.undercooked.game.util.Listener;

public class CustomerController {
	int lockout;
	int amountActiveCustomers;
	ArrayList<ArrayList<Integer>> customerCells;
	TextureManager textureManager;
	/** An array of all {@link Customer}s, sorted in the order they are spawned. */
	Array<Customer> customers;
	/** The {@link #customers} array, but it is sorted based on Y level. */
	Array<Customer> drawCustomers;
	Array<Customer> toSpawn;
	Array<Register> registers;

	Map map;
	float spawnX, spawnY;
	float customerSpeed, waitSpeed;

	Listener<Customer> getMoney;
	Listener<Customer> loseReputation;
	Comparator<Customer> customerDrawComparator;
	CustomerTarget targetType;

	public CustomerController(TextureManager textureManager, Map map) {
		this.textureManager = textureManager;
		this.map = map;
		this.customers = new Array<>();
		this.drawCustomers = new Array<>();
		this.toSpawn = new Array<>();
		this.registers = new Array<>();
		// computeCustomerZone(gameMap);
		amountActiveCustomers = 0;
		lockout = 0;

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

	public CustomerController(TextureManager textureManager) {
		this(textureManager, null);
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public void load(String textureGroup) {
		// Finds the registers
		findRegisters();
		// Load all the customer textures
		textureManager.load(textureGroup, "entities/cust3f.png");
		textureManager.load(textureGroup, "entities/cust3b.png");
		textureManager.load(textureGroup, "entities/cust3r.png");
		textureManager.load(textureGroup, "entities/cust3l.png");
		// Load any customers that are already in the arrays
		for (Customer customer : customers) {
			customer.load(textureManager, textureGroup);
		}
		for (Customer customer : toSpawn) {
			customer.load(textureManager, textureGroup);
		}
	}

	public void postLoad() {
		// Post load any customers that are already in the arrays
		for (Customer customer : customers) {
			customer.postLoad(textureManager);
		}
		for (Customer customer : toSpawn) {
			customer.postLoad(textureManager);
		}
	}

	public void unload() {
		// Unload the texture
		textureManager.unloadTexture("entities/cust3f.png");
		textureManager.unloadTexture("entities/cust3b.png");
		textureManager.unloadTexture("entities/cust3r.png");
		textureManager.unloadTexture("entities/cust3l.png");
	}

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

	public void draw(SpriteBatch batch) {
		// First sort the draw array
		drawCustomers.sort(customerDrawComparator);
		// Draw all the Customers
		for (Customer customer : drawCustomers) {
			customer.draw(batch);
		}
	}

	public void draw(ShapeRenderer shape) {
		// First sort the draw array
		drawCustomers.sort(customerDrawComparator);
		// Draw all the Customers
		for (Customer customer : drawCustomers) {
			customer.draw(shape);
		}
	}

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

	public boolean canSpawn() {
		return !(customerInSquareGrid(spawnX, spawnY) || customerInSquareGrid(spawnX, spawnY + 1));
	}

	public void spawnCustomer(Request request) {
		Customer newCustomer = new Customer(3, this, textureManager);
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
		amountActiveCustomers += 1;
	}

	protected void setCustomersListeners(Customer customer) {
		customer.servedListener = getMoney;
		customer.failedListener = loseReputation;
	}

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

	public boolean customerInSquareGrid(float x, float y) {
		return customerInSquareGrid(x, y, null);
	}

	public boolean customerInSquarePos(float x, float y) {
		return customerInSquareGrid(MapManager.posToGrid(x), MapManager.posToGrid(y), null);
	}

	public boolean customerInSquarePos(float x, float y, Customer ignoredCustomer) {
		return customerInSquareGrid(MapManager.posToGrid(x), MapManager.posToGrid(y), ignoredCustomer);
	}

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

	protected void deleteCustomer(Customer customer) {
		customers.removeValue(customer, true);
		toSpawn.removeValue(customer, true);
		drawCustomers.removeValue(customer, true);
		amountActiveCustomers -= 1;

	}

	public void setReputationListener(Listener<Customer> reputationListener) {
		this.loseReputation = reputationListener;
	}

	public void setServedListener(Listener<Customer> servedListener) {
		this.getMoney = servedListener;
	}

	public void setTargetType(CustomerTarget targetType) {
		this.targetType = targetType;
	}

	public void setCustomerSpeed(float customerSpeed) {
		this.customerSpeed = customerSpeed;
	}

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

	public boolean serve(MapCell registerCell, Item item) {
		// Make sure that the target is a valid register
		// by getting the register, and making sure the
		// result isn't null.
		Register register = getRegister(registerCell);
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

	public boolean isRegister(MapCell cell) {
		// Loop through the registers and check
		for (Register register : registers) {
			if (register.getRegisterCell() == cell) {
				return true;
			}
		}
		return false;
	}

	private Register getRegister(MapCell cell) {
		// Loop through the registers and check
		for (Register register : registers) {
			if (register.getRegisterCell() == cell) {
				return register;
			}
		}
		return null;
	}

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

	public void dispose() {
		customers.clear();
		toSpawn.clear();
		drawCustomers.clear();
		registers.clear();
		getMoney = null;
		loseReputation = null;
		map = null;
	}

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

	public void deserializeCustomers(GameLogic logic, JsonValue jsonValue) {
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
			amountActiveCustomers += 1;
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
