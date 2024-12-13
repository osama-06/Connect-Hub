package Frontend;

import Backend.Group;
import Backend.GroupManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class JoinedWindow extends JFrame {
    private GroupManager groupManager;
    private String username;

    public JoinedWindow(GroupManager groupManager, String username) {
        this.groupManager = groupManager;
        this.username = username;

        setTitle("Joined Groups - " + username);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Joined Groups", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(headerLabel, BorderLayout.NORTH);

        // Groups list
        JPanel groupsPanel = new JPanel();
        groupsPanel.setLayout(new BoxLayout(groupsPanel, BoxLayout.Y_AXIS));

        List<Group> joinedGroups = groupManager.getUserGroups(username); // Assume this method exists
        if (joinedGroups.isEmpty()) {
            groupsPanel.add(new JLabel("No joined groups."));
        } else {
            for (Group group : joinedGroups) {
                JButton groupButton = new JButton(group.getGroupName());
                groupButton.addActionListener(e -> openGroupWindow(group));
                groupsPanel.add(groupButton);
            }
        }

        JScrollPane scrollPane = new JScrollPane(groupsPanel);
        add(scrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }
   
    
    
    
    private void openGroupWindow(Group group) {
        String primaryAdmin = group.getPrimaryAdmin();
        List<String> admins = group.getAdmins();

        if (username.equals(primaryAdmin)) {
            new PrimaryAdminWindow(groupManager, group, username);
        } else if (admins.contains(username)) {
            new AdminWindow(groupManager, group, username);
        } else {
            new MemberWindow(groupManager, group, username);
        }

        dispose(); // Close the current window
    }

public static void main(String[] args) {
    // Initialize the GroupManager
    GroupManager groupManager = new GroupManager();

    // Load groups from the JSON file
    try {
        groupManager.loadFromDatabase();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Failed to load groups from the database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Launch the JoinedWindow for user123
    SwingUtilities.invokeLater(() -> new JoinedWindow(groupManager, "admin456"));
}

}
