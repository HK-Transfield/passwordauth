import java.io.*;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Registration {

    public static final String regex = "^[A-Za-z][A-Za-z0-9_]{7,29}$";

    /**
     * Username must comply with the following rules:
     */
    public static boolean isUsernameValid(String username) {
        boolean regexValid = false;
        boolean notSwearWord = false;

        // ! 1. Case insensitive
        username = username.toLowerCase();

        //! 2. Should only use characters from set [a-zA-z0-9_]
        // https://laasyasettyblog.hashnode.dev/validating-username-using-regex
        if(username.matches(regex)) {
            regexValid = true;
        } else {
            AppUtils.print(AppUtils.ANSI_RED + "!! Warning: " + AppUtils.ANSI_RESET + "Invalid characters in username.");
        }

        // ! 3. No swear words
        ProfanityFilter.initialise();

        // ! 4. Should not be able to bypass rule 3 by substituting numbers for letters
        notSwearWord = ProfanityFilter.checkForSwears(username);

        return regexValid && notSwearWord;
    }
}

/** Represents a profanity filter
 * https://gist.github.com/PimDeWitte/c04cc17bc5fa9d7e3aee6670d4105941
 */
class ProfanityFilter {

    // declare class scope variables
     static Map<String, String[]> words = new HashMap<>();
     static int largestWordLength = 0;

     public static void initialise() {
         String line = "";
         int counter = 0;

         try {
            File f = new File("Word_Filter.csv");
            FileInputStream fis = new FileInputStream(f);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            while((line = br.readLine()) != null) {

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

            }
            fis.close();
            br.close();

            System.out.println("Loaded " + counter + " words to filter out");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Iterates over a String input and checks whether a cuss word was found in a list, then checks if the word should be ignored (e.g. bass contains the word *ss).
    * @param input
    * @return
    */

    public static ArrayList<String> searchForSwears(String input) {

        if(input == null) {
            return new ArrayList<>();
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
        return swearWords;
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

    public static boolean checkForSwears(String username) {
        ArrayList<String> swears = searchForSwears(username);

        if(swears.size() > 0) { // swears found in the username
            System.out.println(AppUtils.ANSI_RED + "!! Warning: " + AppUtils.ANSI_RESET + " qualified as a bad word in a username");
            return false;
        }

        return true;
    }
}
