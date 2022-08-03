package com.cscie97.store.authentication;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Authentication Service supports controlling access to the Store 24X7 application and the IoT devices. It implements
 * the iVisitableElement interface to allow Visitors to perform behaviors such as printing inventory of permission tree.
 */
public class AuthenticationService implements iVisitableElement{
    /**
     * Map to be able to locate any permission, given its Id
     * key = permission Id, value = Permission object
     */
    private HashMap<String, Permission> permissions;

    /**
     * Map to be able to locate any role, given its Id
     * key = role Id, value = Role object
     */
    private HashMap<String, Role> roles;

    /**
     * Map to be able to locate any resource role, given its Id
     * key = resource role Id, value = Resource object
     */
    private HashMap<String, ResourceRole> resourceRoles;

    /**
     * Map to be able to locate any user, given their Id
     * key = user Id, value = User object
     */
    private HashMap<String, User> users;

    /**
     * Map to be able to locate any resource, given its Id
     * key = resource Id, value = Resource object
     */
    private HashMap<String, Resource> resources;

    /**
     * Map to be able to locate any access token, given its Id
     * key = access token Id, value = AccessToken object
     */
    private HashMap<String, AccessToken> tokens;

    /**
     * Root permission which will be at the root of the entitlement hierarchy
     */
    private Permission rootPermission;

    /**
     * Permission to create, delete, update entitlements with roles and permissions
     */
    private Permission entitlementAdminPermission;

    /**
     * Top level root role
     */
    private Role rootRole;

    /**
     * The authentication system root user with all permissions obtained with top level rootPermission Entitlement
     */
    private User rootUser;

    /**
     * The automated system, such as Store Model Service, as a user too
     */
    private User storeSystemAutomatedUser;

    /**
     * Constant value for default root password, to be changed in a functioning system to obtain from other sources.
     * Passwords for systems in production must not be stored in the code repository.
     */
    final static private String ROOT_PASSWORD_STRING = "default";

    /**
     * Constant value for automated system password, to be changed in a functioning system to obtain from other sources.
     * Passwords for systems in production must not be stored in the code repository.
     */
    final static private String STORE_SYSTEM_PASSWORD_STRING = "automated-system-password";

    /**
     * Constant value for default user password, to be changed in a functioning system to obtain from other sources.
     * Passwords for systems in production must not be stored in the code repository.
     */
    final static private String DEFAULT_USER_PASSWORD_STRING = "user-password";

    /**
     * For Singleton pattern - Create only 1 instance of this AuthenticationService.
     */
    public static AuthenticationService singletonInstance;

    /**
     * Static block to initialize a Singleton instance. Lazy initialization: Object is created only if it is needed.
     * This may prevent resource wastage. Use the getInstance method to get an instance of the Authentication service.
     * @return  AuthenticationService   A Singleton object of type AuthenticationService
     */
    public static AuthenticationService getInstance() {
        if(singletonInstance == null)
        {
            singletonInstance = new AuthenticationService();
        }
        return singletonInstance;
    }

