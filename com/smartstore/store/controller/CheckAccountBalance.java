package com.cscie97.store.controller;

import com.cscie97.store.model.Basket;
import com.cscie97.store.model.Customer;
import com.cscie97.store.model.Microphone;
import com.cscie97.store.model.Store;

/**
 * The Concrete Command class for the Command Pattern. Customer says "What is the total basket value?"
 * Reflects situation to calculate basket total and compare with the customer's account balance.
 */
public class CheckAccountBalance extends Command {
    /**
     * The microphone into which this request was made
     */
    Microphone microphone;

    /**
     * The customer whose account balance was requested
     */
    Customer customer;

    /**
     * The basket whose total to calculate
     */
    Basket basket;

    /**
     * The store in which this happened
     */
    Store store;

    /**
     * Constructor to create CheckAccountBalance objects
     * @param microphone    Microphone  The microphone into which this request was made
     * @param store Store   The store in which this happened
     * @param customer  Customer    The customer whose account balance was requested
     */
    public CheckAccountBalance(Microphone microphone, Store store, Customer customer)
    {
        this.microphone = microphone;
        this.customer = customer;
        this.basket = customer.getAssignedBasket();
        this.store = store;
    }

    /**
     * The execute method called by the Invoker in the Command Pattern.
     * 1. compute the value of items in the basket
     * 2. check account balance
     * 3. speaker: "total value of basket items is value which is (more|less) than you account balance of balance
     */
    public void execute()
    {
        // 1. compute the value of items in the basket
        int basketTotal = this.basket.getBasketTotalCost();

        // 2. check account balance
        int accountBalance = this.customer.getAccountBalance();

        // 3. speaker: "total value of basket items is <value> which is (more|less) than you account balance of <balance>
        System.out.println("CHECK ACCOUNT BALANCE FOR CUSTOMER " + this.customer.getCustomerId() +
                " WITH BASKET " + this.basket.getBasketId() + ": \n" +
                " Total value of basket items is " + basketTotal + " which is " +
                (basketTotal < accountBalance ? "less" : "more") +
                " than your account balance of " + accountBalance + "\n\n");
    }
}
