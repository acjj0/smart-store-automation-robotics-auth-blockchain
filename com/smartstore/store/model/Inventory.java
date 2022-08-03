package com.cscie97.store.model;

/**
 * The Inventory is used to define the products available for sale within the store and where they
 * are located. Inventory location is specified with store aisle and shelf. Inventory maintains the
 * count of the product which must remain greater than or equal to and less than or equal to capacity.
 */
public class Inventory {
    /**
     * Alphanumeric Identifier of the inventory
     */
    private String inventoryId;

    /**
     * Location, Store:Aisle:Shelf
     */
    private String inventoryLocation;

    /**
     * Capacity, the maximum number of product items that can fit on the shelf
     */
    private Integer inventoryCapacity;

    /**
     * Count, which is the current count of product items on the shelf.
     */
    private Integer inventoryCount;

    /**
     * Product Id for which this is the inventory
     */
    private String inventoryProductId;

    /**
     * Constructor to create an Inventory object
     * @param inventoryId String    Alphanumeric Identifier of the inventory
     * @param inventoryLocation String  Location, Store:Aisle:Shelf
     * @param inventoryCapacity Integer Capacity, the maximum number of product items that can fit on the shelf
     * @param inventoryCount Integer    Count, which is the current count of product items on the shelf.
     * @param inventoryProductId String Product Id for which this is the inventory
     */
    public Inventory(String inventoryId, String inventoryLocation, Integer inventoryCapacity, Integer inventoryCount,
                     String inventoryProductId)
    {
        this.inventoryId = inventoryId;
        this.inventoryLocation = inventoryLocation;
        this.inventoryCapacity = inventoryCapacity;
        this.inventoryCount = inventoryCount;
        this.inventoryProductId = inventoryProductId;
    }

    /**
     * Getter for the Id for the store where this inventory is present. Extract this info from inventory location.
     * @return String   Alhanumeric Id for the store where this inventory is present
     */
    public String getStoreId()
    {
        String[] storeIdAisleNumberShelfId = this.inventoryLocation.split(":");
        return storeIdAisleNumberShelfId[0];
    }

    /**
     * Getter for inventory count
     * @return  int The inventory count
     */
    public int getInventoryCount()
    {
        return this.inventoryCount;
    }

    /**
     * Getter for inventory capacity
     * @return  int The inventory capacity
     */
    public int getInventoryCapacity()
    {
        return this.inventoryCapacity;
    }

    /**
     * Method to change inventory count to the new total count
     * @param newCount Integer  The new total count
     */
    public void updateCount(Integer newCount)
    {
        this.inventoryCount = newCount;
    }

    /**
     * Printable JSON-like view of this object and its contents
     * @return String   Text containing all parts of the object information available
     */
    public String toString()
    {
        return "SHOWING INVENTORY \n{\n " +
                "\t Number = " + this.inventoryId + "\n" +
                "\t Location = " + this.inventoryLocation + "\n" +
                "\t Capacity = " + this.inventoryCapacity + "\n" +
                "\t Count = " + this.inventoryCount + "\n" +
                "\t Product Id = " + this.inventoryProductId + "\n" +
                "}\n\n";
    }
}
