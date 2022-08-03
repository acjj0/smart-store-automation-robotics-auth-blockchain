package com.cscie97.ledger;

/**
 * The Ledger Exception is returned from the Ledger API methods in response to an error
 * condition. The Ledger Exception captures the action that was attempted and the reason for the
 * failure.
 */
public class LedgerException extends Throwable
{
    /**
     * Action that was performed
     * (e.g., “submit transaction”)
     */
    private String action;

    /**
     * Reason for the exception
     * (e.g. “insufficient funds in the payer account to cover the transaction amount and fee”).
     */
    private String reason;

    /**
     * Constructor for LedgerException that initializes the action and reason
     * @param action String     Action when the exception happened
     * @param reason String     Reason why the exception happened
     */
    public LedgerException(String action, String reason)
    {
        this.action = action;
        this.reason = reason;
    }

    /**
     * Getter to read the action that led to an exception
     * @return String   Action when the exception happened
     */
    public String getAction()
    {
        return this.action;
    }

    /**
     * Getter to read the reason why that exception happened
     * @return String   Reason why the exception happened
     */
    public String getReason()
    {
        return this.reason;
    }
}