package com.hktransfield;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MainFrame extends JFrame {
    private final int COLUMNS = 20;
    private final String REGISTRATION = "One";
    private final String LOGIN = "Two";
    private final String HOME = "Three";
    private final String BTN_REGISTRATION_TEXT = "Sign up";
    private final String BTN_LOGIN_TEXT = "Sign in to existing account";
    private final String BTN_BACK_TEXT = "Go Back";
    private final String BTN_LOGIN_SUBMIT_TEXT = "Login";
    private final String BTN_REGISTRATION_SUBMIT_TEXT = "Create account";

    private CardLayout card;
    private JButton btnRegistration, btnLogin, btnBack, btnLoginSubmit, btnRegistrationSubmit;
    private JCheckBox jcb;
    private JPanel cards, cardHome, cardRegistration, cardLogin;
    private JLabel userLoginLabel, passwordLoginLabel, warningLabel;
    private JLabel userRegistrationLabel, passwordRegistrationLabel;
    private JTextField textFieldLoginUser, tf;
    private JTextField textFieldRegistrationUser;
    private JPasswordField textFieldRegistrationPassword;
    private JPasswordField textFieldLoginPassword;

    // call datastore
    UserDatastore udb;

    /**
     * Constructor
     */
    public MainFrame() {
        super();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        passBtn();
    }

    /**
     * Set up interface
     */
    private void passBtn() {
        // set up layout manager and panels
        card = new CardLayout();
        cards = new JPanel(card);
        cardHome = cardLogin = cardRegistration = new JPanel();

        jcb = new JCheckBox("Show Passwords");

        cardLogin.add(new JLabel("Username"));    //set username label to panel
        cardLogin.add(textFieldLoginUser = new JTextField(COLUMNS));   //set text field to panel
        cardLogin.add(new JLabel("Password"));    //set password label to panel
        cardLogin.add(textFieldLoginPassword = new JPasswordField(COLUMNS));   //set text field to panel

        cardRegistration.add(new JLabel("Create a username: "));    //set username label to panel
        cardRegistration.add(textFieldRegistrationUser = new JTextField(COLUMNS));   //set text field to panel
        cardRegistration.add(new JLabel("Create a password: "));    //set password label to panel
        cardRegistration.add(textFieldRegistrationPassword = new JPasswordField(COLUMNS));   //set text field to panel


        //Create the panel that contains the "cards".
        cards.add(cardRegistration, REGISTRATION);
        cards.add(cardLogin, LOGIN);
        cards.add(cardHome, HOME);

        btnRegistration = new JButton(BTN_REGISTRATION_TEXT);
        cardHome.add(btnRegistration);

        btnLogin = new JButton(BTN_LOGIN_TEXT);
        cardHome.add(btnLogin);

        btnRegistrationSubmit = new JButton(BTN_REGISTRATION_SUBMIT_TEXT);
        cardRegistration.add(btnRegistrationSubmit);

        btnBack = new JButton(BTN_BACK_TEXT);

        add(cards);
        card.show(cards, HOME);

        /*********************************************************************************/
        /*********************************************************************************/
        /*********************************************************************************/

        /**
         * Switch to REGISTRATION panel
         */
        btnRegistration.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    card.show(cards, REGISTRATION);
                    cardRegistration.add(btnBack);
                    cardRegistration.add(jcb);
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
                    card.show(cards, LOGIN);
                    cardLogin.add(btnBack);
                    cardLogin.add(jcb);
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
                    card.show(cards, HOME);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        /**
         *
         */
        btnRegistrationSubmit.addActionListener(new ActionListener() {

            private String username;
            private char[] password;

            public void actionPerformed(ActionEvent e) {
                try {




                    card.show(cards, HOME);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        /**
         *
         */
        jcb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    AbstractButton abstractButton = (AbstractButton) e.getSource();
                    boolean selected = abstractButton.getModel().isSelected();
                    if(selected) {
                        if(cardRegistration.isDisplayable())
                            textFieldRegistrationPassword.setEchoChar((char) 0);
                        if(cardLogin.isDisplayable())
                            textFieldLoginPassword.setEchoChar((char) 0);
                    }
                    else {
                        if(cardRegistration.isDisplayable())
                            textFieldRegistrationPassword.setEchoChar('*');
                        if(cardLogin.isDisplayable())
                            textFieldLoginPassword.setEchoChar('*');
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        /**
         * Listen for changes in the textfield
         *
         * @author https://stackoverflow.com/questions/17132452/java-check-if-jtextfield-is-empty-or-not
         */
        textFieldRegistrationUser.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
              changed();
            }
            public void removeUpdate(DocumentEvent e) {
              changed();
            }
            public void insertUpdate(DocumentEvent e) {
              changed();
            }

            public void changed() {
               if (textFieldRegistrationUser.getText().equals("")){
                 btnRegistrationSubmit.setEnabled(false);
               }
               else {
                 btnRegistrationSubmit.setEnabled(true);
              }

            }
          });
    }
}
