package com.cscie97.store.controller;

import com.cscie97.store.model.*;

import java.util.HashMap;
import java.util.Iterator;


/**
 * The Concrete Command class for the Command Pattern. Class for emergency in aisle where emergency is one of:
 * fire, flood, earthquake, armed intruder
 */
public class Emergency extends Command{
    /**
     * The camera that captured the event
     */
    private Camera camera;

    /**
     * The store where the event happened
     */
    private Store store;

    /**
     * The type of emergency captured in an enum
     */
    private EmergencyTypeEnum emergencyType;

    /**
     * The location of the emergency represented as store:aisle
     */
    private String location;

    /**
     * Constructor for Emergency class, also the creation mechanism for the client in the Command Pattern
     * @param camera    Camera  The camera that captured the event
     * @param store Store   The store where the event happened
     * @param emergencyType EmergencyTypeEnum   The type of emergency captured in an enum
     * @param location  String  The location of the emergency represented as store:aisle
     */
    public Emergency(Camera camera, Store store, EmergencyTypeEnum emergencyType, String location)
    {
        this.camera = camera;
        this.store = store;
        this.emergencyType = emergencyType;
        this.location = location;
    }

    /**
     * The execute method called by the Invoker in the Command Pattern
     * 1. Open all turnstiles
     * 2. announce: "There is an emergency in aisle, please leave store immediately"
     * 3. Robot 1: "address emergency in aisle"
     * 4. remaining robots: "Assist customers leaving the store"
     */
    public void execute()
    {
        // 1. Open all turnstiles
        System.out.println("OPEN ALL TURNSTILES");
        Iterator hmIterator = this.store.getTurnstiles().entrySet().iterator();
        // Iterate through the hashmap
        while (hmIterator.hasNext()) {
            HashMap.Entry hmElement = (HashMap.Entry) hmIterator.next();
            Turnstile turnstile = (Turnstile) hmElement.getValue();
            System.out.println(turnstile.getDeviceId());
        }
        System.out.println(" ");

        // 2. announce: "There is a <emergency> in <aisle>, please leave <store> immediately"
        String [] locationSplit = this.location.split(":");
        String aisle = locationSplit[1];
        System.out.println("THERE IS A " + this.emergencyType + " IN AISLE " + aisle + " PLEASE LEAVE " +
                this.store.getStoreName() + " IMMEDIATELY!");
        System.out.println(" ");

        // 3. Robot 1: "address <emergency> in <aisle>"
        HashMap<String, Robot> robots = this.store.getRobots();
        Robot robot = this.store.findRobotInLocation(robots, this.location);
        System.out.println("ROBOT " + robot.getDeviceId() + " ADDRESS " + emergencyType + " IN " + aisle);
        System.out.println(" ");

        // 4. remaining robots: "Assist customers leaving the <store>"
        System.out.println("OTHER ROBOTS ASSIST CUSTOMERS LEAVING THE " + this.store.getStoreName());
        Iterator hmIterator1 = robots.entrySet().iterator();
        // Iterate through the hashmap
        while (hmIterator1.hasNext()) {
            HashMap.Entry hmElement = (HashMap.Entry) hmIterator1.next();
            Robot otherrobot = (Robot)hmElement.getValue();
            if(robot.getDeviceId() != otherrobot.getDeviceId()) {
                System.out.println(otherrobot.getDeviceId());
            }
        }
        System.out.println("\n\n");
    }
}
