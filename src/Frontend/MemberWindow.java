
package Frontend;

import Backend.Group;
import Backend.GroupManager;
import Backend.Gpost;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MemberWindow extends JFrame {
    String author;
    private JTextArea postsArea;

    public MemberWindow(GroupManager groupManager, Group group, String username) {
        author = username;
        setTitle("Member Panel - " + group.getGroupName());
        setSize(500, 600);  // Increased height for posts
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Member Panel", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(headerLabel, BorderLayout.NORTH);

        // Actions panel
        JPanel actionsPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        JButton postContentButton = new JButton("Post Content");
        postContentButton.addActionListener(e -> postContent(groupManager, group));
        actionsPanel.add(postContentButton);

        JButton leaveGroupButton = new JButton("Leave Group");
        leaveGroupButton.addActionListener(e -> leaveGroup(groupManager, group, username));
        actionsPanel.add(leaveGroupButton);

        add(actionsPanel, BorderLayout.CENTER);

        // Posts panel
        JPanel postsPanel = new JPanel(new BorderLayout());
        postsArea = new JTextArea();
        postsArea.setEditable(false);  // Make posts area non-editable

        JScrollPane scrollPane = new JScrollPane(postsArea);
        postsPanel.add(scrollPane, BorderLayout.CENTER);
        add(postsPanel, BorderLayout.SOUTH);

        // Load the posts into the panel
        loadPosts(group);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Load all posts of the group into the JTextArea
    private void loadPosts(Group group) {
        StringBuilder postsText = new StringBuilder();
        List<Gpost> posts = group.getPosts();
        for (int i = 0; i < posts.size(); i++) {
            Gpost post = posts.get(i);
            postsText.append("Post ").append(i + 1).append(":\n")
                     .append("Author: ").append(post.getAuthorId()).append("\n")
                     .append("Content: ").append(post.getContent()).append("\n\n");
        }
        postsArea.setText(postsText.toString());
    }

    private void postContent(GroupManager groupManager, Group group) {
        // Ask the user for the content of the post
        String content = JOptionPane.showInputDialog(this, "Enter your post content:");
        if (content != null && !content.trim().isEmpty()) {
            // Add the post to the group
            groupManager.addPostToGroup(group.getGroupName(), content, author); // Assuming the primary admin is the author for now
            JOptionPane.showMessageDialog(this, "Post added successfully!");

            // Refresh the posts after adding a new one
            loadPosts(group);
        } else {
            JOptionPane.showMessageDialog(this, "Content cannot be empty.");
        }
    }

    private void leaveGroup(GroupManager groupManager, Group group, String username) {
        // Confirm the user wants to leave the group
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to leave the group?", "Leave Group", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            // Remove the user from the group
            if (groupManager.removeMemberFromGroup(group.getGroupName(), username)) {
                JOptionPane.showMessageDialog(this, "You have successfully left the group.");
                dispose();  // Close the window after leaving the group
            } else {
                JOptionPane.showMessageDialog(this, "An error occurred while trying to leave the group.");
            }
        }
    }
}
