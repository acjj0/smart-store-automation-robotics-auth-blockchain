package com.cscie97.store.authentication;

/**
 * Visitor pattern implementation of the concrete Visitor class that implements the iVisitor interface. This class
 * implements the overloaded visit methods for permission and role objects.
 */
public class CheckPermissionVisitor implements iVisitor{
    /**
     * The result of the permission check
     */
    private Permission result = null;

    /**
     * The permission Id to check
     */
    private String permissionIdToCheck;

    /**
     * Constructor for this concrete Visitor class
     * @param permission    Permission  The permission to check for the user
     */
    public CheckPermissionVisitor(Permission permission)
    {
        this.permissionIdToCheck = permission.getId();
    }

    /**
     * Getter for the result of verifying that the access token has permissions for the resource
     * @return  Permission  Permission object being checked by this visitor
     */
    public Permission getResult() {
        return this.result;
    }

    /**
     * Overloaded method that can visit Resource objects to perform custom actions.
     * @param resource  Resource    The resource object
     */
    @Override
    public void visit(Resource resource) {

    }

    /**
     * Overloaded method that can visit Permission objects to perform custom actions.
     * @param permission    Permission  The permission object
     */
    @Override
    public void visit(Permission permission) {
//        System.out.println("permission.getId().equals(this.permissionIdToCheck) : " + permission.getId().equals(this.permissionIdToCheck));
//        System.out.println("permission.getId() : " + permission.getId());
//        System.out.println("this.permissionIdToCheck : " + this.permissionIdToCheck);
        if(permission.getId().equals(this.permissionIdToCheck))
        {
            this.result = permission;
        }
    }

    /**
     * Overloaded method that can visit Role objects to perform custom actions.
     * @param role  Role    The role object
     */
    @Override
    public void visit(Role role) {
        role.getAllEntitlements().forEach((key, value) -> {
            value.acceptVisitor(this);
        });
    }

    /**
     * Overloaded method that can visit User objects to perform custom actions.
     * @param user  User    The user object
     */
    @Override
    public void visit(User user) {

    }

    /**
     * Overloaded method that can visit Credential objects to perform custom actions.
     * @param credential    Credential  The credential object
     */
    @Override
    public void visit(Credential credential) {

    }

    /**
     * Overloaded method that can visit AccessToken objects to perform custom actions.
     * @param accessToken   AccessToken The access token object
     */
    @Override
    public void visit(AccessToken accessToken) {

    }

    /**
     * Overloaded method that can visit AuthenticationService objects to perform custom actions.
     * @param authenticationService AuthenticationService   The authentication service object
     */
    @Override
    public void visit(AuthenticationService authenticationService) {

    }
}
