package com.cscie97.store.authentication;

/**
 * Interface for the Visitor pattern Visitor type. This method is implemented by all classes that are visitors and will
 * perform certain behaviors with the structure of the Elements.
 */
public interface iVisitor {
    /**
     * Overloaded method that can visit Resource objects to perform custom actions.
     * @param resource  Resource    The resource object
     */
    void visit(Resource resource);

    /**
     * Overloaded method that can visit Permission objects to perform custom actions.
     * @param permission    Permission  The permission object
     */
    void visit(Permission permission);

    /**
     * Overloaded method that can visit Role objects to perform custom actions.
     * @param role  Role    The role object
     */
    void visit(Role role);

    /**
     * Overloaded method that can visit User objects to perform custom actions.
     * @param user  User    The user object
     */
    void visit(User user);

    /**
     * Overloaded method that can visit Credential objects to perform custom actions.
     * @param credential    Credential  The credential object
     */
    void visit(Credential credential);

    /**
     * Overloaded method that can visit AccessToken objects to perform custom actions.
     * @param accessToken   AccessToken The access token object
     */
    void visit(AccessToken accessToken);

    /**
     * Overloaded method that can visit AuthenticationService objects to perform custom actions.
     * @param authenticationService AuthenticationService   The authentication service object
     */
    void visit(AuthenticationService authenticationService);
}
