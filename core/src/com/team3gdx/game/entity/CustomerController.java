package com.team3gdx.game.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.util.Control;

public class CustomerController {
	int lockout;
	int amountActiveCustomers;
	ArrayList<ArrayList<Integer>> customerCells;
	Customer[] customers = new Customer[5];
	Customer[] leavingcustomers = new Customer[5];
	TiledMap gameMap;
	int top;
	int bottom;
	int xCoordinate;

	public CustomerController(TiledMap map) {
		this.gameMap = map;
		computeCustomerZone(gameMap);
		amountActiveCustomers = 0;
		lockout = 0;
	}

	private void computeCustomerZone(TiledMap gameMap) {
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
				ArrayList<Integer> templist = new ArrayList<Integer>();
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
	}

	public void spawnCustomer() {
		for (int i = 0; i < this.customers.length; i++) {
			if (customers[i] == null) {
				customers[i] = new Customer(this.xCoordinate, this.bottom, this.top - i);
				amountActiveCustomers += 1;
				break;
			}
		}
	}

	public void delCustomer(int num) {
		if (this.customers[num].locked) {
			amountActiveCustomers -= 1;
			this.leavingcustomers[num] = this.customers[num];
			this.leavingcustomers[num].setTargetsquare(-1);
			this.customers[num] = null;
		}
	}

	public void delCustomer(Customer customer) {
		int i = 0;
		for (Customer curCustomer : customers) {
			if (customers[i] == customer) {
				delCustomer(i);
				return;
			}
			i++;
		}

	}

	// public void drawCustBot(Batch b1){
	// for (Customer c: this.customers){
	// if (c != null) {
	// c.renderCustomersBot(b1);
	// }
	// }
	// for (Customer c: this.leavingcustomers){
	// if (c != null) {
	// c.renderCustomersBot(b1);
	// }
	// }
	// }
	public void drawCustTop(Batch b1) {
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
	}

	public int toRemove;

	public void updateCustomers(Control ctrls) {
		if (lockout == 100) {
			lockout = 0;
		}
		if (lockout > 0) {
			lockout++;
		}

		if (ctrls.del && lockout == 0) {
			int ctr1 = 0;
			for (Customer c : this.customers) {
				if (c != null) {
					if (c.locked) {
						break;
					}
				}
				ctr1++;
			}
			if (ctr1 < 5) {
				delCustomer(ctr1);
				lockout = 1;
			}
		}
		int sumlocked = 0;
		for (Customer c : this.customers) {
			if (c != null) {
				c.stepTarget();
				if (c.locked) {
					sumlocked++;
				}
			}
		}
//		if (sumlocked == amountActiveCustomers) {
//			spawnCustomer();
//		}
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
	}

	public Customer isCustomerAtPos(Vector2 pos) {
		for (Customer customer : customers)
			if (customer != null && Math.floor(customer.posx / 60) == pos.x && Math.floor(customer.posy / 60) == pos.y
					&& customer.locked)
				return customer;

		return null;
	}
}
