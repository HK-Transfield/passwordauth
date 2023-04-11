package com.hktransfield.menus;

import com.hktransfield.PrintHelps;

public class Home {

    public static void openMenu() {
        String[] options = {
            "1. Login",
            "2. Sign up",
            "3. Exit"
        };

        PrintHelps.clearConsole();
        PrintHelps.println(PrintHelps.ANSI_YELLOW + "***********************" + PrintHelps.ANSI_RESET);
        PrintHelps.println(PrintHelps.ANSI_YELLOW + "Password Authentication System" + PrintHelps.ANSI_RESET);
        PrintHelps.println(PrintHelps.ANSI_YELLOW + "By Harmon Transfield" + PrintHelps.ANSI_RESET);
        PrintHelps.println(PrintHelps.ANSI_YELLOW + "***********************" + PrintHelps.ANSI_RESET);
        PrintHelps.println("\n");
        PrintHelps.printMenu(options);
        PrintHelps.print("Choose an option (1/2/3): ");
    }
}
