package com.hktransfield.menus;

import java.io.Console;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.hktransfield.PrintUtils;
import com.hktransfield.RegistrationUtils;
import com.hktransfield.UserDatabase;

/** Represents a registration form
 *
 */
public class Registration {

    /**
     *
     * @param udb
     */
    public static void openMenu(UserDatabase udb) {
        String username = "";
        char[] initPassword;
        char[] reenteredPassword = {};
        boolean usernameValid, passwordValid;
        Console cnsl = System.console();

        PrintUtils.clearConsole();
        PrintUtils.println(PrintUtils.ANSI_BLUE + "***********************" + PrintUtils.ANSI_RESET);
        PrintUtils.println(PrintUtils.ANSI_BLUE + "Registration new user" + PrintUtils.ANSI_RESET);
        PrintUtils.println(PrintUtils.ANSI_BLUE + "***********************" + PrintUtils.ANSI_RESET);
        PrintUtils.println("\n");

        try {
            // read username from console
            usernameValid = false;

            if(cnsl == null) {
                PrintUtils.println("No console available");
                return;
            }

            while(!usernameValid) {

                username = cnsl.readLine("Create your username: ");
                usernameValid = RegistrationUtils.isUsernameValid(username);

                if(usernameValid)
                    break;
                else
                    PrintUtils.println(PrintUtils.ANSI_RED + "Please enter a valid username!" + PrintUtils.ANSI_RESET);
                }

                passwordValid = false;

                // TODO: becuase users cannot see their passwords, make sure they type it in again

                while(!passwordValid) {

                initPassword = cnsl.readPassword("Create your password: ");
                reenteredPassword = cnsl.readPassword("Please re-enter your password: ");

                if(Arrays.equals(initPassword, reenteredPassword))
                    // passwordValid = RegistrationUtils.isPasswordValid(reenteredPassword);
                    break;
            }

            // SUCCESSFUL REGISTRATION
            udb.registerUser(username, reenteredPassword);
            PrintUtils.println(PrintUtils.ANSI_GREEN + "Successfully created an account!" + PrintUtils.ANSI_RESET);
            TimeUnit.SECONDS.sleep(5); // display success prompt before returning to menu
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
