package com.cscie97.store.model;

import com.cscie97.ledger.Ledger;
import com.cscie97.ledger.LedgerException;

import java.time.Clock;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The Store is used to model a store instance. Note that the Store 24X7 is a cloud-based service
 * and must be able to manage multiple Stores.
 */
public class Store {
    /**
     * Globally unique identifier (e.g. store100)
     */
    private String storeId;

    /**
     * Store Name (e.g. “Harvard Square Store”)
     */
    private String storeName;

    /**
     * Address (street, city, state) (e.g., “ 1400 Mass Avenue, Cambridge, MA 02138”)
     */
    private String storeAddress;

    /**
     * Customers associated with this store
     */
    private HashMap<String, Customer> customers;

    /**
     * Aisles associated with this store
     */
    private HashMap<String, Aisle> aisles;

    /**
     * Shelves associated with this store
     */
    private HashMap<String, Shelf> shelves;

    /**
     * Inventories associated with this store
     */
    private HashMap<String, Inventory> storeInventories;

    /**
     * Sensor devices associated with this store
     */
    private HashMap<String, Sensor> sensors;

    /**
     * Microphone devices associated with this store
     */
    private HashMap<String, Microphone> microphones;

    /**
     * Camera devices associated with this store
     */
    private HashMap<String, Camera> cameras;

    /**
     * Appliance devices associated with this store
     */
    private HashMap<String, Appliance> appliances;

    /**
     * Robot devices associated with this store
     */
    private HashMap<String, Robot> robots;

    /**
     * Speaker devices associated with this store
     */
    private HashMap<String, Speaker> speakers;

    /**
     * Turnstile devices associated with this store
     */
    private HashMap<String, Turnstile> turnstiles;

    /**
     * The store's blockchain address
     */
    private String storeBlockChainAddress;

    /**
     * The ledger
     */
    private Ledger ledger;

    /**
     * Constructor for a store that sets up all starting parameters including initializing mapping to aisles, shelves,
     * inventories, sensors, appliances, customers
     * @param storeId String    Globally unique identifier (e.g. store100)
     * @param storeName String  Store Name (e.g. “Harvard Square Store”)
     * @param storeAddress String   Address (street, city, state) (e.g., “ 1400 Mass Avenue, Cambridge, MA 02138”)
     * @param ledger    Ledger  The blockchain ledger object
     */
    public Store(String storeId, String storeName, String storeAddress, Ledger ledger)
    {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeAddress = storeAddress;

        // Initialize maps for customers, aisles, shelves, inventories, sensors, devices associated with this store
        this.customers = new HashMap<String, Customer>();
        this.aisles = new HashMap<String, Aisle>();
        this.shelves = new HashMap<String, Shelf>();
        this.storeInventories = new HashMap<String, Inventory>();
        this.sensors = new HashMap<String, Sensor>();
        this.microphones = new HashMap<String, Microphone>();
        this.cameras = new HashMap<String, Camera>();
        this.appliances = new HashMap<String, Appliance>();
        this.robots = new HashMap<String, Robot>();
        this.speakers = new HashMap<String, Speaker>();
        this.turnstiles = new HashMap<String, Turnstile>();
        this.ledger = ledger;
        this.storeBlockChainAddress = this.storeId;

        this.createLedgerAccount(this.storeBlockChainAddress);

        this.processTransaction((this.storeBlockChainAddress + Clock.systemUTC().instant().toString()),
                10000, 10, "New Store Account", "master", this.storeBlockChainAddress);
    }

    /**
     * Getter for store Id
     * @return String    Globally unique identifier (e.g. store100)
     */
    public String getStoreId()
    {
        return this.storeId;
    }

    /**
     * Getter for store name
     * @return String   Store Name (e.g. “Harvard Square Store”)
     */
    public String getStoreName()
    {
        return this.storeName;
    }

    /**
     * Getter for all robots
     * @return  Hashmap Robot ID strings mapped to Robots in a Hashmap
     */
    public HashMap<String, Robot> getRobots() { return robots; }

    /**
     * Getter for all turnstiles
     * @return  Hashmap Turnstile ID strings mapped to Turnstile in a Hashmap
     */
    public HashMap<String, Turnstile> getTurnstiles() { return turnstiles; }

    /**
     * Getter for store address
     * @return String   Address (street, city, state) (e.g., “ 1400 Mass Avenue, Cambridge, MA 02138”)
     */
    public String getStoreAddress()
    {
        return this.storeAddress;
    }

    /**
     * Getter for store's blockchain address
     * @return  String  The store's blockchain address
     */
    public String getStoreBlockChainAddress() { return this.storeBlockChainAddress; }

    /**
     * Responds to the command to create an account
     * @param customerBlockchainAddress  List of strings for each argument passed with the command
     */
    private void createLedgerAccount(String customerBlockchainAddress)
    {
        try
        {
            System.out.println(String.format("CREATED NEW LEDGER ACCOUNT \n" +
                            "AccountId: %s\n",
                    this.ledger.createAccount(customerBlockchainAddress)));
        }
        catch (LedgerException e)
        {
            System.out.println("Ledger Account Creation Failed: " + e.toString());
        }
    }

