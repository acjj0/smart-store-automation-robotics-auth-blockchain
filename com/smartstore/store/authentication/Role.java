package com.cscie97.store.authentication;

import java.util.HashMap;

/**
 * Role is a concrete class that extends the abstract class Entitlement and represents each role in the authentication
 * service.
 */
public class Role extends Entitlement{
    /**
     * The set of entitlements associated with the role, with role or permission IDs as the String type key values
     */
    private HashMap<String, Entitlement> childEntitlements;

    /**
     * Constructor for the Role objects
     * @param id    String  Id of the role
     * @param name  String  Name of the role
     * @param description   String  Description of the role
     */
    public Role(String id, String name, String description)
    {
        super(id, name, description);
        this.childEntitlements = new HashMap<String, Entitlement>();
    }

    /**
     * Getter for all entitlements associated with this role
     * @return  HashMap Mapping of role or permission IDs to Entitlement objects
     */
    public HashMap<String, Entitlement> getAllEntitlements()
    {
        return this.childEntitlements;
    }

    /**
     * Method to add entitlements to this role
     * @param entitlement   Entitlement The entitlement to add for this role.
     */
    public void addEntitlement(Entitlement entitlement)
    {
        this.childEntitlements.put(entitlement.getId(), entitlement);
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
        return "SHOWING ROLE \n{\n " +
                "\t Id = " + super.getId() + "\n" +
                "\t Name = " + super.getName() + "\n" +
                "\t Description = " + super.getDescription() + "\n" +
                "}\n\n";
    }
}
