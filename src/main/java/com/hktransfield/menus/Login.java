package com.hktransfield.menus;

import java.io.Console;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.hktransfield.PrintUtils;
import com.hktransfield.RegistrationUtils;
import com.hktransfield.UserDatabase;

public class Login {

	public static void openMenu(UserDatabase udb) {
        String username = "";
        char[] password;

        boolean usernameValid, passwordValid;
        Console cnsl = System.console();


        PrintUtils.clearConsole();
        PrintUtils.println(PrintUtils.ANSI_BLUE + "***********************" + PrintUtils.ANSI_RESET);
        PrintUtils.println(PrintUtils.ANSI_BLUE + "Login to existing acocount" + PrintUtils.ANSI_RESET);
        PrintUtils.println(PrintUtils.ANSI_BLUE + "***********************" + PrintUtils.ANSI_RESET);
        PrintUtils.println("\n");

        try {
            // read username from console
            usernameValid = false;

            if(cnsl == null) {
                PrintUtils.println("No console available");
                return;
            }
            username = cnsl.readLine("Username: ");
            password = cnsl.readPassword("Password: ");

            if(udb.isLoginCorrect(username, password)) {
                System.out.println("Success!");
                TimeUnit.SECONDS.sleep(5);
            } else {
                TimeUnit.SECONDS.sleep(5);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
