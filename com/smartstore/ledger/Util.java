package com.cscie97.ledger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class with common utilities
 */
public class Util
{
    /**
     * Method to get a hash value.
     * Inspiration from https://www.quickprogrammingtips.com/java/how-to-generate-sha256-hash-in-java.html
     * @param stringToHash  String to hash
     * @return String   Hash of string passed in also represented as a string.
     */
    public static String sha256(String stringToHash)
    {
        MessageDigest messageDigest = null;

        try
        {
            messageDigest = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        messageDigest.update(stringToHash.getBytes());
        String stringHash = new String(messageDigest.digest());
        return stringHash;
    }
}