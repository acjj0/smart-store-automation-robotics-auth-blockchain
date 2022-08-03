package com.cscie97.store.model;

import com.cscie97.ledger.Ledger;
import com.cscie97.store.authentication.*;
import com.cscie97.store.controller.Message;
import com.cscie97.store.controller.iObserver;
import com.cscie97.store.controller.iSubject;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

/**
 * The Store Model Service provides a top-level Service interface for provisioning stores. It also
 * supports controlling the store appliances (Turnstiles, Robots, Speakers). Any external entity
 * that wants to interact with the Store Model Service, must access it through the public API of the
 * Store Model Service.
 * The Store Model Service provides a service interface for managing the state of the stores.
 * The API supports commands for
 * ● Defining the store configuration
 * ● Showing the store configuration
 * ● Creating/Simulating sensor events
 * ● Sending command messages to appliances
 * ● Accessing sensor and appliance state
 * ● Monitoring and supporting customers
 * All API methods should include an auth_token parameter that will be used later to support
 * access control.
 *
 * Command API
 * The Store Model Service supports a Command Line Interface (CLI) for configuring stores. The
 * commands can be listed in a file to provide a configuration script. The CLI should use the
 * service interface to implement the commands
 */
public class StoreModelService implements iSubject {

    /**
     * Map to be able to locate any store, given its Store Id
     * key = store Id, value = Store object
     */
    private HashMap<String, Store> stores;

    /**
     * Map to be able to locate any inventory, given its Inventory Id
     * key = inventory Id, value = Inventory object
     */
    private HashMap<String, Inventory> globalInventories;

    /**
     * Map to be able to locate any Product, given its Product Id
     * key = product Id, value = Product object
     */
    private HashMap<String, Product> products;

    /**
     * Map to be able to locate any Customer, given the Customer Id
     * key = customer Id, value = Customer object
     */
    private HashMap<String, Customer> customers;

    /**
     * Map to be able to locate any Basket, given the Basket Id
     * key = basket Id, value = Basket object
     */
    private HashMap<String, Basket> baskets;

    /**
     * List of all observers attached to this subject as part of the Observer pattern
     */
    private List<iObserver> observers = new ArrayList<iObserver>();

    /**
     * Map to be able to locate any Device, given the Device Id
     * key = device Id, value = Device
     */
    private HashMap<String, Device> devices;

    /**
     * The blockchain ledger to use
     */
    private Ledger ledger;

    /**
     * DUMMY VALUE - Replace with actual Authentication Token implementation
     */
    private AccessToken auth_token;

    /**
     * Getter for the Store object
     * @param storeId   String  The Store Id
     * @return  Store   The Store object
     */
    public Store getStore(String storeId)
    {
        return this.stores.get(storeId);
    }

    /**
     * Getter for a Device object
     * @param deviceId  String  The Device ID
     * @return  Device  The Device object
     */
    public Device getDevice(String deviceId)
    {
        return this.devices.get(deviceId);
    }

    /**
     * Getter for a Customer object
     * @param customerId    String  The Customer ID
     * @return  Customer    The Customer object
     */
    public Customer getCustomer(String customerId)
    {
        return this.customers.get(customerId);
    }

    /**
     * Method to locate an unassigned basket, for example, to assign to a customer
     * @return  Basket  The Basket object that is currently unassigned
     */
    public Basket getUnassignedBasket()
    {
        int countOfBaskets = this.baskets.size();
        if(countOfBaskets > 0) {
            for (Basket basket : this.baskets.values()) {
                if (basket.getAssignedCustomer() == null) {
                    return basket;
                }
            }
        }
        String newBasketId = "b" + Integer.toString(countOfBaskets+1);
        this.defineBasket(newBasketId, this.auth_token);
        return this.baskets.get(newBasketId);
    }

