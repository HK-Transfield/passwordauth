import java.util.Scanner;

public class AuthenticationSystem {

    public static void initialiseMenu() {
        int option = 0;
        String[] options = {
            "1. Login",
            "2. Sign up",
            "3. Exit"
        };

        AppUtils.clearConsole();
        AppUtils.println(AppUtils.ANSI_YELLOW + "***********************" + AppUtils.ANSI_RESET);
        AppUtils.println(AppUtils.ANSI_YELLOW + "Password Authentication System" + AppUtils.ANSI_RESET);
        AppUtils.println(AppUtils.ANSI_YELLOW + "By Harmon Transfield" + AppUtils.ANSI_RESET);
        AppUtils.println(AppUtils.ANSI_YELLOW + "***********************" + AppUtils.ANSI_RESET);
        AppUtils.println("\n");
        AppUtils.printMenu(options);
        AppUtils.print("Choose an option (1/2/3): ");

        Scanner s = new Scanner(System.in);
        boolean shouldBreak = true;
        while(shouldBreak) {
            try {
                option = s.nextInt();
                switch (option) {
                    case 1: // Sign in
                        AppUtils.print("You've selected: " + option);
                        shouldBreak = true;
                        break;
                    case 2: // Registration
                        AppUtils.print("You've selected: " + option);
                        shouldBreak = true;
                        break;
                    case 3: // Exit the application
                        AppUtils.print("You've selected: " + option);
                        shouldBreak = true;
                        break;
                    default:
                        AppUtils.println("Please enter a valid option! (1/2/3)");
                        break;
                }

                if(shouldBreak) break;
            } catch (Exception e) {
                AppUtils.println(AppUtils.ANSI_RED + "Something unexpected happened:" + AppUtils.ANSI_RESET);
                System.err.println(e);
            }
        }

        switch (option) {
            case 1: // Sign in
                break;
            case 2: // Registration
                break;
            case 3: // Exit the application
                break;
        }

        s.close();
    }

    public static void RegistrationMenu() {
        String username, password;
        Scanner s = new Scanner(System.in);

        AppUtils.clearConsole();

        try {
            // read username from console
            AppUtils.print("Create your username: ");
            username = s.nextLine();

            // read password from console
            AppUtils.print("\nCreate your password: ");
            password = s.nextLine();

        } catch (Exception e) {
            System.err.println(e);
        }


        Registration.
    }

    public static void main(String[] args) {
       initialiseMenu();
    }
}
