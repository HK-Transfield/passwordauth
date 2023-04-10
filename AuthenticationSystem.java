import java.io.Console;
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

        AppUtils.clearConsole();
        AppUtils.println(AppUtils.ANSI_YELLOW + "***********************" + AppUtils.ANSI_RESET);
        AppUtils.println(AppUtils.ANSI_YELLOW + "Password Authentication System" + AppUtils.ANSI_RESET);
        AppUtils.println(AppUtils.ANSI_YELLOW + "By Harmon Transfield" + AppUtils.ANSI_RESET);
        AppUtils.println(AppUtils.ANSI_YELLOW + "***********************" + AppUtils.ANSI_RESET);
        AppUtils.println("\n");
        AppUtils.printMenu(options);
        AppUtils.print("Choose an option (1/2/3): ");

        Scanner s = new Scanner(System.in);
        boolean keepRunning = true;
        while(keepRunning) {
            try {
                option = s.nextInt();
                switch (option) {
                    case 1: // Sign in
                        AppUtils.print("You've selected: " + option);
                        break;
                    case 2: // Registration
                        AppUtils.print("You've selected: " + option);
                        RegistrationMenu();
                        break;
                    case 3: // Exit the application
                        AppUtils.print("You've selected: " + option);
                        keepRunning = false;
                        break;
                    default:
                        System.out.println();
                        AppUtils.println("Please enter a valid option! (1/2/3)");
                        break;
                }

                if(!keepRunning) break;
            } catch (Exception e) {
                AppUtils.println(AppUtils.ANSI_RED + "Something unexpected happened:" + AppUtils.ANSI_RESET);
                System.err.println(e);
            }
        }

        s.close();
    }

    /**
     * Opens a menu that allows users to create a new username and password.
     * The method will check that both username and password meet certain
     * criteria,
     */
    public static void RegistrationMenu() {
        String username;
        char[] initPassword, reenteredPassword;
        boolean usernameValid, passwordValid;
        Console cnsl = System.console();

        AppUtils.clearConsole();
        AppUtils.println(AppUtils.ANSI_BLUE + "***********************" + AppUtils.ANSI_RESET);
        AppUtils.println(AppUtils.ANSI_BLUE + "Registration new user" + AppUtils.ANSI_RESET);
        AppUtils.println(AppUtils.ANSI_BLUE + "***********************" + AppUtils.ANSI_RESET);
        AppUtils.println("\n");

        try {
            // read username from console
            usernameValid = false;

            if(cnsl == null) {
                AppUtils.println("No console available");
                return;
            }


            while(!usernameValid) {

                username = cnsl.readLine("Create your username: ");
                usernameValid = RegistrationForm.isUsernameValid(username);

                if(usernameValid)
                    break;
                else
                    AppUtils.println(AppUtils.ANSI_RED + "Please enter a valid username!" + AppUtils.ANSI_RESET);
            }

            passwordValid = false;

            while(!passwordValid) {

                initPassword = cnsl.readPassword("Create your password: ");
                RegistrationForm.isPasswordValid(initPassword);
            }
            reenteredPassword = cnsl.readPassword("Please re-enter your password: ");

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
       startupMenu();
    }
}


class User {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String un) {
        this.username = un;
    }

    public void setPassword(String pwd) {
        this.password = pwd;
    }
}