    /**
     * Private constructor for AuthenticationService so that it can only be instantiated as part of the Singleton
     * pattern getInstance method.
     */
    private AuthenticationService(){
        this.permissions = new HashMap<String, Permission>();
        this.roles = new HashMap<String, Role>();
        this.resourceRoles = new HashMap<String, ResourceRole>();
        this.users = new HashMap<String, User>();
        this.resources = new HashMap<String, Resource>();
        this.tokens = new HashMap<String, AccessToken>();

        // Define permission to create, delete, update entitlements with roles and permissions
        this.entitlementAdminPermission = new Permission("entitlement_admin_permission",
                "Entitlement Admin Permission","Create, Update, Delete Entitlements");
        this.permissions.put(this.entitlementAdminPermission.getId(), this.entitlementAdminPermission);
        System.out.println("DEFINED ENTITLEMENT ADMIN PERMISSION " + this.entitlementAdminPermission);

        // Define root role
        this.rootRole = new Role("entitlement_admin_role", "Authentication Admin Role", "Root Role");
        this.roles.put(rootRole.getId(), rootRole);
        System.out.println("DEFINED ADMIN OR ROOT ROLE " + rootRole);

        // Assign permissions to the root role to be both root and entitlement admin
        this.rootRole.addEntitlement(entitlementAdminPermission);
        System.out.println("ASSIGNED ROOT ROLE AND THE ENTITLEMENT ADMIN PERMISSIONS");

        // Define root user, with password credentials
        this.rootUser = new User("root", "Root User");
        this.users.put(this.rootUser.getUserId(), this.rootUser);
        this.rootUser.addCredential(new Credential(UUID.randomUUID().toString(), ROOT_PASSWORD_STRING,
                CredentialTypeEnum.PASSWORD));
        this.rootUser.addEntitlement(this.rootRole);
        System.out.println("DEFINED ROOT USER WITH PASSWORD CREDENTIALS AND ROOT ROLE ENTITLEMENT");
    }

    /**
     * Getter for all permissions in this authentication service
     * @return  HashMap Mapping of permission names to Permission objects
     */
    public HashMap<String, Permission> getPermissions() { return this.permissions; }

    /**
     * Getter method for get the list of all users for whom AuthenticationService accounts have been created
     * @return  Hashmap Mapping of userIds to User objects
     */
    public HashMap<String, User> getUsers()
    {
        return this.users;
    }

    /**
     * Getter method for get the list of all tokens in AuthenticationService
     * @return  Hashmap Mapping of tokenIds to AccessToken objects
     */
    public HashMap<String, AccessToken> getTokens()
    {
        return this.tokens;
    }


    /**
     * Verify that the AccessToken provided has access to the Permission whose id is also provided. Visitor pattern
     * implementation of CheckAccessVisitor is utilized to perform this verification.
     * @param accessToken   AccessToken The access token which is to be verified
     * @param permission    Permission  The permission to which this method will verify the token has access
     * @throws InvalidAccessTokenException  Exception   Thrown when there is any issue with the AccessToken
     */
    public void checkAccess(AccessToken accessToken, Permission permission) throws InvalidAccessTokenException
    {
        CheckAccessVisitor checkAccessVisitor = new CheckAccessVisitor(accessToken, permission);

        this.tokens.forEach((key, value) -> {
            value.acceptVisitor(checkAccessVisitor);
        });

        if(checkAccessVisitor.doesUserHaveAccess() == false)
        {
            throw new InvalidAccessTokenException("checkAccess method in AuthenticationService failed",
                    "Issue with Access Token",
                    accessToken);
        }
        System.out.println("ACCESS VERIFIED" + "\n");
    }


    /**
     * Define a new Permission object using the permission id, name and description provided
     * @param permissionid  String  The permission Id provided
     * @param name  String  The name provided for this permission
     * @param description   String  The description provided for this permission
     * @param token AccessToken The access token to verify that this token is eligible to create new permissions
     * @throws AuthenticationException  Exception   Thrown when there is any issue with the AccessToken
     */
    public void definePermission(String permissionid, String name, String description, AccessToken token)
            throws AuthenticationException {
        try
        {
            if(token.getUser().getUserId().equals("root"))
            {
                this.checkAccess(token, this.entitlementAdminPermission);
            }
            else
            {
                this.checkAccess(token, this.permissions.get("create_entitlement"));
            }
        }
        catch (InvalidAccessTokenException e)
        {
            throw new AuthenticationException("definePermission method in AuthenticationService",
                    "Invalid Access Token", token.getTokenId());
        }

        if(this.permissions.get(permissionid) != null)
        {
            throw new AuthenticationException("definePermission method in AuthenticationService",
                    "Permission with this ID already exists", permissionid);
        }

        if(this.roles.get(permissionid) != null)
        {
            throw new AuthenticationException("definePermission method in AuthenticationService",
                    "Role with this ID already exists", permissionid);
        }

        Permission newPermission = new Permission(permissionid, name, description);
        this.permissions.put(newPermission.getId(), newPermission);

        // Add the new permission to the tree under rootRole
        this.rootRole.addEntitlement(newPermission);
        System.out.println("SUCCESS!!! NEW PERMISSION CREATED: " + newPermission + "\n\n");
    }

