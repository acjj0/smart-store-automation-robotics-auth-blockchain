package com.cscie97.store.test;

import com.cscie97.store.model.CommandProcessor;
import com.cscie97.store.model.CommandProcessorException;

/**
 * The Class used to test drive the scripts
 */
public class TestDriver
{
    /**
     * Java starting point
     * @param args  Arguments passed through the file path
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