    /**
     * Responds to the command to process a transaction
     * @param transactionId String  The transaction ID to use
     * @param amount    int The amount of the transaction
     * @param fee   int The fees to be paid for the transaction
     * @param note  String  Comments or description associated with the transaction
     * @param payer String  Blockchain address of the payer
     * @param receiver  String  Blockchain address of the receiver
     * @return  String  The transaction ID used if successful
     */
    public String processTransaction(String transactionId, int amount, int fee, String note, String payer,
                                     String receiver)
    {
        String transactionIdSuccessful = "";

        try
        {
            transactionIdSuccessful = this.ledger.createTransaction(transactionId,
                    amount,
                    fee,
                    note,
                    payer,
                    receiver);

            System.out.println(String.format("TRANSACTION PROCESSED Transaction ID: %s \n", transactionIdSuccessful));
            return transactionIdSuccessful;
        }
        catch (LedgerException e)
        {
            System.out.println("Ledger Process Transaction  Failed: " + e.toString());
        }
        return null;
    }

    /**
     * Responds to the command to get an account balance
     * @return  int     Integer number reflecting total account balance
     */
    public int getAccountBalance()
    {
        int accountBalance = 0;

        try
        {
            accountBalance = this.ledger.getAccountBalance(this.storeBlockChainAddress);
        }
        catch (LedgerException e)
        {
            System.out.println("Ledger Account Balance Check Failed: " + e.toString());
        }

        return accountBalance;
    }

    /**
     * Method to define each Aisle object associated with this store
     * @param aisleNumber String    Assigned aisle number, e.g., aisle_1
     * @param aisleName String  Assigned aisle number, e.g., aisle_1
     * @param aisleDescription String   Aisle description, e.g., milk, cheese, eggs, and other dairy products
     * @param aisleLocation AisleLocationEnum   Either floor or storeroom
     */
    public void defineAisle(String aisleNumber, String aisleName, String aisleDescription,
                            AisleLocationEnum aisleLocation)
    {
        this.aisles.put(aisleNumber, new Aisle(aisleNumber, aisleName, aisleDescription, aisleLocation));
    }

    /**
     * Getter for Aisle object associated with the provided aisle number
     * @param aisleNumber String    Aisle number, e.g., aisle_1
     * @return Aisle    The Aisle object associated with the Aisle Number
     */
    public Aisle getAisle(String aisleNumber)
    {
        return this.aisles.get(aisleNumber);
    }

    /**
     * Method to define each Shelf object associated with this store's aisles
     * @param aisleNumber String    Aisle number, e.g., aisle_1
     * @param shelfId String    The shelf identifier, e.g., shelf_1
     * @param shelfName String  The name of the shelf, e.g., milk
     * @param shelfLevel ShelfLevelEnum The height of the shelf (high, medium, low)
     * @param shelfDescription String   The description of shelf contents, e.g., skim and whole milk, soy and almond milk
     * @param shelfTemperature TemperatureEnum  Values: frozen, refrigerated, ambient, warm, hot. default value: ambient
     */
    public void defineShelf(String aisleNumber, String shelfId, String shelfName, ShelfLevelEnum shelfLevel,
                            String shelfDescription, TemperatureEnum shelfTemperature)
    {
        String shelfKey = aisleNumber + ":" + shelfId;
        this.shelves.put(shelfKey, new Shelf(shelfKey, shelfName, shelfLevel, shelfDescription, shelfTemperature));
    }

    /**
     * Getter for Shelf object associated with the aisle Id and shelf Id provided
     * @param aisleNumber String    Aisle number, e.g., aisle_1
     * @param shelfId String    The shelf identifier, e.g., shelf_1
     * @return Shelf    Shelf object associated with the aisle Id and shelf Id provided
     */
    public Shelf getShelf(String aisleNumber, String shelfId)
    {
        return this.shelves.get(aisleNumber + ":" + shelfId);
    }

    /**
     * Method to define each Inventory object associated with this store
     * @param aisleId String    Aisle number, e.g., aisle_1
     * @param shelfId String    The shelf identifier, e.g., shelf_1
     * @param inventoryId String    Alphanumeric Identifier of the inventory
     * @param inventoryLocation String  Location, Store:Aisle:Shelf
     * @param inventoryCapacity Integer Capacity, the maximum number of product items that can fit on the shelf
     * @param inventoryCount Integer    Count, which is the current count of product items on the shelf.
     * @param inventoryProductId String Product Id for which this is the inventory
     * @return Inventory    The Inventory object associated with the Inventory Id provided
     */
    public Inventory defineInventory(String aisleId, String shelfId, String inventoryId, String inventoryLocation, Integer inventoryCapacity,
                                     Integer inventoryCount, String inventoryProductId)
    {
        Inventory newInventory = new Inventory(inventoryId, inventoryLocation, inventoryCapacity, inventoryCount, inventoryProductId);
        this.storeInventories.put(inventoryId, newInventory);
        return newInventory;
    }

