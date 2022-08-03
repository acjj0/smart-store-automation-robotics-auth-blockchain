package com.cscie97.store.authentication;

/**
 * Permission is a concrete class that extends the abstract class Entitlement, and represents each permission in the
 * authentication service.
 */
public class Permission extends Entitlement{
    /**
     * Constructor for the Permission objects
     * @param id    String  Id of the permissions
     * @param name  String  Name of the permission
     * @param description   String  Description of the permission
     */
    public Permission(String id, String name, String description)
    {
        super(id, name, description);
    }

    /**
     * The method that accepts a visitor for this element
     * @param visitor   iVisitor    An iVisitor object that is being accepted as a visitor to this element
     */
    @Override
    public void acceptVisitor(iVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Printable JSON-like view of this object and its contents
     * @return String   Text containing all parts of the object information available
     */
    public String toString()
    {
        return "SHOWING PERMISSION \n{\n " +
                "\t Id = " + super.getId() + "\n" +
                "\t Name = " + super.getName() + "\n" +
                "\t Description = " + super.getDescription() + "\n" +
                "}\n\n";
    }
}
