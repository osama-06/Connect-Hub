
package Frontend;
import Backend.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class ProfileManagement extends JFrame {
    private DatabaseManager databaseManager;
    private ProfileService profileService;
    private User currentUser;
    private User searchedUser;

    private JTextArea bioField;
    private JPasswordField passwordField;
    private JLabel profilePhotoLabel;
    private JLabel coverPhotoLabel;
    private JButton changeProfilePhotoButton;
    private JButton changeCoverPhotoButton;
    private JButton saveButton;
    private JButton goToNewsFeedButton; // Button to go to News Feed
    private JButton addFriendButton; // Button to add friend
    private JButton removeFriendButton; // Button to remove friend
    private JList<String> friendsList; // List to display friends
    private JTextField searchField; // Search field for username
    private JButton searchButton; // Button to initiate search
    private JButton logoutButton; // Button to log out
    private JPanel notificationPanel;
    private JLabel notificationLabel;
   

    private FriendshipService friendService; // FriendService for handling friend requests

    public ProfileManagement(DatabaseManager databaseManager, User user) {
        this.databaseManager = databaseManager;
        this.profileService = new ProfileService(databaseManager);
        this.friendService = new FriendshipService(databaseManager);
        this.currentUser = user;
     
        setTitle("Connect Hub Profile Management");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Header (Cover Photo)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 250));

        coverPhotoLabel = new JLabel();
        coverPhotoLabel.setHorizontalAlignment(JLabel.CENTER);
        coverPhotoLabel.setVerticalAlignment(JLabel.CENTER);
        coverPhotoLabel.setPreferredSize(new Dimension(800, 250));
        displayImage(currentUser.getCoverPhoto(), coverPhotoLabel, 800, 250);
        headerPanel.add(coverPhotoLabel, BorderLayout.CENTER);

        changeCoverPhotoButton = new JButton("Change Cover Photo");
        styleButton(changeCoverPhotoButton, 150, 40);
        headerPanel.add(changeCoverPhotoButton, BorderLayout.SOUTH);

        // Body Panel
        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setBackground(Color.WHITE);
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Left Panel (Profile Photo)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);

        profilePhotoLabel = new JLabel();
        profilePhotoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        displayImage(currentUser.getProfilePhoto(), profilePhotoLabel, 150, 150);
        leftPanel.add(profilePhotoLabel);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing
        changeProfilePhotoButton = new JButton("Change Profile Photo");
        styleButton(changeProfilePhotoButton, 180, 50);
        changeProfilePhotoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(changeProfilePhotoButton);

        // Right Panel (Editable Info)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout(10, 10));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createTitledBorder("Edit Profile"));

        // Bio Field
        JPanel bioPanel = new JPanel(new BorderLayout());
        bioPanel.setBackground(Color.WHITE);
        bioPanel.add(new JLabel("Bio:"), BorderLayout.NORTH);

        bioField = new JTextArea(currentUser.getBio());
        bioField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        bioField.setFont(new Font("Arial", Font.PLAIN, 14));
        bioField.setLineWrap(true);
        bioField.setWrapStyleWord(true);
        JScrollPane bioScrollPane = new JScrollPane(bioField);
        bioScrollPane.setPreferredSize(new Dimension(400, 150));
        bioPanel.add(bioScrollPane, BorderLayout.CENTER);

        // Password Field
        JPanel passwordPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(currentUser.getPassword());
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField = new JPasswordField("");
        passwordPanel.add(passwordField);

        // Save Button
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        savePanel.setBackground(Color.WHITE);
        saveButton = new JButton("Save Changes");
        styleButton(saveButton, 160, 40);
        savePanel.add(saveButton);

        // Add components to right panel
        rightPanel.add(bioPanel, BorderLayout.NORTH);
        rightPanel.add(passwordPanel, BorderLayout.CENTER);
        rightPanel.add(savePanel, BorderLayout.SOUTH);

        // Go to News Feed Button (New Feature)
        goToNewsFeedButton = new JButton("Go to News Feed");
        styleButton(goToNewsFeedButton, 180, 50);
        goToNewsFeedButton.addActionListener(e -> {
            NewsFeed newsFeed = new NewsFeed(databaseManager, currentUser);
            newsFeed.setVisible(true);
            this.dispose(); // Close the profile page
        });

        // **Logout Button** to log out and go back to the main frame
        logoutButton = new JButton("Logout");
        styleButton(logoutButton, 180, 50);
        logoutButton.addActionListener(e -> {
            MainFrame mainframe = new MainFrame(new UserService(databaseManager));  // Assuming you have a LoginFrame for login screen
            mainframe.setVisible(true);
            this.dispose(); // Close the profile page
        });

        // Add Go to News Feed and Logout button to body panel (at the bottom)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(goToNewsFeedButton);
        buttonPanel.add(logoutButton);  // Add logout button beside "Go to News Feed"
        bodyPanel.add(buttonPanel, BorderLayout.SOUTH);

        // **Friends Panel**
        JPanel friendsPanel = new JPanel();
        friendsPanel.setLayout(new BorderLayout());
        friendsPanel.setBackground(Color.WHITE);
        friendsPanel.setBorder(BorderFactory.createTitledBorder("Friends"));

        DefaultListModel<String> friendsModel = new DefaultListModel<>();
        for (Friend friend : currentUser.getFriends()) {
            friendsModel.addElement(friend.getUsername());
        }

        friendsList = new JList<>(friendsModel);
        JScrollPane friendsScrollPane = new JScrollPane(friendsList);
        friendsPanel.add(friendsScrollPane, BorderLayout.CENTER);
              



         // Create notification panel
        notificationPanel = new JPanel();
        notificationPanel.setBackground(new Color(66, 135, 245));
        notificationPanel.setPreferredSize(new Dimension(getWidth(), 40));
         notificationPanel.setLayout(new BorderLayout());
        notificationPanel.setVisible(false); // Hidden by default

     notificationLabel = new JLabel("", SwingConstants.CENTER);
    notificationLabel.setForeground(Color.WHITE);
      notificationPanel.add(notificationLabel, BorderLayout.CENTER);

    // Add to the frame (you can adjust where this is placed, e.g., at the top)
    add(notificationPanel, BorderLayout.NORTH);

         // **Search Friend Panel**
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);

        searchField = new JTextField(20);
        searchButton = new JButton("Search User");
        styleButton(searchButton, 100, 30);

        searchButton.addActionListener(e -> searchForUser());

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        friendsPanel.add(searchPanel, BorderLayout.NORTH);

        // Add Friend and Remove Friend buttons
        addFriendButton = new JButton("Add Friend");
        addFriendButton.setVisible(false);
        addFriendButton.addActionListener(e -> {
            if (searchedUser != null) {
                addFriend(searchedUser); // Call addFriend with the searched user
            } else {
                JOptionPane.showMessageDialog(this, "No user selected to add as friend.");
            }
        });

        removeFriendButton = new JButton("Remove Friend");
        removeFriendButton.setVisible(false); // Start with hidden
        removeFriendButton.addActionListener(e -> {
            if (searchedUser != null) {
                removeFriend(searchedUser); // Call removeFriend with the searched user
            } else {
                JOptionPane.showMessageDialog(this, "No user selected to remove from friends.");
            }
        });
        

        friendsPanel.add(addFriendButton, BorderLayout.SOUTH);
        friendsPanel.add(removeFriendButton, BorderLayout.SOUTH);

        // Add friends panel to body
        bodyPanel.add(friendsPanel, BorderLayout.EAST);

        // Add panels to body
        bodyPanel.add(leftPanel, BorderLayout.WEST);
        bodyPanel.add(rightPanel, BorderLayout.CENTER);

        // Add components to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(bodyPanel, BorderLayout.CENTER);

        // Button Actions
        changeProfilePhotoButton.addActionListener(e -> changeProfilePhoto());
        changeCoverPhotoButton.addActionListener(e -> changeCoverPhoto());
        saveButton.addActionListener(e -> saveChanges());

        
    }

    private void searchForUser() {
        String username = searchField.getText().trim();
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a username to search.");
            return;
        }

        User user = friendService.searchUserByUsername(username);

        if (user != null) {
            searchedUser = user;
            JOptionPane.showMessageDialog(this, "Found user: " + user.getUsername());
        } else {
            JOptionPane.showMessageDialog(this, "User not found.");
        }
    }

  
    private void addFriend(User user) {
        // Existing addFriend implementation
        friendService.sendFriendRequest(currentUser, user);
        currentUser.addFriend(new Friend(user.getUsername(), "/path/to/profile/photo"));
        JOptionPane.showMessageDialog(this, "Friend added successfully.");
        refreshFriendsList();
    }

    private void removeFriend(User user) {
        // Existing removeFriend implementation
        friendService.removeFriend(currentUser, user);
        JOptionPane.showMessageDialog(this, "Friend removed successfully.");
        refreshFriendsList();
    }

    private void refreshFriendsList() {
        DefaultListModel<String> model = (DefaultListModel<String>) friendsList.getModel();
        model.clear();
        for (Friend friend : currentUser.getFriends()) {
            model.addElement(friend.getUsername());
        }
    }

    private void displayImage(String path, JLabel label, int width, int height) {
        try {
            ImageIcon imageIcon = new ImageIcon(path);
            Image image = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(image));
            label.setText("");
        } catch (Exception e) {
            label.setIcon(null);
            label.setText("No image available");
        }
    }

    private void changeProfilePhoto() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String newPhotoPath = fileChooser.getSelectedFile().getAbsolutePath();
            try {
                profileService.changeProfilePhoto(currentUser, newPhotoPath);
                currentUser.setProfilePhoto(newPhotoPath);
                displayImage(newPhotoPath, profilePhotoLabel, 150, 150);
                JOptionPane.showMessageDialog(this, "Profile photo updated successfully.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error updating profile photo: " + ex.getMessage());
            }
        }
    }

    private void showNotification(String message) {
        // Set the notification message
        notificationLabel.setText(message);
    
        // Show the panel
        notificationPanel.setVisible(true);
    
        // Timer to hide the notification after 3 seconds
        Timer timer = new Timer(3000, e -> notificationPanel.setVisible(false));
        timer.setRepeats(false);  // Make sure it only runs once
        timer.start();
    }
    

    private void changeCoverPhoto() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String newPhotoPath = fileChooser.getSelectedFile().getAbsolutePath();
            try {
                profileService.changeCoverPhoto(currentUser, newPhotoPath);
                currentUser.setCoverPhoto(newPhotoPath);
                displayImage(newPhotoPath, coverPhotoLabel, 800, 250);
                JOptionPane.showMessageDialog(this, "Cover photo updated successfully.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error updating cover photo: " + ex.getMessage());
            }
        }
    }

    private void saveChanges() {
        String newBio = bioField.getText();
        String newPassword = new String(passwordField.getPassword());

        try {
            profileService.updateBio(currentUser, newBio);
            profileService.updatePassword(currentUser, newPassword);
            databaseManager.updateUser(currentUser);
            JOptionPane.showMessageDialog(this, "Profile updated successfully.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving profile changes: " + ex.getMessage());
        }
    }

    private void styleButton(JButton button, int width, int height) {
        button.setPreferredSize(new Dimension(width, height));
        button.setBackground(new Color(66, 135, 245));
        button.setForeground(Color.BLACK); // Change text color to black
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
    }
}
