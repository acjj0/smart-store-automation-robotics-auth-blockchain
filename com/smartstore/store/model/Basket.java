package com.cscie97.store.model;

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

/**
 * The Basket represents a shopping basket used by the customers to carry product items taken
 * from the shelves of the store. A basket is assigned to registered customers as they enter the
 * store.
 */
public class Basket {
    /**
     * Basket Identifier
     */
    private String basketId;

    /**
     * A list of Products that are contained in the basket with a product-specific count.
     */
    private HashMap<String, Integer> basketItems;

    /**
     * List of product Ids mapped to their associated Product objects
     */
    private HashMap<String, Product> productsInBasket;

    /**
     * Currently assigned customer to this basket, which could be null when no customer is assigned
     */
    private Customer assignedCustomer;

    /**
     * Constructor to set up an object of type Basket
     * @param basketId String   Alphaumeric identifier for the basket
     */
    public Basket(String basketId)
    {
        this.basketItems = new HashMap<String, Integer>();
        this.productsInBasket = new HashMap<String, Product>();
        this.basketId = basketId;
        this.assignedCustomer = null;
    }

    /**
     * Getter for basket ID
     * @return String   Returns alphanumeric identifier for the basket
     */
    public String getBasketId()
    {
        return this.basketId;
    }

    /**
     * Method called to add an item to the basket
     * @param productId String  Alphanumeric identifier for the product
     * @param product   Product The product object
     * @param itemCount Integer Count of how many of this product are added to the basket
     */
    public void addBasketItem(String productId, Product product, Integer itemCount)
    {
        // Check if the basket contains this type of product, and if so just increment the count.
        if(this.basketItems.containsKey(productId))
        {
            this.basketItems.put(productId, this.basketItems.get(productId) + itemCount);
        }
        // If the basket does not already contain this type of product, add the item and its count.
        else
        {
            this.basketItems.put(productId, itemCount);
            this.productsInBasket.put(productId, product);
        }
        System.out.println("PRODUCT " + productId + " COUNT " + itemCount + " ADDED TO BASKET " + this.basketId + "\n\n");
    }

    /**
     * Method to remove an item from teh basket
     * @param productId String  Alphanumeric identifier for the product
     * @param itemCount Integer Count of how many of this product are added to the basket
     */
    public void removeBasketItem(String productId, Integer itemCount)
    {
        // If the item is in the basket, decrease the quantity by the number removed
        if(this.basketItems.containsKey(productId))
        {
            this.basketItems.put(productId, this.basketItems.get(productId) - itemCount);
            System.out.println("PRODUCT " + productId + " COUNT " + itemCount + " REMOVED FROM BASKET " + this.basketId + "\n\n");
        }
        // If the item is not in the basket inform the calling function that such an item wasn't present.
        else
        {
            System.out.println("PRODUCT " + productId + " WAS NEVER IN CART \n\n");
        }
    }

    /**
     * Method to remove all items in this basket object
     */
    public void clearBasket()
    {
        this.basketItems.clear();
        System.out.println("BASKET " + this.basketId + " CLEARED \n\n");
    }

    /**
     * Setter to assign a customer to this basket object
     * @param customer Customer Object of type Customer assigned to this basket object
     */
    public void setAssignedCustomer(Customer customer)
    {
        this.assignedCustomer = customer;
    }

    /**
     * Getter to access the Customer object currently assigned to this basket object
     * @return Customer An object of type Customer or null if no customer is assigned to this basket object
     */
    public Customer getAssignedCustomer()
    {
        return this.assignedCustomer;
    }

    /**
     * Method to calculate total cost of all items in the basket
     * @return  int Total cost as an integer number
     */
    public int getBasketTotalCost()
    {
        int basketTotal = 0;
        Iterator hmIterator = basketItems.entrySet().iterator();

        // Iterate through the hashmap
        while (hmIterator.hasNext()) {
            HashMap.Entry hmElement = (HashMap.Entry)hmIterator.next();
            int productUnitPrice = this.productsInBasket.get(hmElement.getKey()).getProductUnitPrice();
            int numberOfUnits = Integer.parseInt(hmElement.getValue().toString());
            basketTotal += productUnitPrice * numberOfUnits;
        }
        return basketTotal;
    }

    /**
     * Printable JSON-like view of this object and its contents
     * @return String   Text containing all parts of the object information available
     */
    public String toString()
    {
        String returnValue = "BASKET " + this.basketId;

        // If no customer is assigned to this basket, deal with nulls by using this control flow
        if(this.assignedCustomer != null)
        {
            returnValue = returnValue + " ASSIGNED TO CUSTOMER " + this.assignedCustomer.getCustomerId();
        }
        else
        {
            returnValue = returnValue + " IS UNASSIGNED";
        }

        // Deal with the possibility of empty basket
        if (!this.basketItems.isEmpty())
        {
            returnValue = returnValue + " CONTAINS PRODUCTS \n{\n";

            // Iterate through all products in the basket and list them out
            Set keys = this.basketItems.keySet();
            Iterator i = keys.iterator();
            while (i.hasNext()) {
                String key = (String) i.next();
                returnValue = returnValue + "\tProduct: " + key + ", Count: " + this.basketItems.get(key) + "\n";
            }

            returnValue = returnValue + "}\n\n";
        }
        else
        {
            returnValue = returnValue + " IS EMPTY\n\n";
        }

        return returnValue;
    }
}