    /**
     * Constructor for StoreModelService that initializes stores, global inventories, products, customers, baskets,
     * and devices that are tracked at the global level across all stores
     * @param ledger    Ledger  The blockchain ledger object to use
     * @param auth_token AccessToken Authentication Token passed into this API
     */
    public StoreModelService(Ledger ledger, AccessToken auth_token)
    {
        this.ledger = ledger;

        if(! this.isTokenValid(auth_token, "entitlement_admin_permission", "defineStore"))
        {
            return;
        }

        this.stores = new HashMap<String, Store>();
        this.globalInventories = new HashMap<String, Inventory>();
        this.products = new HashMap<String, Product>();
        this.customers = new HashMap<String, Customer>();
        this.baskets = new HashMap<String, Basket>();
        this.devices = new HashMap<String, Device>();

        System.out.println("STORE MODEL SERVICE CREATED\n");
    }

    /**
     * This method validates a token, verifies that the
     * @param accessToken   AccessToken The access token to validate
     * @param permissionId    String  The name of the permission to check whether this AccessToken enables
     * @param callingMethodName String  Name of the method that called isTokenValid, used for logging purposes
     * @return  Boolean True if the token is valid to access the permission, false if it is not valid
     */
    private Boolean isTokenValid(AccessToken accessToken, String permissionId, String callingMethodName)
    {
        if (accessToken == null) {
            System.out.println("AuthenticationException: Method (" + callingMethodName + ") has Null Access Token ");
            return false;
        }

        AuthenticationService authenticationService = AuthenticationService.getInstance();
        Permission permission = authenticationService.getPermissions().get(permissionId);

        try
        {
            authenticationService.checkAccess(accessToken, permission);
        }
        catch (InvalidAccessTokenException e)
        {
            System.out.println("AuthenticationException (" + callingMethodName + "): Invalid Access Token ");
            return false;
        }
        
        return true;
    }

    /**
     * Method to define each Store object
     * @param storeId String    Globally unique identifier (e.g. store100)
     * @param storeName String  Store Name (e.g. “Harvard Square Store”)
     * @param storeAddress String   Address (street, city, state) (e.g., “ 1400 Mass Avenue, Cambridge, MA 02138”)
     * @param auth_token AccessToken Authentication Token passed into this API
     */
    public void defineStore(String storeId, String storeName, String storeAddress, AccessToken auth_token)
    {
        if(! this.isTokenValid(auth_token, "create_resource", "defineStore")) { return; }

        this.stores.put(storeId, new Store(storeId, storeName, storeAddress, this.ledger));
        System.out.println("STORE DEFINED: " + storeId + "\n" + this.stores.get(storeId).toString());
    }

    /**
     * Method to display information associated with a given Store object's Id
     * @param storeId String    Globally unique identifier (e.g. store100)
     * @param auth_token AccessToken Authentication Token passed into this API
     * @throws AuthenticationException  Exception thrown when the user doesn't have permission to run this method
     */
    public void showStore(String storeId, AccessToken auth_token) throws AuthenticationException {
        if(! this.isTokenValid(auth_token, "read_resource", "showStore")) { return; }

        System.out.print(this.stores.get(storeId).toString());
    }

    /**
     * Method to define an Aisle object and associate with a Store object given the Store Id
     * @param storeId String    Globally unique identifier (e.g. store100)
     * @param aisleNumber String    Assigned aisle number, e.g., aisle_1
     * @param aisleName String  Assigned aisle number, e.g., aisle_1
     * @param aisleDescription String   Aisle description, e.g., milk, cheese, eggs, and other dairy products
     * @param aisleLocation AisleLocationEnum   Either floor or storeroom
     * @param auth_token AccessToken Authentication Token passed into this API
     * @throws AuthenticationException  Exception thrown when the user doesn't have permission to run this method
     */
    public void defineAisle(String storeId, String aisleNumber, String aisleName, String aisleDescription,
                            String aisleLocation, AccessToken auth_token) throws AuthenticationException {
        if(! this.isTokenValid(auth_token, "create_resource", "defineAisle")) { return; }

        AisleLocationEnum aisleLocationEnum = AisleLocationEnum.valueOf(aisleLocation.toUpperCase());
        this.stores.get(storeId).defineAisle(aisleNumber, aisleName, aisleDescription, aisleLocationEnum);
        System.out.println("AISLE DEFINED IN STORE " + storeId + ": " + aisleNumber + "\n");
    }

