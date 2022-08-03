package com.cscie97.store.controller;

/**
 * Interface for the Subject in the Observer pattern
 */
public interface iSubject {
    /**
     * Method to attach another observer
     * @param observer  iObserver   Another observer to attach
     */
    public void attachObserver(iObserver observer);

    /**
     * Method to detach an existing observer
     * @param observer  iObserver   An existing observer to detach
     */
    public void detachObserver(iObserver observer);

    /**
     * Method for Subject to notify all attached Observers
     * @param message   Message The immutable message sent by the subject to all observers
     */
    public void notifyObservers(Message message);
}