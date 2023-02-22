package com.team3gdx.game.tests;

import com.badlogic.gdx.Gdx;
import static org.junit.Assert.*;

import com.team3gdx.game.entity.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class CustomerTests {

    // Check arrival time is as expected
    @Test
    public void testArrived () {
        Customer customer = new Customer(10, 10, 10, 1);
        assertEquals(Math.round(customer.arrived()), Math.round(System.currentTimeMillis()), 0.0001);
    }

    // Check waiting time of a customer is recorded correctly
    // Use 10 as must take into account the time it takes to run the instruction hence 10ms of buffer allows for this.
    @Test
    public void testWaitTime () throws InterruptedException {
        Customer customer = new Customer(10, 10, 10, 1);
        customer.arrived();
        Thread.sleep(100);
        float result = Math.round(customer.waitTime());
        assertEquals(result, 100, 10);
    }

}