    /**
     * Define a new Role object.
     * @param roleId    String  The role Id to use for the Role object
     * @param name  String  The name to use for the Role object
     * @param description   String  The description to use for the Role object
     * @param token AccessToken The access token to verify eligibility to define a new Role
     * @throws AuthenticationException  Thrown if the token is invalid, or if a Role with the provided Id already exists
     */
    public void defineRole(String roleId, String name, String description, AccessToken token)
            throws AuthenticationException {
        try
        {
            if(token.getUser().getUserId().equals("root"))
            {
                this.checkAccess(token, this.entitlementAdminPermission);
            }
            else
            {
                this.checkAccess(token, this.permissions.get("create_entitlement"));
            }
        }
        catch (InvalidAccessTokenException e)
        {
            throw new AuthenticationException("defineRole method in AuthenticationService",
                    "Invalid Access Token", token.getTokenId());
        }

        if(this.roles.get(roleId) != null)
        {
            throw new AuthenticationException("defineRole method in AuthenticationService",
                    "Role with this ID already exists", roleId);
        }

        if(this.permissions.get(roleId) != null)
        {
            throw new AuthenticationException("defineRole method in AuthenticationService",
                    "Permission with this ID already exists", roleId);
        }

        Role newRole = new Role(roleId, name, description);
        this.roles.put(newRole.getId(), newRole);

        this.rootRole.addEntitlement(newRole);
        System.out.println("SUCCESS!!! NEW ROLE CREATED: " + newRole);
    }

    /**
     * Add permission to a role
     * @param roleId    String  The role Id provided
     * @param permissionId  String The permission Id provided
     * @param token AccessToken The access token to verify eligibility to perform this action
     * @throws AuthenticationException  Thrown if the access token is invalid or if the role or permission don't exist.
     */
    public void addPermissionToRole(String roleId, String permissionId, AccessToken token)
            throws AuthenticationException {
        try
        {
            if(token.getUser().getUserId().equals("root"))
            {
                this.checkAccess(token, this.entitlementAdminPermission);
            }
            else
            {
                this.checkAccess(token, this.permissions.get("update_entitlement"));
            }
        }
        catch (InvalidAccessTokenException e)
        {
            throw new AuthenticationException("addPermissionToRole method in AuthenticationService",
                    "Invalid Access Token", token.getTokenId());
        }

        // Make sure that the role and the permission already exist
        Role role = this.roles.get(roleId);
        if(role == null)
        {
            throw new AuthenticationException("addPermissionToRole method in AuthenticationService",
                    "Role does not exist", roleId);
        }

        Permission permission = this.permissions.get(permissionId);
        if(permission == null)
        {
            throw new AuthenticationException("addPermissionToRole method in AuthenticationService",
                    "Permission does not exist", permissionId);
        }

        role.addEntitlement(permission);
        System.out.println("SUCCESS!!! PERMISSION " + permissionId + " ADDED TO ROLE " + roleId + "\n");
        System.out.println(role);
    }

    /**
     * Define a new User
     * @param userId    String  The user Id to use
     * @param name  String  The name to use for the user
     * @param token Access Token    The access token to verify eligibility to perform this action
     * @throws AuthenticationException  Thrown if the access token is invalid or if the user Id already exists
     */
    public void createUser(String userId, String name, AccessToken token) throws AuthenticationException {
        try
        {
            if(token.getUser().getUserId().equals("root"))
            {
                this.checkAccess(token, this.entitlementAdminPermission);
            }
            else
            {
                this.checkAccess(token, this.permissions.get("create_user"));
            }
        }
        catch (InvalidAccessTokenException e)
        {
            throw new AuthenticationException("createUser method in AuthenticationService", "Invalid Access Token", token.getTokenId());
        }

        if(this.users.get(userId) != null)
        {
            throw new AuthenticationException("createUser method in AuthenticationService", "User Id Already Exists", userId);
        }

        User newUser = new User(userId, name);
        this.users.put(newUser.getUserId(), newUser);
        System.out.println("SUCCESS!!! NEW USER CREATED: " + newUser + "\n\n");
    }

