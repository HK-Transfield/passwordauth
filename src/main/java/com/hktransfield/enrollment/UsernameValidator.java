package com.hktransfield.enrollment;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsernameValidator {
    private static final String REGEX = "^[A-Za-z][A-Za-z0-9_]{7,29}$";  // regex from https://laasyasettyblog.hashnode.dev/validating-username-using-regex

    /**
     *
     * @param username
     * @return
     */
    public static boolean hasValidCharacters(String username) {
        username = username.toLowerCase();

        return username.matches(REGEX);
    }

    /**
     *
     * @param username
     * @return
     */
    public static boolean hasSwearWords(String username) {
        username = username.toLowerCase();

        ProfanityFilter.loadWordFilter();

        return ProfanityFilter.checkForBadWords(username);
    }

    /** Represents a profanity filter
     * https://gist.github.com/PimDeWitte/c04cc17bc5fa9d7e3aee6670d4105941
     */
    private static class ProfanityFilter {

        // declare class scope variables
        static Map<String, String[]> words = new HashMap<>();
        static int largestWordLength = 0;

        /**
         * Loads in the CSV file containing all swears and obscenities.
         */
        private static void loadWordFilter() {
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

        private static boolean checkForBadWords(String input) {

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
