package Backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupManager {
    private Map<String, Group> groups;  // Use groupName as the key

    public GroupManager() {
        this.groups = new HashMap<>();
        loadFromDatabase();  // Load existing data from the JSON file
    }

    public Map<String, Group> getGroups() {
        return groups;
    }

    // Create a new group
    public Group createGroup(String groupName, String description, String groupPhoto, String primaryAdmin) {
        if (groups.containsKey(groupName)) {
            return null;  // Group with the same name already exists
        }
        Group newGroup = new Group(groupName, description, groupPhoto, primaryAdmin);
        groups.put(groupName, newGroup);

        saveToDatabase();  // Save data after creating the group
        return newGroup;
    }

    // Get group by name
    public Group getGroupByName(String groupName) {
        return groups.get(groupName);
    }

    // Add a group
    public void addGroup(Group group) {
        if (group != null && !groups.containsKey(group.getGroupName())) {
            groups.put(group.getGroupName(), group);
            saveToDatabase();  // Save data after adding a group
        }
    }

    // Promote a member to admin
    public boolean promoteMemberToAdmin(String groupName, String memberId) {
        Group group = groups.get(groupName);
        if (group != null && !group.isAdmin(memberId)) {
            group.promoteToAdmin(memberId);
            saveToDatabase();  // Save data after promoting a member
            return true;
        }
        return false;
    }

    // Demote an admin to member
    public boolean demoteAdminToMember(String groupName, String adminId) {
        Group group = groups.get(groupName);
        if (group != null && group.isAdmin(adminId) && !group.isPrimaryAdmin(adminId)) {
            group.demoteToMember(adminId);
            saveToDatabase();  // Save data after demoting a member
            return true;
        }
        return false;
    }

    // Save data to JSON database
    public void saveToDatabase() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Write the 'groups' map to a file with pretty-printing
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("groups.json"), groups);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save data to database.");
        }
    }

// Edit a post in the group by its content
public boolean editPostInGroup(String groupName, String oldContent, String newContent) {
    Group group = groups.get(groupName);
    if (group != null) {
        boolean success = group.editPost(oldContent, newContent);  // Call Group's editPost method with content
        if (success) {
            saveToDatabase();  // Save data after editing the post
        }
        return success;
    }
    return false;  // Return false if group is not found
}


// Delete a post from the group by its content
public boolean deletePostFromGroup(String groupName, String postContent) {
    Group group = groups.get(groupName); // Retrieve the group by name
    if (group != null) {
        boolean success = group.deletePost(postContent); // Call Group's deletePost method with post content
        if (success) {
            saveToDatabase(); // Save data after deleting the post
        }
        return success; // Return whether the deletion was successful
    }
    return false; // Return false if the group is not found
}

    // Add a post to the group
    public boolean addPostToGroup(String groupName, String content, String authorId) {
        Group group = groups.get(groupName);
        if (group != null) {
            group.addPost(content, authorId);
            saveToDatabase();  // Save data after adding a post
            return true;
        }
        return false;  // Group not found, post not added
    }

    // Delete a group
    public boolean deleteGroup(String groupName) {
        if (groups.containsKey(groupName)) {
            groups.remove(groupName);
            saveToDatabase();  // Save data after deleting the group
            return true;
        }
        return false;  // Group not found, deletion failed
    }

    // Load data from the JSON database
    public void loadFromDatabase() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File("groups.json");
            if (file.exists()) {
                groups = objectMapper.readValue(file, objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Group.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load data from database.");
        }
    }

    // Remove a user from a group
    public boolean removeUserFromGroup(String groupName, String userId) {
        Group group = groups.get(groupName);
        if (group != null) {
            if (group.getMembers().contains(userId)) {
                group.removeMember(userId);  // Assuming there's a method to remove a member
                saveToDatabase();  // Save data after removing the user
                return true;  // User successfully removed
            }
        }
        return false;  // Group not found or user not part of the group
    }

    // Get a list of groups a user is part of
    public List<Group> getUserGroups(String userId) {
        List<Group> userGroups = new ArrayList<>();
        for (Group group : groups.values()) {
            if (group.getMembers().contains(userId)) {
                userGroups.add(group);  // Add the group to the user's list if they are a member
            }
        }
        return userGroups;
    }

    // Add a member to a group
    public boolean addMemberToGroup(String groupName, String memberId) {
        Group group = groups.get(groupName);
        if (group != null) {
            group.addMember(memberId);  // Assuming there's a method to add a member
            saveToDatabase();  // Save data after adding the member
            return true;
        }
        return false;  // Group not found, member not added
    }

    // Remove a member from a group
    public boolean removeMemberFromGroup(String groupName, String memberId) {
        Group group = groups.get(groupName);
        if (group != null && group.getMembers().contains(memberId)) {
            group.removeMember(memberId);
            saveToDatabase();  // Save data after removing the member
            return true;
        }
        return false;  // Member not found in the group
    }

    // Get the names of all groups
    public List<String> getGroupNames() {
        return new ArrayList<>(groups.keySet());  // Return a list of all group names
    }

    public String[] getGroupPosts(String groupName) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Load the JSON file
            File file = new File("groups.json");
            if (!file.exists()) {
                return new String[0];  // Return an empty array if the file doesn't exist
            }

            // Parse the JSON file into a JsonNode object
            JsonNode rootNode = objectMapper.readTree(file);

            // Find the group by its name
            JsonNode groupNode = rootNode.get(groupName);
            if (groupNode == null) {
                return new String[0];  // Return an empty array if the group is not found
            }

            // Get the posts array for the group
            JsonNode postsNode = groupNode.get("posts");
            if (postsNode == null || !postsNode.isArray()) {
                return new String[0];  // Return an empty array if there are no posts
            }

            // Create an array to hold the post contents
            String[] postContents = new String[postsNode.size()];

            // Extract the content of each post
            for (int i = 0; i < postsNode.size(); i++) {
                JsonNode postNode = postsNode.get(i);
                String content = postNode.get("content").asText();  // Assuming each post has a "content" field
                postContents[i] = content;
            }

            return postContents;  // Return the array of post contents

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to read from database.");
        }
        return new String[0];  // Return an empty array if there's an error
    }
    
    
    
    // Add a membership request to the group
    public void addMembershipRequest(String groupName, String username) {
        Group group = getGroupByName(groupName);
        if (group != null) {
            group.addPendingMembershipRequest(username);
        }
    }

    // Approve a membership request for a group
    public void approveMembershipRequest(String groupName, String username) {
        Group group = getGroupByName(groupName);
        if (group != null) {
            group.addMember(username);  // Add the user to the members list
            group.removePendingMembershipRequest(username);  // Remove the request
        }
    }

    // Decline a membership request for a group
    public void declineMembershipRequest(String groupName, String username) {
        Group group = getGroupByName(groupName);
        if (group != null) {
            group.removePendingMembershipRequest(username);  // Remove the request
        }
    }
}
