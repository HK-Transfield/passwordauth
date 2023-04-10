package com.hktransfield;
import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;


public class AuthenticationSystem {
/*
 * https://medium.com/@kasunpdh/how-to-store-passwords-securely-with-pbkdf2-204487f14e84
 * https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/#4-stronger-hashes-using-pbkdf2withhmacsha1-algorithm
 * https://github.com/Password4j/password4j
 *
 *
 *
 */

    /**
     * The main menu of the program. Here, users can choose
     * to either login, create a new account, or exit the
     * application.
     */
    public static void startupMenu() {
        int option = 0;
        String[] options = {
            "1. Login",
            "2. Sign up",
            "3. Exit"
        };

        PrintUtils.clearConsole();
        PrintUtils.println(PrintUtils.ANSI_YELLOW + "***********************" + PrintUtils.ANSI_RESET);
        PrintUtils.println(PrintUtils.ANSI_YELLOW + "Password Authentication System" + PrintUtils.ANSI_RESET);
        PrintUtils.println(PrintUtils.ANSI_YELLOW + "By Harmon Transfield" + PrintUtils.ANSI_RESET);
        PrintUtils.println(PrintUtils.ANSI_YELLOW + "***********************" + PrintUtils.ANSI_RESET);
        PrintUtils.println("\n");
        PrintUtils.printMenu(options);
        PrintUtils.print("Choose an option (1/2/3): ");

        Scanner s = new Scanner(System.in);
        boolean keepRunning = true;
        while(keepRunning) {
            try {
                option = s.nextInt();
                switch (option) {
                    case 1: // Sign in
                        PrintUtils.print("You've selected: " + option);
                        break;
                    case 2: // Registration
                        PrintUtils.print("You've selected: " + option);
                        registrationMenu();
                        break;
                    case 3: // Exit the application
                        PrintUtils.print("You've selected: " + option);
                        keepRunning = false;
                        break;
                    default:
                        System.out.println();
                        PrintUtils.println("Please enter a valid option! (1/2/3)");
                        break;
                }

                if(!keepRunning) break;
            } catch (Exception e) {
                PrintUtils.println(PrintUtils.ANSI_RED + "Something unexpected happened:" + PrintUtils.ANSI_RESET);
                System.err.println(e);
            }
        }

        s.close();
    }

    /**
     * Opens a menu that allows users to login to an existing username and password.
     */
    public static void loginMenu() {

    }

    /**
     * Opens a menu that allows users to create a new username and password.
     * The method will check that both username and password meet certain
     * criteria,
     */
    public static void registrationMenu() {
        String username = "";
        char[] initPassword;
        char[] reenteredPassword = {};
        boolean usernameValid, passwordValid;
        UserDatabase udb;
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

            while(!passwordValid) {

                initPassword = cnsl.readPassword("Create your password: ");
                reenteredPassword = cnsl.readPassword("Please re-enter your password: ");

                if(Arrays.equals(initPassword, reenteredPassword))
                    passwordValid = RegistrationUtils.isPasswordValid(reenteredPassword);
            }

            // SUCCESSFUL REGISTRATION
            udb = UserDatabase.getInstance();
            udb.registerUser(username, reenteredPassword);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}

