package com.cscie97.store.controller;

import com.cscie97.store.model.*;

import java.time.Clock;

/**
 * The Concrete Command class for the Command Pattern. Customer approaches turnstile. Reflects situation to checkout
 * customer's basket, charge them the cost of the items, let them out of the store, say goodbye, restore basket
 */
public class Checkout extends Command{
    /**
     * The turnstile which helped with the checkout
     */
    Turnstile turnstile;

    /**
     * The store in which this happened
     */
    Store store;

    /**
     * The customer who was checked out
     */
    Customer customer;

    /**
     * The basket of the customer
     */
    Basket basket;

    /**
     * Constructor to create Checkout object
     * @param turnstile Turnstile   The turnstile which helped with the checkout
     * @param store Store   The store in which this happened
     * @param customer  Customer    The customer who was checked out
     */
    public Checkout(Turnstile turnstile, Store store, Customer customer)
    {
        this.turnstile = turnstile;
        this.customer = customer;
        this.basket = customer.getAssignedBasket();
        this.store = store;
    }

    /**
     * 1. Identify customer
     * 2. compute the total cost of items in the basket
     * 3. create transaction
     * 4. submit the transaction to the blockchain
     * 5. open turnstile
     * 6. goodbye message :"goodbye customer_name, thanks for shopping at store_name!"
     */
    public void execute()
    {
        int basketTotal = this.basket.getBasketTotalCost();
        int accountBalance = this.customer.getAccountBalance();
        this.customer.processTransaction((this.customer.getCustomerBlockchainAddress() + Clock.systemUTC().instant().toString()),
                basketTotal, 10, "Checkout Transaction", this.customer.getCustomerBlockchainAddress(),
                this.store.getStoreBlockChainAddress());

        System.out.println("Goodbye " + this.customer.getCustomerFirstName() + ", thanks for shopping at " +
                store.getStoreName() + "\n");

        this.basket.clearBasket();
        this.customer.setAssignedBasket(null);
        System.out.println("Cleared basket and removed basket from customer. Final Customer Status: \n" +
                this.customer.toString());

    }
}
