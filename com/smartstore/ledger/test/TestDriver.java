package com.cscie97.ledger.test;

import com.cscie97.ledger.CommandProcessor;
import com.cscie97.ledger.CommandProcessorException;

/**
 * Test Driver
 */
public class  TestDriver
{
    /**
     * Java main method
     * @param args  String array    List of parameters passed from the command line
     */
    public static void main(String[] args)
    {
        CommandProcessor commandProcessor = new CommandProcessor();

        try
        {
            commandProcessor.processCommandFile(args[0]);
        }
        catch (CommandProcessorException e)
        {
            System.out.println(String.format("ERROR (%s): %s %s\n",
                    "file read error", e.getCommand().toUpperCase(), e.getReason().toUpperCase()));
        }
    }

}