    /**
     * Add credential for the given user
     * @param userId    String  The user Id to use
     * @param credentialValue   String  The credential value such as password or biometrics
     * @param credentialType    CredentialTypeEnum  Can be of types Password, Face print or Voice print
     * @param token AccessToken The access token to verify eligibility to perform this action
     * @throws AuthenticationException  Thrown when the token is invalid or if there is not such user
     */
    public void addUserCredential(String userId, String credentialValue, CredentialTypeEnum credentialType,
                                  AccessToken token) throws AuthenticationException {
        try
        {
            if(token.getUser().getUserId().equals("root"))
            {
                this.checkAccess(token, this.entitlementAdminPermission);
            }
            else
            {
                this.checkAccess(token, this.permissions.get("update_user"));
            }
        }
        catch (InvalidAccessTokenException e)
        {
            throw new AuthenticationException("addUserCredential method in AuthenticationService",
                    "Invalid Access Token", token.getTokenId());
        }

        User user = this.users.get(userId);
        if(user == null)
        {
            throw new AuthenticationException("addUserCredential method in AuthenticationService", "No such user", userId);
        }

        user.addCredential(new Credential(UUID.randomUUID().toString(), credentialValue, credentialType));

        System.out.println("SUCCESS!!! CREDENTIAL " + credentialValue + " ADDED TO USER " + userId + "\n\n");
    }

    /**
     * Method to add a given role to a given user
     * @param userId    String  The user Id to use
     * @param roleId    String  The role Id to use
     * @param token AccessToken The access token to verify eligibility to perform this action
     * @throws AuthenticationException  Thrown if the access token is invalid or if there is no such role or user
     */
    public void addRoleToUser(String userId, String roleId, AccessToken token) throws AuthenticationException {
        try
        {
            if(token.getUser().getUserId().equals("root"))
            {
                this.checkAccess(token, this.entitlementAdminPermission);
            }
            else
            {
                this.checkAccess(token, this.permissions.get("update_user"));
            }
        }
        catch (InvalidAccessTokenException e)
        {
            throw new AuthenticationException("addRoleToUser method in AuthenticationService", "Invalid Access Token",
                    token.getTokenId());
        }

        User user = this.users.get(userId);
        if(user == null)
        {
            throw new AuthenticationException("addRoleToUser method in AuthenticationService", "No such user", userId);
        }

        Role role = this.roles.get(roleId);
        if(role == null)
        {
            throw new AuthenticationException("addRoleToUser method in AuthenticationService", "No such role", roleId);
        }

        user.addEntitlement(role);

        System.out.println("SUCCESS!!! ENTITLEMENT ROLE " + roleId + " ADDED TO USER " + userId + "\n\n");
    }

    /**
     * Method to print an inventory of all authentication service objects
     * @param token AccessToken The access token to verify whether the requester has permissions for this method
     * @throws AuthenticationException  Thrown if the access token is invalid or if there is no such role or user
     */
    public void printInventory(AccessToken token) throws AuthenticationException {
        try
        {
            if(token.getUser().getUserId().equals("root"))
            {
                this.checkAccess(token, this.entitlementAdminPermission);
            }
            else
            {
                this.checkAccess(token, this.permissions.get("read_entitlement"));
            }
        }
        catch (InvalidAccessTokenException e)
        {
            throw new AuthenticationException("addRoleToUser method in AuthenticationService", "Invalid Access Token",
                    token.getTokenId());
        }

        InventoryVisitor inventoryVisitor = new InventoryVisitor();
        this.rootRole.acceptVisitor(inventoryVisitor);
        this.acceptVisitor(inventoryVisitor);
    }

