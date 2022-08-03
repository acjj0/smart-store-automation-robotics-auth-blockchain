package com.cscie97.store.controller;

import java.util.List;

/**
 * State object in an immutable object so that no class can modify it’s content by mistake.
 */
public class Message
{
    /**
     * The immutable list of parameters in transit
     */
    final List<String> args;

    /**
     * Constructor to create a Message object
     * @param args  List of Strings The complete command broken up into strings for processing as command data
     */
    public Message (List<String> args) {
        this.args = args;
    }

    /**
     * Method to access the content of this message
     * @return  List of Strings The content of the message
     */
    public List<String> getMessageContent() {
        return args;
    }
}