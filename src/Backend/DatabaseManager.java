package Backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.io.*;

public class DatabaseManager {

    // Save or update a user's data in a file named after their user ID
    public void updateUser(User user) throws IOException {
        String userData = serializeUser(user);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(user.getUserId() + ".txt"))) {
            writer.write(userData);
        }
    }

    // Fetch a user's data from the file
    public User getUserById(String userId) throws IOException {
        File file = new File(userId + ".txt");
        if (!file.exists()) {
            throw new FileNotFoundException("User database not found for user ID: " + userId);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder userData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                userData.append(line).append("\n");
            }
            return deserializeUser(userData.toString());
        }
    }

    // Serialize user to plain text (including posts)
    private String serializeUser(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append(user.getUserId()).append("\n")
          .append(user.getUsername()).append("\n")
          .append(user.getEmail()).append("\n")
          .append(user.getPassword()).append("\n")
          .append(user.getBio()).append("\n")
          .append(user.getProfilePhoto()).append("\n")
          .append(user.getCoverPhoto()).append("\n")
          .append(user.getStatus()).append("\n");
    
        // Serialize posts
        sb.append("Posts:\n");
        for (Post post : user.getPosts()) {
            sb.append(post.getText()).append("||").append(post.getImagePath()).append("\n");
        }

         // Serialize friends
    sb.append("Friends:\n");
    for (Friend friend : user.getFriends()) {
        sb.append(friend.getUsername()).append("||").append(friend.getProfilePhoto()).append("\n");
    }

    return sb.toString();
}
private User deserializeUser(String userData) {
    String[] lines = userData.split("\n");
    User user = new User();
    user.setUserId(lines[0]);
    user.setUsername(lines[1]);
    user.setEmail(lines[2]);
    user.setPassword(lines[3]);
    user.setBio(lines[4]);
    user.setProfilePhoto(lines[5]);
    user.setCoverPhoto(lines[6]);
    user.setStatus(lines[7]);

    // Deserialize posts
    List<Post> posts = new ArrayList<>();
    for (int i = 9; i < lines.length; i++) {
        if (lines[i].startsWith("Posts:")) break;
        String[] postData = lines[i].split("\\|\\|");
        if (postData.length == 2) {
            posts.add(new Post(postData[0], postData[1]));
        }
    }
    user.setPosts(posts);

    // Deserialize friends
    List<Friend> friends = new ArrayList<>();
    for (int i = lines.length - 1; i >= 0; i--) {
        if (lines[i].startsWith("Friends:")) break;
        String[] friendData = lines[i].split("\\|\\|");
        if (friendData.length == 2) {
            friends.add(new Friend(friendData[0], friendData[1]));
        }
    }
    user.setFriends(friends);

    return user;
}
}