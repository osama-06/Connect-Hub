package Frontend;

import Backend.Gpost;
import Backend.Group;
import Backend.GroupManager;

import javax.swing.*;
import java.awt.*;

public class PrimaryAdminWindow extends JFrame {
    String author;
    private JTextArea postsArea;
    public PrimaryAdminWindow(GroupManager groupManager, Group group, String username) {
        author = username;
        setTitle("Primary Admin Panel - " + group.getGroupName());
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Primary Admin Panel", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(headerLabel, BorderLayout.NORTH);

        // Actions panel
        JPanel actionsPanel = new JPanel(new GridLayout(5, 1, 10, 10));

        JButton promoteDemoteButton = new JButton("Promote/Demote Admins");
        promoteDemoteButton.addActionListener(e -> promoteDemoteAdmins(groupManager, group));
        actionsPanel.add(promoteDemoteButton);

        JButton removeMemberButton = new JButton("Remove Members");
        removeMemberButton.addActionListener(e -> removeMembers(groupManager, group));
        actionsPanel.add(removeMemberButton);

        JButton managePostsButton = new JButton("Manage Posts");
        managePostsButton.addActionListener(e -> managePosts(groupManager, group));
        actionsPanel.add(managePostsButton);

        JButton deleteGroupButton = new JButton("Delete Group");
        deleteGroupButton.addActionListener(e -> deleteGroup(groupManager, group));
        actionsPanel.add(deleteGroupButton);

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
        java.util.List<Gpost> posts = group.getPosts();
        for (int i = 0; i < posts.size(); i++) {
            Gpost post = posts.get(i);
            postsText.append("Post ").append(i + 1).append(":\n")
                     .append("Author: ").append(post.getAuthorId()).append("\n")
                     .append("Content: ").append(post.getContent()).append("\n\n");
        }
        postsArea.setText(postsText.toString());
    }

    private void promoteDemoteAdmins(GroupManager groupManager, Group group) {
        String username = JOptionPane.showInputDialog(this, "Enter the username of the member to promote/demote:");
        if (username != null && !username.trim().isEmpty()) {
            if (group.isAdmin(username)) {
                groupManager.demoteAdminToMember(group.getGroupName(), username);
                JOptionPane.showMessageDialog(this, username + " has been demoted from admin.");
            } else {
                groupManager.promoteMemberToAdmin(group.getGroupName(), username);
                JOptionPane.showMessageDialog(this, username + " has been promoted to admin.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Username cannot be empty.");
        }
    }

    private void removeMembers(GroupManager groupManager, Group group) {
        String username = JOptionPane.showInputDialog(this, "Enter the username of the member to remove:");
        if (username != null && !username.trim().isEmpty()) {
            if (groupManager.removeMemberFromGroup(group.getGroupName(), username)) {
                JOptionPane.showMessageDialog(this, username + " has been removed from the group.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to remove member. Make sure the username is correct.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Username cannot be empty.");
        }
    }

    private void managePosts(GroupManager groupManager, Group group) {
        String[] options = {"Add Post", "Edit Post", "Delete Post"};
        int choice = JOptionPane.showOptionDialog(this, "Choose an action:", "Manage Posts",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0: // Add Post
                addPost(groupManager, group);
                break;
            case 1: // Edit Post
                editPost(groupManager, group);
                break;
            case 2: // Delete Post
                deletePost(groupManager, group);
                break;
            default:
                break;
        }
    }

    private void addPost(GroupManager groupManager, Group group) {
        String postContent = JOptionPane.showInputDialog(this, "Enter the content of the new post:");
        if (postContent != null && !postContent.trim().isEmpty()) {
            if (groupManager.addPostToGroup(group.getGroupName(), postContent, author)) {
                JOptionPane.showMessageDialog(this, "Post added successfully.");
                                // Load the posts into the panel
                loadPosts(group);

                setLocationRelativeTo(null);
                setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add the post.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Post content cannot be empty.");
        }
    }

    private void editPost(GroupManager groupManager, Group group) {
        String[] posts = groupManager.getGroupPosts(group.getGroupName());
        if (posts.length > 0) {
            String postToEdit = (String) JOptionPane.showInputDialog(this, "Select a post to edit:", "Edit Post",
                    JOptionPane.QUESTION_MESSAGE, null, posts, posts[0]);
            if (postToEdit != null) {
                String newContent = JOptionPane.showInputDialog(this, "Edit the content of the post:", postToEdit);
                if (newContent != null && !newContent.trim().isEmpty()) {
                    if (groupManager.editPostInGroup(group.getGroupName(), postToEdit, newContent)) {
                        JOptionPane.showMessageDialog(this, "Post edited successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to edit the post.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Content cannot be empty.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no posts in this group.");
        }
    }

    private void deletePost(GroupManager groupManager, Group group) {
        String[] posts = groupManager.getGroupPosts(group.getGroupName());
        if (posts.length > 0) {
            String postToDelete = (String) JOptionPane.showInputDialog(this, "Select a post to delete:", "Delete Post",
                    JOptionPane.QUESTION_MESSAGE, null, posts, posts[0]);
            if (postToDelete != null) {
                int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this post?", "Delete Post",
                        JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    if (groupManager.deletePostFromGroup(group.getGroupName(), postToDelete)) {
                        JOptionPane.showMessageDialog(this, "Post deleted successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete the post.");
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no posts in this group.");
        }
    }

    private void deleteGroup(GroupManager groupManager, Group group) {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this group?", "Delete Group",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            if (groupManager.deleteGroup(group.getGroupName())) {
                JOptionPane.showMessageDialog(this, "Group deleted successfully.");
                dispose();  // Close the window after deleting the group
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete the group.");
            }
        }
    }
}
