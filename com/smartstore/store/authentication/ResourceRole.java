package com.cscie97.store.authentication;

/**
 * ResourceRole is a concrete class that extends the Role and represents each ResourceRole in the authentication
 * service.
 */
public class ResourceRole extends Role implements iVisitableElement{
    /**
     *  Each role that needs to be associated with a resource
     */
    private Role role;

    /**
     * Each resource that needs to be associated with a role
     */
    private Resource resource;

    /**
     * Constructor for the ResourceRole objects
     * @param id    String  Id of the permissions
     * @param name  String  Name of the permission
     * @param description   String  Description of the permission
     * @param role  Role    The role associated with this ResourceRole
     * @param resource  Resource    The resource associated with the ResourceRole
     */
    public ResourceRole(String id, String name, String description, Role role, Resource resource)
    {
        super(id, name, description);
        this.role = role;
        this.resource = resource;
    }

    /**
     * Getter for the role associated with this ResourceRole
     * @return  Role    The role associated with the ResourceRole
     */
    public Role getRole()
    {
        return this.role;
    }

    /**
     * Getter for the resource associated with this ResourceRole
     * @return  Role    The resource associated with the ResourceRole
     */
    public Resource getResource()
    {
        return this.resource;
    }

    /**
     * Printable JSON-like view of this object and its contents
     * @return String   Text containing all parts of the object information available
     */
    public String toString()
    {
        return "SHOWING RESOURCE ROLE \n{\n " +
                "\t Id = " + super.getId() + "\n" +
                "\t Name = " + super.getName() + "\n" +
                "\t Description = " + super.getDescription() + "\n" +
                "}\n\n";
    }
}
