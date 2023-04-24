package com.hktransfield.enrollment;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Represents a password validtator
 *
 * This utilities class provides methods to enfore various NIST password requirements you might
 * want to implement in a password authentication based system.
 */
public class PasswordValidator {
    //declare constants
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 64;

    /**
     * Ensures that the prospect password does not match a prospect username
     *
     * @param username the username the user wants
     * @param password the password the user wants
     * @return true if the password matches the username
     */
    public static boolean matchesUsername(String username, char[] password) {
        return Arrays.equals(username.toCharArray(), password);
    }

    /**
     * Imposes the NIST requirements that passwords must be a minimum length of
     * 8 characters
     *
     * @param password the prospect password the user wants
     * @return true if password does not meet minimum length
     */
    public static boolean isMinimumLength(char[] password) {
        return password.length < MIN_PASSWORD_LENGTH;
    }

    /**
     * Imposes the NIST requirements that passwords not be greater than length of
     * 64 characters
     *
     * @param password the prospect password the user wants
     * @return true if password is longer than maximum length
     */
    public static boolean exceedsMaxmimumLength(char[] password) {
        return password.length > MAX_PASSWORD_LENGTH;
    }

    /**
     * Checks prospect password against a list of known weak or breached passwords
     *
     * @param password the prospect password
     * @param filename the name of the list
     * @return true if password is found in a list
     * @throws IOException file not found
     */
    public static boolean isPasswordKnown(char[] password, String filename) throws IOException {

         // declare IO
         ClassLoader cl = Thread.currentThread().getContextClassLoader();
         InputStream fis = cl.getResourceAsStream(filename);
         InputStreamReader isr = new InputStreamReader(fis);
         BufferedReader br = new BufferedReader(isr);

         // declare variables
         List<String> listKnownPasswords = new ArrayList<String>();
         String line = br.readLine();

         // checking for end of file
         while (line != null) {
             listKnownPasswords.add(line);
             line = br.readLine();
         }

         // closing IO
         fis.close();
         isr.close();
         br.close();


        for(String knownPassword : listKnownPasswords)
            if(Arrays.equals(password, knownPassword.toCharArray()))
                return true;

        return false;
    }
}