    /**
     * Getter for the Inventory object associated with the inventory Id provided
     * @param inventoryId String    Alphanumeric Identifier of the inventory
     * @return Inventory    The Inventory object associated with the Inventory Id
     */
    public Inventory getInventory(String inventoryId)
    {
        return this.storeInventories.get(inventoryId);
    }

    /**
     * Method to define each Sensor object associated with this store, which could be a microphone or camera
     * @param deviceId String   All Sensors have an ID, inherited from the parent abstract class Device
     * @param deviceName String   All Sensors have a Name, inherited from the parent abstract class Device
     * @param deviceType SensorTypesEnum   All Sensors have a type, e.g. camera, microphone, etc
     * @param deviceLocation String   All Sensors have a location, inherited from the parent abstract class Device
     * @return Device   An object of type Device which could contain a Microphone or a Camera object
     */
    public Device defineSensor(String deviceId, String deviceName, SensorTypesEnum deviceType, String deviceLocation)
    {
        Device returnValue = null;

        switch(deviceType.toString())
        {
            case "MICROPHONE":
                Microphone microphone = new Microphone(deviceId, deviceName, deviceType, deviceLocation);
                this.sensors.put(deviceId, microphone);
                this.microphones.put(deviceId, microphone);
                returnValue = microphone;
                break;

            case "CAMERA":
                Camera camera = new Camera(deviceId, deviceName, deviceType, deviceLocation);
                this.sensors.put(deviceId, camera);
                this.cameras.put(deviceId, camera);
                returnValue = camera;
                break;
        }
        System.out.println("SENSOR DEVICE " + deviceId + " DEFINED OF TYPE " + deviceType +
                " IN STORE " + this.storeId + "\n\n");
        return returnValue;
    }

    /**
     * Method to define each Appliance object associated with this store, which could be speaker, robot or turnstile
     * @param deviceId String   All Appliances have an ID, inherited from the parent abstract class Device
     * @param deviceName String   All Appliances have a Name, inherited from the parent abstract class Device
     * @param deviceType ApplianceTypesEnum   All Appliances have a type, e.g. speaker, robot, or turnstile
     * @param deviceLocation String   All Appliances have a location, inherited from the parent abstract class Device
     * @return Device   An object of type Device which could contain a Speaker, Robot, or Turnstile object
     */
    public Device defineAppliance(String deviceId, String deviceName, ApplianceTypesEnum deviceType, String deviceLocation)
    {
        Device returnValue = null;

        switch(deviceType.toString())
        {
            case "SPEAKER":
                Speaker speaker = new Speaker(deviceId, deviceName, deviceType, deviceLocation);
                this.appliances.put(deviceId, speaker);
                this.speakers.put(deviceId, speaker);
                returnValue = speaker;
                break;

            case "ROBOT":
                Robot robot = new Robot(deviceId, deviceName, deviceType, deviceLocation);
                this.appliances.put(deviceId, robot);
                this.robots.put(deviceId, robot);
                returnValue = robot;
                break;

            case "TURNSTILE":
                Turnstile turnstile = new Turnstile(deviceId, deviceName, deviceType, deviceLocation);
                this.appliances.put(deviceId, turnstile);
                this.turnstiles.put(deviceId, turnstile);
                returnValue = turnstile;
                break;
        }
        System.out.println("SENSOR DEVICE " + deviceId + " DEFINED OF TYPE " + deviceType +
                " IN STORE " + this.storeId + "\n\n");
        return returnValue;
    }

    /**
     * Method to locate nearest robot to a given location
     * @param robots    Hashmap Robot devices associated with this store
     * @param location  String  Location store:aisle of the robot
     * @return  Robot   The nearest robot to the given location
     */
    public Robot findRobotInLocation(HashMap<String, Robot> robots, String location)
    {
        Iterator hmIterator = robots.entrySet().iterator();

        // Iterate through the hashmap
        while (hmIterator.hasNext()) {
            HashMap.Entry hmElement = (HashMap.Entry)hmIterator.next();
            Robot robot = (Robot) hmElement.getValue();
            String deviceLocation = robot.getDeviceLocation();
            if(deviceLocation.equals(location))
            {
                return robot;
            }
        }
        return null;
    }

    /**
     * Printable JSON-like view of this object and its contents
     * @return String   Text containing all parts of the object information available
     */
    public String toString()
    {
        return "SHOWING STORE \n{\n " +
                "\t Id = " + this.storeId + "\n" +
                "\t Name = " + this.storeName + "\n" +
                "\t Address = " + this.storeAddress + "\n" +
                "\t Block Chain Address = " + this.storeBlockChainAddress + "\n" +
                "\t Account Balance = " + this.getAccountBalance() + "\n" +
            "}\n\n";
    }

}