    /**
     * Method to display information associated with a given Store Id and Aisle Number
     * @param storeId String    Globally unique identifier (e.g. store100)
     * @param aisleNumber String    Assigned aisle number, e.g., aisle_1
     * @param auth_token AccessToken Authentication Token passed into this API
     * @throws AuthenticationException  Exception thrown when the user doesn't have permission to run this method
     */
    public void showAisle(String storeId, String aisleNumber, AccessToken auth_token) throws AuthenticationException {
        if(! this.isTokenValid(auth_token, "read_resource", "showAisle")) { return; }

        System.out.print(this.stores.get(storeId).getAisle(aisleNumber).toString());
    }

    /**
     * Method to define Shelf object and associate with a Store and Aisle, given the Store Id and Aisle Id
     * @param storeId String    Globally unique identifier (e.g. store100)
     * @param aisleNumber String    Assigned aisle number, e.g., aisle_1
     * @param shelfId String    The shelf identifier, e.g., shelf_1
     * @param shelfName String  The name of the shelf, e.g., milk
     * @param shelfLevel ShelfLevelEnum The height of the shelf (high, medium, low)
     * @param shelfDescription String   The description of shelf contents, e.g., skim and whole milk, soy and almond milk
     * @param shelfTemperature TemperatureEnum  Values: frozen, refrigerated, ambient, warm, hot. default value: ambient
     * @param auth_token AccessToken Authentication Token passed into this API
     * @throws AuthenticationException  Exception thrown when the user doesn't have permission to run this method
     */
    public void defineShelf(String storeId, String aisleNumber, String shelfId, String shelfName, String shelfLevel,
                            String shelfDescription, String shelfTemperature, AccessToken auth_token)
            throws AuthenticationException {
        if(! this.isTokenValid(auth_token, "create_resource", "defineShelf")) { return; }

        ShelfLevelEnum shelfLevelEnum = ShelfLevelEnum.valueOf(shelfLevel.toUpperCase());
        TemperatureEnum shelfTemperatureEnum = TemperatureEnum.valueOf(shelfTemperature.toUpperCase());
        this.stores.get(storeId).defineShelf(aisleNumber, shelfId, shelfName, shelfLevelEnum, shelfDescription, shelfTemperatureEnum);
        System.out.println("SHELF DEFINED IN AISLE " + aisleNumber + " IN STORE " + storeId + ": " + shelfId + "\n");
    }

    /**
     * Method to display information associated with a shelf, given the Store Id, Aisle Number, and Shelf Id
     * @param storeId String    Globally unique identifier (e.g. store100)
     * @param aisleNumber String    Assigned aisle number, e.g., aisle_1
     * @param shelfId String    The shelf identifier, e.g., shelf_1
     * @param auth_token AccessToken Authentication Token passed into this API
     * @throws AuthenticationException  Exception thrown when the user doesn't have permission to run this method
     */
    public void showShelf(String storeId, String aisleNumber, String shelfId, AccessToken auth_token)
            throws AuthenticationException {
        if(! this.isTokenValid(auth_token, "read_resource", "showShelf")) { return; }

        System.out.print(this.stores.get(storeId).getShelf(aisleNumber, shelfId).toString());
    }

