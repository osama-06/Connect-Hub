package Frontend;

import Backend.User;
import Backend.UserService;

import javax.swing.*;
import java.awt.*;



public class LoginForm extends JPanel {
    private final UserService userService;
    private final Runnable onLoginSuccess;

    public LoginForm(UserService userService, Runnable onLoginSuccess) {
        this.userService = userService;
        this.onLoginSuccess = onLoginSuccess;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding around components

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20); // Adjust text field size
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20); // Adjust text field size
        JButton loginButton = new JButton("Login");
        JLabel resultLabel = new JLabel(" ");

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

        gbc.gridx = 1;
        add(resultLabel, gbc);
    }
}
