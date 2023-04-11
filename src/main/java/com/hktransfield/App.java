package com.hktransfield;

import java.util.Scanner;
import com.hktransfield.menus.*;

public class App {

    /**
     * Main method. It is the entry point into the program
     *
     * @param args
     */
    public static void main(String[] args) {
        int option = 0;
        Scanner s = new Scanner(System.in);
        boolean keepRunning = true;
        UserDatastore udb = UserDatastore.getInstance();

        Home.openMenu();

        while(keepRunning) {
            try {
                option = s.nextInt();
                switch (option) {
                    case 1: // Sign in
                        Login.openMenu(udb);
                        break;
                    case 2: // Registration
                        Registration.openMenu(udb);
                        break;
                    case 3: // Exit the application
                        PrintHelps.print("Goodbye!");
                        keepRunning = false;
                        break;
                        default:
                        System.out.println();
                        PrintHelps.println("Please enter a valid option! (1/2/3)");
                        break;
                    }

                    if(!keepRunning) break;
                    else Home.openMenu();
            } catch (Exception e) {
                PrintHelps.println(PrintHelps.ANSI_RED + "Something unexpected happened:" + PrintHelps.ANSI_RESET);
                System.err.println(e);
            }
        }
        s.close();
    }
}