    /**
     * Method to define Product object and update records to track this Product object
     * @param productId String  Alphanumeric Product Id
     * @param productName String    Name of the product
     * @param productDescription String Description of the product
     * @param productSize String    Product size (weight and/or volume)
     * @param productCategory String    Category, the type of product, e.g., produce, dairy, deli, frozen meals
     * @param productUnitPrice String   Unit Price in blockchain currency of Units
     * @param productTemperature TemperatureEnum    Temperature values: Frozen, Refrigerated, Ambient (default), Warm, Hot
     * @param auth_token AccessToken Authentication Token passed into this API
     */
    public void defineProduct(String productId, String productName, String productDescription, String productSize,
                              String productCategory, Integer productUnitPrice, String productTemperature,
                              AccessToken auth_token) {
        if(! this.isTokenValid(auth_token, "create_resource", "defineProduct")) { return; }

        TemperatureEnum productTemperatureEnum = TemperatureEnum.valueOf(productTemperature.toUpperCase());
        this.products.put(productId, new Product(productId, productName, productDescription, productSize,
                productCategory, productUnitPrice, productTemperatureEnum));
        System.out.println("PRODUCT DEFINED IN STORE MODEL SERVICE " + productId + "\n");
    }

    /**
     * Method to display information associated with a Product object, given the Product Id
     * @param productId String  Alphanumeric identifier for the product
     * @param auth_token AccessToken Authentication Token passed into this API
     */
    public void showProduct(String productId, AccessToken auth_token) {
        if(! this.isTokenValid(auth_token, "read_resource", "showProduct")) { return; }

        System.out.print(this.products.get(productId).toString());
    }

    /**
     * Getter for Product object given product ID
     * @param productId String  The product ID provided
     * @return  Product The Product object associated with the product Id provided
     */
    public Product getProductById(String productId)
    {
        if(! this.isTokenValid(auth_token, "read_resource", "getProductById")) { return null; }

        return this.products.get(productId);
    }

    /**
     * Method to define Inventory object. Store Id, Aisle Number and Shelf Id are passed here for future use
     * @param storeId String    Globally unique identifier (e.g. store100)
     * @param aisleNumber String    Assigned aisle number, e.g., aisle_1
     * @param shelfId String    The shelf identifier, e.g., shelf_1
     * @param inventoryId String    Alphanumeric Identifier of the inventory
     * @param inventoryLocation String  Location, Store:Aisle:Shelf
     * @param inventoryCapacity Integer Capacity, the maximum number of product items that can fit on the shelf
     * @param inventoryCount Integer    Count, which is the current count of product items on the shelf.
     * @param inventoryProductId String Product Id for which this is the inventory
     * @param auth_token AccessToken Authentication Token passed into this API
     */
    public void defineInventory(String storeId, String aisleNumber, String shelfId, String inventoryId,
                                String inventoryLocation, Integer inventoryCapacity, Integer inventoryCount,
                                String inventoryProductId, AccessToken auth_token) {
        if(! this.isTokenValid(auth_token, "create_resource", "defineInventory")) { return; }

        Inventory newInventory = this.stores.get(storeId).defineInventory(aisleNumber, shelfId, inventoryId,
                inventoryLocation, inventoryCapacity, inventoryCount, inventoryProductId);
        System.out.println("INVENTORY DEFINED IN SHELF " + shelfId + " AISLE " + aisleNumber + " IN STORE " + storeId +
                ": " + inventoryId + "\n\n");
        this.globalInventories.put(inventoryId, newInventory);
    }

    /**
     * Method to display information associated with an Inventory object, given the Inventory Id
     * @param inventoryId String    Alphanumeric Identifier of the inventory
     * @param auth_token AccessToken Authentication Token passed into this API
     */
    public void showInventory(String inventoryId, AccessToken auth_token) {
        if(! this.isTokenValid(auth_token, "read_resource", "showInventory")) { return; }

        System.out.print(this.globalInventories.get(inventoryId).toString());
    }

