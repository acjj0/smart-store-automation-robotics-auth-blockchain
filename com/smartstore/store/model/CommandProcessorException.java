package com.cscie97.store.model;

/**
 * The CommandProcessorException is returned from the CommandProcessor methods in
 * response to an error condition. The CommandProcessorException captures the command that
 * was attempted and the reason for the failure. In the case where commands are read from a file,
 * the line number of the command should be included in the exception.
 */
public class CommandProcessorException extends Throwable
{
    /**
     * Command that was performed (e.g., “submit transaction”)
     */
    private String command;

    /**
     * Reason for the exception
     * (e.g. “insufficient funds in the payer account to cover the transaction amount and fee”).
     */
    private String reason;

    /**
     * The line number of the command in the input file.
     */
    private String lineNumber;

    /**
     * Create a new Command Processor exception using the params in the args provided
     * @param command String The command issued that resulted in an exception
     * @param reason String The reason the command failed
     * @param lineNumber String The line number of the script where the command is that failed
     */
    public CommandProcessorException(String command, String reason, String lineNumber)
    {
        this.command = command;
        this.reason = reason;
        this.lineNumber = lineNumber;
    }

    /**
     * Getter for the command associated with this exception
     * @return String The command issued that resulted in an exception
     */
    public String getCommand()
    {
        return this.command;
    }

    /**
     * Getter for the reason for this exception
     * @return String The reason the command failed
     */
    public String getReason()
    {
        return this.reason;
    }

    /**
     * Getter for the line number that caused this exception
     * @return String The line number of the script where the command is that failed
     */
    public String getLineNumber()
    {
        return this.lineNumber;
    }

}
