/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Frontend;
import Backend.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SignupForm extends JPanel {
    private final UserService userService;

    public SignupForm(UserService userService) {
        this.userService = userService;
        setLayout(new GridLayout(6, 2, 10, 10));

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JLabel dobLabel = new JLabel("Date of Birth (YYYY-MM-DD):");
        JTextField dobField = new JTextField();

        JButton signupButton = new JButton("Signup");
        JLabel resultLabel = new JLabel("");

        signupButton.addActionListener((ActionEvent e) -> {
            String email = emailField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String dob = dobField.getText();

            if (userService.signup(email, username, password, dob)) {
                resultLabel.setText("Signup successful!");
                resultLabel.setForeground(Color.GREEN);
            } else {
                resultLabel.setText("Email already exists.");
                resultLabel.setForeground(Color.RED);
            }
        });

        add(emailLabel);
        add(emailField);
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(dobLabel);
        add(dobField);
        add(signupButton);
        add(resultLabel);
    }
}