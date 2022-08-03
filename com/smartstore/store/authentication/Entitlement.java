package com.cscie97.store.authentication;

/**
 * Entitlements include Roles and Permissions. This is an abstract class that gathers together some of the traits of
 * Roles and Permissions to maximize reuse.
 */
public abstract class Entitlement implements iVisitableElement{
    /**
     * The entitlement ID
     */
    private String id;

    /**
     * Name of the specific entitlement
     */
    private String name;

    /**
     * Description of the entitlement
     */
    private String description;

    /**
     * Constructor for the entitlement
     * @param id    String  The entitlement ID
     * @param name  String  Name of the specific entitlement
     * @param description   String  Description of the entitlement
     */
    public Entitlement(String id, String name, String description)
    {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Getter for the entitlement's ID
     * @return  String  The entitlement ID
     */
    public String getId() {
        return this.id;
    }

    /**
     * Getter for the entitlement's name
     * @return String  Name of the specific entitlement
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for the entitlement's description
     * @return   String  Description of the entitlement
     */
    public String getDescription() {
        return this.description;
    }
}
