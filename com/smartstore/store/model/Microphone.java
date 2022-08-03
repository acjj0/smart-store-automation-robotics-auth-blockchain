package com.cscie97.store.model;

/**
 * Microphone is a listening device for receiving voice commands from customers
 */
public class Microphone extends Sensor{
    /**
     * Constructor to create object of type Microphone with required parameters passed in
     * @param deviceId String   All Microphones have an ID, inherited from the parent abstract class Device
     * @param deviceName String   All Microphones have a Name, inherited from the parent abstract class Device
     * @param sensorTypesEnum SensorTypesEnum   All Microphones are of type MICROPHONE from SensorTypesEnum
     * @param deviceLocation String   All Microphones have a location, inherited from the parent abstract class Device
     */
    public Microphone(String deviceId, String deviceName, SensorTypesEnum sensorTypesEnum, String deviceLocation)
    {
        super(deviceId, deviceName, sensorTypesEnum, deviceLocation);
    }

    /**
     * Printable JSON-like view of this object and its contents
     * @return String   Text containing all parts of the object information available
     */
    public String toString()
    {
        return "SHOWING MICROPHONE" + super.toString();
    }
}
