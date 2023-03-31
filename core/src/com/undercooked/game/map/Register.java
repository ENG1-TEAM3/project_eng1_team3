package com.undercooked.game.map;

import com.undercooked.game.entity.customer.Customer;

public class Register {
    protected Customer customer;
    protected MapCell registerCell;

    public Register(MapCell registerCell) {
        this.registerCell = registerCell;
    }

    public boolean hasCustomer() {
        return customer != null;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setRegisterCell(MapCell registerCell) {
        this.registerCell = registerCell;
    }

    public Customer getCustomer() {
        return customer;
    }

    public MapCell getRegisterCell() {
        return registerCell;
    }
}