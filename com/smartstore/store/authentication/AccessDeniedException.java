package com.cscie97.store.authentication;

/**
 * Exception class used when the user associated with the AccessToken lacks the permission required by the method.
 */
public class AccessDeniedException extends Throwable{
    /**
     * The action taken during which the exception occurred
     */
    private String action;

    /**
     * The potential reason why the exception occurred
     */
    private String reason;

    /**
     * Constructor for the exception class, taking in the action and the reason
     * @param action    String  The action taken during which the exception occurred
     * @param reason    String  The potential reason why the exception occurred
     */
    public AccessDeniedException(String action, String reason)
    {
        this.action = action;
        this.reason = reason;
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
}
