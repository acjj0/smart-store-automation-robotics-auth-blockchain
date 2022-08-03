package com.cscie97.store.model;

/**
 * Turnstiles listen to customers, talk to customers, open and close turnstile
 */
public class Turnstile extends Appliance{
    /**
     * Constructor for Turnstile objects
     * @param deviceId String   All Turnstiles have an ID, inherited from the parent abstract class Device
     * @param deviceName String   All Turnstiles have a Name, inherited from the parent abstract class Device
     * @param applianceTypesEnum ApplianceTypesEnum   All Turnstiles have type TURNSTILE from ApplianceTypesEnum
     * @param deviceLocation String   All Turnstiles have a location, inherited from the parent abstract class Device
     */
    public Turnstile(String deviceId, String deviceName, ApplianceTypesEnum applianceTypesEnum, String deviceLocation)
    {
        super(deviceId, deviceName, applianceTypesEnum, deviceLocation);
    }

    /**
     * Printable JSON-like view of this object and its contents
     * @return String   Text containing all parts of the object information available
     */
    public String toString()
    {
        return "SHOWING TURNSTILE" + super.toString();
    }
}
