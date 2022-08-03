package com.cscie97.ledger;

/**
 * The Transaction class represents a transaction in the Ledger System. A transaction contains a
 * transaction id, an amount, a fee, a note, and references a payer account and a receiver
 * account. The transaction amount is transferred from the payer’s account balance to the
 * receiver’s account balance. The transaction fee is transferred from the payer’s account to the
 * master account. Transactions are aggregated within blocks.
 */
public class Transaction
{
    /**
     * Unique identifier for the transaction assigned to the transaction by the Ledger System.
     */
    private String transactionId;

    /**
     * The transaction amount to be deducted from the payer account and added to the receiver’s account. The
     * amount must be greater or equal to 0 and less than or equal to max.integer.
     */
    private int amount;

    /**
     * The fee is taken from the payer account and added to the master account.
     * The fee must be greater than or equal to 10.
     */
    private int fee;

    /**
     * An arbitrary string that may be up to 1024 characters in length.
     */
    private String note;

    /**
     * Assocation - The Account issuing the transaction. The amount of the transaction and the
     * transaction fee will be deducted from the payer’s account balance.
     */
    private Account payer;

    /**
     * Assocation - The amount of the transaction will be added to the balance of the receiver account.
     */
    private Account receiver;

    /**
     * Constructor for each transaction
     * @param transactionId String   Unique identifier for the transaction
     * @param amount   int      Transaction amount to deduct from the payer account and add to the receiver account
     * @param fee      int      Fee taken from the payer account and added to the master account
     * @param note     String   Information about the transaction for future reference
     * @param payer    Account  Reference to the payer of the transaction
     * @param receiver Account  Reference to the receiver of the transaction
     */
    public Transaction(String transactionId, int amount, int fee, String note, Account payer, Account receiver)
    {
        this.transactionId = transactionId;
        this.amount = amount;
        this.fee = fee;
        this.note = note;
        this.payer = payer;
        this.receiver = receiver;
    }

    /**
     * Getter for the transaction Id
     * @return String   Unique identifier for the transaction
     */
    public String getTransactionId()
    {
        return this.transactionId;
    }

    /**
     * Getter for the amount of this transaction
     * @return int  Transaction amount to deduct from the payer account and add to the receiver account
     */
    public int getAmount()
    {
        return this.amount;
    }

    /**
     * Getter for the fee of this transaction
     * @return int  Fee taken from the payer account and added to the master account
     */
    public int getFee()
    {
        return this.fee;
    }

    /**
     * Getter for referece to the payer account
     * @return Account  Reference to the payer of the transaction
     */
    public Account getPayer()
    {
        return this.payer;
    }

    /**
     * Getter for reference to the receiver account
     * @return Account  Reference to the receiver of the transaction
     */
    public Account getReceiver()
    {
        return this.receiver;
    }

    /**
     * Overriding the default toString() with concatenated readable string
     * @return String   Concatenated string with transaction info
     */
    public String toString()
    {
        return String.format("ID: %s %s from %s to %s, Fee: %s, Note: %s",
                this.getTransactionId(),
                this.amount,
                this.payer.getAddress(),
                this.receiver.getAddress(),
                this.fee,
                this.note);
    }

    /**
     * Get the hash for the toString() of this transaction object
     * @return String   Get a unique hash for the transaction toString()
     */
    public String toHash()
    {
        return Util.sha256(this.toString());
    }
}