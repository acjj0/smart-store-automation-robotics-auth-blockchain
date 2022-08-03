package com.cscie97.store.model;

import com.cscie97.ledger.Ledger;
import com.cscie97.store.authentication.*;
import com.cscie97.store.controller.Message;
import com.cscie97.store.controller.StoreControllerService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that manages all commands provided in a script file.
 */
public class CommandProcessor {
    /**
     * The Singleton AuthenticationService object
     */
    private AuthenticationService authenticationService;

    /**
     * The StoreModelService object.
     */
    private StoreModelService storeModelService;

    /**
     * The StoreControllerService object
     */
    private StoreControllerService storeControllerService;

    /**
     * The blockchain ledger
     */
    private Ledger ledger;

    /**
     * DUMMY VALUE - Replace with actual Authentication Token implementation
     */
    private AccessToken currentUsersAccessToken;

    /**
     * Constructor for the CommandProcessor, where the Model, Controller, Ledger, and Singleton pattern instance of
     * Authentication service are first created
     */
    public CommandProcessor()
    {
        System.out.println("STORE INITIALIZATION BEGINS ............................");
        this.createLedger(Arrays.asList("create-ledger", "test", "description", "test ledger 2020", "seed", "harvard"));
        // Hold the Singleton pattern instance of the AuthenticationService
        this.authenticationService = AuthenticationService.getInstance();
        try
        {
            this.currentUsersAccessToken = this.authenticationService.login("root", "default",
                    CredentialTypeEnum.PASSWORD);
            this.storeModelService = new StoreModelService(this.ledger, this.currentUsersAccessToken);
            this.storeControllerService = new StoreControllerService(this.storeModelService, this.ledger,
                    this.currentUsersAccessToken);
            this.authenticationService.logout(this.currentUsersAccessToken);
        }
        catch (AuthenticationException e)
        {
            System.out.println(String.format("SCRIPT ERROR at line number: (%s)\n" +
                            "COMMAND: %s \n" +
                            "REASON: %s\n",
                    e.getAction(), e.getReason(), e.getOffendingValue()));
        }
        System.out.println("............................ STORE INITIALIZATION COMPLETE");
    }

    /**
     * Responds to the command to create a ledger
     * @param args  List of strings for each argument passed with the command
     */
    private void createLedger(List<String> args)
    {
        this.ledger = new Ledger(args.get(1), args.get(3), args.get(5));
        System.out.println(String.format("CREATED NEW LEDGER \n" +
                        "Name: %s \n" +
                        "Description: %s \n" +
                        "Seed: %s\n",
                this.ledger.getName(),
                this.ledger.getDescription(),
                this.ledger.getSeed()));
    }

    /**
     * Method to read a script file containing commands to process
     * @param fileName String   The file path for the script file to read
     * @throws CommandProcessorException    Exception thrown due to bad command
     */
    public void processCommandFile(String fileName) throws CommandProcessorException
    {
        // Inspiration from https://www.javatpoint.com/post/java-scanner-hasnextline-method
        File file = new File(fileName);
        Scanner script;

        int lineNumber = 0;

        try
        {
            script = new Scanner(file);
        }
        catch (FileNotFoundException e)
        {
            throw new CommandProcessorException("processCommandFile", "File not found", null);
        }

        while (script.hasNextLine()) {
            String line = script.nextLine();
            lineNumber++;

            // Skip empty lines and lines that are comments
            if (line.length() != 0 && !(line.startsWith("#"))) {
                try {
                    System.out.println("\n\nCOMMAND: " + line + "\n");
                    processCommand(line);
                } catch (CommandProcessorException e) {
                    System.out.println(String.format("SCRIPT ERROR at line number: (%s)\n" +
                                    "COMMAND: %s \n" +
                                    "REASON: %s\n",
                            lineNumber, e.getCommand(), e.getReason()));
                }
            }
        }

        script.close();
    }

