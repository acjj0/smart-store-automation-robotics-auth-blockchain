package com.cscie97.ledger;

/**
 * The Account class represents an individual account within the Ledger Service. An account
 * contains an address that provides a unique identity for the Account. The Account also contains
 * a balance that represents the value of the account. The account can only be updated by the
 * Ledger Service.
 */
public class Account
{
    /**
     * Unique identifier for the account, assigned automatically when an Account instance is created.
     */
    private String address;

    /**
     * Balance of the account which reflects the total transfers to and from the Account,
     * including fees for transactions where the account is the payer.
     */
    private int balance;

    /**
     * Class constructor
     * Create new account, set its unique address
     * @param address   string  Unique identifier for the account
     */
    public Account(String address)
    {
        this.address = address;
    }

    /**
     * Getter to access the current balance of the account
     * @return current balance of the account object
     */
    public int getBalance()
    {
        return this.balance;
    }

    /**
     * Setter to assign a current balance amount to the account
     * @param balance to set for the account object
     */
    public void setBalance(int balance)
    {
        this.balance = balance;
    }

    /**
     * Getter to access the unique address for this account
     * @return unique address of the account object
     */
    public String getAddress()
    {
        return this.address;
    }

}