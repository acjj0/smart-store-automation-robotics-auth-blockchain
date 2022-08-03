package com.cscie97.store.controller;

/**
 * The abstract Command class for the Command Pattern
 */
public abstract class Command
{
    /**
     * The execute method called by the Invoker in the Command Pattern. Concrete derived classes will have more info
     * about how to process the command data.
     */
    public void execute() { }
}

