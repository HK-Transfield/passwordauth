package com.hktransfield;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Represents a Registration form
 *
 */
public class Enrollment {
    // declare constants
    public static final String REGEX = "^[A-Za-z][A-Za-z0-9_]{7,29}$";  // regex from https://laasyasettyblog.hashnode.dev/validating-username-using-regex
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 64;

    /**
     * Imposes rules for creating a username and checks if it
     * is valid.
     *
     * @param username The preliminary username to check
     * @return True if the username follows all rules
     */
    public static boolean isUsernameValid(String username) {
        boolean notTaken = false;

        // * rule 1) Case insensitive
        //username = username.toLowerCase();

        // * rule 2) Should only use characters from set [a-zA-z0-9_]
        boolean charactersValid = username.matches(REGEX);
        if(!charactersValid)
            PrintHelps.println(PrintHelps.ANSI_RED + "!! Warning: " + PrintHelps.ANSI_RESET + "Invalid characters in username, can only have letters [a-z, A-Z], numbers [0-9], and underscore [_].");


        // * rule 3-4) No swear words, nor should users bypass with leetspeak
        ProfanityFilter.loadWordFilter();

        boolean notSwearWord = ProfanityFilter.checkForBadWords(username);
        if(!notSwearWord)
            PrintHelps.println(PrintHelps.ANSI_RED + "!! Warning: " + PrintHelps.ANSI_RESET + " please do not include swear words in username.");

        return charactersValid && notSwearWord; // if either are false, then the username is not valid
    }

    // TODO: Implement the following NIST guidelines
    // TODO: Make sure that usernames do not match passwords
    /**
     * https://blog.netwrix.com/2022/11/14/nist-password-guidelines/
     * 8 < length < 64
     *
     *
     * @param password the newly created password
     * @return
     */
    public static boolean isPasswordValid(char[] pwd) throws IOException {
        System.out.println(pwd.length);

        // validate the minimum length of the password
        if(pwd.length < MIN_PASSWORD_LENGTH) {
            PrintHelps.println(PrintHelps.ANSI_RED + "WARNING: Password too short! Please create a password of at least 8 characters or more" + PrintHelps.ANSI_RESET);
            return false;
        }

        // validate the maximum length of the password
        if(pwd.length > MAX_PASSWORD_LENGTH) {
            PrintHelps.println(PrintHelps.ANSI_RED + "WARNING: Password too long!" + PrintHelps.ANSI_RESET);
            return false;
        }

        // check if password is a weak password
        List<String> listWeakPasswords = textFileToList("weakpasswords.txt");
        for(String weakPassword : listWeakPasswords) {
            if(Arrays.equals(pwd, weakPassword.toCharArray())) {
                PrintHelps.println(PrintHelps.ANSI_RED + "WARNING: Your password is considered too weak" + PrintHelps.ANSI_RESET);
                return false;
            }
        }

        // check if password is in a breached database
        List<String> listBreachedPasswords = textFileToList("breachedpasswords.txt");
        for(String breachedPassword : listBreachedPasswords) {
            if(Arrays.equals(pwd, breachedPassword.toCharArray())) {
                PrintHelps.println(PrintHelps.ANSI_RED + "WARNING: Your password was found in a database of breached passwords" + PrintHelps.ANSI_RESET);
                return false;
            }
        }
        return true;
    }

    /**
     * Converts a text file into a list of strings.
     *
     * @param filename
     * @return
     * @throws IOException
     */
    private static List<String> textFileToList(String filename) throws IOException {
        List<String> listStrings = new ArrayList<String>();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream fis = cl.getResourceAsStream(filename);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
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

    /** Represents a profanity filter
     * https://gist.github.com/PimDeWitte/c04cc17bc5fa9d7e3aee6670d4105941
     */
    static class ProfanityFilter {

        // declare class scope variables
        static Map<String, String[]> words = new HashMap<>();
        static int largestWordLength = 0;

        /**
         * Loads in the CSV file containing all swears and obscenities.
         */
        protected static void loadWordFilter() {
            String line = "";

            try {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                InputStream fis = cl.getResourceAsStream("Word_Filter.csv");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);

                while(line != null) { // begin reading file

                    String[] content = null;

                    try {
                        content = line.split(",");

                        if(content.length == 0) continue;

                        String word = content[0];
                        String[] ignoreInCombinationWithWords = new String[]{};

                        if(content.length > 1) {
                            ignoreInCombinationWithWords = content[1].split("_");
                        }

                        if(word.length() > largestWordLength) {
                            largestWordLength = word.length();
                        }
                        words.put(word.replaceAll(" ", ""), ignoreInCombinationWithWords);

                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    line = br.readLine();
                }
                fis.close();
                isr.close();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        /**
         * Iterates over a String input and checks whether a cuss word was found in a list, then checks if the word should be ignored (e.g. bass contains the word *ss).
        * @param input
        * @return
        */

        public static boolean checkForBadWords(String input) {

            if(input == null) {
                return true;
            }

            // don't forget to remove leetspeak, probably want to move this to its own function and use regex if you want to use this
            input = removeLeetSpeak(input);

            ArrayList<String> swearWords = new ArrayList<>();
            input = input.toLowerCase().replaceAll("[^a-zA-Z]", "");
            input = input.replaceAll("_", " ");

            // iterate over each letter in the word
            for(int i = 0; i < input.length(); i++) {

                // from each letter, keep going to find bad words until either the end of the sentence is reached, or the max word length is reached.
                for(int offset = 1; offset < (input.length()+1 - i) && offset < largestWordLength; offset++)  {

                    String wordToCheck = input.substring(i, i + offset);

                    if(words.containsKey(wordToCheck)) {
                        // for example, if you want to say the word bass, that should be possible.
                        String[] ignoreCheck = words.get(wordToCheck);
                        boolean ignore = false;

                        for(int s = 0; s < ignoreCheck.length; s++ ) {

                            if(input.contains(ignoreCheck[s])) {
                                ignore = true;
                                break;
                            }
                        }

                        if(!ignore) {
                            swearWords.add(wordToCheck);
                        }
                    }
                }
            }

            if(swearWords.size() > 0)
                return false;

            return true;
        }

        /**
         * Ensures that users are not able to bypass
         * the rule that does not allow the use of
         * swear words when creating a username by
         * substituting numbers or symbols for letters.
         *
         * @param input
         * @return
         */
        private static String removeLeetSpeak(String input) {
            input = input.replaceAll("1","i");
            input = input.replaceAll("!","i");
            input = input.replaceAll("3","e");
            input = input.replaceAll("4","a");
            input = input.replaceAll("@","a");
            input = input.replaceAll("5","s");
            input = input.replaceAll("7","t");
            input = input.replaceAll("0","o");
            input = input.replaceAll("9","g");

            return input;
        }
    }

}

