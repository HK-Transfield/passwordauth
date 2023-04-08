public class AppUtils {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    /**
     * Clears the console of any text.
     */
    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

     /**
     * Prints out a console menu.
     *
     * @param options An array of strings with available options users can select from
     */
    public static void printMenu(String[] options) {
        for(String option : options) {
            System.out.println(option);
        }
    }

    /**
     * Prints string to console.
     *
     * @param s Text to be written
     */
    public static void println(String s) {
        System.out.println(s);
    }

    /**
     * Prints string to console.
     *
     * @param s Text to be written
     */
    public static void print(String s) {
        System.out.print(s);
    }
}
