package com.cscie97.store.model;

/**
 * Product provides an abstraction for the types of products available for sale within the store.
 * Products are placed on shelves as inventory. Customers take products from shelves and place
 * them into their shopping baskets. When leaving the store and passing through the store, the
 * products in the consumer’s basket are used to compute the bill for the consumer.
 */
public class Product {
    /**
     * Alphanumeric Product Id
     */
    private String productId;

    /**
     * Name of the product
     */
    private String productName;

    /**
     * String Description of the product
     */
    private String productDescription;

    /**
     * Product size (weight and/or volume)
     */
    private String productSize;

    /**
     * Category, the type of product, e.g., produce, dairy, deli, frozen meals
     */
    private String productCategory;

    /**
     * Unit Price in blockchain currency of Units
     */
    private Integer productUnitPrice;

    /**
     * Temperature values: Frozen, Refrigerated, Ambient (default), Warm, Hot
     */
    private TemperatureEnum productTemperature;

    /**
     * Constructor for the Product object which tracks each product
     * @param productId String  Alphanumeric Product Id
     * @param productName String    Name of the product
     * @param productDescription String Description of the product
     * @param productSize String    Product size (weight and/or volume)
     * @param productCategory String    Category, the type of product, e.g., produce, dairy, deli, frozen meals
     * @param productUnitPrice String   Unit Price in blockchain currency of Units
     * @param productTemperature TemperatureEnum    Temperature values: Frozen, Refrigerated, Ambient (default), Warm, Hot
     */
    public Product(String productId, String productName, String productDescription, String productSize,
                   String productCategory, Integer productUnitPrice, TemperatureEnum productTemperature)
    {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productSize = productSize;
        this.productCategory = productCategory;
        this.productUnitPrice = productUnitPrice;
        this.productTemperature = productTemperature;
    }

    /**
     * Getter for the product ID
     * @return  String  The product ID
     */
    public String getProductId()
    {
        return this.productId;
    }

    /**
     * Getter for the product's unit price
     * @return  int The unit price
     */
    public int getProductUnitPrice() { return this.productUnitPrice; }

    /**
     * Printable JSON-like view of this object and its contents
     * @return String   Text containing all parts of the object information available
     */
    public String toString()
    {
        return "SHOWING PRODUCT \n{\n " +
                "\t Id = " + this.productId + "\n" +
                "\t Name = " + this.productName + "\n" +
                "\t Description = " + this.productDescription + "\n" +
                "\t Size = " + this.productSize + "\n" +
                "\t Category = " + this.productCategory + "\n" +
                "\t Unit Price = " + this.productUnitPrice + "\n" +
                "\t Temperature = " + this.productTemperature + "\n" +
                "}\n\n";
    }
}
