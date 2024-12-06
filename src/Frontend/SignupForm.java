/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Frontend;
import Backend.User;
import Backend.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupForm extends JPanel {
    private final UserService userService;
    private final CardLayout cardLayout = null;
    private final JPanel cardPanel = null;

    public SignupForm(UserService userService  ) {
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

            // Check if all fields are filled
            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || dob.isEmpty()) {
                resultLabel.setText("Please fill all fields.");
                resultLabel.setForeground(Color.RED);
                return;
            }

            // Validate email format
            if (!isValidEmail(email)) {
                resultLabel.setText("Invalid email format.");
                resultLabel.setForeground(Color.RED);
                return;
            }

            // Try to sign up
            if (userService.signup(email, username, password, dob)) {
                resultLabel.setText("Signup successful!");
                resultLabel.setForeground(Color.GREEN);
                // Switch back to main menu
                cardLayout.show(cardPanel, "MainMenu");
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

    // Method to check if email format is valid using regex
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}