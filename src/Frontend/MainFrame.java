/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Frontend;

import Backend.UserService;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout; // To manage switching between views
    private JPanel cardPanel;
    private UserService userService;

    public MainFrame(UserService userService) {
        

        // Setup JFrame
        setTitle("User Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize CardLayout and cardPanel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create views
        JPanel mainMenuPanel = createMainMenuPanel();
        JPanel loginForm = new LoginForm(userService, this::onLoginSuccess);
        JPanel signupForm = new SignupForm(userService);

        // Add views to cardPanel
        cardPanel.add(mainMenuPanel, "MainMenu");
        cardPanel.add(loginForm, "Login");
        cardPanel.add(signupForm, "Signup");

        // Add cardPanel to the frame
        add(cardPanel);

        // Display the main menu
        cardLayout.show(cardPanel, "MainMenu");
    }

    // Main Menu Panel (Choose Login or Signup)
    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to the User Management System", JLabel.CENTER);
        panel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Signup");

        // Action listeners for buttons
        loginButton.addActionListener(e -> cardLayout.show(cardPanel, "Login"));
        signupButton.addActionListener(e -> cardLayout.show(cardPanel, "Signup"));

        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);

        panel.add(buttonPanel, BorderLayout.CENTER);
        return panel;
    }

    // Action to handle successful login
    private void onLoginSuccess() {
        JOptionPane.showMessageDialog(this, "Login successful!");
        cardLayout.show(cardPanel, "MainMenu"); // Return to the main menu or implement a new post-login screen
    }

    
    
}

