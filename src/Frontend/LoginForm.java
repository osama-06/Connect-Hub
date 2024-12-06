
package Frontend;

import Backend.User;
import Backend.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginForm extends JPanel {
    private final UserService userService;
    private final Runnable onLoginSuccess;

    public LoginForm(UserService userService, Runnable onLoginSuccess) {
        this.userService = userService;
        this.onLoginSuccess = onLoginSuccess;

        setLayout(new GridLayout(4, 2, 10, 10));

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JLabel resultLabel = new JLabel("");

        loginButton.addActionListener((ActionEvent e) -> {
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

        add(emailLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(resultLabel);
    }
}