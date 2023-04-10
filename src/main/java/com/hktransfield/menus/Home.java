package com.hktransfield.menus;
import java.util.Scanner;

import com.hktransfield.AuthenticationSystem;
import com.hktransfield.PrintUtils;

public class Home {

    public static void openMenu() {
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
    }
}
