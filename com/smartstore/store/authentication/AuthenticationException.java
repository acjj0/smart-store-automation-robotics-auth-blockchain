package com.cscie97.store.authentication;

/**
 * Exception class used to convey that authentication has failed.
 */
public class AuthenticationException extends Throwable{
    /**
     * The action taken during which the exception occurred
     */
    private String action;

    /**
     * The potential reason why the exception occurred
     */
    private String reason;

    /**
     * The userId or other information about the thing that caused the exception, for logging or debugging the problem
     */
    private String offendingValue;

    /**
     * Constructor for the exception class, taking in the parameters for action, reason, and offending value.
     * @param action    String  The action taken during which the exception occurred
     * @param reason    String  The potential reason why the exception occurred
     * @param offendingValue  String The userId or other information about the thing that caused the exception
     */
    public AuthenticationException(String action, String reason, String offendingValue)
    {
        this.action = action;
        this.reason = reason;
        this.offendingValue = offendingValue;
    }

    /**
     * Getter for the action taken
     * @return  String  The action taken, typically the method name
     */
    public String getAction() { return this.action; }

    /**
     * Getter for the potential reason
     * @return  String  The potential reason for the exception
     */
    public String getReason() { return this.reason; }

    /**
     * Getter for the userId or other value that caused the exception
     * @return  String  The userId or other thing that caused the exception
     */
    public String getOffendingValue() { return this.offendingValue; }
}
