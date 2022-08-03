package com.cscie97.store.model;

import java.util.List;

/**
 * Class that helps create objects reflecting each appliance event such as SPILLED_MILK, CUSTOMER_QUESTION, PRICE_CHECK.
 * Derived from abstract class Event.
 */
public class ApplianceEvent extends Event
{
    /**
     * Constructor for ApplianceEvent class
     * @param originatingDeviceId String    ID of device associated with this event
     * @param args List of Strings  Contains all arguments passed in to create an Event
     */
    public ApplianceEvent(String originatingDeviceId, List<String> args)
    {
        super(originatingDeviceId, args);
    }

    /**
     * Printable JSON-like view of all objects of type Appliance and its contents. Uses its parent class' method
     * @return String   Printable view of contents.
     */
    public String toString()
    {
        return super.toString();
    }
}