    /**
     * Method to update the count of an inventory, given the inventory Id
     * @param inventoryId String    Alphanumeric Identifier of the inventory
     * @param newCount Integer  The new total count
     * @param auth_token AccessToken Authentication Token passed into this API
     */
    public void updateInventoryCount(String inventoryId, Integer newCount, AccessToken auth_token) {
        if(! this.isTokenValid(auth_token, "update_resource", "updateInventoryCount")) { return; }

        this.stores.get(this.globalInventories.get(inventoryId).getStoreId()).getInventory(inventoryId).updateCount(newCount);
        System.out.println("INVENTORY " + inventoryId + " UPDATED WITH NEW COUNT " + newCount + "\n\n");
        this.showInventory(inventoryId, auth_token);
    }

    /**
     * Getter for Inventory object associated with inventory ID provided
     * @param inventoryId   String  Inventory ID provided
     * @return  Inventory   The Inventory object associated with the inventory ID provided
     */
    public Inventory getInventoryById(String inventoryId)
    {
        if(! this.isTokenValid(auth_token, "read_resource", "getInventoryById")) { return null; }

        return this.globalInventories.get(inventoryId);
    }

    /**
     * Method to define a customer, given all information to set up a new customer, and tracks it globally
     * @param customerId String Customer Identifier
     * @param customerFirstName String Customer First Name
     * @param customerLastName String   Customer Last Name
     * @param customerType CustomerTypeEnum Customer Type (Registered or Guest)
     * @param customerIsAdult String Customer is Adult or Child
     * @param customerEmailAddress String   Customer's Email Address
     * @param customerBlockchainAddress String  Account Blockchain Address used for billing
     * @param customerLocation String   Current Location containing Store ID and Aisle ID separated by colon
     * @param customerTimeLastSeen String   Time last seen (updated when location updates)
     * @param auth_token AccessToken Authentication Token passed into this API
     */
    public void defineCustomer(String customerId, String customerFirstName, String customerLastName,
                               String customerType, String customerIsAdult, String customerEmailAddress,
                               String customerBlockchainAddress, String customerLocation, String customerTimeLastSeen,
                               AccessToken auth_token) {
        if(! this.isTokenValid(auth_token, "create_resource", "defineCustomer")) { return; }

        CustomerTypeEnum customerTypeEnum = CustomerTypeEnum.valueOf(customerType.toUpperCase());

        this.customers.put(customerId, new Customer(customerId, customerFirstName, customerLastName, customerTypeEnum,
                customerIsAdult, customerEmailAddress, customerBlockchainAddress, this.ledger, customerLocation,
                customerTimeLastSeen));
        System.out.println("CUSTOMER DEFINED IN STORE MODEL SERVICE " + customerId + "\n\n");
    }

    /**
     * Method to display information associated with a Customer object, given the Customer Id
     * @param customerId String Customer Identifier
     * @param auth_token AccessToken Authentication Token passed into this API
     */
    public void showCustomer(String customerId, AccessToken auth_token) {
        if(! this.isTokenValid(auth_token, "read_resource", "showCustomer")) { return; }

        System.out.println(this.customers.get(customerId).toString());
    }

    /**
     * Method to update location of the Customer object given the Customer Id and location information
     * @param customerId String Customer Identifier
     * @param customerLocation String   The Store Id and Aisle Id separated by a colon where the customer was last seen
     * @param auth_token AccessToken Authentication Token passed into this API
     */
    public void updateCustomerLocation(String customerId, String customerLocation, AccessToken auth_token) {
        if(! this.isTokenValid(auth_token, "update_resource", "updateCustomerLocation")) { return; }

        this.customers.get(customerId).updateLocation(customerLocation);
        System.out.println("CUSTOMER " + customerId + " LOCATION UPDATED TO " + customerLocation + "\n\n");
    }

    /**
     * Method to define a Basket object and track it globally
     * @param basketId String   Alphaumeric identifier for the basket
     * @param auth_token AccessToken Authentication Token passed into this API
     */
    public void defineBasket(String basketId, AccessToken auth_token) {
        if(! this.isTokenValid(auth_token, "create_resource", "defineBasket")) { return; }

        this.baskets.put(basketId, new Basket(basketId));
        System.out.println("BASKET DEFINED " + basketId + "\n\n");
    }

