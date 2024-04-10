package com.rossijr.remoteauth.authentication;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utils class
 * <p>
 * Responsible for providing utility methods
 */
public class Utils {

    /**
     * Hash a string using SHA-256
     * @param string String to be hashed
     * @return Hashed string
     */
    public static String hashString(String string){
        try {
            // Configure the hash algorithm to be used
            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            // Hash the string with UTF-8 encoding
            byte[] messageDigest = algorithm.digest(string.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                // Convert the byte to hexadecimal to string | 0xff & b -> mask to obtain only the 8 most significant bits
                String hex = Integer.toHexString(0xff & b);
                // If the length hex is equal to 1, append 0 to the string
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("RemoteAuth --/ERROR/-- Error during hashing - class {" + Utils.class.getName() + "}");
            return null;

            }
    }
}