    /**
     * Method to define the store
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void defineStore(List<String> args)
    {
        String storeId = args.get(1);
        String storeName = args.get(3);
        String storeAddress = args.get(5);

        storeModelService.defineStore(storeId, storeName, storeAddress, this.currentUsersAccessToken);
    }

    /**
     * Method to show store information
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void showStore(List<String> args)
    {
        try
        {
            storeModelService.showStore(args.get(1), this.currentUsersAccessToken);
        }
        catch (AuthenticationException e)
        {
            System.out.println(String.format("SCRIPT ERROR at line number: (%s)\n" +
                            "COMMAND: %s \n" +
                            "REASON: %s\n",
                    e.getAction(), e.getReason(), e.getOffendingValue()));
        }

    }

    /**
     * Method to define a new aisle
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void defineAisle(List<String> args)
    {
        String [] storeIdAisleNumber = args.get(1).split(":");
        String storeId = storeIdAisleNumber[0];
        String aisleNumber = storeIdAisleNumber[1];
        String aisleName = args.get(3);
        String aisleDescription = args.get(5);
        String aisleLocation = args.get(7);

        try
        {
            storeModelService.defineAisle(storeId, aisleNumber, aisleName, aisleDescription, aisleLocation, this.currentUsersAccessToken);
        }
        catch (AuthenticationException e)
        {
            System.out.println(String.format("SCRIPT ERROR at line number: (%s)\n" +
                            "COMMAND: %s \n" +
                            "REASON: %s\n",
                    e.getAction(), e.getReason(), e.getOffendingValue()));
        }
    }

    /**
     * Method to show an aisle's information
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void showAisle(List<String> args)
    {
        String [] storeIdAisleNumber = args.get(1).split(":");
        String storeId = storeIdAisleNumber[0];
        String aisleNumber = storeIdAisleNumber[1];

        try
        {
            storeModelService.showAisle(storeId, aisleNumber, this.currentUsersAccessToken);
        }
        catch (AuthenticationException e)
        {
            System.out.println(String.format("SCRIPT ERROR at line number: (%s)\n" +
                            "COMMAND: %s \n" +
                            "REASON: %s\n",
                    e.getAction(), e.getReason(), e.getOffendingValue()));
        }
    }

    /**
     * Method to define a new shelf
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void defineShelf(List<String> args)
    {
        String [] storeIdAisleNumberShelfId = args.get(1).split(":");
        String storeId = storeIdAisleNumberShelfId[0];
        String aisleNumber = storeIdAisleNumberShelfId[1];
        String shelfId = storeIdAisleNumberShelfId[2];

        String shelfName = args.get(3);
        String shelfLevel = args.get(5);
        String shelfDescription = args.get(7);
        String shelfTemperature = args.get(9);

        try
        {
            storeModelService.defineShelf(storeId, aisleNumber, shelfId, shelfName, shelfLevel, shelfDescription,
                    shelfTemperature, this.currentUsersAccessToken);
        }
        catch (AuthenticationException e)
        {
            System.out.println(String.format("SCRIPT ERROR at line number: (%s)\n" +
                            "COMMAND: %s \n" +
                            "REASON: %s\n",
                    e.getAction(), e.getReason(), e.getOffendingValue()));
        }
    }

    /**
     * Method to show a shelf's information
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void showShelf(List<String> args)
    {
        String [] storeIdAisleNumberShelfId = args.get(1).split(":");
        String storeId = storeIdAisleNumberShelfId[0];
        String aisleNumber = storeIdAisleNumberShelfId[1];
        String shelfId = storeIdAisleNumberShelfId[2];

        try {
            storeModelService.showShelf(storeId, aisleNumber, shelfId, this.currentUsersAccessToken);
        }
        catch (AuthenticationException e)
        {
            System.out.println(String.format("SCRIPT ERROR at line number: (%s)\n" +
                            "COMMAND: %s \n" +
                            "REASON: %s\n",
                    e.getAction(), e.getReason(), e.getOffendingValue()));
        }
    }

    /**
     * Method to define a new product
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void defineProduct(List<String> args)
    {
        String productId = args.get(1);
        String productName = args.get(3);
        String productDescription = args.get(5);
        String productSize = args.get(7);
        String productCategory = args.get(9);
        Integer productUnitPrice = Integer.parseInt(args.get(11));
        String productTemperature = args.get(13);

        storeModelService.defineProduct(productId, productName, productDescription, productSize, productCategory,
                productUnitPrice, productTemperature, this.currentUsersAccessToken);
    }

    /**
     * Method to display a product's information
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void showProduct(List<String> args)
    {
        storeModelService.showProduct(args.get(1), this.currentUsersAccessToken);
    }

    /**
     * Method to define an inventory's count
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void defineInventory(List<String> args)
    {
        String [] storeIdAisleNumberShelfId = args.get(3).split(":");
        String storeId = storeIdAisleNumberShelfId[0];
        String aisleNumber = storeIdAisleNumberShelfId[1];
        String shelfId = storeIdAisleNumberShelfId[2];



        String inventoryId = args.get(1);
        String inventoryLocation = args.get(3);
        Integer inventoryCapacity = Integer.parseInt(args.get(5));
        Integer inventoryCount = Integer.parseInt(args.get(7));
        String inventoryProductId = args.get(9);

        this.storeModelService.defineInventory(storeId, aisleNumber, shelfId, inventoryId,  inventoryLocation,
                inventoryCapacity, inventoryCount, inventoryProductId, this.currentUsersAccessToken);
    }

    /**
     * Method to show information about an inventory
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void showInventory(List<String> args)
    {
        storeModelService.showInventory(args.get(1), this.currentUsersAccessToken);
    }

    /**
     * Method to update an inventory
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void updateInventory(List<String> args)
    {
        storeModelService.updateInventoryCount(args.get(1), Integer.parseInt(args.get(3)), this.currentUsersAccessToken);
    }

    /**
     * Method to define a new customer
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void defineCustomer(List<String> args)
    {
        String customerId = args.get(1);
        String customerFirstName = args.get(3);
        String customerLastName = args.get(5);
        String customerType = (args.get(7).equals("true") ? "REGISTERED" : "GUEST");
        String customerIsAdult = (args.get(9).equals("true") ? "ADULT" : "CHILD");
        String customerEmailAddress = args.get(11);
        String customerBlockchainAddress = args.get(13);
        String customerLocation = "";
        String customerTimeLastSeen = "";

        storeModelService.defineCustomer(customerId, customerFirstName, customerLastName, customerType, customerIsAdult,
                customerEmailAddress, customerBlockchainAddress, customerLocation, customerTimeLastSeen, this.currentUsersAccessToken);
    }

    /**
     * Method to display information about a customer
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void showCustomer(List<String> args)
    {
        storeModelService.showCustomer(args.get(1), this.currentUsersAccessToken);
    }

    /**
     * Method to update a customer's location
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void updateCustomer(List<String> args)
    {
        String customerId = args.get(1);
        String customerLocation = args.get(3);
        this.storeModelService.updateCustomerLocation(customerId, customerLocation, this.currentUsersAccessToken);
    }

    /**
     * Method to define a new basket
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void defineBasket(List<String> args)
    {
        String basketId = args.get(1);
        this.storeModelService.defineBasket(basketId, this.currentUsersAccessToken);
    }

    /**
     * Method to display information about a basket
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void showBasket(List<String> args)
    {
        String basketId = args.get(1);
        this.storeModelService.showBasket(basketId, this.currentUsersAccessToken);
    }

    /**
     * Method to assign a basket to a customer
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void assignBasket(List<String> args)
    {
        String basketId = args.get(1);
        String customerId = args.get(3);
        this.storeModelService.assignBasket(basketId, customerId, this.currentUsersAccessToken);
    }

    /**
     * Method to retrieve a basket information for a customer
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void getCustomerBasket(List<String> args)
    {
        String customerId = args.get(1);
        this.storeModelService.getCustomerBasket(customerId, this.currentUsersAccessToken);
    }

    /**
     * Method to add a product item with specified item count to a basket
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void addBasketItem(List<String> args)
    {
        String basketId = args.get(1);
        String productId = args.get(3);
        Integer itemCount = Integer.parseInt(args.get(5));

        this.storeModelService.addBasketItem(basketId, productId, itemCount, this.currentUsersAccessToken);
    }

    /**
     * Method to remove a given quantity of a product from the basket
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void removeBasketItem(List<String> args)
    {
        String basketId = args.get(1);
        String productId = args.get(3);
        Integer itemCount = Integer.parseInt(args.get(5));

        this.storeModelService.removeBasketItem(basketId, productId, itemCount, this.currentUsersAccessToken);
    }

    /**
     * Method to clear all count of all products from a given basket
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void clearBasket(List<String> args)
    {
        String baskedId = args.get(1);
        this.storeModelService.clearBasket(baskedId, this.currentUsersAccessToken);
    }

    /**
     * Method to define a new device
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void defineDevice(List<String> args)
    {
        String deviceId = args.get(1);
        String deviceName = args.get(3);
        String deviceType = args.get(5);
        String deviceLocation = args.get(7);
        this.storeModelService.defineDevice(deviceId, deviceName, deviceType, deviceLocation, this.currentUsersAccessToken);
    }

    /**
     * Method to display information associated with a given device
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void showDevice(List<String> args)
    {
        String deviceId = args.get(1);
        this.storeModelService.showDevice(deviceId, this.currentUsersAccessToken);
    }

    /**
     * Method to create new Appliance event
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void createEvent(List<String> args) {

        this.storeModelService.createEvent(new Message(args), this.currentUsersAccessToken);
    }

    /**
     * Login to the Authentication Service
     * @param args List of Strings  Arguments passed in, including the command itself.
     */
    private void login(List<String> args)
    {
        try
        {
            if(args.get(2).equals("password"))
            {
                this.currentUsersAccessToken = this.authenticationService.login(args.get(1), args.get(3),
                        CredentialTypeEnum.valueOf(args.get(2).toUpperCase()));
            }
            else if(args.get(1).toUpperCase().equals("faceprint".toUpperCase()) ||
                    args.get(1).toUpperCase().equals("voiceprint".toUpperCase()))
            {
                this.currentUsersAccessToken = this.authenticationService.login(args.get(2),
                        CredentialTypeEnum.valueOf(args.get(1).toUpperCase()));
            }
            System.out.println("CURRENT USER'S ACCESS TOKEN: " + currentUsersAccessToken.getTokenId().toString());
        }
        catch (AuthenticationException e)
        {
            System.out.println(String.format("SCRIPT ERROR DURING ACTION: (%s)\n" +
                            "REASON: %s \n" +
                            "OFFENDING VALUE: %s\n",
                    e.getAction(), e.getReason(), e.getOffendingValue()));
        }
    }

