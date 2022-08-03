package com.cscie97.store.model;

import java.util.List;

/**
 * Abstract class that helps create objects reflecting each event from sensors or appliances.
 */
public abstract class Event {
    /**
     * ID of device associated with this event
     */
    String originatingDeviceId;

    /**
     * Contains all arguments passed in to create an Event
     */
    List<String> args;

    /**
     * Constructor for Event class
     * @param originatingDeviceId String    ID of device associated with this event
     * @param args List of Strings  Contains all arguments passed in to create an Event
     */
    public Event(String originatingDeviceId, List<String> args)
    {
        this.originatingDeviceId = originatingDeviceId;
        this.args = args;
    }

    /**
     * Printable JSON-like view of this object and its contents
     * @return String   Text containing all parts of the object information available
     */
    public String toString()
    {
        return "\n{\n" +
                "\tOriginating Device = " + this.originatingDeviceId + "\n" +
                "\tEvent String = " + this.args + "\n" +
                "}\n\n";
    }
}
