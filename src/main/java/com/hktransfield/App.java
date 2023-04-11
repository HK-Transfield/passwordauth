package com.hktransfield;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Scanner;
import com.hktransfield.menus.*;

//create CreateLoginForm class to create login form
//class extends JFrame to create a window where our component add
//class implements ActionListener to perform an action on button click
class CreateLoginForm extends JFrame implements ActionListener
{
    //initialize button, panel, label, and text field
    JButton b1;
    JPanel newPanel;
    JLabel userLabel, passLabel;
    final JTextField  textField1, textField2;

    //calling constructor
    CreateLoginForm()
    {

        //create label for username
        userLabel = new JLabel();
        userLabel.setText("Username");      //set label value for textField1

        //create text field to get username from the user
        textField1 = new JTextField(15);    //set length of the text

        //create label for password
        passLabel = new JLabel();
        passLabel.setText("Password");      //set label value for textField2

        //create text field to get password from the user
        textField2 = new JPasswordField(15);    //set length for the password

        //create submit button
        b1 = new JButton("SUBMIT"); //set label to button

        //create panel to put form elements
        newPanel = new JPanel(new GridLayout(3, 1));
        newPanel.add(userLabel);    //set username label to panel
        newPanel.add(textField1);   //set text field to panel
        newPanel.add(passLabel);    //set password label to panel
        newPanel.add(textField2);   //set text field to panel
        newPanel.add(b1);           //set button to panel

        //set border to panel
        add(newPanel, BorderLayout.CENTER);

        //perform action on button click
        b1.addActionListener(this);     //add action listener to button
        setTitle("LOGIN FORM");         //set title to the login form
    }

    //define abstract method actionPerformed() which will be called on button click
    public void actionPerformed(ActionEvent ae)     //pass action listener as a parameter
    {
        String userValue = textField1.getText();        //get user entered username from the textField1
        String passValue = textField2.getText();        //get user entered pasword from the textField2

    }
}

public class App {

    /**
     * Main method. It is the entry point into the program
     *
     * @param args
     */
    public static void main(String[] args) {
        try
        {
            //create instance of the CreateLoginForm
            CreateLoginForm form = new CreateLoginForm();
            form.setSize(300,100);  //set size of the frame
            form.setVisible(true);  //make form visible to the user
        }
        catch(Exception e)
        {
            //handle exception
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        // int option = 0;
        // Scanner s = new Scanner(System.in);
        // boolean keepRunning = true;
        // UserDatastore udb = UserDatastore.getInstance();

        // Home.openMenu();

        // while(keepRunning) {
        //     try {
        //         option = s.nextInt();
        //         switch (option) {
        //             case 1: // Sign in
        //                 Login.openMenu(udb);
        //                 break;
        //             case 2: // Registration
        //                 Registration.openMenu(udb);
        //                 break;
        //             case 3: // Exit the application
        //                 PrintHelps.print("Goodbye!");
        //                 keepRunning = false;
        //                 break;
        //                 default:
        //                 System.out.println();
        //                 PrintHelps.println("Please enter a valid option! (1/2/3)");
        //                 break;
        //             }

        //             if(!keepRunning) break;
        //             else Home.openMenu();
        //     } catch (Exception e) {
        //         PrintHelps.println(PrintHelps.ANSI_RED + "Something unexpected happened:" + PrintHelps.ANSI_RESET);
        //         System.err.println(e);
        //     }
        // }
        // s.close();
    }
}
