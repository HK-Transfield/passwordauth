package com.hktransfield;

import java.awt.CardLayout;
import java.awt.Color;
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

public class MainFrame extends JFrame {
    private final int COLUMNS = 15;

    /**
     * The maximum number of attempts the user can make
     */
    private final int MAX_ATTEMPTS = 10;

    /**
     * The lockout time for incorrect login attempts
     */
    private final int FIFTEEN_MINUTES = 900000;

    private final String REGISTRATION = "One";
    private final String LOGIN = "Two";
    private final String HOME = "Three";
    private final String SUCCESS = "four";
    private final String WELCOME = "five";
    private final String BTN_REGISTRATION_TEXT = "Sign up";
    private final String BTN_LOGIN_TEXT = "Sign in to existing account";
    private final String BTN_BACK_TEXT = "Go Back";
    private final String BTN_LOGIN_SUBMIT_TEXT = "Login";
    private final String BTN_REGISTRATION_SUBMIT_TEXT = "Create account";

    private CardLayout card;
    private JButton btnRegistration, btnLogin, btnBack, btnLoginSubmit, btnRegistrationSubmit;
    private JCheckBox jcb;
    private JPanel panels, panelHome, panelRegistration, panelLogin, panelSuccess, panelWelcome;
    private JLabel labelWarning;
    private JLabel labelSuccess, labelWelcome;
    private JTextField textFieldLogin_User;
    private JTextField textFieldRegistration_User;
    private JPasswordField textFieldRegistration_Password;
    private JPasswordField textFieldLogin_Password;

    UserDatastore uds;

