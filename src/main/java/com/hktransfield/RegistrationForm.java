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
class RegistrationForm {
    // declare constants
    public static final String REGEX = "^[A-Za-z][A-Za-z0-9_]{7,29}$";  // regex from https://laasyasettyblog.hashnode.dev/validating-username-using-regex
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 64;

    /**
     * Imposes rules for creating a username and checks if it
     * is valid.
     *
     * @param username The preliminary username to check
     * @return True if the username is valid
     */
    public static boolean isUsernameValid(String username) {
        boolean charactersValid = false;
        boolean notSwearWord = false;

        // ! 1. Case insensitive
        username = username.toLowerCase();

        //! 2. Should only use characters from set [a-zA-z0-9_]
        charactersValid = username.matches(REGEX);
        if(!charactersValid)
            AppUtils.print(AppUtils.ANSI_RED + "!! Warning: " + AppUtils.ANSI_RESET + "Invalid characters in username, can only have letters [a-z, A-Z], numbers [0-9], and underscore [_].");


        // ! 3. No swear words
        ProfanityFilter.initialise();

        // ! 4. Should not be able to bypass rule 3 by substituting numbers for letters
        notSwearWord = ProfanityFilter.checkForSwears(username);
        if(!notSwearWord)
            AppUtils.print(AppUtils.ANSI_RED + "!! Warning: " + AppUtils.ANSI_RESET + " qualified as a bad word in a username");

        return charactersValid && notSwearWord; // if either are false, then the username is not valid
    }

    // TODO: Implement the following NIST guidelines
    /**
     * https://blog.netwrix.com/2022/11/14/nist-password-guidelines/
     * 8 < length < 64
     *
     *
     * @param password
     * @return
     */
    public static boolean isPasswordValid(char[] pwd) throws IOException {
        System.out.println(pwd.length);

        // validate the minimum length of the password
        if(pwd.length < MIN_PASSWORD_LENGTH) {
            AppUtils.println(AppUtils.ANSI_RED + "!! Warning: Password too short! Please create a password of at least 8 characters or more" + AppUtils.ANSI_RESET);
            return false;
        }

        // validate the maximum length of the password
        if(pwd.length > MAX_PASSWORD_LENGTH) {
            AppUtils.println(AppUtils.ANSI_RED + "!! Warning: Password too long!" + AppUtils.ANSI_RESET);
            return false;
        }

        // check if password is a weak password
        List<String> listWeakPasswords = textFileToList("res/weakpasswords.txt");
        for(String weakPassword : listWeakPasswords) {
            if(Arrays.equals(pwd, weakPassword.toCharArray())) {
                AppUtils.println(AppUtils.ANSI_RED + "!! Warning: Your password is considered too weak" + AppUtils.ANSI_RESET);
                return false;
            }
        }

        // check if password is in a breached database
        List<String> listBreachedPasswords = textFileToList("res/breachedpasswords.txt");
        for(String breachedPassword : listBreachedPasswords) {
            if(Arrays.equals(pwd, breachedPassword.toCharArray())) {
                AppUtils.println(AppUtils.ANSI_RED + "!! Warning: Your password was found in a database of breached passwords" + AppUtils.ANSI_RESET);
                return false;
            }
        }
        return false;
    }

    private static List<String> textFileToList(String filepath) throws IOException {
        List<String> listStrings = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(filepath));
        String line = br.readLine();

        // checking for end of file
        while (line != null) {
            listStrings.add(line);
            line = br.readLine();
        }

        // closing bufferreader object
        br.close();

        return listStrings;
    }

    /** Represents a profanity filter
     * https://gist.github.com/PimDeWitte/c04cc17bc5fa9d7e3aee6670d4105941
     */
    protected static class ProfanityFilter {

        // declare class scope variables
        static Map<String, String[]> words = new HashMap<>();
        static int largestWordLength = 0;

        protected static void initialise() {
            String line = "";
            int counter = 0;

            try {
                File f = new File("res/Word_Filter.csv");
                FileInputStream fis = new FileInputStream(f);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);

                while(line != null) {

                    counter++;
                    String[] content = null;

                    try {
                        content = line.split(",");

                        if(content.length == 0) continue;


                        String word = content[0];
                        String[] ignore_in_combination_with_words = new String[]{};

                        if(content.length > 1) {
                            ignore_in_combination_with_words = content[1].split("_");
                        }

                        if(word.length() > largestWordLength) {
                            largestWordLength = word.length();
                        }
                        words.put(word.replaceAll(" ", ""), ignore_in_combination_with_words);

                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    line = br.readLine();
                }
                fis.close();
                br.close();

                // System.out.println("Loaded " + counter + " words to filter out");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        /**
         * Iterates over a String input and checks whether a cuss word was found in a list, then checks if the word should be ignored (e.g. bass contains the word *ss).
        * @param input
        * @return
        */

        public static boolean checkForSwears(String input) {

            if(input == null) {
                return true;
            }

            // don't forget to remove leetspeak, probably want to move this to its own function and use regex if you want to use this
            input = removeLeetSpeak(input);

            ArrayList<String> swearWords = new ArrayList<>();
            input = input.toLowerCase().replaceAll("[^a-zA-Z]", "");

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
         * @param username
         * @return
         */
        private static String removeLeetSpeak(String username) {
            username = username.replaceAll("1","i");
            username = username.replaceAll("!","i");
            username = username.replaceAll("3","e");
            username = username.replaceAll("4","a");
            username = username.replaceAll("@","a");
            username = username.replaceAll("5","s");
            username = username.replaceAll("7","t");
            username = username.replaceAll("0","o");
            username = username.replaceAll("9","g");

            return username;
        }
    }

}

