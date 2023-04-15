package com.hktransfield.enrollment;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PasswordValidator {
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 64;

    /**
     *
     * @param username
     * @param password
     * @return
     */
    public static boolean matchesUsername(String username, char[] password) {
        return Arrays.equals(username.toCharArray(), password);
    }

    /**
     *
     * @param password
     * @return
     */
    public static boolean isMinimumLength(char[] password) {
        return password.length < MIN_PASSWORD_LENGTH;
    }

    /**
     *
     * @param password
     * @return
     */
    public static boolean exceedsMaxmimumLength(char[] password) {
        return password.length > MAX_PASSWORD_LENGTH;
    }

    /**
     *
     * @param password
     * @return
     * @throws IOException
     */
    public static boolean isPasswordWeak(char[] password) throws IOException {
        // check if password is a weak password
        List<String> listWeakPasswords = textFileToList("weakpasswords.txt");

        for(String weakPassword : listWeakPasswords)
            if(Arrays.equals(password, weakPassword.toCharArray()))
                return true;

        return false;
    }

    /**
     *
     * @param password
     * @return
     * @throws IOException
     */
    public static boolean isPasswordBreached(char[] password) throws IOException {
        // check if password is in a breached database
        List<String> listBreachedPasswords = textFileToList("breachedpasswords.txt");

        for(String breachedPassword : listBreachedPasswords)
            if(Arrays.equals(password, breachedPassword.toCharArray()))
                return true;

        return false;
    }

    /**
     * Converts a text file into a list of strings.
     *
     * @param filename
     * @return
     * @throws IOException
     */
    private static List<String> textFileToList(String filename) throws IOException {
        // declare IO
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream fis = cl.getResourceAsStream(filename);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        // declare variables
        List<String> listStrings = new ArrayList<String>();
        String line = br.readLine();

        // checking for end of file
        while (line != null) {
            listStrings.add(line);
            line = br.readLine();
        }

        // closing bufferreader object
        fis.close();
        isr.close();
        br.close();

        return listStrings;
    }
}
