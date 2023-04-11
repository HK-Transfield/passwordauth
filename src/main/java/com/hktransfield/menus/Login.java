package com.hktransfield.menus;

import java.io.Console;
import java.util.concurrent.TimeUnit;
import com.hktransfield.PrintHelps;
import com.hktransfield.UserDatastore;

public class Login {
    // declare class-scope constants
    private final static int MAX_ATTEMPTS = 10;

    /**
     * Opens a console menu that allos
     *
     * @param udb the user datastore to connect to to retrieve login information
     */
	public static void openMenu(UserDatastore udb) {
        // declare method variables
        int remainingAttempts = MAX_ATTEMPTS;
        String username;
        char[] password;
        boolean isLoginCorrect;
        Console cnsl = System.console();

        // print useful information
        PrintHelps.clearConsole();
        PrintHelps.println(PrintHelps.ANSI_BLUE + "***********************" + PrintHelps.ANSI_RESET);
        PrintHelps.println(PrintHelps.ANSI_BLUE + "Login to existing acocount" + PrintHelps.ANSI_RESET);
        PrintHelps.println(PrintHelps.ANSI_BLUE + "***********************" + PrintHelps.ANSI_RESET);
        PrintHelps.println("\n");

        try {
            if(cnsl == null) {
                PrintHelps.println("No console available");
                return;
            }

            while(remainingAttempts > 0) {
                username = cnsl.readLine("Username: ");
                password = cnsl.readPassword("Password for " + username +": ");
                isLoginCorrect = udb.isLoginCorrect(username, password);

                if(isLoginCorrect) { // successfully logged in, display welcome message
                    PrintHelps.println(PrintHelps.ANSI_GREEN + "Welcome, you have successfully logged in!" + PrintHelps.ANSI_RESET);
                    TimeUnit.SECONDS.sleep(5);
                    return;
                }

                   remainingAttempts--;

                if(remainingAttempts == 0) { // failed attempt, reduce
                    PrintHelps.println(PrintHelps.ANSI_RED + "You have made too many failed attempts, please wait 15 minutes" + PrintHelps.ANSI_RESET);

                    // 15 minute wait period before resetting
                    TimeUnit.MINUTES.sleep(15);

                    // reset attempts, give the user another try to log in
                    remainingAttempts = MAX_ATTEMPTS;
                } else {
                    PrintHelps.println(PrintHelps.ANSI_RED + "Incorrect username or password, you have " + remainingAttempts + " attempts remaining!" + PrintHelps.ANSI_RESET);
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
