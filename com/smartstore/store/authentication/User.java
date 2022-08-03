package com.cscie97.store.authentication;

import java.util.HashMap;

/**
 * This class represents each user. It implements the iVisitableElement interface that enables the Visitor pattern
 * concrete visitors to visit objects of this class to perform custom actions.
 */
public class User implements iVisitableElement{
    /**
     * The user's ID
     */
    private String userId;

    /**
     * The user's name
     */
    private String name;

    /**
     * Mapping of the credential strings to the Credential objects
     */
    private HashMap<String, Credential> credentials;

    /**
     * Mapping of the entitlement IDs to the Entitlement objects
     */
    private HashMap<String, Entitlement> entitlements;

    /**
     * Mapping of the access token ID to the AccessToken objects associated with this user
     */
    private HashMap<String, AccessToken> currentAccessTokens;

    /**
     * Constructor for the User class
     * @param userId    String  The user's ID
     * @param name  String  The user's name
     */
    public User(String userId, String name)
    {
        this.userId = userId;
        this.name = name;
        this.credentials = new HashMap<String, Credential>();
        this.entitlements = new HashMap<String, Entitlement>();
    }

    /**
     * Getter for the user's ID
     * @return  String The user's ID
     */
    public String getUserId()
    {
        return this.userId;
    }

    /**
     * Getter for the user's name
     * @return  String  The user's name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Getter for all credentials associated with this user
     * @return  HashMap Mapping of the credential strings to the Credential objects
     */
    public HashMap<String, Credential> getCredentials()
    {
        return  this.credentials;
    }

    /**
     * Method to add one credential at a time to this user
     * @param credential    Credential  The Credential object to associate with this user
     */
    public void addCredential(Credential credential)
    {
        this.credentials.put(credential.getID(), credential);
    }

    /**
     * Getter for all entitlements associated with this user
     * @return  HashMap Mapping of the entitlement IDs to the Entitlement objects
     */
    public HashMap<String, Entitlement> getEntitlements()
    {
        return this.entitlements;
    }

    /**
     * Method to add one entitlement at a time to this user
     * @param entitlement   Entitlement The Entitlement object to associate with this user
     */
    public void addEntitlement(Entitlement entitlement)
    {
        this.entitlements.put(entitlement.getId(), entitlement);
    }

    /**
     * Check whether the given credential string is a valid one for this user
     * @param credentialString  String  The credential string to check
     * @return  Boolean True to confirm that the credential string is a match, and false if it is not a match
     */
    public Boolean verifyCredentialString(String credentialString)
    {
        final Boolean[] verified = {false};
        this.credentials.forEach((key, value) ->
        {
            if(value.isCredentialStringMatching(credentialString))
            {
                verified[0] = true;
            }
        });
        return verified[0];
    }

    /**
     * Check whether this user has the given permission
     * @param permission    Permission  The permission to check whether this user has
     * @return  Permission  The confirmed permission or null if the User does not have the given permission
     */
    public Permission verifyPermission(Permission permission)
    {
        CheckPermissionVisitor checkPermissionVisitor = new CheckPermissionVisitor(permission);

        this.entitlements.forEach((key, value) -> {
            value.acceptVisitor(checkPermissionVisitor);
        });

        return checkPermissionVisitor.getResult();
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
        return "SHOWING USER \n{\n " +
                "\t Id = " + this.userId + "\n" +
                "\t Name = " + this.name + "\n" +
                "\t Credentials Count = " + this.credentials.size() + "\n" +
                "\t Entitlements Count = " + this.entitlements.size() + "\n" +
                "}\n\n";
    }
}
