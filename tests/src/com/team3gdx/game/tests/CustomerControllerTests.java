package com.team3gdx.game.tests;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.team3gdx.game.entity.Customer;
import com.team3gdx.game.entity.CustomerController;
import com.team3gdx.game.save.CustomerInfo;
import com.team3gdx.game.util.GameMode;
import com.team3gdx.game.util.ScenarioMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class CustomerControllerTests {

    TiledMap map1 = new TmxMapLoader().load("map/art_map/customertest.tmx");
    GameMode mode = mock(ScenarioMode.class);

    @Test
    public void testController() {
        CustomerController newController = new CustomerController(map1, mode);
        assertEquals(newController.amountActiveCustomers, 0);
        assertEquals(newController.lockout, 0);
    }

    @Test
    public void testSpawnWave() {
        CustomerController controller = new CustomerController(map1, mode);
        Customer cust = new Customer(1, 2, 3, 4);

        // No customers in list hence spawn 1 customer
        controller.spawnWave();
        assertEquals(controller.amountActiveCustomers, 1, 0.001);

        // Two customers in list so three active customers after spawning one
        controller.customers[0] = cust;
        controller.customers[1] = cust;
        controller.spawnWave();
        assertEquals(controller.amountActiveCustomers, 2, 0.001);

    }

    @Test
    public void testSpawnWaveSave() {
        CustomerController controller = new CustomerController(map1, mode);
        CustomerInfo cust1 = new CustomerInfo(1, "Burger", 1, 1, 2);

        // No customers passed in list
        CustomerInfo[] newList = new CustomerInfo[] {};
        controller.spawnWave(newList);
        assertEquals(controller.amountActiveCustomers, 0);

        // Two customers passed in list
        CustomerInfo[] newList2 = new CustomerInfo[] {cust1, cust1};
        controller.spawnWave(newList2);
        assertEquals(controller.amountActiveCustomers, 2);
    }



    @Test
    public void testDeleteCustomer() {
        CustomerController controller = new CustomerController(map1, mode);

        // Create a customer to delete
        controller.spawnWave();
        assertEquals(1, controller.amountActiveCustomers);

        // Test should not delete when not locked
        Customer toDelete = controller.customers[0];
        controller.delCustomer(toDelete);
        assertEquals(1, controller.amountActiveCustomers, 0.001);

        // Test should delete when locked
        controller.customers[0].locked = true;
        Customer toDeleteL = controller.customers[0];
        controller.delCustomer(toDeleteL);
        assertEquals(0, controller.amountActiveCustomers, 0.001);
    }

    @Test
    public void isCustomerAtPos(){
        CustomerController controller = new CustomerController(map1, mode);
        // No customers
        Customer failedResult = controller.isCustomerAtPos(new Vector2(60, 60));
        assertEquals(null, failedResult);

        // Spawned customer
        controller.spawnWave();
        Customer one = controller.customers[0];
        one.locked = true;
        Customer trueResult = controller.isCustomerAtPos(new Vector2(1, 0));
        assertEquals(one, trueResult);
    }

}
