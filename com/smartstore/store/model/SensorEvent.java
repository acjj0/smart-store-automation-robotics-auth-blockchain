package com.cscie97.store.model;

import java.util.List;

/**
 * Class that helps create objects reflecting each sensor event such as item_added_to_basket, customer_asked_question.
 * Derived from abstract class Event
 */
public class SensorEvent extends Event
{
    /**
     * Constructor for SensorEvent class
     * @param originatingDeviceId String    ID of device associated with this event
     * @param args List of Strings  Contains all arguments passed in to create an Event
     */
    public SensorEvent(String originatingDeviceId, List<String> args)
    {
        super(originatingDeviceId, args);
    }

    /**
     * Printable JSON-like view of this object and its contents
     * @return String   Text containing all parts of the object information available
     */
    public String toString()
    {
        return super.toString();
    }
}
