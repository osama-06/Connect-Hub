package Frontend;

import Backend.Group;
import Backend.GroupManager;
import Backend.Gpost;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class NormalUserPanel extends JFrame {
    private GroupManager groupManager;
    private String currentUserId;  // To identify the logged-in user
    private JComboBox<Group> groupListComboBox;
    private JTextArea postContentTextArea;
    private JButton postButton, leaveGroupButton;
    private JTextArea postsArea; // To display posts from the group
    private JPanel panel;

    public NormalUserPanel(GroupManager groupManager, String userId) {
        this.groupManager = groupManager;
        this.currentUserId = userId;

        // Set up the UI components
        setTitle("Normal User Panel");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // List of groups the user is part of
        groupListComboBox = new JComboBox<>();
        loadUserGroups();
        groupListComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Group selectedGroup = (Group) groupListComboBox.getSelectedItem();
                if (selectedGroup != null) {
                    loadPosts(selectedGroup); // Load posts for the selected group
                }
            }
        });
        panel.add(new JLabel("Select a Group:"));
        panel.add(groupListComboBox);

        // Display posts from the selected group
        postsArea = new JTextArea(10, 40);
        postsArea.setEditable(false);
        panel.add(new JLabel("Group Posts:"));
        panel.add(new JScrollPane(postsArea));

        // Post content input area
        postContentTextArea = new JTextArea(5, 40);
        panel.add(new JLabel("Enter your post:"));
        panel.add(new JScrollPane(postContentTextArea));

        // Button to post content
        postButton = new JButton("Post Content");
        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Group selectedGroup = (Group) groupListComboBox.getSelectedItem();
                if (selectedGroup != null && !postContentTextArea.getText().isEmpty()) {
                    if (groupManager.addPostToGroup(selectedGroup.getGroupName(), postContentTextArea.getText(), currentUserId)) {
                        postContentTextArea.setText(""); // Clear text after posting
                        loadPosts(selectedGroup); // Refresh posts
                    } else {
                        JOptionPane.showMessageDialog(panel, "Failed to post content.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        panel.add(postButton);

        // Button to leave the group
        leaveGroupButton = new JButton("Leave Group");
        leaveGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Group selectedGroup = (Group) groupListComboBox.getSelectedItem();
                if (selectedGroup != null) {
                    if (groupManager.removeUserFromGroup(selectedGroup.getGroupName(), currentUserId)) {
                        loadUserGroups(); // Refresh the group list after leaving the group
                        postsArea.setText(""); // Clear the posts area
                    } else {
                        JOptionPane.showMessageDialog(panel, "Failed to leave the group.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        panel.add(leaveGroupButton);

        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Load the groups the user is part of
    private void loadUserGroups() {
        // Fetch groups the user is part of
        List<Group> userGroups = groupManager.getUserGroups(currentUserId);

        // Update the group list in the UI using DefaultComboBoxModel
        DefaultComboBoxModel<Group> groupModel = new DefaultComboBoxModel<>();
        for (Group group : userGroups) {
            groupModel.addElement(group);
        }
        groupListComboBox.setModel(groupModel);

        // Ensure posts are loaded for the first selected group
        if (!userGroups.isEmpty()) {
            loadPosts(userGroups.get(0)); // Automatically load posts for the first group
        }
    }

    // Load posts for the selected group
    private void loadPosts(Group group) {
        StringBuilder postsText = new StringBuilder();
        for (Gpost post : group.getPosts()) {
            postsText.append(post.getAuthorId()).append(": ").append(post.getContent()).append("\n");
        }
        postsArea.setText(postsText.toString());
    }

    public static void main(String[] args) {
        // Simulate creating a GroupManager instance and passing a userId
        GroupManager groupManager = new GroupManager(); // Make sure the GroupManager constructor is properly initialized
        String userId = "user123";  // Example user ID
        
        // Create and show the NormalUserPanel
        SwingUtilities.invokeLater(() -> new NormalUserPanel(groupManager, userId));
    }
}
