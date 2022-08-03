package com.cscie97.store.controller;

import com.cscie97.store.model.*;

/**
 * The Concrete Command class for the Command Pattern. Turnstile customer waiting to enter at the turnstile.
 */
public class EnterStore extends Command
{
    /**
     * The turnstile that generated the EnterStore event
     */
    Turnstile turnstile;

    /**
     * The store in which the customer entered the store
     */
    Store store;

    /**
     * The customer who entered the store
     */
    Customer customer;

    /**
     * The basket that was assigned to this customer at entry
     */
    Basket basket;

    /**
     * The location where the customer entered represented as store:aisle
     */
    String location;

    /**
     * Constructor for EnterStore class, also the creation mechanism for the client in the Command Pattern
     * @param turnstile Turnstile   The turnstile that generated the EnterStore event
     * @param store Store   The store in which the customer entered the store
     * @param customer  Customer    The customer who entered the store
     * @param unassignedBasket  Basket  The basket that was assigned to this customer at entry
     * @param location  String  The location where the customer entered represented as store:aisle
     */
    public EnterStore(Turnstile turnstile, Store store, Customer customer, Basket unassignedBasket, String location)
    {
        this.turnstile = turnstile;
        this.store = store;
        this.customer = customer;
        this.basket = unassignedBasket;
        this.location = location;
    }

    /**
     * The execute method called by the Invoker in the Command Pattern.
     * 1. Lookup Customer
     * 2. Check for positive account balance
     * 3. Assign Customer a basket
     * 4. open turnstile
     * 5. welcome message "Hello customer_name, welcome to store name!"
     */

    public void execute()
    {
        System.out.println("Assigned Basket: \n" + this.basket);
        this.customer.setAssignedBasket(basket);
        this.basket.setAssignedCustomer(this.customer);

        this.customer.updateLocation(this.location);

        System.out.println("Hello " + this.customer.getCustomerFirstName() + ", welcome to " +
                this.store.getStoreName() + "\n");
        System.out.println("CUSTOMER WHO ENTERED STORE: \n" + this.customer.toString());
    }
}
