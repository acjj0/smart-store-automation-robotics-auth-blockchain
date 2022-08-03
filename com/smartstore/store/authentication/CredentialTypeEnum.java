package com.cscie97.store.authentication;

/**
 * Enum for the Credential Type for passwords, or biometrics (faceprint or voiceprint)
 */
public enum CredentialTypeEnum {
    /**
     * For Voiceprint
     */
    VOICEPRINT,

    /**
     * For Faceprint
     */
    FACEPRINT,

    /**
     * For passwords
     */
    PASSWORD
}
