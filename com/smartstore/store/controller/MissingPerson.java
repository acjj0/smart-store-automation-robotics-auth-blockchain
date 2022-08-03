package com.cscie97.store.controller;

import com.cscie97.store.model.Customer;
import com.cscie97.store.model.Microphone;
import com.cscie97.store.model.Store;

/**
 * The Concrete Command class for the Command Pattern. Microphone hears "can you help me find customer name"
 */
public class MissingPerson extends Command {
    /**
     * The microphone into which the request was made
     */
    Microphone microphone;

    /**
     * The store in which this happened
     */
    Store store;

    /**
     * The customer who needs to be located
     */
    Customer customer;

    /**
     * Constructor to create MissingPerson request objects
     * @param microphone    Microphone  The microphone into which the request was made
     * @param store Store   The store in which this happened
     * @param customer  Customer    The customer who needs to be located
     */
    public MissingPerson(Microphone microphone, Store store, Customer customer)
    {
        this.microphone = microphone;
        this.store = store;
        this.customer = customer;
    }

    /**
     * The execute method called by the Invoker in the Command Pattern.
     * locate customer name speaker: name is in aisle aisle
     */
    public void execute()
    {
        String [] locationSplit = this.customer.getCustomerLocation().split(":");
        String aisle = locationSplit[1];
        System.out.println("CUSTOMER " + this.customer.getCustomerFirstName() + " WAS LAST SEEN IN AISLE " + aisle +
                " AT TIME " + this.customer.getCustomerTimeLastSeen() + "\n\n");
    }
}