    /**
     * Define permissions
     * @param args  List of Strings  Arguments passed in, including the command itself.
     */
    private void definePermission(List<String> args)
    {
        try
        {
            authenticationService.definePermission(args.get(1), args.get(3), args.get(5), this.currentUsersAccessToken);
        }
        catch (AuthenticationException e)
        {
            System.out.println(String.format("SCRIPT ERROR DURING ACTION: (%s)\n" +
                            "REASON: %s \n" +
                            "OFFENDING VALUE: %s\n",
                    e.getAction(), e.getReason(), e.getOffendingValue()));
        }
    }

    /**
     * Define roles
     * @param args   List of Strings  Arguments passed in, including the command itself.
     */
    private void defineRole(List<String> args)
    {
        try
        {
            authenticationService.defineRole(args.get(1), args.get(3), args.get(5), this.currentUsersAccessToken);
        }
        catch (AuthenticationException e)
        {
            System.out.println(String.format("SCRIPT ERROR DURING ACTION: (%s)\n" +
                            "REASON: %s \n" +
                            "OFFENDING VALUE: %s\n",
                    e.getAction(), e.getReason(), e.getOffendingValue()));
        }
    }

    /**
     * Add a Permission to Role
     * @param args   List of Strings  Arguments passed in, including the command itself.
     */
    private void addPermissionToRole(List<String> args)
    {
        try
        {
            authenticationService.addPermissionToRole(args.get(1), args.get(2), this.currentUsersAccessToken);
        }
        catch (AuthenticationException e)
        {
            System.out.println(String.format("SCRIPT ERROR DURING ACTION: (%s)\n" +
                            "REASON: %s \n" +
                            "OFFENDING VALUE: %s\n",
                    e.getAction(), e.getReason(), e.getOffendingValue()));
        }
    }

