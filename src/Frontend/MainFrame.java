package Frontend;

import Backend.UserService;
import Backend.User;
import javax.swing.*;
import java.awt.*;
import Backend.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private UserService userService;

    public MainFrame(UserService userService) {
        this.userService = userService;

        // Setup JFrame
        setTitle("User Management System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize CardLayout and cardPanel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create views
        JPanel mainMenuPanel = createMainMenuPanel();
        JPanel loginForm = new LoginForm(userService, this::onLoginSuccess);
        JPanel signupForm = new SignupForm(userService, cardLayout, cardPanel);

        // Add views to cardPanel
        cardPanel.add(mainMenuPanel, "MainMenu");
        cardPanel.add(loginForm, "Login");
        cardPanel.add(signupForm, "Signup");

        // Add cardPanel to the frame
        add(cardPanel);

        // Display the main menu
        cardLayout.show(cardPanel, "MainMenu");

        // Make the frame visible
        setVisible(true);
    }

    // Main Menu Panel (Choose Login or Signup)
    private JPanel createMainMenuPanel() {
        // Create a panel with GridBagLayout for full centering
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create and center the welcome label
        JLabel welcomeLabel = new JLabel("Welcome to the User Management System", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Set GridBagConstraints to center the label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span both columns to make it centered
        panel.add(welcomeLabel, gbc);

        // Create the buttons (Login and Signup)
        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Signup");

        // Set buttons size and alignment
        loginButton.setPreferredSize(new Dimension(150, 40));
        signupButton.setPreferredSize(new Dimension(150, 40));

        // Add action listeners to the buttons
        loginButton.addActionListener(e -> cardLayout.show(cardPanel, "Login"));
        signupButton.addActionListener(e -> cardLayout.show(cardPanel, "Signup"));

        // Add the buttons to the panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));  // Center the buttons
        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);

        // Set GridBagConstraints to center the button panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Span both columns to center the buttons
        panel.add(buttonPanel, gbc);

        return panel;
    }

    // Action to handle successful login
    private void onLoginSuccess() {
        // Assuming the user is successfully logged in, get the logged-in user
        User loggedInUser = userService.getLoggedInUser(); // Adjust this method in your UserService to return the logged-in user
        
        
        // Open the ProfileManagement frame with user details
        ProfileManagement profileManagement = new ProfileManagement(userService.getDatabaseManager(), loggedInUser);
        profileManagement.setVisible(true);

        // Hide the current login window
        this.setVisible(false);
    }
}
