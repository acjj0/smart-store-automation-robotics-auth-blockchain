package com.cscie97.store.controller;

import com.cscie97.store.model.Customer;
import com.cscie97.store.model.Robot;
import com.cscie97.store.model.Store;
import com.cscie97.store.model.Turnstile;

import java.util.HashMap;

/**
 * The Concrete Command class for the Command Pattern. Turnstile checkout, when total weight of products of
 * basket exceeds 10 lbs, and object of this type will be created.
 */
public class AssistCustomerToCar extends Command{
    /**
     * The turnstile that requested customer assistance
     */
    Turnstile turnstile;

    /**
     * The store in which this situation happened
     */
    Store store;

    /**
     * The customer who is the subject of this assistance
     */
    Customer customer;

    /**
     * The location of the assistance request represented as store:aisle
     */
    String location;

    /**
     * Constructor for AssistCustomerToCar class, also the creation mechanism for the client in the Command Pattern
     * @param turnstile Turnstile   The turnstile that requested customer assistance
     * @param store Store   The store in which this situation happened
     * @param customer  Customer    The customer who is the subject of this assistance
     * @param location  String  The location of the assistance request represented as store:aisle
     */
    public AssistCustomerToCar(Turnstile turnstile, Store store, Customer customer, String location)
    {
        this.turnstile = turnstile;
        this.store = store;
        this.customer = customer;
        this.location = location;
    }

    /**
     * The execute method called by the Invoker in the Command Pattern. Request robot to assist customer to their car.
     */
    public void execute()
    {
        HashMap<String, Robot> robots = this.store.getRobots();
        Robot robot = this.store.findRobotInLocation(robots, this.location);
        String [] locationSplit = this.location.split(":");
        String aisle = locationSplit[1];
        System.out.println("ROBOT " + robot.getDeviceId() + " ASSIST CUSTOMER " + this.customer.getCustomerId() +
                " TO THEIR CAR \n\n" + robot + "\n");
    }
}
