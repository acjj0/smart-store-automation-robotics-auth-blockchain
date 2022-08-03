package com.cscie97.store.authentication;

/**
 * Resource is a concrete class that extends the abstract class Entitlement that represents each resource defined in
 * the authentication service.
 */
public class Resource implements iVisitableElement{
    /**
     * The Id of the resource
     */
    private String id;

    /**
     * The description of the resource
     */
    private String description;

    /**
     * Constructor for the Resource objects
     * @param id    String  Id of the permissions
     * @param description   String  Description of the permission
     */
    public Resource(String id, String description)
    {
        this.id = id;
        this.description = description;
    }

    /**
     * Getter for the ID
     * @return id    String  Id of the permissions
     */
    public String getId() { return this.id; }

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
    public String toString() {
        return "SHOWING RESOURCE \n{\n " +
                "\t Id = " + this.id + "\n" +
                "\t Description = " + this.description + "\n" +
                "}\n\n";
    }
}