    /**
     * Method to define a new ResourceRole
     * @param resourceRoleId    String  ID for Resource Role
     * @param roleId    String  The role ID to use
     * @param resourceId    String  The resource ID to use
     * @param token AccessToken The access token to verify eligibility to perform this action
     * @throws AuthenticationException  Thrown if the access token is invalid or if there is no such resource or role
     */
    public void createResourceRole(String resourceRoleId, String roleId, String resourceId, AccessToken token) throws AuthenticationException {
        try
        {
            if(token.getUser().getUserId().equals("root"))
            {
                this.checkAccess(token, this.entitlementAdminPermission);
            }
            else
            {
                this.checkAccess(token, this.permissions.get("create_resource"));
            }
        }
        catch (InvalidAccessTokenException e)
        {
            throw new AuthenticationException("createResourceRole method in AuthenticationService", "Invalid Access Token",
                    token.getTokenId());
        }

        Resource resource = this.resources.get(resourceId);
        if(resource == null)
        {
            resource = this.defineResource(resourceId, resourceId, token);
        }

        Role role = roles.get(roleId);
        if(role == null)
        {
            throw new AuthenticationException("createResourceRole method in AuthenticationService", "No such role", roleId);
        }

        ResourceRole resourceRole = new ResourceRole(resourceRoleId, resourceRoleId, resourceRoleId, role, resource);
        this.resourceRoles.put(resourceRoleId, resourceRole);

        role.addEntitlement(resourceRole);

        System.out.println("SUCCESS!!! CREATED NEW RESOURCE ROLE " + resourceRole.getId() + " FOR ROLE " + role.getId() +
                " TO ACCESS " + resource.getId() + "\n\n");
        System.out.println(resourceRole);
    }

    /**
     * Method to add ResourceRole to a given User
     * @param userId    String  The user Id to use
     * @param resourceRoleId    String  The resource role Id to use
     * @param token AccessToken The access token to verify eligibility to perform this action
     * @throws AuthenticationException  Thrown if the access token is invalid or if there is no such user
     */
    public void addResourceRoleToUser(String userId, String resourceRoleId, AccessToken token) throws AuthenticationException {
        try
        {
            if(token.getUser().getUserId().equals("root"))
            {
                this.checkAccess(token, this.entitlementAdminPermission);
            }
            else
            {
                this.checkAccess(token, this.permissions.get("update_user"));
            }
        }
        catch (InvalidAccessTokenException e)
        {
            throw new AuthenticationException("addResourceRoleToUser method in AuthenticationService", "Invalid Access Token",
                    token.getTokenId());
        }

        User user = this.users.get(userId);
        if(user == null)
        {
            throw new AuthenticationException("addResourceRoleToUser method in AuthenticationService", "No such user", userId);
        }

        ResourceRole resourceRole = this.resourceRoles.get(resourceRoleId);
        if(resourceRole == null)
        {
            throw new AuthenticationException("addResourceRoleToUser method in AuthenticationService", "No such resource role", resourceRoleId);
        }

        user.addEntitlement(resourceRole);

        System.out.println("SUCCESS!!! RESOURCE ROLE " + resourceRoleId + " ADDED TO USER " + userId + "\n\n");
    }

    /**
     * Define a resource to which user or systems need access
     * @param id    String  The resource Id to use
     * @param description   String  The description to use
     * @param token AccessToken The access token to verify eligibility to perform this action
     * @return  Resource    Resource created using the parameters passed
     * @throws AuthenticationException   Thrown if the access token is invalid or if such a Resource already exists
     */
    public Resource defineResource(String id, String description, AccessToken token) throws AuthenticationException {
        try
        {
            if(token.getUser().getUserId().equals("root"))
            {
                this.checkAccess(token, this.entitlementAdminPermission);
            }
            else
            {
                this.checkAccess(token, this.permissions.get("create_resource"));
            }
        }
        catch (InvalidAccessTokenException e)
        {
            throw new AuthenticationException("defineResource method in AuthenticationService", "Invalid Access Token",
                    token.getTokenId());
        }

        if(resources.get(id) != null)
        {
            throw new AuthenticationException("defineResource method in AuthenticationService", "Resource already defined", id);
        }

        Resource resource = new Resource(id, description);
        this.resources.put(resource.getId(), resource);
        System.out.printf("SUCCESS!!! DEFINED RESOURCE: " + resource);

        return resource;
    }