    /**
     * Constructor
     */
    public MainFrame() {
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
        jcb = new JCheckBox("Show Passwords");

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

        panelLogin.add(new JLabel("Username"));    //set username label to panel
        panelLogin.add(textFieldLogin_User = new JTextField(COLUMNS));   //set text field to panel

        panelLogin.add(new JLabel("Password"));    //set password label to panel
        panelLogin.add(textFieldLogin_Password = new JPasswordField(COLUMNS));   //set text field to panel


        panelRegistration.add(new JLabel("Create a username: "));    //set username label to panel
        panelRegistration.add(textFieldRegistration_User = new JTextField(COLUMNS));   //set text field to panel

        panelRegistration.add(new JLabel("Create a password: "));    //set password label to panel
        panelRegistration.add(textFieldRegistration_Password = new JPasswordField(COLUMNS));   //set text field to panel


        //Create the panel that contains the "cards".
        panels.add(panelRegistration, REGISTRATION);
        panels.add(panelLogin, LOGIN);
        panels.add(panelHome, HOME);
        panels.add(panelSuccess, SUCCESS);
        panels.add(panelWelcome, WELCOME);

        btnRegistration = new JButton(BTN_REGISTRATION_TEXT);
        panelHome.add(btnRegistration);

        btnLogin = new JButton(BTN_LOGIN_TEXT);
        panelHome.add(btnLogin);

        btnRegistrationSubmit = new JButton(BTN_REGISTRATION_SUBMIT_TEXT);
        panelRegistration.add(btnRegistrationSubmit);

        btnLoginSubmit = new JButton(BTN_LOGIN_SUBMIT_TEXT);
        panelLogin.add(btnLoginSubmit);

        btnBack = new JButton(BTN_BACK_TEXT);

        add(panels);
        card.show(panels, HOME);



        /************************/
        /*BUTTON EVENT LISTENERS*/
        /************************/

        /**
         * Switch to REGISTRATION panel
         */
        btnRegistration.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    card.show(panels, REGISTRATION);
                    panelRegistration.add(btnBack);
                    panelRegistration.add(jcb);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        /**
         * Switch to LOGIN panel
         */
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    card.show(panels, LOGIN);
                    panelLogin.add(btnBack);
                    panelLogin.add(jcb);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        /**
         *
         */
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    card.show(panels, HOME);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        /**
         *
         */
        btnRegistrationSubmit.addActionListener(new ActionListener() {

            /**
             * https://stackoverflow.com/questions/41701702/jlabel-set-as-text-list-items
             * @param e
             */
            public void actionPerformed(ActionEvent e) {
                try {
                    // retrieve username and password
                    String username = textFieldRegistration_User.getText();
                    char[] password = textFieldRegistration_Password.getPassword();

                    ArrayList<String> warnings = new ArrayList<String>();
                    StringBuilder result = new StringBuilder();

                    if(username.isEmpty() || password.length == 0) {
                        warnings.add("Please fill in the empty fields!<br>");
                    } else {
                        // conditional checks for username
                        boolean isUsernameTaken = uds.isUsernameTaken(username);
                        boolean hasValidCharacters = UsernameValidator.hasValidCharacters(username);
                        boolean hasSwearWords = UsernameValidator.hasSwearWords(username);
                        boolean isUsernameValid = hasValidCharacters && hasSwearWords && !isUsernameTaken;

                        boolean matchesUsername = PasswordValidator.matchesUsername(username, password);
                        boolean passwordBreached = PasswordValidator.isPasswordBreached(password);
                        boolean passwordWeak = PasswordValidator.isPasswordWeak(password);
                        boolean minimumLength = PasswordValidator.isMinimumLength(password);
                        boolean exceedsMaxmimumLength = PasswordValidator.exceedsMaxmimumLength(password);
                        boolean isPasswordValid = !matchesUsername && !passwordBreached && !minimumLength && !exceedsMaxmimumLength;


                        if(isUsernameValid && isPasswordValid) {
                            textFieldRegistration_Password.setText("");
                            textFieldRegistration_User.setText("");
                            uds.registerUser(username, password);
                            card.show(panels, SUCCESS);
                        } else {
                            /* Check if username is valid */
                            if(isUsernameTaken)
                                warnings.add("That username already exists!<br>");
                            if(!hasValidCharacters)
                                warnings.add("Username can only have letters, numbers, underscores, and be between 8-30 characters!<br>");
                            if(!hasSwearWords)
                                warnings.add("Username cannot contain swear words<br>");

                            /* Check if password is valid */
                            if(matchesUsername)
                                warnings.add("Password cannot be the same as the username!<br>");
                            if(passwordBreached)
                                warnings.add("Password has been found in a breached database!<br>");
                            if(passwordWeak)
                                warnings.add("Password is too weak!");
                            if(minimumLength)
                                warnings.add("Password should be at least 8 characters!");
                            if(exceedsMaxmimumLength)
                                warnings.add("Password is too long!");

                            }
                        }

                    // use HTML tags to print warnings on seperate lines
                    for(String warning : warnings) {
                        result.append(warning);
                    }
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

        btnLoginSubmit.addActionListener(new ActionListener() {

            private int attempts = MAX_ATTEMPTS;

            /**
             * https://stackoverflow.com/questions/41701702/jlabel-set-as-text-list-items
             * @param e
             */
            public void actionPerformed(ActionEvent e) {
                try {
                    // retrieve username and password
                    String username = textFieldLogin_User.getText();
                    char[] password = textFieldLogin_Password.getPassword();
                    System.out.println(uds.isLoginCorrect(username, password));

                    if(uds.isLoginCorrect(username, password)) {
                        textFieldLogin_User.setText("");
                        textFieldLogin_Password.setText("");
                        card.show(panels, WELCOME);
                    } else {
                        // ran out of attempts
                        if(attempts == 0) {
                            labelWarning.setText("You've made too many attempts, please wait 15 minutes");
                            panelLogin.add(labelWarning);
                            panelRegistration.revalidate();
                            panelRegistration.repaint();
                            btnLoginSubmit.setEnabled(false);

                            /**
                             * https://stackoverflow.com/questions/23073741/java-swing-restart-timer-after-operation/23073802#23073802
                             */
                            Timer timer = new Timer(FIFTEEN_MINUTES, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    btnLoginSubmit.setEnabled(true);
                                }
                            });
                            timer.setRepeats(false);
                            timer.start();

                        } else {
                            labelWarning.setText("Your username or password was incorrect, you have " + attempts + " left");
                            attempts--;
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
         * https://stackoverflow.com/questions/27850288/add-listener-to-jpanel-that-will-be-only-called-onload
         */
        panelSuccess.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentShown(ComponentEvent ce) {
                try {

                    TimeUnit.SECONDS.sleep(5);
                    card.show(panels, HOME);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });



        /**************************/
        /*CHECKBOX EVENT LISTENERS*/
        /**************************/

        /**
         *
         */
        jcb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    AbstractButton abstractButton = (AbstractButton) e.getSource();
                    boolean selected = abstractButton.getModel().isSelected();
                    if(selected) {
                        if(panelRegistration.isDisplayable())
                            textFieldRegistration_Password.setEchoChar((char) 0);
                        if(panelLogin.isDisplayable())
                            textFieldLogin_Password.setEchoChar((char) 0);
                    }
                    else {
                        if(panelRegistration.isDisplayable())
                            textFieldRegistration_Password.setEchoChar('*');
                        if(panelLogin.isDisplayable())
                            textFieldLogin_Password.setEchoChar('*');
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });



        /***************************/
        /*TEXTFIELD EVENT LISTENERS*/
        /***************************/


        // /**
        //  * Listen for changes in the textfield
        //  *
        //  * @author https://stackoverflow.com/questions/17132452/java-check-if-jtextfield-is-empty-or-not
        //  */
        // textFieldRegistration_User.getDocument().addDocumentListener(new DocumentListener() {
        //     public void changedUpdate(DocumentEvent e) {
        //       changed();
        //     }
        //     public void removeUpdate(DocumentEvent e) {
        //       changed();
        //     }
        //     public void insertUpdate(DocumentEvent e) {
        //       changed();
        //     }

        //     public void changed() {
        //        if (textFieldRegistration_User.getText().equals("")){
        //          btnRegistrationSubmit.setEnabled(false);
        //        }
        //        else {
        //          btnRegistrationSubmit.setEnabled(true);
        //       }

        //     }
        // });
    }
}
