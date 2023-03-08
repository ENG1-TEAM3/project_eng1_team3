package com.team3gdx.game.tests;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.entity.Customer;
import com.team3gdx.game.entity.CustomerController;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class CustomerControllerTests {

    TiledMap map1 = new TmxMapLoader().load("map/art_map/customertest.tmx");

    @Test
    public void testController() {
        CustomerController newController = new CustomerController(map1);
        assertEquals(newController.amountActiveCustomers, 0);
        assertEquals(newController.lockout, 0);
    }

    @Test
    public void testSpawnCustomer() {
        CustomerController newController = new CustomerController(map1);
        newController.spawnCustomer();
        int total = 0;
        for (int i = 0; i < newController.customers.length; i++) {
            if (newController.customers[i] != null) {
                total += 1;
            }
        }
        assertEquals(1, total, 0.001);
        assertEquals(1, newController.amountActiveCustomers, 0.0001);
    }

    @Test
    public void testDeleteCustomer() {
        CustomerController newController = new CustomerController(map1);
        // Create a customer to delete
        newController.spawnCustomer();
        // We know from previous tests this creates one customer
        testSpawnCustomer();
        // Grab the customer to delete
        Customer toDelete = newController.customers[0];
        // Customer should not delete if it is not locked
        testSpawnCustomer();
        // Lock customer and delete
        toDelete.locked = true;
        newController.delCustomer(toDelete);
        // Check it has been deleted
        int total = 0;
        for (int i = 0; i < newController.customers.length; i++) {
            if (newController.customers[i] != null) {
                total += 1;
            }
        }
        assertEquals(0, total, 0.001);
        assertEquals(0, newController.amountActiveCustomers, 0.0001);
    }

    @Test
    public void isCustomerAtPos(){
        CustomerController newController = new CustomerController(map1);
        // No customers
        Customer failedResult = newController.isCustomerAtPos(new Vector2(60, 60));
        assertEquals(null, failedResult);
        // Spawned customer
        newController.spawnCustomer();
        Customer one = null;
        one = newController.customers[0];
        one.locked = true;
        Customer trueResult = newController.isCustomerAtPos(new Vector2(1, 0));
        assertEquals(one, trueResult);
    }
}