    /**
     * Log in a user after verifying that such a user exists, verify their credential, and generate access token
     * @param userId    String  The user's Id
     * @param credentialString  String  The string such as a password used to authenticate the user
     * @param credentialType    CredentialTypeEnum  The type of credential such as password, faceprint, voiceprint
     * @return  AccessToken The object containing the AccessToken to verify access to the particular resource
     * @throws AuthenticationException  Exception thrown if the user can't be found.
     */
    public AccessToken login(String userId, String credentialString, CredentialTypeEnum credentialType)
            throws AuthenticationException
    {
        User user = this.users.get(userId);

        // No such user
        if(user == null)
        {
            throw new AuthenticationException("login()", "No such user", userId);
        }

        // If such a user exists, verify their credential and generate accessToken
        AccessToken accessToken = null;
        if(user.verifyCredentialString(credentialString))
        {
            accessToken = new AccessToken(user);
            accessToken.setTokenState(AccessTokenStateEnum.ACTIVE);
            this.tokens.put(accessToken.getTokenId(), accessToken);
            System.out.println("ACCESS TOKEN CREATED: " + accessToken.getTokenId());
        }
        else
        {
            throw new AuthenticationException("login()", "Password incorrect", credentialString);
        }

        System.out.println("SUCCESS!!! LOGGED IN USER " + userId + " WITH " + credentialType + " CREDENTIAL " + credentialString);
        System.out.println("ACCESS TOKEN CREATED: " + accessToken.getTokenId());

        return accessToken;
    }

    /**
     * This overlaoded login method is called for biometric identification when faceprint or voiceprint are used as
     * credentials to login. This method calls the more generic login method after identifying the user from biometrics.
     * @param credentialString  String  The faceprint or voiceprint information represented as a String
     * @param credentialType    CredentialTypeEnum  One of FACEPRINT or VOICEPRINT. Can be entered in any case
     * @return  AccessToken     The token derived from the call to the generic login method
     * @throws AuthenticationException  Exception thrown if the user cannot be logged in.
     */
    public AccessToken login(String credentialString, CredentialTypeEnum credentialType)
            throws AuthenticationException
    {
        User user = null;

        for (Map.Entry<String, User> entry : this.users.entrySet()) {
            User checkUser = entry.getValue();
            if (checkUser.verifyCredentialString(credentialString)) {
                user = checkUser;
            }
        }

        return login(user.getUserId(), credentialString, credentialType);
    }

    /**
     * Log out the user given their user Id. Logout marks the given Auth Token as invalid.
     * @param token AccessToken  User Id of the user to log out
     * @throws AuthenticationException  Exception thrown if the user cannot be found
     */
    public void logout(AccessToken token) throws AuthenticationException {
        if(token == null)
        {
            throw new AuthenticationException("logout", "No user is logged in", null);
        }
        User user = token.getUser();

        // No such user
        if(user == null)
        {
            throw new AuthenticationException("logout()", "No such user", null);
        }

        // Logout marks the given Auth Token as invalid.
        this.tokens.forEach((key, value) -> {
            if(value.getUser().getUserId().equals(user.getUserId())) {
                value.setTokenState(AccessTokenStateEnum.INACTIVE);
            }
        });

        System.out.println("SUCCESS!!! LOGGED OUT USER " + user.getUserId());
    }

    /**
     * The method that accepts a visitor for this element
     * @param visitor   iVisitor    An iVisitor object that is being accepted as a visitor to this element
     */
    @Override
    public void acceptVisitor(iVisitor visitor) {
        visitor.visit(this);
    }
}