    /**
     * Create a new User
     * @param args   List of Strings  Arguments passed in, including the command itself.
     */
    private void createUser(List<String> args)
    {
        try
        {
            authenticationService.createUser(args.get(1), args.get(3), this.currentUsersAccessToken);
        }
        catch (AuthenticationException e)
        {
            System.out.println(String.format("SCRIPT ERROR DURING ACTION: (%s)\n" +
                            "REASON: %s \n" +
                            "OFFENDING VALUE: %s\n",
                    e.getAction(), e.getReason(), e.getOffendingValue()));
        }
    }

    /**
     * Add Credentials for a User
     * @param args   List of Strings  Arguments passed in, including the command itself.
     */
    private void addUserCredential(List<String> args)
    {
        try
        {
            authenticationService.addUserCredential(args.get(1), args.get(3),
                    CredentialTypeEnum.valueOf(args.get(2).toUpperCase()), this.currentUsersAccessToken);
        }
        catch (AuthenticationException e)
        {
            System.out.println(String.format("SCRIPT ERROR DURING ACTION: (%s)\n" +
                            "REASON: %s \n" +
                            "OFFENDING VALUE: %s\n",
                    e.getAction(), e.getReason(), e.getOffendingValue()));
        }
    }

    /**
     * Add a Role to a User
     * @param args   List of Strings  Arguments passed in, including the command itself.
     */
    private void addRoleToUser(List<String> args)
    {
        try
        {
            authenticationService.addRoleToUser(args.get(1), args.get(2), this.currentUsersAccessToken);
        }
        catch (AuthenticationException e)
        {
            System.out.println(String.format("SCRIPT ERROR DURING ACTION: (%s)\n" +
                            "REASON: %s \n" +
                            "OFFENDING VALUE: %s\n",
                    e.getAction(), e.getReason(), e.getOffendingValue()));
        }
    }

