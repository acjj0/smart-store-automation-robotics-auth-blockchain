package com.cscie97.store.controller;

import com.cscie97.store.model.Camera;
import com.cscie97.store.model.Customer;
import com.cscie97.store.model.Store;

/**
 * The Concrete Command class for the Command Pattern. Camera sees Customer enter aisle. Reflects sighting of a
 * customer by a camera to update their location and time last seen
 */
public class CustomerSeen extends Command{
    /**
     * The camera that generated this event
     */
    Camera camera;

    /**
     * The store in which this event happened
     */
    Store store;

    /**
     * The customer who is the subject of this sighting
     */
    Customer customer;

    /**
     * The location where the customer was seen represented as store:aisle
     */
    String location;

    /**
     * Constructor for CustomerSeen objects
     * @param camera Camera   The camera that generated this event
     * @param store   Store   The store in which this event happened
     * @param customer  Customer    The customer who is the subject of this sighting
     * @param location  String  The location where the customer was seen represented as store:aisle
     */
    CustomerSeen(Camera camera, Store store, Customer customer, String location)
    {
        this.camera = camera;
        this.store = store;
        this.customer = customer;
        this.location = location;
    }

    /**
     * The execute method called by the Invoker in the Command Pattern. Update customer location aisle
     */
    public void execute()
    {
        this.customer.updateLocation(location);
        System.out.println("UPDATED CUSTOMER " + this.customer.getCustomerId() + " LOCATION TO " +
                this.customer.getCustomerLocation() + " AT TIME " + this.customer.getCustomerTimeLastSeen() + "\n\n");
    }
}