    /**
     * Method to display information associated with a Basket object given the Basket's Id
     * @param basketId String   Alphaumeric identifier for the basket
     * @param auth_token AccessToken Authentication Token passed into this API
     */
    public void showBasket(String basketId, AccessToken auth_token) {
        if(! this.isTokenValid(auth_token, "read_resource", "showBasket")) { return; }

        System.out.println(this.baskets.get(basketId).toString());
    }

    /**
     * Method to assign a Basket object to a Customer object
     * @param basketId String   Alphaumeric identifier for the basket
     * @param customerId String Customer Identifier
     * @param auth_token AccessToken Authentication Token passed into this API
     */
    public void assignBasket(String basketId, String customerId, AccessToken auth_token) {
        if(! this.isTokenValid(auth_token, "create_resource", "assignBasket")) { return; }

        this.baskets.get(basketId).setAssignedCustomer(this.customers.get(customerId));
        this.customers.get(customerId).setAssignedBasket(this.baskets.get(basketId));
        System.out.println("BASKET " + basketId + " ASSIGNED TO " + customerId + "\n\n");
    }

    /**
     * Getter to retrieve the Basket object assigned to the Customer
     * @param customerId String The Customer Id for whom we need the basket
     * @param auth_token AccessToken Authentication Token passed into this API
     */
    public void getCustomerBasket(String customerId, AccessToken auth_token) {
        if(! this.isTokenValid(auth_token, "read_resource", "getCustomerBasket")) { return; }

        System.out.println("ASSIGNED BASKET FOR CUSTOMER " + customerId + " IS " +
                this.customers.get(customerId).getAssignedBasket().getBasketId() + "\n\n");
    }

    /**
     * Method to add an item to a Basket object given the Product Id and item count
     * @param basketId String   Alphaumeric identifier for the basket
     * @param productId String  Alphanumeric identifier for the product
     * @param itemCount Integer Count of how many of this product are added to the basket
     * @param auth_token AccessToken Authentication Token passed into this API
     */
    public void addBasketItem(String basketId, String productId, Integer itemCount, AccessToken auth_token) {
        if(! this.isTokenValid(auth_token, "update_resource", "addBasketItem")) { return; }

        this.baskets.get(basketId).addBasketItem(productId, this.products.get(productId), itemCount);
    }

    /**
     * Method to remove a given quantity of a Product item from a given Basket
     * @param basketId String   Alphaumeric identifier for the basket
     * @param productId String  Alphanumeric identifier for the product
     * @param itemCount Integer Count of how many of this Product are removed from the Basket
     * @param auth_token AccessToken Authentication Token passed into this API
     */
    public void removeBasketItem(String basketId, String productId, Integer itemCount, AccessToken auth_token) {
        if(! this.isTokenValid(auth_token, "update_resource", "removeBasketItem")) { return; }

        this.baskets.get(basketId).removeBasketItem(productId, itemCount);
    }

    /**
     * Method to remove all units of all Products from the given Basket
     * @param basketId String   Alphaumeric identifier for the basket
     * @param auth_token AccessToken Authentication Token passed into this API
     */
    public void clearBasket(String basketId, AccessToken auth_token)
    {
        if(! this.isTokenValid(auth_token, "update_resource", "clearBasket")) { return; }

        this.baskets.get(basketId).clearBasket();
    }

