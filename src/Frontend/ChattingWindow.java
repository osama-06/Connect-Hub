package Frontend;

import Backend.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChattingWindow extends JFrame {
    private JPanel panel1; // Panel for friends list
    private JButton openChatButton;
    private String currentUser;
    private JList<String> friendsList;
    private List<String> friends; // List to store the current user's friends

    // Constructor that accepts currentUser as a parameter
    public ChattingWindow(String currentUser) {
        this.currentUser = currentUser;
        this.friends = new ArrayList<>();

        // Set up the JFrame
        setTitle("Chatting Window - User: " + currentUser);
        setSize(600, 400); // Adjust the window size to fit the panel
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize panel1 (only one panel now for the friends list)
        panel1 = new JPanel();
        panel1.setBackground(Color.LIGHT_GRAY); // Left panel color (light gray)
        panel1.setLayout(new BorderLayout()); // Use BorderLayout for better layout control

        // Initialize the friends list and load friends
        loadFriends();

        // Create a JList to display friends in panel1
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String friend : friends) {
            listModel.addElement(friend); // Add each friend to the list model
        }
        friendsList = new JList<>(listModel);
        friendsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane friendsScrollPane = new JScrollPane(friendsList);
        panel1.add(friendsScrollPane, BorderLayout.CENTER); // Add JList to the panel

        // Initialize button
        openChatButton = new JButton("Open Chat");
        openChatButton.addActionListener(e -> openChat());

        // Add components to the JFrame
        add(panel1, BorderLayout.CENTER); // Add the friends list panel to the center
        add(openChatButton, BorderLayout.SOUTH); // Button at the bottom

        setVisible(true);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    // Method to load the current user's friends from the JSON file
    private void loadFriends() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode users = objectMapper.readTree(new File("users.json"));

            // Iterate over each user in the JSON array
            for (JsonNode user : users) {
                // Check if the current user matches the specified username
                if (user.get("username").asText().equals(currentUser)) {
                    // Get the friends array of the user
                    JsonNode friendsNode = user.get("friends");

                    // Check if the user has friends and load them
                    if (friendsNode.isArray() && friendsNode.size() > 0) {
                        for (JsonNode friend : friendsNode) {
                            friends.add(friend.get("username").asText()); // Add each friend's username to the list
                        }
                    }
                    return; // Exit after processing the target user
                }
            }
            System.out.println("User " + currentUser + " not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openChat() {
        int selectedIndex = friendsList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedFriend = friendsList.getModel().getElementAt(selectedIndex);
            // Here, you can open the chat window with the selected friend
            System.out.println("Starting chat with: " + selectedFriend);

            // Test the ChattingChatWindow with dummy data
            User user = new User();
            user.setUsername(currentUser);

            User friend = new User();
            friend.setUsername(selectedFriend);

            ChattingChatWindow chatWindow = new ChattingChatWindow(friend, user);
            chatWindow.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a friend to start a chat.");
        }
    }


}
