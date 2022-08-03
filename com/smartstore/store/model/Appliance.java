package com.cscie97.store.model;

/**
 * Abstract class with generic appliance functionality from which appliances such as turnstile and robot can be created.
 * Derived from the abstract class Device
 */
public abstract class Appliance extends Device{
    /**
     * Uses known appliance types information
     */
    ApplianceTypesEnum applianceType;

    /**
     * Constructor which sets up all common properties of an Appliance
     * @param deviceId String   All Appliances have an ID, inherited from the parent abstract class Device
     * @param deviceName String   All Appliances have a Name, inherited from the parent abstract class Device
     * @param applianceTypesEnum ApplianceTypesEnum   All Appliances have a type, e.g. turnstile, robot, etc
     * @param deviceLocation String   All Appliances have a location, inherited from the parent abstract class Device
     */
    public Appliance(String deviceId, String deviceName, ApplianceTypesEnum applianceTypesEnum, String deviceLocation)
    {
        super(deviceId, deviceName, deviceLocation);
        this.applianceType = applianceTypesEnum;
        this.deviceType = applianceTypesEnum.toString();
    }

    /**
     * Printable JSON-like view of this object and its contents
     * @return String   Text containing all parts of the object information available
     */
    public String toString()
    {
        return "\n{\n" +
                "\tId = " + this.deviceId + "\n" +
                "\tName = " + this.deviceName + "\n" +
                "\tType = " + this.applianceType + "\n" +
                "\tLocation = " + this.deviceLocation + "\n" +
                "}\n\n";
    }
}
