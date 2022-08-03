package com.cscie97.store.authentication;

/**
 * Interface for the Visitor pattern Element. This method is implemented by all classes that are willing to accept
 * visitors that implement the Visitor pattern.
 */
public interface iVisitableElement
{
    /**
     * The method that accepts a visitor for this element
     * @param visitor   iVisitor    An iVisitor object that is being accepted as a visitor to this element
     */
    public void acceptVisitor(iVisitor visitor);
}
