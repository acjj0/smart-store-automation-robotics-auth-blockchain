package com.cscie97.store.authentication;

import java.util.Locale;

/**
 * Visitor pattern behavior implementation of the iVisitor interface.
 */
public class InventoryVisitor implements iVisitor{
    /**
     * Constructor of this class
     */
    public InventoryVisitor() {}

    /**
     * Overloaded method that can visit Resource objects to perform custom actions.
     * @param resource  Resource    The resource object
     */
    @Override
    public void visit(Resource resource) {
        System.out.println(resource);
    }

    /**
     * Overloaded method that can visit Permission objects to perform custom actions.
     * @param permission    Permission  The permission object
     */
    @Override
    public void visit(Permission permission) {
        System.out.println(permission);
    }

    /**
     * Overloaded method that can visit Role objects to perform custom actions.
     * @param role  Role    The role object
     */
    @Override
    public void visit(Role role) {
        System.out.println(role);

        // If role is a ResourceRole, visit the Resource
        if(role instanceof ResourceRole)
        {
            System.out.println("RESOURCES OF " + role.getId());
            ((ResourceRole) role).getResource().acceptVisitor(this);
        }

        // Visit each child of this role
        role.getAllEntitlements().forEach((key, value) -> {
            System.out.println(value.getClass().getSimpleName().toLowerCase(Locale.ROOT) + " OF " + role.getId());
            value.acceptVisitor(this);
        });
    }

    /**
     * Overloaded method that can visit User objects to perform custom actions.
     * @param user  User    The user object
     */
    @Override
    public void visit(User user) {
        System.out.println(user);

        user.getCredentials().forEach((key, value) -> {
            System.out.println("CREDENTIAL OF " + user.getUserId());
            value.acceptVisitor(this);
        });
    }

    /**
     * Overloaded method that can visit Credential objects to perform custom actions.
     * @param credential    Credential  The credential object
     */
    @Override
    public void visit(Credential credential) {
        System.out.println(credential);
    }

    /**
     * Overloaded method that can visit AccessToken objects to perform custom actions.
     * @param accessToken   AccessToken The access token object
     */
    @Override
    public void visit(AccessToken accessToken) {
        System.out.println(accessToken);
    }

    /**
     * Overloaded method that can visit AuthenticationService objects to perform custom actions.
     * @param authenticationService AuthenticationService   The authentication service object
     */
    @Override
    public void visit(AuthenticationService authenticationService) {
        System.out.println("USERS AND THEIR CREDENTIALS");
        authenticationService.getUsers().forEach((key, value) -> {
            value.acceptVisitor(this);
        });

        System.out.println("ACCESS TOKENS");
        authenticationService.getTokens().forEach((key, value) -> {
            value.acceptVisitor(this);
        });
    }
}
