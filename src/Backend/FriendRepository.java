package Backend;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FriendRepository {
    private static final String FILE_PATH = "friends.json";
    private List<Friend> friends;

    public FriendRepository() {
        initializeFile(); // Ensure the file exists
        this.friends = loadFriends(); // Load friends from the file
    }

    private List<Friend> loadFriends() {
        File file = new File(FILE_PATH);

        // Check if the file exists and is not empty
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>(); // Return an empty list if the file is empty
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Friend>>() {}.getType();
            List<Friend> loadedFriends = new Gson().fromJson(reader, listType);

            // If the file contains invalid JSON, return an empty list
            return loadedFriends != null ? loadedFriends : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveFriends() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            new Gson().toJson(friends, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFriend(Friend friend) {
        friends.add(friend);
        saveFriends();
    }

    public List<Friend> getFriends() {
        return friends;
    }

    // Fetch friends by a specific user
    public List<Friend> getFriends(String userId) {
        // Filter friends by the user ID
        return friends.stream()
                .filter(friend -> friend.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    // Method to initialize the file if it does not exist
    private void initializeFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try (Writer writer = new FileWriter(file)) {
                writer.write("[]"); // Initialize with an empty JSON array
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
