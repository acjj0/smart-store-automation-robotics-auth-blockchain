package com.cscie97.store.authentication;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * AccessTokens or Auth tokens have a unique id, an expiration time, and a state (active or expired). These tokens are
 * associated with a User and a set of Permissions
 */
public class AccessToken implements iVisitableElement{
    /**
     * The unique tokenId for each AccessToken
     */
    private String tokenId;

    /**
     * The token itself which is again unique
     */
    private String token;

    /**
     * The expiration date and time for this token's validity
     */
    private Date expiration;

    /**
     * The state of the token whether active or inactive
     */
    private AccessTokenStateEnum tokenState;

    /**
     * The user with whom this token is associated. These tokens cannot be transferred between users.
     */
    private User user;

    /**
     * The entitlements at any point in time that are currently granted to the user who holds these tokens
     */
    private HashMap<String, Entitlement> entitlements;

    /**
     * Static value to be able to easily change the expiration duration
     */
    private static final int EXPIRATION_MINUTES = 10;

    /**
     * The Constructor which takes in only the User object and does all the rest
     * @param user  User    The object representing the user.
     */
    public AccessToken(User user)
    {
        this.tokenId = UUID.randomUUID().toString();
        this.token = UUID.randomUUID().toString();
        this.expiration = Date.from(new Date().toInstant().plus(Duration.ofMinutes(EXPIRATION_MINUTES)));
        this.user = user;
        this.entitlements = user.getEntitlements();
    }

    /**
     * Getter for the token Id
     * @return  String  The token Id
     */
    public String getTokenId()
    {
        return this.tokenId;
    }

    /**
     * Getter for the AccessToken object
     * @return  String  The token itself
     */
    public String getToken()
    {
        return this.token;
    }

    /**
     * Getter to retrive the user holding this token
     * @return  User    The object representing the User who holds this token
     */
    public User getUser()
    {
        return this.user;
    }

    /**
     * Getter for the expiration date and time of this token.
     * @return  Date    The object representing the date and time of expiration of this token
     */
    public Date getExpiration()
    {
        return this.expiration;
    }

    /**
     * Getter for the current state of the token, whether active or inactive
     * @return  AccessTokenStateEnum    A value that's either active or inactive.
     */
    public AccessTokenStateEnum getTokenState() {
        return this.tokenState;
    }

    /**
     * Setter to change the state of the token between active and inactive.
     * @param tokenState    AccessTokenStateEnum    A value that's either active or inactive.
     */
    public void setTokenState(AccessTokenStateEnum tokenState) {
        this.tokenState = tokenState;
    }

    /**
     * Method to verify user permissions using the Visitor pattern
     * @param permission    Permission  The kind of access available
     * @return  Permission  The permissions verified this
     */
    public Permission verifyUserPermission(Permission permission)
    {
        CheckPermissionVisitor checkPermissionVisitor = new CheckPermissionVisitor(permission);

        this.entitlements.forEach((key, value) -> {
            value.acceptVisitor(checkPermissionVisitor);
        });

        return checkPermissionVisitor.getResult();
    }

    /**
     * Visitor pattern implementation of the Element
     * @param visitor   iVisitor    A class can signals that it can be visited by the Visitors
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
        return "SHOWING ACCESS TOKEN \n{\n " +
                "\t Token Id = " + this.tokenId + "\n" +
                "\t Token = " + this.token + "\n" +
                "\t Expiration = " + this.expiration + "\n" +
                "\t Token State = " + this.tokenState + "\n" +
                "\t User = " + this.user.getUserId() + "\n" +
                "\t Entitlement Count = " + this.entitlements.size() + "\n" +
                "}\n\n";
    }
}