    /**
     * Method to define a Device object of any type, including sensors and appliances
     * @param deviceId String   Unique identifier for each device
     * @param deviceName String Alphanumeric name of the device
     * @param deviceType String Device type, such as camera, microphone, speaker, robot or turnstile
     * @param deviceLocation String Location of the device which includes Store Id and Aisle Id
     * @param auth_token AccessToken Authentication Token passed into this API
     */
    public void defineDevice(String deviceId, String deviceName, String deviceType, String deviceLocation,
                             AccessToken auth_token)
    {
        if(! this.isTokenValid(auth_token, "create_resource", "defineDevice")) { return; }

        String [] locationSplit = deviceLocation.split(":");
        String storeId = locationSplit[0];
        Device device;

        if(EnumSet.allOf(SensorTypesEnum.class).toString().contains(deviceType.toUpperCase()))
        {
            SensorTypesEnum sensorTypesEnum = SensorTypesEnum.valueOf(deviceType.toUpperCase());
            device = this.stores.get(storeId).defineSensor(deviceId, deviceName, sensorTypesEnum, deviceLocation);
            this.devices.put(deviceId, device);
        }
        else if(EnumSet.allOf(ApplianceTypesEnum.class).toString().contains(deviceType.toUpperCase()))
        {
            ApplianceTypesEnum applianceTypesEnum = ApplianceTypesEnum.valueOf(deviceType.toUpperCase());
            device = this.stores.get(storeId).defineAppliance(deviceId, deviceName, applianceTypesEnum, deviceLocation);
            this.devices.put(deviceId, device);
        }
    }

    /**
     * Method to display information associated with the Device object whose Id is provided
     * @param deviceId String   Unique identifier for each device
     * @param auth_token AccessToken Authentication Token passed into this API
     */
    public void showDevice(String deviceId, AccessToken auth_token)
    {
        if(! this.isTokenValid(auth_token, "read_resource", "showDevice")) { return; }

        System.out.println(this.devices.get(deviceId));
    }

    /**
     * Method to create a Command for an Appliance
     * @param originatingDeviceId String    ID of device associated with this command
     * @param args List of Strings  Contains all arguments passed in to create a Command
     * @param auth_token AccessToken Authentication Token passed into this API
     */
    public void createCommand(String originatingDeviceId, List<String> args, AccessToken auth_token)
    {
        if(! this.isTokenValid(auth_token, "create_resource", "createCommand")) { return; }

        Command command = new Command(originatingDeviceId, args);
        System.out.println("COMMAND FROM " + originatingDeviceId + command.toString());
    }

    /**
     * Method to create an event. This method may be called when a sensor change is observed or due to some conditions
     * such as when weight of items exceeds a predetermined number and a robot must be asked to assist the customer.
     * This method notifies all Observers in the Observer pattern.
     * @param message   Message The message associated with the event that may be used in the Command pattern
     */
    /**
     * Method to create an event. This method may be called when a sensor change is observed or due to some conditions
     * such as when weight of items exceeds a predetermined number and a robot must be asked to assist the customer.
     * This method notifies all Observers in the Observer pattern.
     * @param message   Message The message associated with the event that may be used in the Command pattern
     * @param auth_token    AccessToken Token to authentication access to perform the createEvent API call.
     */
    public void createEvent(Message message, AccessToken auth_token)  {
        if(! this.isTokenValid(auth_token, "create_resource", "createEvent")) { return; }

        notifyObservers(message);
    }

    /**
     * Overriding method to attach Observers in the Observer pattern
     * @param observer  iObserver   Another observer to attach
     */
    @Override
    public void attachObserver(iObserver observer)  {
        this.observers.add(observer);
        System.out.println(String.format("%s is registered as Observer of StoreModelService", observer.toString()));
    }

    /**
     * Overriding method to detach Observers in the Observer pattern
     * @param observer  iObserver   An existing observer to detach
     */
    @Override
    public void detachObserver(iObserver observer)  {
        this.observers.remove(observer);
        System.out.println(String.format("%s is removed as Observer of StoreModelService", observer.toString()));
    }

    /**
     * Overriding method to notify all Observers in the Observer pattern
     * @param message   Message The immutable message sent by the subject to all observers
     */
    public void notifyObservers(Message message) {
        for (iObserver observer : this.observers)
        {
            try {
                observer.update(message);
            }
            catch (Exception e) {
                System.out.println("notifyObservers() failed: " + e.toString());
            }
        }
    }
}
