package com.cscie97.store.model;

import com.cscie97.ledger.Ledger;
import com.cscie97.ledger.LedgerException;

import java.time.Clock;

/**
 * Customer represents a person who shops at the store. Customers are recognized by the
 * Store24X7 system through facial and voice recognition. Cameras and Microphones located in
 * each aisle of the store monitor the location of all customers. Customers can be either Adults or
 * Children. Customers can be known and registered or unknown (e.g. guest). All known
 * customers have a name for reference.
 */
public class Customer {
    /**
     * Customer Identifier
     */
    private String customerId;

    /**
     * Customer First Name
     */
    private String customerFirstName;

    /**
     * Customer Last Name
     */
    private String customerLastName;

    /**
     * Customer Type (Registered or Guest)
     */
    private CustomerTypeEnum customerType;

    /**
     * Customer Is Adult Or Child
     */
    private String customerIsAdult;

    /**
     * Customer's Email Address
     */
    private String customerEmailAddress;

    /**
     * Account Blockchain Address used for billing
     */
    private String customerBlockchainAddress;

    /**
     * Copy of ledger here to process transactions of this customer
     */
    private Ledger ledger;

    /**
     * Current Location containing Store ID and Aisle ID separated by colon
     */
    private String customerLocation;

    /**
     * Time last seen (updated when location updates)
     */
    private String customerTimeLastSeen;

    /**
     * Basket currently assigned to this customer, null if no basket is currently assigned
     */
    private Basket assignedBasket;

    /**
     * Constructor to set up a customer object with all required parameters
     * @param customerId String Customer Identifier
     * @param customerFirstName String Customer First Name
     * @param customerLastName String   Customer Last Name
     * @param customerType CustomerTypeEnum Customer Type (Registered or Guest)
     * @param customerIsAdult String Customer Adult or Child
     * @param customerEmailAddress String   Customer's Email Address
     * @param customerBlockchainAddress String  Account Blockchain Address used for billing
     * @param ledger    Ledger  Local copy of the ledger for transactions to be initiated in the customer object
     * @param customerLocation String   Current Location containing Store ID and Aisle ID separated by colon
     * @param customerTimeLastSeen String   Time last seen (updated when location updates)
     */
    public Customer(String customerId, String customerFirstName, String customerLastName, CustomerTypeEnum customerType,
                    String customerIsAdult, String customerEmailAddress, String customerBlockchainAddress, Ledger ledger,
                    String customerLocation, String customerTimeLastSeen)
    {
        this.customerId = customerId;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerType = customerType;
        this.customerIsAdult = customerIsAdult;
        this.customerEmailAddress = customerEmailAddress;
        this.customerBlockchainAddress = customerBlockchainAddress;
        this.ledger = ledger;
        this.customerLocation = customerLocation;
        this.customerTimeLastSeen = customerTimeLastSeen;

        if(this.customerType.name() == "REGISTERED") {
            this.createLedgerAccount(this.customerBlockchainAddress);
            this.processTransaction((this.customerBlockchainAddress + Clock.systemUTC().instant().toString()),
                    200, 10, "Account Creation Funding",
                    "master", this.customerBlockchainAddress);
        }
    }

    /**
     * Getter for customer identifer
     * @return String   Customer Identifier
     */
    public String getCustomerId()
    {
        return this.customerId;
    }

    /**
     * Getter for first name, without making the first letter uppercase
     * @return  String  First name of the customer
     */
    public String getCustomerFirstName() { return this.customerFirstName; }

    /**
     * Getter for this customer's blockchain address
     * @return  String  Blockchain address of this customer
     */
    public String getCustomerBlockchainAddress() { return this.customerBlockchainAddress; }

    /**
     * Getter for the customer's location
     * @return  String  Blockchain address meant to for payments
     */
    public String getCustomerLocation() { return this.customerLocation; }

    /**
     * Getter for the time when the customer was last seen
     * @return  String  The time when the customer was last ween.
     */
    public String getCustomerTimeLastSeen() { return this.customerTimeLastSeen; }

    /**
     * Method to update location of this customer, and update time when the customer was seen at this location
     * @param location String   The Store Id and Aisle Id separated by a colon where the customer was last seen
     */
    public void updateLocation(String location)
    {
        this.customerLocation = location;
        this.customerTimeLastSeen = Clock.systemUTC().instant().toString();
    }

    /**
     * Setter to assign a basket to this customer
     * @param basket Basket The Basket object assigned to this customer
     */
    public void setAssignedBasket(Basket basket)
    {
        this.assignedBasket = basket;
    }

    /**
     * Getter to retrieve the Basket object which is currently assigned to this customer, which can be null too
     * @return Basket   The Basket object assigned to this customer
     */
    public Basket getAssignedBasket()
    {
        return this.assignedBasket;
    }

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
            accountBalance = this.ledger.getAccountBalance(this.customerBlockchainAddress);
        }
        catch (LedgerException e)
        {
            System.out.println("Ledger Account Balance Check Failed: " + e.toString());
        }

        return accountBalance;
    }

    /**
     * Printable JSON-like view of this object and its contents
     * @return String   Text containing all parts of the object information available
     */
    public String toString()
    {
        return "SHOWING CUSTOMER INFO \n{\n " +
                "\t Id = " + this.customerId + "\n" +
                "\t First Name = " + this.customerFirstName + "\n" +
                "\t Last Name = " + this.customerLastName + "\n" +
                "\t Type = " + this.customerType + "\n" +
                "\t Adult Or Child = " + this.customerIsAdult + "\n" +
                "\t Email Address = " + this.customerEmailAddress + "\n" +
                "\t Block Chain Address = " + this.customerBlockchainAddress + "\n" +
                "\t Account Balance = " + this.getAccountBalance() + "\n" +
                "\t Location = " + this.customerLocation + "\n" +
                "\t Time Last Seen = " + this.customerTimeLastSeen + "\n" +
                "}\n\n";
    }


}
