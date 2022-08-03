package com.cscie97.store.model;

/**
 * Camera monitors location of customers. Cameras are of type Sensor which is an abstract class derived from Device
 */
public class Camera extends Sensor{
    /**
     * Constructor to create object of type Camera with required parameters passed in
     * @param deviceId String   All Cameras have an ID, inherited from the parent abstract class Device
     * @param deviceName String   All Cameras have a Name, inherited from the parent abstract class Device
     * @param sensorTypesEnum SensorTypesEnum   All Cameras are of type CAMERA from SensorTypesEnum
     * @param deviceLocation String   All Cameras have a location, inherited from the parent abstract class Device
     */
    public Camera(String deviceId, String deviceName, SensorTypesEnum sensorTypesEnum, String deviceLocation)
    {
        super(deviceId, deviceName, sensorTypesEnum, deviceLocation);
    }

    /**
     * Printable JSON-like view of this object and its contents
     * @return String   Text containing all parts of the object information available
     */
    public String toString()
    {
        return "SHOWING CAMERA" + super.toString();
    }
}