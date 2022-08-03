package com.cscie97.store.model;

/**
 * An Aisle is a location within the store where shelves can be placed.
 * Number - the assigned aisle number, e.g., aisle_1
 * Name - the name of the aisle, e.g., dairy
 * Description - aisle description, e.g., milk, cheese, eggs, and other dairy products
 * Location, either floor or storeroom
 */
public class Aisle {
    /**
     * Assigned aisle number, e.g., aisle_1
     */
    private String aisleNumber;

    /**
     * Assigned aisle number, e.g., aisle_1
     */
    private String aisleName;

    /**
     * Aisle description, e.g., milk, cheese, eggs, and other dairy products
     */
    private String aisleDescription;

    /**
     * Either floor or storeroom
     */
    private AisleLocationEnum aisleLocation;

    /**
     * Constructor to create an Aisle object
     * @param aisleNumber String    Assigned aisle number, e.g., aisle_1
     * @param aisleName String  Assigned aisle number, e.g., aisle_1
     * @param aisleDescription String   Aisle description, e.g., milk, cheese, eggs, and other dairy products
     * @param aisleLocation AisleLocationEnum   Either floor or storeroom
     */
    public Aisle(String aisleNumber, String aisleName, String aisleDescription, AisleLocationEnum aisleLocation){
        this.aisleNumber = aisleNumber;
        this.aisleName = aisleName;
        this.aisleDescription = aisleDescription;
        this.aisleLocation = aisleLocation;
    }

    /**
     * Getter for Aisle Number
     * @return String   Contains alphanumeric aisle number
     */
    public String getNumber(){
        return this.aisleNumber;
    }

    /**
     * Getter for Aisle Name
     * @return String   Contains aisle name e.g. dairy
     */
    public String getName(){
        return this.aisleName;
    }

    /**
     * Getter for Aisle Description
     * @return String   Aisle description, e.g., milk, cheese, eggs, and other dairy products
     */
    public String getDescription(){
        return this.aisleDescription;
    }

    /**
     * Getter for Aisle Location
     * @return AisleLocationEnum   Either floor or storeroom
     */
    public AisleLocationEnum getLocation(){
        return this.aisleLocation;
    }

    /**
     * Printable JSON-like view of this object and its contents
     * @return String   Text containing all parts of the object information available
     */
    public String toString()
    {
        return "SHOWING AISLE \n{\n " +
                "\t Number = " + this.aisleNumber + "\n" +
                "\t Name = " + this.aisleName + "\n" +
                "\t Description = " + this.aisleDescription + "\n" +
                "\t Location = " + this.aisleLocation + "\n" +
                "}\n\n";
    }

}
