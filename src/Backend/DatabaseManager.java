package Backend;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseManager {

    private static final String USERS_FILE_PATH = "users.json";
    private static final String GROUPS_FILE_PATH = "groups.json";
    private List<User> users;
    private List<Group> groups;

    public DatabaseManager() {
        loadUsers();
        loadGroups();
    }

    // Load users from JSON file
    private void loadUsers() {
        try (FileReader reader = new FileReader(USERS_FILE_PATH)) {
            Type userListType = new TypeToken<ArrayList<User>>() {}.getType();
            users = new Gson().fromJson(reader, userListType);
            if (users == null) users = new ArrayList<>();
        } catch (IOException e) {
            users = new ArrayList<>();
        }
    }

    // Save users to JSON file
    private void saveUsers() {
        try (FileWriter writer = new FileWriter(USERS_FILE_PATH)) {
            new Gson().toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load groups from JSON file
    private void loadGroups() {
        try (FileReader reader = new FileReader(GROUPS_FILE_PATH)) {
            Type groupListType = new TypeToken<ArrayList<Group>>() {}.getType();
            groups = new Gson().fromJson(reader, groupListType);
            if (groups == null) groups = new ArrayList<>();
        } catch (IOException e) {
            groups = new ArrayList<>();
        }
    }

    // Save groups to JSON file
    private void saveGroups() {
        try (FileWriter writer = new FileWriter(GROUPS_FILE_PATH)) {
            new Gson().toJson(groups, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get all groups for a user
    public List<Group> getGroupsForUser(User user) throws IOException {
        List<Group> userGroups = new ArrayList<>();
        for (Group group : groups) {
            if (group.getMembers().contains(user)) {  // Assuming Group has a method getMembers() that returns a list of members
                userGroups.add(group);
            }
        }
        return userGroups;
    }

    // Find a group by name
    public Group getGroupByName(String groupName) throws IOException {
        return findGroupByName(groupName).orElse(null);  // Return null if the group is not found
    }

    // User Management
    public void addUser(User user) {
        users.add(user);
        saveUsers();
    }

    public Optional<User> findUserByEmail(String email) {
        return users.stream().filter(user -> user.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    public void updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equalsIgnoreCase(updatedUser.getEmail())) {
                users.set(i, updatedUser);
                saveUsers();
                break;
            }
        }
    }

    public List<User> getUsers() {
        return users;
    }

    // Group Management
    public void saveGroup(Group group) {
        groups.add(group);
        saveGroups();
    }

    public Optional<Group> findGroupByName(String name) {
        return groups.stream().filter(group -> group.getName().equalsIgnoreCase(name)).findFirst();
    }

    public void updateGroup(Group updatedGroup) {
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).getName().equalsIgnoreCase(updatedGroup.getName())) {
                groups.set(i, updatedGroup);
                saveGroups();
                break;
            }
        }
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void deleteGroup(Group group) {
        groups.removeIf(g -> g.getName().equalsIgnoreCase(group.getName()));
        saveGroups();
    }

    // Group Posts Management
    public void addPostToGroup(Post post, Group group) {
        group.addPost(post);
        updateGroup(group);
    }

    public void deletePostFromGroup(Post post, Group group) {
        group.removePost(post);
        updateGroup(group);
    }
   
     // Get a user by username
     public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;  // Return null if user is not found
    }


    
}
