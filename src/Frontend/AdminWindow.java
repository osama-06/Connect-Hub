package Frontend;

import Backend.Group;
import Backend.GroupManager;
import Backend.Gpost;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminWindow extends JFrame {
    String author;
    private GroupManager groupManager;
    private Group group;
    private JTextArea postsArea;

    public AdminWindow(GroupManager groupManager, Group group, String username) {
        author = username;
        this.groupManager = groupManager;
        this.group = group;

        setTitle("Admin Panel - " + group.getGroupName());
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Admin Panel", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(headerLabel, BorderLayout.NORTH);

        // Actions panel
        JPanel actionsPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton approveDeclineButton = new JButton("Approve/Decline Membership Requests");
        approveDeclineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                approveDeclineRequests();
            }
        });
        actionsPanel.add(approveDeclineButton);

        JButton removeMemberButton = new JButton("Remove Members");
        removeMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeMembers();
            }
        });
        actionsPanel.add(removeMemberButton);

        JButton managePostsButton = new JButton("Manage Posts");
        managePostsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                managePosts();
            }
        });
        actionsPanel.add(managePostsButton);

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
    

    private void approveDeclineRequests() {
        // Assuming the Group class has a method to get pending membership requests.
        List<String> pendingRequests = group.getPendingMembershipRequests();

        if (pendingRequests.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No pending membership requests.");
        } else {
            StringBuilder requestList = new StringBuilder("Pending Requests:\n");
            for (String request : pendingRequests) {
                requestList.append(request).append("\n");
            }

            String selectedUser = (String) JOptionPane.showInputDialog(this,
                    requestList.toString(), "Select User to Approve/Decline", JOptionPane.QUESTION_MESSAGE,
                    null, pendingRequests.toArray(), pendingRequests.get(0));

            if (selectedUser != null) {
                int choice = JOptionPane.showConfirmDialog(this,
                        "Approve membership request for " + selectedUser + "?",
                        "Approve Request", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    groupManager.approveMembershipRequest(group.getGroupName(), selectedUser);
                    JOptionPane.showMessageDialog(this, "Membership request approved.");
                } else {
                    groupManager.declineMembershipRequest(group.getGroupName(), selectedUser);
                    JOptionPane.showMessageDialog(this, "Membership request declined.");
                }
            }
        }
    }

    private void removeMembers() {
        // Display list of members
        List<String> members = group.getMembers();
        String selectedMember = (String) JOptionPane.showInputDialog(this,
                "Select member to remove:", "Remove Member", JOptionPane.QUESTION_MESSAGE,
                null, members.toArray(), members.get(0));

        if (selectedMember != null) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to remove " + selectedMember + " from the group?",
                    "Remove Member", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                groupManager.removeUserFromGroup(group.getGroupName(), selectedMember);
                JOptionPane.showMessageDialog(this, selectedMember + " has been removed.");
            }
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
}
