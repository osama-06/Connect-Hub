package Frontend;

import Backend.*;
import Backend.DatabaseManager;
import Backend.FriendManagement;
import Backend.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FriendManagementGUI extends JFrame { // Extend JFrame here
    private JButton viewRequestsButton;
    private JButton viewFriendsButton;
    private JButton viewSuggestionsButton;
    private JButton blockRemoveButton;
    private JButton viewBlockedUsersButton;
    private JTextArea displayArea;
    private DatabaseManager databaseManager;
    private User currentUser;
    private FriendManagement friendManagement;
    
    
    /* FriendManagementGUI(DatabaseManager databaseManager, User currentUser) {
        

        // Set JFrame properties directly
        setTitle("Friend Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 400);
        setLayout(new BorderLayout());

        // Create buttons
        viewRequestsButton = new JButton("View and Respond to Friend Requests");
        viewFriendsButton = new JButton("View and Manage Friend List");
        viewSuggestionsButton = new JButton("View and Send Friend Suggestions");
        blockRemoveButton = new JButton("Block or Remove Friends");
        viewBlockedUsersButton = new JButton("View and Unblock Users");

        // Create display area (JTextArea) to show the information
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1));
        buttonPanel.add(viewRequestsButton);
        buttonPanel.add(viewFriendsButton);
        buttonPanel.add(viewSuggestionsButton);
        buttonPanel.add(blockRemoveButton);
        buttonPanel.add(viewBlockedUsersButton);  // Add the new button to the panel

        // Add components to the frame
        add(buttonPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        // Add ActionListeners to buttons
        viewRequestsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAndRespondToRequests();
            }
        });

        viewFriendsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAndManageFriends();
            }
        });

        viewSuggestionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAndSendFriendSuggestions();
            }
        });

        blockRemoveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blockOrRemoveFriend();
            }
        });

        viewBlockedUsersButton.addActionListener(new ActionListener() {  // ActionListener for the new button
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAndUnblockUsers();
            }
        });

        // Show the frame
        setVisible(true);
    }
    */
    public FriendManagementGUI(String currentUserId) {
        friendManagement = new FriendManagement(currentUserId);  // Initialize FriendManagement with current user ID

        // Set JFrame properties directly
        setTitle("Friend Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 400);
        setLayout(new BorderLayout());

        // Create buttons
        viewRequestsButton = new JButton("View and Respond to Friend Requests");
        viewFriendsButton = new JButton("View and Manage Friend List");
        viewSuggestionsButton = new JButton("View and Send Friend Suggestions");
        blockRemoveButton = new JButton("Block or Remove Friends");
        viewBlockedUsersButton = new JButton("View and Unblock Users");

        // Create display area (JTextArea) to show the information
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1));
        buttonPanel.add(viewRequestsButton);
        buttonPanel.add(viewFriendsButton);
        buttonPanel.add(viewSuggestionsButton);
        buttonPanel.add(blockRemoveButton);
        buttonPanel.add(viewBlockedUsersButton);  // Add the new button to the panel

        // Add components to the frame
        add(buttonPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        // Add ActionListeners to buttons
        viewRequestsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAndRespondToRequests();
            }
        });

        viewFriendsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAndManageFriends();
            }
        });

        viewSuggestionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAndSendFriendSuggestions();
            }
        });

        blockRemoveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blockOrRemoveFriend();
            }
        });

        viewBlockedUsersButton.addActionListener(new ActionListener() {  // ActionListener for the new button
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAndUnblockUsers();
            }
        });

        // Show the frame
        setVisible(true);
    }

    

    private void viewAndRespondToRequests() {
        try {
            List<String> friendRequests = friendManagement.loadFriendRequests().getOrDefault(friendManagement.getCurrentUserId(), new ArrayList<>());
            
            if (friendRequests.isEmpty()) {
                displayArea.setText("No pending friend requests.");
            } else {
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                StringBuilder requestText = new StringBuilder("Pending Friend Requests:\n");
                for (String request : friendRequests) {
                    requestText.append(request).append("\n");
                }
                displayArea.setText(requestText.toString());

                JComboBox<String> userComboBox = new JComboBox<>(friendRequests.toArray(new String[0]));
                panel.add(new JLabel("Select a user to respond to:"));
                panel.add(userComboBox);

                JComboBox<String> actionComboBox = new JComboBox<>(new String[]{"Accept", "Decline"});
                panel.add(new JLabel("Select action:"));
                panel.add(actionComboBox);

                int selectedOption = JOptionPane.showConfirmDialog(this, panel, "Respond to Friend Request", JOptionPane.OK_CANCEL_OPTION);

                if (selectedOption == JOptionPane.OK_OPTION) {
                    String selectedUser = (String) userComboBox.getSelectedItem();
                    String selectedAction = (String) actionComboBox.getSelectedItem();

                    if (selectedAction.equals("Accept")) {
                        friendManagement.acceptFriendRequest(selectedUser);
                        JOptionPane.showMessageDialog(this, "Friend request accepted.");
                    } else if (selectedAction.equals("Decline")) {
                        friendManagement.declineFriendRequest(selectedUser);
                        JOptionPane.showMessageDialog(this, "Friend request declined.");
                    }

                    List<String> updatedFriendRequests = friendManagement.loadFriendRequests().getOrDefault(friendManagement.getCurrentUserId(), new ArrayList<>());
                    
                    StringBuilder updatedRequestText = new StringBuilder("Updated Pending Friend Requests:\n");
                    if (updatedFriendRequests.isEmpty()) {
                        updatedRequestText.append("No pending friend requests.");
                    } else {
                        for (String request : updatedFriendRequests) {
                            updatedRequestText.append(request).append("\n");
                        }
                    }
                    displayArea.setText(updatedRequestText.toString());
                }
            }
        } catch (IOException e) {
            displayArea.setText("Error loading friend requests.");
        }
    }

    private void viewAndManageFriends() {
        try {
            List<String> friends = friendManagement.loadFriendships().getOrDefault(friendManagement.getCurrentUserId(), new ArrayList<>());
            if (friends.isEmpty()) {
                displayArea.setText("You have no friends.");
            } else {
                StringBuilder friendsText = new StringBuilder("Your Friends List:\n");
                for (String friend : friends) {
                    friendsText.append(friend).append("\n");
                }
                displayArea.setText(friendsText.toString());

                String friendAction = JOptionPane.showInputDialog(this, "Enter the friend ID to remove:");
                if (friendAction != null) {
                    friendManagement.removeFriend(friendAction);
                    JOptionPane.showMessageDialog(this, "Friend removed.");
                }
            }
        } catch (IOException e) {
            displayArea.setText("Error loading friends list.");
        }
    }

    private void viewAndSendFriendSuggestions() {
        try {
            List<String> suggestions = friendManagement.suggestFriends();
            if (suggestions.isEmpty()) {
                displayArea.setText("No friend suggestions available.");
            } else {
                StringBuilder suggestionsText = new StringBuilder("Friend Suggestions:\n");
                for (String suggestion : suggestions) {
                    suggestionsText.append(suggestion).append("\n");
                }
                displayArea.setText(suggestionsText.toString());

                String sendSuggestion = JOptionPane.showInputDialog(this, "Enter the user ID to send a friend request:");
                if (sendSuggestion != null && !sendSuggestion.trim().isEmpty()) {
                    friendManagement.sendFriendRequest(sendSuggestion);
                    JOptionPane.showMessageDialog(this, "Friend request sent.");
                }
            }
        } catch (IOException e) {
            displayArea.setText("Error loading friend suggestions.");
        }
    }

    private void blockOrRemoveFriend() {
        try {
            String action = JOptionPane.showInputDialog(this, "Enter 'block' to block or 'remove' to remove a friend:");
            if (action != null) {
                String friendId = JOptionPane.showInputDialog(this, "Enter the friend ID:");
                if (action.equals("block")) {
                    friendManagement.blockFriend(friendId);
                    JOptionPane.showMessageDialog(this, "Friend blocked.");
                } else if (action.equals("remove")) {
                    friendManagement.removeFriend(friendId);
                    JOptionPane.showMessageDialog(this, "Friend removed.");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid action. Please enter 'block' or 'remove'.");
                }
            }
        } catch (IOException e) {
            displayArea.setText("Error blocking/removing friend.");
        }
    }

    private void viewAndUnblockUsers() {
        try {
            List<String> blockedUsers = friendManagement.loadBlockedUsers().getOrDefault(friendManagement.getCurrentUserId(), new ArrayList<>());

            if (blockedUsers.isEmpty()) {
                displayArea.setText("You have no blocked users.");
            } else {
                StringBuilder blockedUsersText = new StringBuilder("Blocked Users List:\n");
                for (String user : blockedUsers) {
                    blockedUsersText.append(user).append("\n");
                }
                displayArea.setText(blockedUsersText.toString());

                String unblockUserId = JOptionPane.showInputDialog(this, "Enter the user ID to unblock:");
                if (unblockUserId != null && !unblockUserId.trim().isEmpty()) {
                    friendManagement.unblockUser(unblockUserId);
                    JOptionPane.showMessageDialog(this, "User unblocked.");
                    
                    viewAndUnblockUsers();  // Refresh the blocked users list after unblocking
                }
            }
        } catch (IOException e) {
            displayArea.setText("Error loading blocked users.");
        }
    }

    public static void main(String[] args) {
        new FriendManagementGUI("currentUser");  // Example usage
    }
}
