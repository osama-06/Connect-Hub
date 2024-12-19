package Frontend;

import Backend.User;
import Backend.UserService;

import javax.swing.*;
import java.awt.*;
import Backend.*;

public class LoginForm extends JPanel {
    private final UserService userService;
    private final Runnable onLoginSuccess;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    public LoginForm(UserService userService, Runnable onLoginSuccess, CardLayout cardLayout, JPanel cardPanel) {
        this.userService = userService;
        this.onLoginSuccess = onLoginSuccess;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding around components

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20); // Adjust text field size
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20); // Adjust text field size
        JButton loginButton = new JButton("Login");
        JButton goBackButton = new JButton("Go Back");
        JLabel resultLabel = new JLabel(" ");

        // Login button action
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            User user = userService.login(email, password);
            if (user != null) {
                resultLabel.setText("Login successful!");
                resultLabel.setForeground(Color.GREEN);
                onLoginSuccess.run(); // Notify the main frame
            } else {
                resultLabel.setText("Invalid credentials.");
                resultLabel.setForeground(Color.RED);
            }
        });

        // Go Back button action
        goBackButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "MainMenu");  // Switch to the main menu
        });

        // Layout the components
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(emailLabel, gbc);
        
        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(loginButton, gbc);

        // Add the Go Back button in the same row as Login
        gbc.gridx = 1;
        add(goBackButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(resultLabel, gbc);
    }
}