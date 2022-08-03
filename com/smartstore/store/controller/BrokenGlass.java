package com.cscie97.store.controller;

import com.cscie97.store.model.Camera;
import com.cscie97.store.model.Microphone;
import com.cscie97.store.model.Robot;
import com.cscie97.store.model.Store;

import java.util.HashMap;

/**
 * The Concrete Command class for the Command Pattern. Camera detects sound of breaking glass in aisle
 */
public class BrokenGlass extends Command {
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
     * Constructor for BrokenGlass class, also the creation mechanism for the client in the Command Pattern
     * @param camera    Camera  The camera that captured this situation
     * @param store Store   The store in which this product spill happened
     * @param location  String  The location of the spill represented by the string storeId:aisleNumber
     */
    BrokenGlass(Camera camera, Store store, String location)
    {
        this.camera = camera;
        this.store = store;
        this.location = location;
    }
    /**
     * The execute method called by the Invoker in the Command Pattern. Robot: "clean up broken glass in aisle"
     */
    public void execute()
    {
        HashMap<String, Robot> robots = this.store.getRobots();
        Robot robot = this.store.findRobotInLocation(robots, this.location);
        String [] locationSplit = this.location.split(":");
        String aisle = locationSplit[1];
        System.out.println("ROBOT " + robot.getDeviceId() + " CLEAN UP BROKEN GLASS IN " + aisle + "\n\n" +
                robot + "\n");
    }
}
