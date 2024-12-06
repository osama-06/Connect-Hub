package Frontend;

import Backend.UserService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupForm extends JPanel {
    private final UserService userService;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    public SignupForm(UserService userService, CardLayout cardLayout, JPanel cardPanel) {
        this.userService = userService;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        JLabel dobLabel = new JLabel("Date of Birth (YYYY-MM-DD):");
        JTextField dobField = new JTextField(20);

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
                JOptionPane.showMessageDialog(this, "Signup Successful!");
                // Switch back to the main menu after successful signup
                cardLayout.show(cardPanel, "MainMenu");
            } else {
                resultLabel.setText("Email already exists.");
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
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(dobLabel, gbc);

        gbc.gridx = 1;
        add(dobField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(signupButton, gbc);

        gbc.gridx = 1;
        add(resultLabel, gbc);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
