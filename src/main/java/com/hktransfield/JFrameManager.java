package com.hktransfield;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import com.hktransfield.enrollment.PasswordValidator;
import com.hktransfield.enrollment.UsernameValidator;

public class JFrameManager extends JFrame {
    /*
     * CONSTANT
     */
    private final int COLUMNS = 15;

    /*
     * CONSTANT:  The maximum number of attempts the user can make
     */
    private final int MAX_ATTEMPTS = 10;

    /*
     * CONSTANT: The lockout time for exceeding incorrect login attempts
     */
    private final int ONE_MINUTE = 60000;

    /*
     * CONSTANT: Declare layout constraints for panels
     */
    private final String REGISTRATION = "One";
    private final String LOGIN = "Two";
    private final String HOME = "Three";
    private final String SUCCESS = "four";
    private final String WELCOME = "five";

    /*
     * Declare AWT layout manager
     */
    private CardLayout card;

    /*
     * Declare class-scope swing buttons
     */
    private JButton btnBack;
    private JButton btnRegistration;
    private JButton btnRegistrationSubmit;
    private JButton btnLogin;
    private JButton btnLoginSubmit;

    /*
     * Declare swing checkbox
     */
    private JCheckBox checkBoxShowPassword;

    /*
     * Declare swing panels
     */
    private JPanel panels;
    private JPanel panelHome;
    private JPanel panelRegistration;
    private JPanel panelLogin;
    private JPanel panelSuccess;
    private JPanel panelWelcome;

    /*
     * Declare swing labels
     */
    private JLabel labelHome;
    private JLabel labelWarning;
    private JLabel labelSuccess;
    private JLabel labelWelcome;

    private JTextField textFieldLogin_User;
    private JTextField textFieldRegistration_User;

    private JPasswordField textFieldLogin_Password;
    private JPasswordField textFieldRegistration_Password;

    UserDatastore uds;

    /**
     * Constructor.
     * Instantiates a new JFrameManager object.
     */
    public JFrameManager() {
        super();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        uds = UserDatastore.getInstance();
        generateInterface();
    }

