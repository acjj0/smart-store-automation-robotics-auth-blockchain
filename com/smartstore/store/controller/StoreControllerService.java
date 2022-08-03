package com.cscie97.store.controller;

import com.cscie97.ledger.Ledger;
import com.cscie97.store.authentication.AccessToken;
import com.cscie97.store.authentication.AuthenticationService;
import com.cscie97.store.authentication.InvalidAccessTokenException;
import com.cscie97.store.authentication.Permission;
import com.cscie97.store.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Store Controller Service is responsible for monitoring the state of the sensors and
 * appliances within the store
 */
public class StoreControllerService implements iObserver{

    /**
     * Copy of reference to the Store Model service.
     */
    StoreModelService storeModelService;

    /**
     * Constructor for the Store Controller Service
     * @param storeModelService StoreModelService   Represents the Store Model service
     * @param ledger    Ledger  The relevant ledger with blockchain
     * @param auth_token    AccessToken  Dummy auth token le
     */
    public StoreControllerService(StoreModelService storeModelService, Ledger ledger, AccessToken auth_token)
    {
        if(! this.isTokenValid(auth_token, "entitlement_admin_permission", "StoreControllerService Constructor"))
        {
            return;
        }

        this.storeModelService = storeModelService;

        System.out.println("STORE CONTROLLER SERVICE CREATED \n");

        try
        {
            this.storeModelService.attachObserver(this);
        }
        catch(Exception e)
        {
            System.out.println(String.format("Error attaching Observer: " +
                            "REASON: %s\n",
                    e.toString()));
        }
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
        Permission permission;

        try
        {
            permission = authenticationService.getPermissions().get(permissionId);
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
     * Method called by the client to create a ConcreteCommand object reflecting store entry by a customer
     * @param turnstile Turnstile   The turnstile that generated this event
     * @param storeId   Store   The store in which this event happened
     * @param eventArgs List of Strings Parameters passed that are specific to this event
     * @return  EnterStore  The ConcreteCommand Object that reflects this event
     */
    public Command enterStore(String turnstile, String storeId, List<String> eventArgs)
    {
        return new EnterStore((Turnstile) this.storeModelService.getDevice(turnstile),
                this.storeModelService.getStore(storeId),
                this.storeModelService.getCustomer(eventArgs.get(1)),
                this.storeModelService.getUnassignedBasket(),
                eventArgs.get(3));
    }

    /**
     * Method called by the client to create a ConcreteCommand object reflecting basket change by a customer
     * @param camera Camera   The camera that generated this event
     * @param storeId   Store   The store in which this event happened
     * @param eventArgs List of Strings Parameters passed that are specific to this event
     * @return  BasketEvent The ConcreteCommand Object that reflects this event
     */
    private Command basketEvent(String camera, String storeId, List<String> eventArgs)
    {
        return new BasketEvent((Camera) this.storeModelService.getDevice(camera),
                this.storeModelService.getStore(storeId),
                this.storeModelService.getCustomer(eventArgs.get(1)),
                this.storeModelService.getProductById(eventArgs.get(2)),
                this.storeModelService.getInventoryById(eventArgs.get(3)),
                Integer.parseInt(eventArgs.get(5)),
                eventArgs.get(4));
    }

    /**
     * Method called by the client to create a ConcreteCommand object reflecting product fetch to a customer by a robot
     * @param microphone Microphone   The microphone that generated this event
     * @param storeId   Store   The store in which this event happened
     * @param eventArgs List of Strings Parameters passed that are specific to this event
     * @return FetchProduct The ConcreteCommand Object that reflects this event
     */
    private Command fetchProduct(String microphone, String storeId, List<String> eventArgs)
    {
        return new FetchProduct((Microphone) this.storeModelService.getDevice(microphone),
                this.storeModelService.getStore(storeId),
                this.storeModelService.getCustomer(eventArgs.get(1)),
                this.storeModelService.getProductById(eventArgs.get(2)),
                this.storeModelService.getInventoryById(eventArgs.get(3)),
                Integer.parseInt(eventArgs.get(5)),
                eventArgs.get(4));
    }

    /**
     * Method called by the client to create a ConcreteCommand object reflecting emergency situation
     * @param camera Camera   The camera that generated this event
     * @param storeId   Store   The store in which this event happened
     * @param eventArgs List of Strings Parameters passed that are specific to this event
     * @return Emergency The ConcreteCommand Object that reflects this event
     */
    private Command emergency(String camera, String storeId, List<String> eventArgs)
    {
        return new Emergency((Camera) this.storeModelService.getDevice(camera),
                this.storeModelService.getStore(storeId),
                EmergencyTypeEnum.valueOf(eventArgs.get(1)),
                eventArgs.get(2));
    }

    /**
     * Method called by the client to create a ConcreteCommand object reflecting sighting of a customer by a camera
     * @param camera Camera   The camera that generated this event
     * @param storeId   Store   The store in which this event happened
     * @param eventArgs List of Strings Parameters passed that are specific to this event
     * @return CustomerSeen The ConcreteCommand Object that reflects this event
     */
    private Command customerSeen(String camera, String storeId, List<String> eventArgs)
    {
        return new CustomerSeen((Camera) this.storeModelService.getDevice(camera),
                this.storeModelService.getStore(storeId),
                this.storeModelService.getCustomer(eventArgs.get(1)),
                eventArgs.get(2));
    }

    /**
     * Method called by the client to create a ConcreteCommand object reflecting broken glass in an aisle
     * @param camera Camera   The camera that generated this event
     * @param storeId   Store   The store in which this event happened
     * @param eventArgs List of Strings Parameters passed that are specific to this event
     * @return BrokenGlass The ConcreteCommand Object that reflects this event
     */
    private Command brokenGlass(String camera, String storeId, List<String> eventArgs)
    {
        return new BrokenGlass((Camera) this.storeModelService.getDevice(camera),
                this.storeModelService.getStore(storeId),
                eventArgs.get(1));
    }

    /**
     * Method called by the client to create a ConcreteCommand object reflecting product spill in an aisle
     * @param camera Camera   The camera that generated this event
     * @param storeId   Store   The store in which this event happened
     * @param eventArgs List of Strings Parameters passed that are specific to this event
     * @return ProductSpill The ConcreteCommand Object that reflects this event
     */
    private Command productSpill(String camera, String storeId, List<String> eventArgs)
    {
        return new ProductSpill((Camera) this.storeModelService.getDevice(camera),
                this.storeModelService.getStore(storeId),
                eventArgs.get(1),
                this.storeModelService.getProductById(eventArgs.get(2)));
    }

    /**
     * Method called by the client to create a ConcreteCommand object reflecting situation to locate a customer
     * @param microphone Microphone   The microphone that generated this event
     * @param storeId   Store   The store in which this event happened
     * @param eventArgs List of Strings Parameters passed that are specific to this event
     * @return MissingPerson The ConcreteCommand Object that reflects this event
     */
    private Command missingPerson(String microphone, String storeId, List<String> eventArgs)
    {
        return new MissingPerson((Microphone) this.storeModelService.getDevice(microphone),
                this.storeModelService.getStore(storeId),
                this.storeModelService.getCustomer(eventArgs.get(2)));
    }

    /**
     * Method called by the client to create a ConcreteCommand object reflecting situation to calculate basket total
     * @param microphone Microphone   The microphone that generated this event
     * @param storeId   Store   The store in which this event happened
     * @param eventArgs List of Strings Parameters passed that are specific to this event
     * @return CheckAccountBalance The ConcreteCommand Object that reflects this event
     */
    private Command checkAccountBalance(String microphone, String storeId, List<String> eventArgs)
    {
        return new CheckAccountBalance((Microphone) this.storeModelService.getDevice(microphone),
                this.storeModelService.getStore(storeId),
                this.storeModelService.getCustomer(eventArgs.get(1)));
    }

    /**
     * Method called by the client to create a ConcreteCommand object reflecting situation to assist a customer to car
     * @param turnstile Turnstile   The turnstile that generated this event
     * @param storeId   Store   The store in which this event happened
     * @param eventArgs List of Strings Parameters passed that are specific to this event
     * @return AssistCustomerToCar The ConcreteCommand Object that reflects this event
     */
    private Command assistCustomer(String turnstile, String storeId, List<String> eventArgs)
    {
        return new AssistCustomerToCar((Turnstile) this.storeModelService.getDevice(turnstile),
                this.storeModelService.getStore(storeId),
                this.storeModelService.getCustomer(eventArgs.get(1)),
                eventArgs.get(2));
    }

    /**
     * Method called by the client to create a ConcreteCommand object reflecting situation to checkout customer's basket
     * @param turnstile Turnstile   The turnstile that generated this event
     * @param storeId   Store   The store in which this event happened
     * @param eventArgs List of Strings Parameters passed that are specific to this event
     * @return Checkout The ConcreteCommand Object that reflects this event
     */
    private Command checkout(String turnstile, String storeId, List<String> eventArgs)
    {
        return new Checkout((Turnstile) this.storeModelService.getDevice(turnstile),
                this.storeModelService.getStore(storeId),
                this.storeModelService.getCustomer(eventArgs.get(1)));
    }

    /**
     * The client that creates the ConcreteCommand Object for the Command Pattern
     * @param args  List of Strings The parameters passed to the client
     * @return  Command The ConcreteCommand Object created based on the client's create request
     */
    private Command client(List<String> args)
    {
        List<String> eventArgs = new ArrayList<>();

        Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");

        Matcher matcher = regex.matcher(args.get(5));

        while (matcher.find()) {
            eventArgs.add(matcher.group().replace("\"", ""));
        }
        Command command = this.createCommand(args, eventArgs);
        return command;
    }

    /**
     * The create Command method that chooses the type of ConcreteCommand Object to create
     * @param args  List of Strings The parameters passed to the client
     * @param eventArgs List of Strings The parameters specific to the event forming the CommandData passed to the client
     * @return  Command The ConcreteCommand Object created based on the client's create request
     */
    private Command createCommand(List<String> args, List<String> eventArgs)
    {
        Command command = null;

        try
        {
            // For each type of command, find unique signature of the command, and call associated methods
            switch (eventArgs.get(0))
            {
                case "enter-store":
                    command = this.enterStore(args.get(1), args.get(3), eventArgs);
                    break;
                case "basket-event":
                    command = this.basketEvent(args.get(1), args.get(3), eventArgs);
                    break;
                case "fetch-product":
                    command = this.fetchProduct(args.get(1), args.get(3), eventArgs);
                    break;
                case "emergency":
                    command = this.emergency(args.get(1), args.get(3), eventArgs);
                    break;
                case "customer-seen":
                    command = this.customerSeen(args.get(1), args.get(3), eventArgs);
                    break;
                case "broken-glass":
                    command = this.brokenGlass(args.get(1), args.get(3), eventArgs);
                    break;
                case "product-spill":
                    command = this.productSpill(args.get(1), args.get(3), eventArgs);
                    break;
                case "missing-person":
                    command = this.missingPerson(args.get(1), args.get(3), eventArgs);
                    break;
                case "check-acc-bal":
                    command = this.checkAccountBalance(args.get(1), args.get(3), eventArgs);
                    break;
                case "assist-customer":
                    command = this.assistCustomer(args.get(1), args.get(3), eventArgs);
                    break;
                case "checkout":
                    command = this.checkout(args.get(1), args.get(3), eventArgs);
                    break;
                default:
                    throw new StoreControllerServiceException(eventArgs.get(0), "Unknown command");
            }
        }
        catch (StoreControllerServiceException e)
        {
            System.out.println("createCommand failed: " + e.toString());
        }

        return command;
    }

    /**
     * The update method of the Observer Pattern sitting within the ConcreteObserver class StoreControllerService.
     * This method manages the transition between the Observer Pattern and the Command Pattern. It handles both the
     * Client's creation of the ConcreteCommand objects and the Invoker's calls to execute the command.
     * @param message   String  The completed command that's passed to this method
     */
    @Override
    public void update(Message message) {
        Command command = this.client(message.getMessageContent());
        this.invoker(command);
    }

    /**
     * The Invoker method that calls the execute method of the Command objects
     * @param command   Command The ConcreteCommand object which contains the logic for execution
     */
    private void invoker(Command command) {
        if(command != null)
        {
            command.execute();
        }
    }
}
