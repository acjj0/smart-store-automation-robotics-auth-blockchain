package com.cscie97.store.controller;

import com.cscie97.store.model.*;

/**
 * The Concrete Command class for the Command Pattern. Microphone hears customer says: Please get me number of
 * product. Reflects product fetch to a customer by a robot.
 */
public class FetchProduct extends Command
{
    /**
     * The Microphone into which this request was made
     */
    Microphone microphone;

    /**
     * The store where this event happened
     */
    Store store;

    /**
     * The customer who needs the product fetched
     */
    Customer customer;

    /**
     * The basket of the customer
     */
    Basket basket;

    /**
     * The product that was requested
     */
    Product product;

    /**
     * The number of units of the product requested
     */
    int count;

    /**
     * The inventory associated with this product
     */
    Inventory inventory;

    /**
     * Location store:aisle where the robot with the product should go to deliver to the customer
     */
    String location;

    /**
     * Constructor for FetchProduct objects
     * @param microphone    Microphone  The Microphone into which this request was made
     * @param store Store   The store where this event happened
     * @param customer  Customer    The customer who needs the product fetched
     * @param product   Product The product that was requested
     * @param inventory Inventory   The inventory associated with this product
     * @param count int The number of units of the product requested
     * @param location  String Location store:aisle where the robot with the product should go to deliver to the customer
     */
    public FetchProduct(Microphone microphone, Store store, Customer customer, Product product, Inventory inventory,
                       int count, String location){
        this.microphone = microphone;
        this.store = store;
        this.customer = customer;
        this.product = product;
        this.inventory = inventory;
        this.count = count;
        this.location = location;
    }

    /**
     * The execute method called by the Invoker in the Command Pattern.
     * robot command: fetch number of product from aisle and shelf and bring to customer in aisle customer_location
     */
    public void execute()
    {
        System.out.println("BEFORE ROBOT FETCH EVENT EXECUTION: \n");
        System.out.println(this.customer.getAssignedBasket().toString());
        System.out.println(this.inventory.toString());

        if(count > 0)
        {
            this.customer.getAssignedBasket().addBasketItem(this.product.getProductId(), this.product, count);
            this.inventory.updateCount(this.inventory.getInventoryCount() - count);
            if(this.inventory.getInventoryCount() < 3)
            {
                // Robot to restock inventory and product
                System.out.println("ROBOT GO RESTOCK INVENTORY " + this.inventory + "CURRENT COUNT IS " +
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

        System.out.println("SUCCESSFULLY EXECUTED ROBOT FETCH EVENT ...\n");

        System.out.println("AFTER ROBOT FETCH EVENT EXECUTION: \n");
        System.out.println(this.customer.getAssignedBasket().toString());
        System.out.println(this.inventory.toString());

        this.customer.updateLocation(this.location);
        System.out.println("UPDATED CUSTOMER LOCATION: \n");
        System.out.println(this.customer.toString());
    }
}
