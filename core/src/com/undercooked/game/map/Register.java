package com.undercooked.game.map;

import com.undercooked.game.entity.customer.Customer;

public class Register {
    public Customer customer;
    public MapCell registerCell;

    public Register(MapCell registerCell) {
        this.registerCell = registerCell;
    }

    public boolean hasCustomer() {
        return customer != null;
    }
}