package com.cscie97.store.authentication;

/**
 * This class represents each credential. It implements the iVisitableElement interface that enables the Visitor pattern
 * concrete visitors to visit objects of this class to perform custom actions.
 */
public class Credential implements iVisitableElement{
    /**
     * Id for this credential
     */
    private String id;

    /**
     * The credential string which can be password, faceprint and voiceprint
     */
    private String credential;

    /**
     * The type of the credential whether password or biometrics (face or voice)
     */
    private CredentialTypeEnum credentialType;

    /**
     * Constructor for the Credential class
     * @param id    String  Id for this credential
     * @param credential    String  Credential string which can be password, faceprint and voiceprint
     * @param credentialType    CredentialTypeEnum  Type of the credential whether password or biometrics (face or voice)
     */
    public Credential(String id, String credential, CredentialTypeEnum credentialType)
    {
        this.id = id;
        this.credential = Util.sha256(credential);
        this.credentialType = credentialType;
    }

    /**
     * Getter for the Id of this credential
     * @return  String  ID for this credential
     */
    public String getID() {
        return this.id;
    }

    /**
     * Method to check if the provided credential matches the credential stored in this Credential object
     * @param credentialString  String  The credential string to check if there's a match
     * @return  Boolean True if there is a perfect match, false if not
     */
    public boolean isCredentialStringMatching(String credentialString) {
        return this.credential.equals(Util.sha256(credentialString));
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
    public String toString() {
        return "SHOWING CREDENTIAL \n{\n " +
                "\t Id = " + this.id + "\n" +
                "\t Credential = " + this.credential + "\n" +
                "\t Credential Type = " + this.credentialType + "\n" +
                "}\n\n";
    }
}
