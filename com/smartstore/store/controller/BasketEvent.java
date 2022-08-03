package com.cscie97.store.controller;

import com.cscie97.store.model.*;

/**
 * The Concrete Command class for the Command Pattern. Customer (adds|removes) product from aisle:shelf.
 * Reflects basket change by a customer.
 */
public class BasketEvent extends Command{
    /**
     * The camera that captured this event
     */
    Camera camera;

    /**
     * The store where this happened
     */
    Store store;

    /**
     * The customer whose basket changed
     */
    Customer customer;

    /**
     * The customer's basket that changed
     */
    Basket basket;

    /**
     * The product that was added or removed in the basket
     */
    Product product;

    /**
     * The inventory associated with the product
     */
    Inventory inventory;

    /**
     * The number of units of the product that changed
     */
    int count;

    /**
     * Location store:aisle of the customer when this event happened
     */
    String location;

    /**
     * Constructor to create BasketEvent objects
     * @param camera    Camera  The camera that captured this event
     * @param store Store   The store where this happened
     * @param customer  Customer    The customer whose basket changed
     * @param product   Product The product that was added or removed in the basket
     * @param inventory Inventory   The inventory associated with the product
     * @param count int The number of units of the product that changed
     * @param location  String Location store:aisle of the customer when this event happened
     */
    public BasketEvent(Camera camera, Store store, Customer customer, Product product, Inventory inventory,
                            int count, String location){
        this.camera = camera;
        this.store = store;
        this.customer = customer;
        this.product = product;
        this.inventory = inventory;
        this.count = count;
        this.location = location;
    }

    /**
     * The execute method called by the Invoker in the Command Pattern.
     * 1. Add/remove the product to/from customer's basket
     * 2. Remove/Add product from aisle:shelf
     * 3. robot: perform task restock for aisle:shelf and product
     */
    public void execute(){
        System.out.println("BEFORE BASKET EVENT EXECUTION: \n");
        System.out.println(this.customer.getAssignedBasket().toString());
        System.out.println(this.inventory.toString());

        if(count > 0)
        {
            // 1. Add/remove the product to/from <customer> basket
            this.customer.getAssignedBasket().addBasketItem(this.product.getProductId(), this.product, count);

            // 2. Remove/Add product from <aisle:shelf>
            this.inventory.updateCount(this.inventory.getInventoryCount() - count);
            if(this.inventory.getInventoryCount() < 3)
            {
                // 3. robot: perform task restock for <aisle:shelf> and <product>
                System.out.println("ROBOT RESTOCK INVENTORY " + this.inventory + ", CURRENT COUNT ONLY " +
                        this.inventory.getInventoryCount());
                this.inventory.updateCount(this.inventory.getInventoryCapacity());
                System.out.println("INVENTORY RESTOCKED to " + this.inventory.getInventoryCount());
            }
        }
        else if(count < 0)
        {
            this.customer.getAssignedBasket().removeBasketItem(this.product.getProductId(), count);
            this.inventory.updateCount(this.inventory.getInventoryCount() + count);
        }

        System.out.println("SUCCESSFULLY EXECUTED BASKET EVENT ...\n");

        System.out.println("AFTER BASKET EVENT EXECUTION: \n");
        System.out.println(this.customer.getAssignedBasket().toString());
        System.out.println(this.inventory.toString());

        this.customer.updateLocation(this.location);
        System.out.println("UPDATED CUSTOMER LOCATION: \n");
        System.out.println(this.customer.toString());
    }
}
