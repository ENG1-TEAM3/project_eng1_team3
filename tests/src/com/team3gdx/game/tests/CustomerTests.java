package com.team3gdx.game.tests;

import com.badlogic.gdx.Gdx;
import static org.junit.Assert.*;

import com.team3gdx.game.entity.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class CustomerTests {

    @Test
    public void testArrived () {
        Customer customer = new Customer(10, 10, 10, 1);
        assertEquals(Math.round(customer.arrived()), Math.round(System.currentTimeMillis()), 0.0001);
    }

}
