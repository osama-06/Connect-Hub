package Frontend;

import Backend.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GroupManagement extends JFrame {
    private GroupService groupService;
    private User currentUser;

    private JList<String> groupList;
    private DefaultListModel<String> groupListModel;

    public GroupManagement(GroupService groupService, User currentUser) throws IOException {
        this.groupService = groupService;
        this.currentUser = currentUser;

        setTitle("Group Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Manage Your Groups", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(headerLabel, BorderLayout.NORTH);

        // Group List Panel
        JPanel groupListPanel = new JPanel(new BorderLayout());
        groupListPanel.setBorder(BorderFactory.createTitledBorder("Your Groups"));

        groupListModel = new DefaultListModel<>();
        groupList = new JList<>(groupListModel);
        JScrollPane groupScrollPane = new JScrollPane(groupList);
        groupListPanel.add(groupScrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh Groups");
        refreshButton.addActionListener(e -> {
            try {
                loadGroups();
            } catch (IOException ex) {
                Logger.getLogger(GroupManagement.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        groupListPanel.add(refreshButton, BorderLayout.SOUTH);

        add(groupListPanel, BorderLayout.CENTER);

        // Action Panel
        JPanel actionPanel = new JPanel(new GridLayout(5, 1, 10, 10));

        JButton createGroupButton = new JButton("Create Group");
        createGroupButton.addActionListener(e -> createGroup());
        actionPanel.add(createGroupButton);

        JButton joinGroupButton = new JButton("Join Group");
        joinGroupButton.addActionListener(e -> joinGroup());
        actionPanel.add(joinGroupButton);

        JButton leaveGroupButton = new JButton("Leave Group");
        leaveGroupButton.addActionListener(e -> leaveGroup());
        actionPanel.add(leaveGroupButton);

        

        JButton backButton = new JButton("Back to Profile");
        backButton.addActionListener(e -> {
            ProfileManagement profileManagement = new ProfileManagement(groupService.getDatabaseManager(), currentUser);
            profileManagement.setVisible(true);
            this.dispose();
        });
        actionPanel.add(backButton);

        add(actionPanel, BorderLayout.EAST);

        loadGroups();
    }

    private void loadGroups() throws IOException {
        groupListModel.clear();
        List<Group> groups = groupService.getGroupsForUser(currentUser);
        for (Group group : groups) {
            groupListModel.addElement(group.getName());
        }
    }

    private void createGroup() {
        String name = JOptionPane.showInputDialog(this, "Enter Group Name:");
        String description = JOptionPane.showInputDialog(this, "Enter Group Description:");
        if (name != null && description != null && !name.isEmpty() && !description.isEmpty()) {
            try {
                Group group = groupService.createGroup(name, description, null, currentUser);
                JOptionPane.showMessageDialog(this, "Group created successfully!");
                loadGroups();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error creating group: " + e.getMessage());
            }
        }
    }

    private void joinGroup() {
        String groupName = JOptionPane.showInputDialog(this, "Enter Group Name to Join:");
        if (groupName != null && !groupName.isEmpty()) {
            try {
                Group group = groupService.findGroupByName(groupName);
                if (group != null) {
                    groupService.addUserToGroup(currentUser, group);
                    JOptionPane.showMessageDialog(this, "Joined group successfully!");
                    loadGroups();
                } else {
                    JOptionPane.showMessageDialog(this, "Group not found.");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error joining group: " + e.getMessage());
            }
        }
    }

    private void leaveGroup() {
        String groupName = groupList.getSelectedValue();
        if (groupName != null) {
            try {
                Group group = groupService.findGroupByName(groupName);
                if (group != null) {
                    groupService.removeUserFromGroup(currentUser, group);
                    JOptionPane.showMessageDialog(this, "Left group successfully!");
                    loadGroups();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error leaving group: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a group to leave.");
        }
    }

    
}
