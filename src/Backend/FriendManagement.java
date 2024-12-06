/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend;

/**
 *
 * @author Abdelrahman
 */
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FriendManagement {
    private static final String FRIENDS_FILE = "friends.json";
    private static final String REQUESTS_FILE = "friend_requests.json";
    private static final String BLOCKED_FILE = "blocked.json";
    private static final String STATUS_FILE = "status.json";  
    private static final String USERS_FILE = "users.json";  

    private final ObjectMapper objectMapper = new ObjectMapper();
    private String currentUserId;  // Store the current user ID

    // Constructor that takes currentUserId as input
    public FriendManagement(String currentUserId) {
        this.currentUserId = currentUserId; 
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    // Method to send a friend request
    public void sendFriendRequest(String receiverId) throws IOException {
        Map<String, List<String>> friendRequests = loadFriendRequests();

        friendRequests.putIfAbsent(receiverId, new ArrayList<>());
        if (!friendRequests.get(receiverId).contains(currentUserId)) {
            friendRequests.get(receiverId).add(currentUserId);
        }

        saveFriendRequests(friendRequests);
        System.out.println("Sent friend request from " + currentUserId + " to " + receiverId);
    }

    // Method to accept a friend request
    public void acceptFriendRequest(String senderId) throws IOException {
        Map<String, List<String>> friendRequests = loadFriendRequests();
        Map<String, List<String>> friendships = loadFriendships();

        friendships.putIfAbsent(currentUserId, new ArrayList<>());
        friendships.putIfAbsent(senderId, new ArrayList<>());

        friendships.get(currentUserId).add(senderId);
        friendships.get(senderId).add(currentUserId);

        // Remove the friend request
        if (friendRequests.containsKey(currentUserId)) {
            friendRequests.get(currentUserId).remove(senderId);
            if (friendRequests.get(currentUserId).isEmpty()) {
                friendRequests.remove(currentUserId);
            }
        }

        saveFriendRequests(friendRequests);
        saveFriendships(friendships);
        System.out.println("Accepted friend request from " + senderId + " to " + currentUserId);
    }

    // Method to decline a friend request
    public void declineFriendRequest(String senderId) throws IOException {
        Map<String, List<String>> friendRequests = loadFriendRequests();

        if (friendRequests.containsKey(currentUserId)) {
            friendRequests.get(currentUserId).remove(senderId);
            if (friendRequests.get(currentUserId).isEmpty()) {
                friendRequests.remove(currentUserId);
            }
        }

        saveFriendRequests(friendRequests);
        System.out.println("Declined friend request from " + senderId);
    }

    // Method to remove a friend
    public void removeFriend(String friendId) throws IOException {
        Map<String, List<String>> friendships = loadFriendships();

        if (friendships.containsKey(currentUserId)) {
            friendships.get(currentUserId).remove(friendId);
            if (friendships.get(currentUserId).isEmpty()) {
                friendships.remove(currentUserId);
            }
        }

        if (friendships.containsKey(friendId)) {
            friendships.get(friendId).remove(currentUserId);
            if (friendships.get(friendId).isEmpty()) {
                friendships.remove(friendId);
            }
        }

        saveFriendships(friendships);
        System.out.println("Removed " + friendId + " from friend list of " + currentUserId);
    }
    
    // Method to block a user
    public void blockFriend(String blockedId) throws IOException {
        Map<String, List<String>> blockedUsers = loadBlockedUsers();

        blockedUsers.putIfAbsent(currentUserId, new ArrayList<>());
        if (!blockedUsers.get(currentUserId).contains(blockedId)) {
            blockedUsers.get(currentUserId).add(blockedId);
        }

        // Remove from friendships if exists
        removeFriend(blockedId);

        saveBlockedUsers(blockedUsers);
        System.out.println("Blocked " + blockedId + " by " + currentUserId);
    }
    
    // Method to unblock a user
    public void unblockUser(String blockedId) throws IOException {
        Map<String, List<String>> blockedUsers = loadBlockedUsers();

        blockedUsers.putIfAbsent(currentUserId, new ArrayList<>());
        if (blockedUsers.get(currentUserId).contains(blockedId)) {
            blockedUsers.get(currentUserId).remove(blockedId);
        }

        saveBlockedUsers(blockedUsers);
        System.out.println("Unblocked " + blockedId + " by " + currentUserId);
    }    

// Method to suggest friends (not already friends or blocked)
    public List<String> suggestFriends() throws IOException {
        Map<String, List<String>> friendships = loadFriendships();
        Map<String, List<String>> blockedUsers = loadBlockedUsers();

        Set<String> allUsers = getAllUsers();
        List<String> friends = friendships.getOrDefault(currentUserId, new ArrayList<>());
        List<String> blocked = blockedUsers.getOrDefault(currentUserId, new ArrayList<>());

        List<String> suggestions = new ArrayList<>();
        for (String user : allUsers) {
            if (!friends.contains(user) && !blocked.contains(user) && !user.equals(currentUserId)) {
                suggestions.add(user);
            }
        }

        return suggestions;
    }

    // Method to get the current user's online/offline status
    public String getCurrentUserStatus() throws IOException {
        Map<String, String> statuses = loadStatuses();
        return statuses.getOrDefault(currentUserId, "Offline");
    }

    // Method to get all users' statuses
    public Map<String, String> getAllUserStatuses() throws IOException {
        return loadStatuses();
    }

    // Load friendships from JSON
    public Map<String, List<String>> loadFriendships() throws IOException {
        return loadJsonFile(FRIENDS_FILE);
    }

    // Load friend requests from JSON
    public Map<String, List<String>> loadFriendRequests() throws IOException {
        return loadJsonFile(REQUESTS_FILE);
    }

    // Load blocked users from JSON
    public Map<String, List<String>> loadBlockedUsers() throws IOException {
        return loadJsonFile(BLOCKED_FILE);
    }

    // Load status from JSON (online/offline)
    public Map<String, String> loadStatuses() throws IOException {
        return loadJsonFile(STATUS_FILE);
    }

    // Generic method to load JSON data
    private <T> T loadJsonFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            return objectMapper.readValue(file, objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        }
        return (T) new HashMap<>();
    }

    // Save friendships to JSON
    private void saveFriendships(Map<String, List<String>> friendships) throws IOException {
        saveJsonFile(FRIENDS_FILE, friendships);
    }

    // Save friend requests to JSON
    private void saveFriendRequests(Map<String, List<String>> friendRequests) throws IOException {
        saveJsonFile(REQUESTS_FILE, friendRequests);
    }

    // Save blocked users to JSON
    private void saveBlockedUsers(Map<String, List<String>> blockedUsers) throws IOException {
        saveJsonFile(BLOCKED_FILE, blockedUsers);
    }

    // Save status to JSON (online/offline)
    private void saveStatuses(Map<String, String> statuses) throws IOException {
        saveJsonFile(STATUS_FILE, statuses);
    }

    // Generic method to save JSON data
    private void saveJsonFile(String fileName, Object data) throws IOException {
        objectMapper.writeValue(new File(fileName), data);
    }
    
    // Method to get all users (load from a JSON file)
    private Set<String> getAllUsers() throws IOException {
    List<String> users = loadJsonFile(USERS_FILE);  
    return new HashSet<>(users);  // Convert it to a Set to avoid duplicates
}

}
