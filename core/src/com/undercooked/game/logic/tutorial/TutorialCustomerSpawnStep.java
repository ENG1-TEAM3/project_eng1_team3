package com.undercooked.game.logic.tutorial;

import com.undercooked.game.entity.Entity;
import com.undercooked.game.entity.customer.Customer;
import com.undercooked.game.entity.customer.CustomerController;
import com.undercooked.game.food.Request;

public class TutorialCustomerSpawnStep extends TutorialFollowEntityStep {
    CustomerController customerController;
    Customer spawnedCustomer;
    Request request;
    public TutorialCustomerSpawnStep(String text, float textSpeed, CustomerController customerController, Request request) {
        super(text, textSpeed, null);
        this.customerController = customerController;
        this.request = request;
    }

    @Override
    public void update(float delta) {
        if (spawnedCustomer != null) {
            x = spawnedCustomer.getX();
            y = spawnedCustomer.getY();
        }
        super.update(delta);
    }

    @Override
    public void start() {
        super.start();
        // Spawn a customer
        spawnedCustomer = customerController.spawnCustomer(request);
    }
}
