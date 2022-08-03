package com.cscie97.store.model;

/**
 * A Shelf is a platform within an aisle within a store for inventory to be placed.
 */
public class Shelf {

    /**
     * The shelf identifier, e.g., shelf_1
     */
    private String shelfId;

    /**
     * The name of the shelf, e.g., milk
     */
    private String shelfName;

    /**
     * The height of the shelf (high, medium, low)
     */
    private ShelfLevelEnum shelfLevel;

    /**
     * The description of shelf contents, e.g., skim and whole milk, soy and almond milk
     */
    private String shelfDescription;

    /**
     * Values: frozen, refrigerated, ambient, warm, hot. default value: ambient
     */
    private TemperatureEnum shelfTemperature;

    /**
     * Constructor to create the Shelf object with all the parameters required to set one up.
     * @param shelfId String    The shelf identifier, e.g., shelf_1
     * @param shelfName String  The name of the shelf, e.g., milk
     * @param shelfLevel ShelfLevelEnum The height of the shelf (high, medium, low)
     * @param shelfDescription String   The description of shelf contents, e.g., skim and whole milk, soy and almond milk
     * @param shelfTemperature TemperatureEnum  Values: frozen, refrigerated, ambient, warm, hot. default value: ambient
     */
    public Shelf(String shelfId, String shelfName, ShelfLevelEnum shelfLevel, String shelfDescription,
                 TemperatureEnum shelfTemperature)
    {
        this.shelfId = shelfId;
        this.shelfName = shelfName;
        this.shelfLevel = shelfLevel;
        this.shelfDescription = shelfDescription;
        this.shelfTemperature = shelfTemperature;
    }

    /**
     * Printable JSON-like view of this object and its contents
     * @return String   Text containing all parts of the object information available
     */
    public String toString()
    {
        return "SHOWING SHELF \n{\n " +
                "\tNumber = " + this.shelfId + "\n" +
                "\tName = " + this.shelfName + "\n" +
                "\tLevel = " + this.shelfLevel + "\n" +
                "\tDescription = " + this.shelfDescription + "\n" +
                "\tTemperature = " + this.shelfTemperature + "\n" +
                "}\n\n";
    }
}
