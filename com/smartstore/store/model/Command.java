package com.cscie97.store.model;

import java.util.List;

/**
 * Commands objects reflect each command associated with an Appliance
 */
public class Command {
    /**
     * Identifier of the Appliance with which this command is associated
     */
    String originatingDeviceId;

    /**
     * Holds the opaque string associated with the command
     */
    List<String> args;

    /**
     * Constructor for the Command class
     * @param originatingDeviceId String    Identifier of the Appliance with which this command is associated
     * @param args List of Strings  Holds the opaque string associated with the command
     */
    public Command(String originatingDeviceId, List<String> args)
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
