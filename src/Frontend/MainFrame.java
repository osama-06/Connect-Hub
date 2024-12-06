package Frontend;

import Backend.UserService;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private UserService userService;

    public MainFrame(UserService userService) {
        this.userService = userService;

        // Setup JFrame
        setTitle("User Management System");
        setSize(800, 600);
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
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel welcomeLabel = new JLabel("Welcome to the User Management System", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; 
        panel.add(welcomeLabel, gbc);

        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Signup");

        loginButton.setPreferredSize(new Dimension(150, 40));
        signupButton.setPreferredSize(new Dimension(150, 40));

        loginButton.addActionListener(e -> cardLayout.show(cardPanel, "Login"));
        signupButton.addActionListener(e -> cardLayout.show(cardPanel, "Signup"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; 
        panel.add(buttonPanel, gbc);

        return panel;
    }

    // Action to handle successful login
    private void onLoginSuccess() {
        // Once login is successful, show the Profile Management Page
        ProfileManagement profileManagementPage = new ProfileManagement(userService);
        profileManagementPage.setVisible(true);
        this.dispose(); // Close the login screen
    }
}
