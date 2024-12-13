package Backend;

import java.util.UUID;
import java.io.IOException;
import java.util.List;

public class GroupService {
    private DatabaseManager databaseManager;

    public GroupService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    // Create a new group
    public Group createGroup(String name, String description, String photoPath, User primaryAdmin) throws IOException {
        Group group = new Group(UUID.randomUUID().toString(), name, description, photoPath, primaryAdmin);
        databaseManager.saveGroup(group);
        return group;
    }

    // Add a user to a group
    public void addUserToGroup(User user, Group group) throws IOException {
        group.addMember(user);
        databaseManager.updateGroup(group);
    }

    // Remove a user from a group
    public void removeUserFromGroup(User user, Group group) throws IOException {
        group.removeMember(user);
        databaseManager.updateGroup(group);
    }

    // Get all groups for a user
    public List<Group> getGroupsForUser(User user) throws IOException {
        return databaseManager.getGroupsForUser(user);
    }

    // Find a group by name
    public Group findGroupByName(String groupName) throws IOException {
        return databaseManager.getGroupByName(groupName);
    }

    // Promote a user to admin
    public void promoteToAdmin(User user, Group group) throws IOException {
        group.addAdmin(user);
        databaseManager.updateGroup(group);
    }

    // Demote an admin to normal user
    public void demoteFromAdmin(User user, Group group) throws IOException {
        group.removeAdmin(user);
        databaseManager.updateGroup(group);
    }

    // Delete a group
    public void deleteGroup(Group group) throws IOException {
        databaseManager.deleteGroup(group);
    }

    // Manage group posts
    public void addPostToGroup(Post post, Group group) throws IOException {
        group.addPost(post);
        databaseManager.updateGroup(group);
    }

    public void deletePostFromGroup(Post post, Group group) throws IOException {
        group.removePost(post);
        databaseManager.updateGroup(group);
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
