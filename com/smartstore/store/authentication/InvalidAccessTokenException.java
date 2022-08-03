package com.cscie97.store.authentication;

/**
 * Exception class used to flag AuthToken that is inactive, or not within the expiration period
 */
public class InvalidAccessTokenException extends Throwable{
    /**
     * The action taken during which the exception occurred
     */
    private String action;

    /**
     * The potential reason why the exception occurred
     */
    private String reason;

    /**
     * The token that caused the exception, used for logging or debugging the problem
     */
    private AccessToken offendingToken;

    /**
     * Constructor for the exception class, taking in the parameters for action, reason, and offending value.
     * @param action    String  The action taken during which the exception occurred
     * @param reason    String  The potential reason why the exception occurred
     * @param offendingTokenId  AccessToken The token that caused the exception
     */
    public InvalidAccessTokenException(String action, String reason, AccessToken offendingTokenId) {
        this.action = action;
        this.reason = reason;
        this.offendingToken = offendingTokenId;
    }

    /**
     * Getter for the action taken
     * @return  String  The action taken, typically the method name
     */
    public String getAction()
    {
        return this.action;
    }

    /**
     * Getter for the potential reason
     * @return  String  The potential reason for the exception
     */
    public String getReason()
    {
        return this.reason;
    }

    /**
     * Getter for the token that caused the exception
     * @return  AccessToken The token that caused the exception
     */
    public AccessToken getOffendingValue()
    {
        return this.offendingToken;
    }
}
