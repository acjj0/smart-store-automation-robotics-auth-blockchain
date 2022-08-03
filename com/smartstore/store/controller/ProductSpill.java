package com.cscie97.store.controller;

import com.cscie97.store.model.*;

import java.util.HashMap;

/**
 * The Concrete Command class for the Command Pattern. This class captures the situation when a camera sees
 * product on floor in store:aisle
 */
public class ProductSpill extends Command {
    /**
     * The camera that captured this situation
     */
    private Camera camera;

    /**
     * The store in which this product spill happened
     */
    private Store store;

    /**
     * The location of the spill represented by the string storeId:aisleNumber
     */
    private String location;

    /**
     * The product that was spilled
     */
    private Product product;

    /**
     * Constructor for ProductSpill class, also the creation mechanism for the client in the Command Pattern
     * @param camera    Camera  The camera that captured this situation
     * @param store Store   The store in which this product spill happened
     * @param location  String  The location of the spill represented by the string storeId:aisleNumber
     * @param product   Product The product that was spilled
     */
    public ProductSpill(Camera camera, Store store, String location, Product product)
    {
        this.camera = camera;
        this.store = store;
        this.location = location;
        this.product = product;
    }

    /**
     * The execute method called by the Invoker in the Command Pattern. Robot: "clean up product in aisle"
     */
    public void execute(){
        HashMap<String, Robot> robots = this.store.getRobots();
        Robot robot = this.store.findRobotInLocation(robots, this.location);
        String [] locationSplit = this.location.split(":");
        String aisle = locationSplit[1];
        System.out.println("ROBOT " + robot.getDeviceId() + " CLEAN UP " + this.product.getProductId() +
                " in location " + aisle + "\n\n" + robot + "\n");
    }
}
