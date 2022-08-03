package com.cscie97.store.controller;

/**
 * Interface for the Observer in the Observer pattern
 */
public interface iObserver {
    /**
     * Update method that will be overridden by the ConcreteObserver
     * @param message   Message The immutable message transmitted to the observers
     */
    public void update(Message message);
}
