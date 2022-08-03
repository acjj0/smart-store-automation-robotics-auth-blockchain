package com.cscie97.store.model;

/**
 * Speakers talk to customers
 */
public class Speaker extends Appliance{
    /**
     * Constructor for Speaker objects
     * @param deviceId String   All Speakers have an ID, inherited from the parent abstract class Device
     * @param deviceName String   All Speakers have a Name, inherited from the parent abstract class Device
     * @param applianceTypesEnum ApplianceTypesEnum   All Speakers have type SPEAKER from ApplianceTypesEnum
     * @param deviceLocation String   All Speakers have a location, inherited from the parent abstract class Device
     */
    public Speaker(String deviceId, String deviceName, ApplianceTypesEnum applianceTypesEnum, String deviceLocation)
    {
        super(deviceId, deviceName, applianceTypesEnum, deviceLocation);
    }

    /**
     * Printable JSON-like view of this object and its contents
     * @return String   Text containing all parts of the object information available
     */
    public String toString()
    {
        return "SHOWING SPEAKER" + super.toString();

    }
}
