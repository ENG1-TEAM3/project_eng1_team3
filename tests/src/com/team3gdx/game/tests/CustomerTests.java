package com.team3gdx.game.tests;

import com.team3gdx.game.entity.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class CustomerTests {

    // Check arrival time is as expected
    // Round this to a reasonable number of milliseconds
    @Test
    public void testArrived() {
        Customer customer = new Customer(10, 10, 10, 1);
        assertEquals(Math.round(customer.arrived()), Math.round(System.currentTimeMillis()), 0.0001);
    }

    // Check waiting time of a customer is recorded correctly
    // Use 10 as must take into account the time it takes to run the instruction hence 10ms of buffer allows for this.
    @Test
    public void testWaitTime() throws InterruptedException {
        Customer customer = new Customer(10, 10, 10, 1);
        customer.arrived();
        Thread.sleep(100);
        float result = Math.round(customer.waitTime());
        assertEquals(result, 100, 10);
    }

    @Test
    public void testTargetSquare() {
        Customer customer = new Customer(10, 10, 10, 1);
        customer.setTargetsquare(2);
        assertEquals(customer.targetsquare, 2, 0.0001);
    }

    @Test
    public void testStepTarget() {
        // less than
        Customer customer = new Customer(10, 10, 10, 1);
        customer.setTargetsquare(20);
        customer.stepTarget();
        assertEquals((10 * 64) + 1, customer.posy, 0.01);

        // greater than
        Customer customer2 = new Customer(10, 10, 10, 1);
        customer2.setTargetsquare(2);
        customer2.stepTarget();
        assertEquals((10 * 64) - 1, customer2.posy, 0.01);

        // equal to >0

        Customer customer3 = new Customer(10, 1, 10, 1);
        customer3.posy = 160;
        customer3.setTargetsquare(2);
        customer3.stepTarget();
        assertEquals(customer3.startposx + 64, customer3.posx, 0.01);
        assertTrue(customer3.locked);

        // equal to <0

        Customer customer4 = new Customer(10, 1, 10, 1);
        customer4.posy = -96;
        customer4.setTargetsquare(-2);
        customer4.stepTarget();
        assertEquals(-96, customer4.posy, 0.01);
        assertTrue(customer4.readyfordeletion);
    }

}