    /**
     * Set up interface
     */
    private void generateInterface() {
        // set up layout manager and panels
        card = new CardLayout();
        panels = new JPanel(card);
        panelHome = new JPanel();
        panelLogin = new JPanel();
        panelRegistration = new JPanel();
        panelSuccess = new JPanel();
        panelWelcome = new JPanel();

        // create checkbox to show and hide passwords
        checkBoxShowPassword = new JCheckBox("Show Passwords");

        // create home label
        labelHome = new JLabel("<html><div style='text-align: center;'>Simple Password Authentication System<br>By Harmon Transfield</div></html>");
        labelHome.setFont(new Font("Arial", Font.BOLD, 15));
        labelHome.setForeground(Color.BLUE);
        panelHome.add(labelHome);

        // create warning label
        labelWarning = new JLabel();
        labelWarning.setForeground(Color.red);

        // create success registration label
        labelSuccess = new JLabel("<html>You've successfully created an account!<br>You will return to the home screen shortly...</html>");
        labelSuccess.setForeground(Color.green);
        panelSuccess.add(labelSuccess);

        // create welcome label after successfully logging in
        labelWelcome = new JLabel("<html>Welcome! You've successfully logged in</html>");
        labelWelcome.setForeground(Color.green);
        panelWelcome.add(labelWelcome);

        // create login username label and textfield
        panelLogin.add(new JLabel("Username"));    //set username label to panel
        panelLogin.add(textFieldLogin_User = new JTextField(COLUMNS));   //set text field to panel

        // create login password label and textfield
        panelLogin.add(new JLabel("Password"));    //set password label to panel
        panelLogin.add(textFieldLogin_Password = new JPasswordField(COLUMNS));   //set text field to panel

        // create registration user label and textfield
        panelRegistration.add(new JLabel("Create a username: "));    //set username label to panel
        textFieldRegistration_User = new JTextField(COLUMNS);
        textFieldRegistration_User.setBounds(100, 27, 193, 28);
        panelRegistration.add(textFieldRegistration_User);   //set text field to panel

        // create registration password label and textfield
        panelRegistration.add(new JLabel("Create a password: "));    //set password label to panel
        textFieldRegistration_Password = new JPasswordField(COLUMNS);
        textFieldRegistration_Password.setBounds(50, 70, 100, 20);
        panelRegistration.add(textFieldRegistration_Password);   //set text field to panel


        //Create the panel that contains the "cards".
        panels.setLocation(new Point( 500, 300));
        panels.add(panelRegistration, REGISTRATION);
        panels.add(panelLogin, LOGIN);
        panels.add(panelHome, HOME);
        panels.add(panelSuccess, SUCCESS);
        panels.add(panelWelcome, WELCOME);
        panels.setSize(new Dimension(400, 200));

        // create registration panel button
        btnRegistration = new JButton("Sign up");
        panelHome.add(btnRegistration);

        // create login panel button
        btnLogin = new JButton("Sign in to existing account");
        panelHome.add(btnLogin);

        // create registration submission button
        btnRegistrationSubmit = new JButton("Create account");
        panelRegistration.add(btnRegistrationSubmit);

        // create login submission button
        btnLoginSubmit = new JButton("Login");
        panelLogin.add(btnLoginSubmit);

        btnBack = new JButton("Go Back");

        add(panels);
        card.show(panels, HOME);

        /************************/
        /*BUTTON EVENT LISTENERS*/
        /************************/


        /* Action listener for the registration button */
        btnRegistration.addActionListener(new ActionListener() {

            /**
             * When the user clicks on this button, it will change
             * panels to the registration form.
             *
             * @param e the user clicks the button
             */
            public void actionPerformed(ActionEvent e) {
                try {
                    card.show(panels, REGISTRATION);
                    labelWarning.setText("");
                    panelRegistration.add(btnBack);
                    panelRegistration.add(checkBoxShowPassword);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        /* Action listener for the login button */
        btnLogin.addActionListener(new ActionListener() {

            /**
             * When the user clicks on this button, it will change
             * panels to the login form.
             *
             * @param e The user clicks on the button
             */
            public void actionPerformed(ActionEvent e) {
                try {
                    card.show(panels, LOGIN);
                    labelWarning.setText("");
                    panelLogin.add(btnBack);
                    panelLogin.add(checkBoxShowPassword);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        /* Action listener for the back button */
        btnBack.addActionListener(new ActionListener() {

            /**
             * When the user clicks on this button, it will change
             * panels to the home screen.
             *
             * @param e The user clicks on the button
             */
            public void actionPerformed(ActionEvent e) {
                try {
                    card.show(panels, HOME);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        /* Action listener for the submit button on the registration pages */
        btnRegistrationSubmit.addActionListener(new ActionListener() {

            /**
             * Validates and submits the username and password the user has entered
             * into the textfields provided on the registration panel. The form will
             * either change to a success message, or it will display approriate error
             * messages on the form.
             *
             * https://stackoverflow.com/questions/41701702/jlabel-set-as-text-list-items
             * @param e The user clicks on the button
             */
            public void actionPerformed(ActionEvent e) {
                try {
                    // retrieve username and password
                    String username = textFieldRegistration_User.getText();
                    char[] password = textFieldRegistration_Password.getPassword();

                    // as there could be multiple warnings, save them in a list
                    ArrayList<String> warnings = new ArrayList<String>();
                    StringBuilder result = new StringBuilder();

                    // make sure no fields are empty
                    if(username.isEmpty() || password.length == 0) warnings.add("Please fill in the empty fields!<br>");

                    else {
                        // conditional checks for username requirements
                        boolean isUsernameTaken = uds.isUsernameTaken(username);
                        boolean hasValidCharacters = UsernameValidator.hasValidCharacters(username);
                        boolean hasProfanity = UsernameValidator.hasProfanity(username);

                        // username is only valid if all requirements are met
                        boolean isUsernameValid = hasValidCharacters && !hasProfanity && !isUsernameTaken;

                        // conditionaly checks for NIST password requirements
                        boolean matchesUsername = PasswordValidator.matchesUsername(username, password);
                        boolean passwordBreached = PasswordValidator.isPasswordKnown(password, "breachedpasswords.txt");
                        boolean passwordWeak = PasswordValidator.isPasswordKnown(password, "weakpasswords.txt");
                        boolean minimumLength = PasswordValidator.isMinimumLength(password);
                        boolean exceedsMaxmimumLength = PasswordValidator.exceedsMaxmimumLength(password);

                        // password is only valid if all requirements are met
                        boolean isPasswordValid = !matchesUsername && !passwordBreached && !passwordWeak && !minimumLength && !exceedsMaxmimumLength;

                        if(isUsernameValid && isPasswordValid) {
                            textFieldRegistration_Password.setText("");
                            textFieldRegistration_User.setText("");
                            uds.registerUser(username, password);
                            card.show(panels, SUCCESS);
                        } else {
                            // set warnings for username rules
                            if(isUsernameTaken) warnings.add("That username already exists!<br>");
                            if(!hasValidCharacters) warnings.add("Username must have letters,numbers, underscores, and be between 8-30 characters!<br>");
                            if(hasProfanity) warnings.add("Username cannot contain swear words<br>");

                            // warnings for password rules
                            if(matchesUsername) warnings.add("Password cannot be the same as the username!<br>");
                            if(passwordBreached) warnings.add("Password found in a breached database!<br>");
                            if(passwordWeak) warnings.add("Password is currently too weak!<br>");
                            if(minimumLength) warnings.add("Password should be at least 8 characters!");
                            if(exceedsMaxmimumLength) warnings.add("Password has too many characters!!");
                        }
                    }

                    // use HTML tags to print warnings on seperate lines
                    for(String warning : warnings) result.append(warning);

                    // display all warning messages
                    labelWarning.setText("<html>" + result.toString() + "</html>");
                    panelRegistration.add(labelWarning);
                    panelRegistration.revalidate();
                    panelRegistration.repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        /* Action listener for the login submit button */
        btnLoginSubmit.addActionListener(new ActionListener() {
            // set the number of attempts possible
            private int attempts = MAX_ATTEMPTS;

            /**
             * Validates and submits the username and password the user has entered
             * into the textfields provided on the login form. The form will either
             * change to a success message, or it will display approriate error
             * messages on the form.
             *
             * https://stackoverflow.com/questions/41701702/jlabel-set-as-text-list-items
             * @param e the user clicks the button
             */
            public void actionPerformed(ActionEvent e) {


                try {

                    // retrieve username and password
                    String username = textFieldLogin_User.getText();
                    char[] password = textFieldLogin_Password.getPassword();

                    if(username.isEmpty() || password.length == 0) // fields are empty
                        labelWarning.setText("Please fill in the empty fields!");
                    else {
                        if(uds.isLoginCorrect(username, password)) { // login is successful
                            TimeUnit.SECONDS.sleep(3);

                            textFieldLogin_User.setText("");
                            textFieldLogin_Password.setText("");
                            card.show(panels, WELCOME);
                        } else {
                            if(attempts == 0) { // ran out of login attempts
                                labelWarning.setText("<html>You've made too many attempts.<br> Please wait 1 minute</html>");
                                panelLogin.add(labelWarning);
                                panelRegistration.revalidate();
                                panelRegistration.repaint();
                                btnLoginSubmit.setEnabled(false);

                                /**
                                 * Set a timer for 1 minute
                                 *
                                 * https://stackoverflow.com/questions/23073741/java-swing-restart-timer-after-operation/23073802#23073802
                                 */
                                Timer timer = new Timer(ONE_MINUTE, new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        btnLoginSubmit.setEnabled(true);
                                        labelWarning.setText("");
                                        attempts = MAX_ATTEMPTS;
                                    }
                                });
                                timer.setRepeats(false);
                                timer.start();

                            } else { // failed login, decrement attempt counter
                                labelWarning.setText("<html>Your username or password was incorrect.<br> You have " + attempts + " left</html>");
                                attempts--;
                            }
                        }
                    }

                    panelLogin.add(labelWarning);
                    panelRegistration.revalidate();
                    panelRegistration.repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });

        /***********************/
        /*PANEL EVENT LISTENERS*/
        /***********************/

        /**
         * Componenet listener, checks whether the success panel is displayed
         *
         * https://stackoverflow.com/questions/27850288/add-listener-to-jpanel-that-will-be-only-called-onload
         */
        panelSuccess.addComponentListener(new ComponentAdapter() {

            /**
             * Adds a slight delay before switching to the successfully registered screen
             */
            @Override
            public void componentShown(ComponentEvent ce) {
                try {

                    TimeUnit.SECONDS.sleep(3);
                    card.show(panels, HOME);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });



        /**************************/
        /*CHECKBOX EVENT LISTENERS*/
        /**************************/

        /* Action listener for checkbox */
        checkBoxShowPassword.addActionListener(new ActionListener() {

            /**
             * When the user selects the checkbox, it will show the password they have
             * typed in the textfield
             *
             * @param e the user clicks on the textfield
             */
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean selected = checkBoxShowPassword.isSelected();

                    // show either the original or hidden character
                    char echoChar = selected ? (char) 0 : '•';

                    // check which panel is currently displayed
                    if(panelRegistration.isDisplayable()) textFieldRegistration_Password.setEchoChar(echoChar);
                    if(panelLogin.isDisplayable()) textFieldLogin_Password.setEchoChar(echoChar);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
