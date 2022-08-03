package com.cscie97.store.model;

/**
 * Abstract class with generic device functionality from which appliances and sensors classes are derived
 */
public abstract class Device {
    /**
     * Unique identifier for each device
     */
    String deviceId;

    /**
     * Alphanumeric name of the device
     */
    String deviceName;

    /**
     * Location of the device which includes Store Id and Aisle Id
     */
    String deviceLocation;

    /**
     * Device type
     */
    String deviceType;

    /**
     * Getter for device ID
     * @return  String  The device ID
     */
    public String getDeviceId() { return deviceId; }

    /**
     * Getter for device location
     * @return  String  The device location represented as store:aisle
     */
    public String getDeviceLocation() { return deviceLocation; }

    /**
     *
     * @param deviceId String   Unique identifier for each device
     * @param deviceName String Alphanumeric name of the device
     * @param deviceLocation String Location of the device which includes Store Id and Aisle Id
     */
    public Device(String deviceId, String deviceName, String deviceLocation)
    {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceLocation = deviceLocation;
    }
}
