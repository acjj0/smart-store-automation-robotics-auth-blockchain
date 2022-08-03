package com.cscie97.store.model;

/**
 * Abstract class with generic sensor functionality from which sensors such as camera and microphone can be created.
 * Derived from the abstract class Device
 */
public abstract class Sensor extends Device {
    /**
     * Uses known sensor types information
     */
    SensorTypesEnum sensorType;

    /**
     * Constructor which sets up all common properties of a Sensor
     * @param deviceId String   All Sensors have an ID, inherited from the parent abstract class Device
     * @param deviceName String   All Sensors have a Name, inherited from the parent abstract class Device
     * @param sensorTypesEnum SensorTypesEnum   All Sensors have a type, e.g. camera, microphone, etc
     * @param deviceLocation String   All Sensors have a location, inherited from the parent abstract class Device
     */
    public Sensor(String deviceId, String deviceName, SensorTypesEnum sensorTypesEnum, String deviceLocation)
    {
        super(deviceId, deviceName, deviceLocation);
        this.sensorType = sensorTypesEnum;
        this.deviceType = sensorTypesEnum.toString();
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
                "\tType = " + this.sensorType + "\n" +
                "\tLocation = " + this.deviceLocation + "\n" +
                "}\n\n";
    }
}
