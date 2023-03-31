package com.undercooked.game.entity.customer;

import java.util.ArrayList;
import java.util.Comparator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.undercooked.game.assets.TextureManager;
import com.undercooked.game.food.Item;
import com.undercooked.game.food.Request;
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

	Listener<Customer> getMoney;
	Listener<Customer> loseReputation;
	Comparator<Customer> customerDrawComparator;

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
	}

	public CustomerController(TextureManager textureManager) {
		this(textureManager, null);
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public void load(String textureGroup) {
		// Load all the customer textures
		textureManager.load(textureGroup, "entities/cust3f.png");
		textureManager.load(textureGroup, "entities/cust3b.png");
		textureManager.load(textureGroup, "entities/cust3r.png");
		textureManager.load(textureGroup, "entities/cust3l.png");
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
			customerOnRegister(waitingCustomer, firstOpenRegister());
		}
		// Update the Customers
		for (int index = 0 ; index < customers.size ; index++) {
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
		return !(customerInSquareGrid(spawnX, spawnY) || customerInSquareGrid(spawnX, spawnY+1));
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

		newCustomer.x = MapManager.gridToPos(spawnX);
		newCustomer.y = MapManager.gridToPos(spawnY);

		newCustomer.servedListener = getMoney;
		newCustomer.failedListener = loseReputation;
		newCustomer.setRequest(request);
		// Try to put the customer on a register
		customerOnRegister(newCustomer, firstOpenRegister());
		amountActiveCustomers += 1;
	}

	public Register firstOpenRegister() {
		// Loop through the registers
		for (Register register : registers) {
			// Return it if the Customer is null
			if (register.getCustomer() == null) {
				return register;
			}
		}
		// If it doesn't find one, return null.
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

	public void findRegisters() {
		// Clear the register array
		registers.clear();
		// Loop through all the map cells on the left wall.
		// All valid registers have to be placed there.
		// Loop from top to bottom, so that they are ordered
		// top to bottom.
		for (int y = map.getHeight()-1 ; y >= 0 ; y--) {
			MapCell thisCell = map.getCell(0, y);
			// Make sure it's not null
			if (thisCell == null) continue;
			// Then get the MapEntity.
			MapEntity thisEntity = thisCell.getMapEntity();
			// Make sure it's not null
			if (thisEntity == null) continue;

			// If it gets here, make sure it's the register
			if (!thisEntity.getID().equals(Constants.REGISTER_ID)) continue;

			// If it's a register, then add the MapCell to the array
			registers.add(new Register(thisCell));
		}
		System.out.println(registers);
	}

	public boolean serve(MapCell registerCell, Item item) {
		// Make sure that the target is a valid register
		// by getting the register, and making sure the
		// result isn't null.
		Register register = getRegister(registerCell);
		if (register == null) return false;

		// Then get the customer of that register
		Customer customer = register.getCustomer();
		// If it's null, then return false
		if (customer == null) return false;

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
	 * Check whether the customer zone is correct in the tile map, and construct
	 * data structures for it
	 * 
	 * @param gameMap - The game tilemap
	 */
	/*private void computeCustomerZone(TiledMap gameMap) {
		// ==============================================================================================================
		TiledMapTileLayer botlayer = (TiledMapTileLayer) gameMap.getLayers().get(0);
		customerCells = new ArrayList<>();
		int mapheight = botlayer.getHeight();
		int mapwidth = botlayer.getWidth();
		for (int y = 0; y < mapheight; y++) {
			for (int x = 0; x < mapwidth; x++) {
				Cell cel1 = botlayer.getCell(x, y);
				if (cel1 != null) {
					TiledMapTile til1 = cel1.getTile();
					MapProperties mp1 = til1.getProperties();
					Object value = mp1.get("customer_zone");
					boolean isCustomerZone = value != null && (boolean) value;
					if (isCustomerZone) {
						ArrayList<Integer> e = new ArrayList<>();
						e.add(x);
						e.add(y);
						customerCells.add(e);
					}
				}
			}
		}
		// ^Scan tilemap for Customer zone
		// tile==========================================================================
		if (customerCells.size() == 0) {
			throw new IllegalArgumentException("No customer zone was included in the tile map");
		}
		// ^If no Customer zone tiles exist throw
		// exception==============================================================
		Integer[] xvalues = new Integer[customerCells.size()];
		Integer[] yvalues = new Integer[customerCells.size()];
		int ctr = 0;
		for (ArrayList<Integer> xypair : customerCells) {
			xvalues[ctr] = xypair.get(0);
			yvalues[ctr] = xypair.get(1);
			ctr++;
		}
		// ^Split x y pairs into 2 separate
		// arrays=======================================================================
		Set<Integer> uniquexvalues = new HashSet<>(Arrays.asList(xvalues));
		if (uniquexvalues.size() != 2) {
			throw new IllegalArgumentException("Customer zone must be a 2 wide rectangle leading to bottom of map");
		}
		// ^Throw exception if more than 2 unique x values exist - the rectangle is not
		// 2 wide===========================
		ArrayList<Integer> yvalueslist = new ArrayList<>(Arrays.asList(yvalues));
		if (!yvalueslist.contains(0)) {
			throw new IllegalArgumentException("Customer zone must extend to the bottom of the map");
		}
		// ^Throw exception if the customer zone tile list does not contain tiles at the
		// bottom index====================
		int ymax = Collections.max(yvalueslist);
		Integer[] uniquexvaluesarray = uniquexvalues.toArray(new Integer[] {});
		for (Integer unx : uniquexvaluesarray) {
			for (int i = ymax; i > 0; i--) {
				ArrayList<Integer> templist = new ArrayList<>();
				templist.add(unx);
				templist.add(i);
				if (!customerCells.contains(templist)) {
					throw new IllegalArgumentException("Customer zone must be a filled rectangle");
				}
			}
		}
		// ^Throw exception if the customer zone is not a filled rectangle. It does this
		// by looking from the maximum=====
		// y value downwards to check if a tile exists all the way down to zero, on both
		// columns.
		this.top = ymax;
		this.bottom = 0;
		this.xCoordinate = xvalues[0]; // We can do this because the search scans left to right, 0th value will be left
	}*/

	/*public void delCustomer(int num) {
		if (this.customers[num].locked) {
			amountActiveCustomers -= 1;
			this.leavingcustomers[num] = this.customers[num];
			this.leavingcustomers[num].setTargetsquare(-1);
			this.customers[num] = null;
		}
	}

	public void delCustomer(Customer customer) {
		for (int i = 0; i < this.customers.length; i++) {
			if (customers[i] == customer) {
				delCustomer(i);
				return;
			}
		}

	}*/

	/**
	 * Draw top of customers
	 * 
	 * @param b1 - spritebatch to render with
	 */
	/*public void drawCustTop(Batch b1) {
		for (Customer c : this.customers) {
			if (c != null) {
				c.renderCustomersBot(b1);
			}
		}
		for (Customer c : this.leavingcustomers) {
			if (c != null) {
				c.renderCustomersBot(b1);
			}
		}
		for (Customer c : this.customers) {
			if (c != null) {
				c.renderCustomersTop(b1);
			}
		}
		for (Customer c : this.leavingcustomers) {
			if (c != null) {
				c.renderCustomersTop(b1);
			}
		}
	}*/

	/**
	 * Update customers
	 */
	/*public void updateCustomers() {
		for (Customer c : this.customers) {
			if (c != null) {
				c.stepTarget();
			}
		}
		int ctr = 0;
		for (Customer c : this.leavingcustomers) {
			if (c != null) {
				c.stepTarget();
				if (c.readyfordeletion) {
					this.leavingcustomers[ctr] = null;
				}
			}
			ctr++;
		}
	}*/

	/**
	 * Check if any of the customers is at one position
	 * 
	 * @param pos - vector position
	 * @return null if no customers are at that position, return the customer that
	 *         is at that position
	 */
	/*public Customer isCustomerAtPos(Vector2 pos) {
		for (Customer customer : customers)
			if (customer != null && Math.ceil(customer.posx / 64f) == pos.x && Math.ceil(customer.posy / 64f) == pos.y
					&& customer.locked)
				return customer;
		return null;
	}*/
}