    /**
     * Create a Resource Role
     * @param args   List of Strings  Arguments passed in, including the command itself.
     */
    private void createResourceRole(List<String> args)
    {
        try
        {
            authenticationService.createResourceRole(args.get(1), args.get(2), args.get(3), this.currentUsersAccessToken);
        }
        catch (AuthenticationException e) {
            System.out.println(String.format("SCRIPT ERROR DURING ACTION: (%s)\n" +
                            "REASON: %s \n" +
                            "OFFENDING VALUE: %s\n",
                    e.getAction(), e.getReason(), e.getOffendingValue()));
        }
    }

    /**
     * Add a Resource Role to a User
     * @param args   List of Strings  Arguments passed in, including the command itself.
     */
    private void addResourceRoleToUser(List<String> args)
    {
        try
        {
            authenticationService.addResourceRoleToUser(args.get(1), args.get(2), this.currentUsersAccessToken);
        }
        catch (AuthenticationException e) {
            System.out.println(String.format("SCRIPT ERROR DURING ACTION: (%s)\n" +
                            "REASON: %s \n" +
                            "OFFENDING VALUE: %s\n",
                    e.getAction(), e.getReason(), e.getOffendingValue()));
        }
    }

    /**
     * Check whether a token has a permission to access a given resource
     * @param args   List of Strings  Arguments passed in, including the command itself.
     */
    private void checkAccess(List<String> args)
    {
        try
        {
            AccessToken accessToken = null;

            if(args.get(3).equals("password"))
            {
                String userId = args.get(2);
                String resourceId = args.get(6);
                String permissionId = args.get(8);

                User user = this.authenticationService.getUsers().get(userId);

                // No such user
                if(user == null)
                {
                    throw new AuthenticationException("checkAccess in CommandProcessor", "No such user", userId);
                }

                // If such a user exists, verify their credential and generate accessToken
                accessToken = null;
                String credentialString = args.get(4);
                if(user.verifyCredentialString(credentialString))
                {
                    accessToken = new AccessToken(user);
                    accessToken.setTokenState(AccessTokenStateEnum.ACTIVE);
                }

                Permission permission = this.authenticationService.getPermissions().get(permissionId);

                this.authenticationService.checkAccess(accessToken, permission);
            }
            else if(args.get(1).equalsIgnoreCase("faceprint") ||
                    args.get(1).equalsIgnoreCase("voiceprint"))
            {
                User user = null;
                String credentialString = args.get(2);
                String resourceId = args.get(4);
                String permissionId = args.get(6);

                for (Map.Entry<String, User> entry : this.authenticationService.getUsers().entrySet()) {
                    User checkUser = entry.getValue();
                    if (checkUser.verifyCredentialString(credentialString)) {
                        user = checkUser;
                    }
                }

                // No such user
                if(user == null)
                {
                    throw new AuthenticationException("checkAccess in CommandProcessor", "No such user", credentialString);
                }

                // If such a user exists, verify their credential and generate accessToken
                accessToken = null;
                if(user.verifyCredentialString(credentialString))
                {
                    accessToken = new AccessToken(user);
                    accessToken.setTokenState(AccessTokenStateEnum.ACTIVE);
                }

                Permission permission = this.authenticationService.getPermissions().get(permissionId);
                this.authenticationService.checkAccess(accessToken, permission);

            }

            System.out.println("GOT ACCESS TOKEN FOR CHECKING ACCESS: " + accessToken.getTokenId().toString());
        }
        catch (AuthenticationException e)
        {
            System.out.println(String.format("SCRIPT ERROR DURING ACTION: (%s)\n" +
                            "REASON: %s \n" +
                            "OFFENDING VALUE: %s\n",
                    e.getAction(), e.getReason(), e.getOffendingValue()));
        }
        catch (InvalidAccessTokenException e)
        {
            System.out.println(String.format("SCRIPT ERROR DURING ACTION: (%s)\n" +
                            "REASON: %s \n" +
                            "OFFENDING VALUE: %s\n",
                    e.getAction(), e.getReason(), e.getOffendingValue()));
        }
    }

