package com.cscie97.store.model;

/**
 * Robots listen to customers, talk to customers, perform tasks
 */
public class Robot extends Appliance{
    /**
     * Constructor for Robot objects
     * @param deviceId String   All Robots have an ID, inherited from the parent abstract class Device
     * @param deviceName String   All Robots have a Name, inherited from the parent abstract class Device
     * @param applianceTypesEnum ApplianceTypesEnum   All Robots have type ROBOT from ApplianceTypesEnum
     * @param deviceLocation String   All Robots have a location, inherited from the parent abstract class Device
     */
    public Robot(String deviceId, String deviceName, ApplianceTypesEnum applianceTypesEnum, String deviceLocation)
    {
        super(deviceId, deviceName, applianceTypesEnum, deviceLocation);
    }

    /**
     * Printable JSON-like view of this object and its contents
     * @return String   Text containing all parts of the object information available
     */
    public String toString()
    {
        return "SHOWING ROBOT" + super.toString();
    }
}
