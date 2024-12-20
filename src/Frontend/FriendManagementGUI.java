
package Frontend;


import Backend.FriendManagement; 
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FriendManagementGUI {
    private JFrame frame;
    private JButton viewRequestsButton;
    private JButton viewFriendsButton;
    private JButton viewSuggestionsButton;
    private JButton blockRemoveButton;
    private JButton viewBlockedUsersButton;
    private JTextArea displayArea;

    private FriendManagement friendManagement;

    public FriendManagementGUI(String currentUserId) {
        friendManagement = new FriendManagement(currentUserId);  // Initialize FriendManagement with current user ID

        frame = new JFrame("Friend Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 400);
        frame.setLayout(new BorderLayout());

        // Create buttons
        viewRequestsButton = new JButton("View and Respond to Friend Requests");
        viewFriendsButton = new JButton("View and Manage Friend List");
        viewSuggestionsButton = new JButton("View and Send Friend Suggestions");
        blockRemoveButton = new JButton("Block or Remove Friends");
        viewBlockedUsersButton = new JButton("View and Unblock Users"); // Add button for viewing blocked users

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
        frame.add(buttonPanel, BorderLayout.WEST);
        frame.add(scrollPane, BorderLayout.CENTER);

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
        frame.setVisible(true);
    }

private void viewAndRespondToRequests() {
    try {
        // Load pending friend requests for the current user
        List<String> friendRequests = friendManagement.loadFriendRequests().getOrDefault(friendManagement.getCurrentUserId(), new ArrayList<>());
        
        // If no friend requests are found
        if (friendRequests.isEmpty()) {
            displayArea.setText("No pending friend requests.");
        } else {
            // Create a panel to hold the user and action dropdowns
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));  // Vertical layout
            
            // Display current list of requests
            StringBuilder requestText = new StringBuilder("Pending Friend Requests:\n");
            for (String request : friendRequests) {
                requestText.append(request).append("\n");
            }
            displayArea.setText(requestText.toString());
            
            // Create JComboBox for selecting a user from friend requests
            JComboBox<String> userComboBox = new JComboBox<>(friendRequests.toArray(new String[0]));
            panel.add(new JLabel("Select a user to respond to:"));
            panel.add(userComboBox);
            
            // Create JComboBox for selecting the action (Accept/Decline)
            JComboBox<String> actionComboBox = new JComboBox<>(new String[]{"Accept", "Decline"});
            panel.add(new JLabel("Select action:"));
            panel.add(actionComboBox);
            
            // Show the panel with the dropdowns in a JOptionPane dialog
            int selectedOption = JOptionPane.showConfirmDialog(frame, panel, "Respond to Friend Request", JOptionPane.OK_CANCEL_OPTION);
            
            // Check if the user clicked OK (not Cancel)
            if (selectedOption == JOptionPane.OK_OPTION) {
                String selectedUser = (String) userComboBox.getSelectedItem();
                String selectedAction = (String) actionComboBox.getSelectedItem();

                // Perform the selected action
                if (selectedAction.equals("Accept")) {
                    friendManagement.acceptFriendRequest(selectedUser);
                    JOptionPane.showMessageDialog(frame, "Friend request accepted.");
                } else if (selectedAction.equals("Decline")) {
                    friendManagement.declineFriendRequest(selectedUser);
                    JOptionPane.showMessageDialog(frame, "Friend request declined.");
                }

                // Update the friend requests list after the action
                List<String> updatedFriendRequests = friendManagement.loadFriendRequests().getOrDefault(friendManagement.getCurrentUserId(), new ArrayList<>());
                
                // Update the display area with the updated list
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

                String friendAction = JOptionPane.showInputDialog(frame, "Enter the friend ID to remove:");
                if (friendAction != null) {
                    friendManagement.removeFriend(friendAction);
                    JOptionPane.showMessageDialog(frame, "Friend removed.");
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

                String sendSuggestion = JOptionPane.showInputDialog(frame, "Enter the user ID to send a friend request:");
                if (sendSuggestion != null && !sendSuggestion.trim().isEmpty()) {
                    friendManagement.sendFriendRequest(sendSuggestion);
                    JOptionPane.showMessageDialog(frame, "Friend request sent.");
                }
            }
        } catch (IOException e) {
            displayArea.setText("Error loading friend suggestions.");
        }
    }

    private void blockOrRemoveFriend() {
        try {
            String action = JOptionPane.showInputDialog(frame, "Enter 'block' to block or 'remove' to remove a friend:");
            if (action != null) {
                String friendId = JOptionPane.showInputDialog(frame, "Enter the friend ID:");
                if (action.equals("block")) {
                    friendManagement.blockFriend(friendId);
                    JOptionPane.showMessageDialog(frame, "Friend blocked.");
                } else if (action.equals("remove")) {
                    friendManagement.removeFriend(friendId);
                    JOptionPane.showMessageDialog(frame, "Friend removed.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid action. Please enter 'block' or 'remove'.");
                }
            }
        } catch (IOException e) {
            displayArea.setText("Error blocking/removing friend.");
        }
    }

public static void main(String[] args) {
    // Ask the user to enter their user ID
    String userId = JOptionPane.showInputDialog(null, "Enter your user ID:", "User ID", JOptionPane.QUESTION_MESSAGE);

    // Check if the user entered an ID (it shouldn't be null or empty)
    if (userId != null && !userId.trim().isEmpty()) {
        // Create the FriendManagementGUI with the entered user ID
        new FriendManagementGUI(userId);
    } else {
        // Show a message if the user didn't provide an ID
        JOptionPane.showMessageDialog(null, "User ID cannot be empty. Exiting application.", "Error", JOptionPane.ERROR_MESSAGE);
        System.exit(0); // Exit the program if no user ID is provided
    }
}

// view and unblock blocked users
    private void viewAndUnblockUsers() {
        try {
            // Load the list of blocked users
            List<String> blockedUsers = friendManagement.loadBlockedUsers().getOrDefault(friendManagement.getCurrentUserId(), new ArrayList<>());

            if (blockedUsers.isEmpty()) {
                displayArea.setText("You have no blocked users.");
            } else {
                StringBuilder blockedUsersText = new StringBuilder("Blocked Users List:\n");
                for (String user : blockedUsers) {
                    blockedUsersText.append(user).append("\n");
                }
                displayArea.setText(blockedUsersText.toString());

                // Ask user for the ID of the blocked user to unblock
                String unblockUserId = JOptionPane.showInputDialog(frame, "Enter the user ID to unblock:");
                if (unblockUserId != null && !unblockUserId.trim().isEmpty()) {
                    friendManagement.unblockUser(unblockUserId);
                    JOptionPane.showMessageDialog(frame, "User unblocked.");
                    
                    // Refresh the blocked users list after unblocking
                    viewAndUnblockUsers();
                }
            }
        } catch (IOException e) {
            displayArea.setText("Error loading blocked users.");
        }
    }


}