    /**
     * Print all Authentication Service objects. Note that this has nothing to do with store inventory. A misnomer!
     */
    private void getInventory()
    {
        try
        {
            authenticationService.printInventory(this.currentUsersAccessToken);
        }
        catch (AuthenticationException e) {
            System.out.println(String.format("SCRIPT ERROR DURING ACTION: (%s)\n" +
                            "REASON: %s \n" +
                            "OFFENDING VALUE: %s\n",
                    e.getAction(), e.getReason(), e.getOffendingValue()));
        }
    }

    /**
     * Logout the current user from the system
     */
    private void logout()
    {
        try
        {
            authenticationService.logout(this.currentUsersAccessToken);
            this.currentUsersAccessToken = null;
        }
        catch (AuthenticationException e) {
            System.out.println(String.format("SCRIPT ERROR DURING ACTION: (%s)\n" +
                            "REASON: %s \n" +
                            "OFFENDING VALUE: %s\n",
                    e.getAction(), e.getReason(), e.getOffendingValue()));
        }
    }

    /**
     * *
     * Process a single command. The output of the command is formatted and displayed to stdout. Throw a
     * CommandProcessorException on error.
     * @param line String    One command containing arguments separated by spaces
     * @throws CommandProcessorException if the command (arg[0]) is not recognized in the switch statement
     */
    public void processCommand (String line) throws CommandProcessorException
    {
        List<String> args = new ArrayList<>();

        // Inspiration from https://howtodoinjava.com/java-regular-expression-tutorials/
        Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");

        Matcher matcher = regex.matcher(line);

        while (matcher.find())
        {
            args.add(matcher.group().replace("\"", ""));
        }

        String command = args.get(0).toLowerCase();

        // Don't need these lines for Assignment 3 script format, but needed for Assignment 2 script format
//        if( Arrays.asList("define", "create", "update", "show", "assign").contains(command) )
//        {
//            command = command + "-" + args.get(1);
//        }

        try
        {
            // For each type of command, find unique signature of the command, and call associated methods
            System.out.println("---------------------------------------------------------------------------");
            System.out.println(command);
            switch (command)
            {
                case "define-store":
                    this.defineStore(args);
                    break;
                case "show-store":
                    this.showStore(args);
                    break;
                case "define-aisle":
                    this.defineAisle(args);
                    break;
                case "show-aisle":
                    this.showAisle(args);
                    break;
                case "define-shelf":
                    this.defineShelf(args);
                    break;
                case "show-shelf":
                    this.showShelf(args);
                    break;
                case "define-product":
                    this.defineProduct(args);
                    break;
                case "show-product":
                    this.showProduct(args);
                    break;
                case "define-inventory":
                    this.defineInventory(args);
                    break;
                case "show-inventory":
                    this.showInventory(args);
                    break;
                case "update-inventory":
                    this.updateInventory(args);
                    break;
                case "define-customer":
                    this.defineCustomer(args);
                    break;
                case "show-customer":
                    this.showCustomer(args);
                    break;
                case "update-customer":
                    this.updateCustomer(args);
                    break;
                case "define-basket":
                    this.defineBasket(args);
                    break;
                case "show-basket_items":
                    this.showBasket(args);
                    break;
                case "assign-basket":
                    this.assignBasket(args);
                    break;
                case "get_customer_basket":
                    this.getCustomerBasket(args);
                    break;
                case "add_basket_item":
                    this.addBasketItem(args);
                    break;
                case "remove_basket_item":
                    this.removeBasketItem(args);
                    break;
                case "clear_basket":
                    this.clearBasket(args);
                    break;
                case "define-device":
                    this.defineDevice(args);
                    break;
                case "show-device":
                    this.showDevice(args);
                    break;
                case "create-event":
                    this.createEvent(args);
                    break;
                case "login":
                    this.login(args);
                    break;
                case "define_permission":
                    this.definePermission(args);
                    break;
                case "define_role":
                    this.defineRole(args);
                    break;
                case "add_permission_to_role":
                    this.addPermissionToRole(args);
                    break;
                case "create_user":
                    this.createUser(args);
                    break;
                case "add_user_credential":
                    this.addUserCredential(args);
                    break;
                case "add_role_to_user":
                    this.addRoleToUser(args);
                    break;
                case "create_resource_role":
                    this.createResourceRole(args);
                    break;
                case "add_resource_role_to_user":
                    this.addResourceRoleToUser(args);
                    break;
                case "check_access":
                    this.checkAccess(args);
                    break;
                case "get_inventory":
                    this.getInventory();
                    break;
                case "logout":
                    this.logout();
                    break;
                default:
                    throw new CommandProcessorException(command, "Unknown command", null);
            }
            System.out.println("---------------------------------------------------------------------------");
        }
        catch (CommandProcessorException e)
        {
            throw new CommandProcessorException(command, e.getReason(), null);
        }
    }
}
