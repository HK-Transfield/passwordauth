package com.hktransfield.menus;

import java.io.Console;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.hktransfield.PrintHelps;
import com.hktransfield.Enrollment;
import com.hktransfield.UserDatastore;

/** Represents a registration form
 *
 */
public class Registration {

    /**
     *
     * @param udb
     */
    public static void openMenu(UserDatastore udb, String username, char[] initPassword, char[] reenteredPassword) {
        boolean usernameValid, passwordValid;

        try {
            // read username from console
            usernameValid = false;

            while(!usernameValid) {
                usernameValid = Enrollment.isUsernameValid(username);

                if(usernameValid)
                    break;
                else
                    PrintHelps.println(PrintHelps.ANSI_RED + "Please enter a valid username!" + PrintHelps.ANSI_RESET);
                }

                passwordValid = false;

                // TODO: becuase users cannot see their passwords, make sure they type it in again

                while(!passwordValid) {

                if(Arrays.equals(initPassword, reenteredPassword))
                    // passwordValid = RegistrationUtils.isPasswordValid(reenteredPassword);
                    break;
            }

            // SUCCESSFUL REGISTRATION
            udb.registerUser(username, reenteredPassword);
            TimeUnit.SECONDS.sleep(5); // display success prompt before returning to menu
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    // /**
    //  *
    //  * @param udb
    //  */
    // public static void openMenu(UserDatastore udb) {
    //     String username = "";
    //     char[] initPassword;
    //     char[] reenteredPassword = {};
    //     boolean usernameValid, passwordValid;
    //     Console cnsl = System.console();

    //     PrintHelps.clearConsole();
    //     PrintHelps.println(PrintHelps.ANSI_BLUE + "***********************" + PrintHelps.ANSI_RESET);
    //     PrintHelps.println(PrintHelps.ANSI_BLUE + "Registration new user" + PrintHelps.ANSI_RESET);
    //     PrintHelps.println(PrintHelps.ANSI_BLUE + "***********************" + PrintHelps.ANSI_RESET);
    //     PrintHelps.println("\n");

    //     try {
    //         // read username from console
    //         usernameValid = false;

    //         if(cnsl == null) {
    //             PrintHelps.println("No console available");
    //             return;
    //         }

    //         while(!usernameValid) {

    //             username = cnsl.readLine("Create your username: ");
    //             usernameValid = Enrollment.isUsernameValid(username);

    //             if(usernameValid)
    //                 break;
    //             else
    //                 PrintHelps.println(PrintHelps.ANSI_RED + "Please enter a valid username!" + PrintHelps.ANSI_RESET);
    //             }

    //             passwordValid = false;

    //             // TODO: becuase users cannot see their passwords, make sure they type it in again

    //             while(!passwordValid) {

    //             initPassword = cnsl.readPassword("Create your password: ");
    //             reenteredPassword = cnsl.readPassword("Please re-enter your password: ");

    //             if(Arrays.equals(initPassword, reenteredPassword))
    //                 // passwordValid = RegistrationUtils.isPasswordValid(reenteredPassword);
    //                 break;
    //         }

    //         // SUCCESSFUL REGISTRATION
    //         udb.registerUser(username, reenteredPassword);
    //         PrintHelps.println(PrintHelps.ANSI_GREEN + "Successfully created an account!" + PrintHelps.ANSI_RESET);
    //         TimeUnit.SECONDS.sleep(5); // display success prompt before returning to menu
    //     } catch (Exception e) {
    //         System.err.println(e);
    //     }
    // }
}
