package com.cscie97.store.authentication;

import java.util.Date;

/**
 * Visitor pattern implementation of the concrete Visitor class that implements the iVisitor interface. This class
 * implements the overloaded visit method for visiting AccessToken object. Use doesUserHaveAccess method to get the
 * result.
 */
public class CheckAccessVisitor implements iVisitor{
    /**
     * A true or false value representing whether the user has access
     */
    private Boolean userHasAccess = false;

    /**
     * The access token to check
     */
    private AccessToken accessToken;

    /**
     * The permission to check
     */
    private Permission permission;

    /**
     * Constructor for CheckAccessVisitor
     * @param accessToken   AccessToken The access token to check access in this visitor
     * @param permission    Permission  The permission to verify the access token enables
     */
    public CheckAccessVisitor(AccessToken accessToken, Permission permission)
    {
        this.accessToken = accessToken;
        this.permission = permission;
    }

    /**
     * Method called to obtain conclusion of user access information
     * @return  Boolean A true or false value representing whether the user has access
     */
    public Boolean doesUserHaveAccess()
    {
        return this.userHasAccess;
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

    }

    /**
     * Overloaded method that can visit Role objects to perform custom actions.
     * @param role  Role    The role object
     */
    @Override
    public void visit(Role role) {

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
//        System.out.println("this.accessToken.getToken().equals(accessToken.getToken() : " + this.accessToken.getToken().equals(accessToken.getToken()));
//        System.out.println("accessToken.getTokenState() == AccessTokenStateEnum.ACTIVE : " + (accessToken.getTokenState() == AccessTokenStateEnum.ACTIVE));
//        System.out.println("accessToken.getExpiration().compareTo(new Date()) > 0 : " + (accessToken.getExpiration().compareTo(new Date()) > 0));
//        System.out.println("accessToken.verifyUserPermission(this.permission) != null : " + (accessToken.verifyUserPermission(this.permission) != null));
//        System.out.println("accessToken.toString : " + (accessToken.toString()));
//        System.out.println("this.permission : " + this.permission);
        if(this.accessToken.getToken().equals(accessToken.getToken()) &&
                accessToken.getTokenState() == AccessTokenStateEnum.ACTIVE &&
                accessToken.getExpiration().compareTo(new Date()) > 0 &&
                accessToken.verifyUserPermission(this.permission) != null)
        {
            userHasAccess = true;
            System.out.println("VERIFIED THAT THE TOKEN " + accessToken.getToken() + " ENABLES THE PERMISSION " + this.permission.getId());
        }
    }

    /**
     * Overloaded method that can visit AuthenticationService objects to perform custom actions.
     * @param authenticationService AuthenticationService   The authentication service object
     */
    @Override
    public void visit(AuthenticationService authenticationService) {

    }
}
