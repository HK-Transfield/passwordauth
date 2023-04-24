package com.hktransfield.enrollment;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** Represents a username validtator
 *
 * This utilities class provides methods to enfore various username requirements you might
 * want to implement in a password authentication based sysmte
 */
public class UsernameValidator {
    private static final String REGEX = "^[A-Za-z0-9][A-Za-z0-9_]{7,29}$";  // regex from https://laasyasettyblog.hashnode.dev/validating-username-using-regex

    /**
     * Imposes a rule that checks if a prospect username contains
     * valid characters and matches the regex.
     *
     * @param username the newly created username entered by the user
     * @return true if the username matches the required regex
     */
    public static boolean hasValidCharacters(String username) {
        return username.matches(REGEX);
    }

    /**
     * Imposes a rule that checks if a prospect username contains any
     * profanities.
     *
     * @param username the newly created username entered by the user
     * @return true if the username contains profanity
     */
    public static boolean hasProfanity(String username) {
        username = username.toLowerCase();

        ProfanityFilter.loadProfanityFilter();

        return ProfanityFilter.checkForProfanity(username);
    }

    /** Represents a profanity filter
     * https://gist.github.com/PimDeWitte/c04cc17bc5fa9d7e3aee6670d4105941
     */
    private static class ProfanityFilter {

        // declare class scope variables
        private static Map<String, String[]> words = new HashMap<>();
        private static int largestWordLength = 0;

        /**
         * Loads in the CSV file containing all swears and obscenities.
         */
        private static void loadProfanityFilter() {
            String line = "";

            try {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                InputStream fis = cl.getResourceAsStream("profanityfilter.csv");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);

                while(line != null) { // begin reading file

                    String[] content = null;

                    // split profanities with words to ignore in combination with
                    content = line.split(",");

                    if(content.length == 0) continue;

                    String word = content[0];
                    String[] ignoreInCombinationWithWords = new String[]{};

                    if(content.length > 1) ignoreInCombinationWithWords = content[1].split("_");
                    if(word.length() > largestWordLength) largestWordLength = word.length();

                    words.put(word.replaceAll(" ", ""), ignoreInCombinationWithWords);

                    line = br.readLine();
                }
                fis.close();
                isr.close();
                br.close();

            } catch(IOException e) {
                e.printStackTrace();
            }
        }


        /**
         * Iterates over a String input and checks whether a cuss word was found in a list, then checks if the word should be ignored (e.g. bass contains the word *ss).
         *
        * @param input the user input to check for any profanity
        * @return true if the input contains profanity
        */
        private static boolean checkForProfanity(String input) {

            if(input == null) return true;

            // remove leetspeak
            input = removeLeetSpeak(input);

            ArrayList<String> swearWords = new ArrayList<>();
            input = input.toLowerCase().replaceAll("[^a-zA-Z]", "");
            input = input.replaceAll("_", " "); // usernames will not have spaces

            // iterate over each letter in the word
            for(int i = 0; i < input.length(); i++) {

                // from each letter, keep going to find bad words until either the end of the sentence is reached, or the max word length is reached.
                for(int offset = 1; offset < (input.length()+1 - i) && offset < largestWordLength; offset++)  {

                    String currWord = input.substring(i, i + offset);

                    if(words.containsKey(currWord)) {
                        // for example, if you want to say the word bass, that should be possible.
                        String[] ignoreCheck = words.get(currWord);
                        boolean ignore = false;

                        for(int s = 0; s < ignoreCheck.length; s++ ) {

                            if(input.contains(ignoreCheck[s])) {
                                ignore = true;
                                break;
                            }
                        }
                        if(!ignore) swearWords.add(currWord);
                    }
                }
            }

            return swearWords.size() > 0;
        }

        /**
         * Ensures that users are not able to bypass the rule that does not
         * allow the use of swear words when creating a username by
         * substituting numbers or symbols for letters.
         *
         * @param input the user input to check for leetspeak
         * @return the input with all substitute numbers or symbols replaced
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
