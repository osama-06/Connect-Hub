package Backend;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final String FILE_PATH = "users.json";
    private List<User> users;

    public UserRepository() {
        initializeFile();
        this.users = loadUsers();
    }

    // Load users from JSON file
    private List<User> loadUsers() {
    File file = new File(FILE_PATH);

    // Check if the file exists and is not empty
    if (!file.exists() || file.length() == 0) {
        return new ArrayList<>(); // Return an empty list
    }

    try (Reader reader = new FileReader(file)) {
        Type listType = new TypeToken<ArrayList<User>>() {}.getType();
        List<User> loadedUsers = new Gson().fromJson(reader, listType);

        // If the file contains invalid JSON, return an empty list
        return loadedUsers != null ? loadedUsers : new ArrayList<>();
    } catch (IOException e) {
        e.printStackTrace();
        return new ArrayList<>();
    }
}


    // Save users to JSON file
    private void saveUsers() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            new Gson().toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addUser(User user) {
        users.add(user);
        saveUsers();
    }

    public User findUserByEmail(String email) {
        return users.stream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
    }

    public void updateUser(User user) {
        users.removeIf(u -> u.getUserid().equals(user.getUserid()));
        users.add(user);
        saveUsers();
    }
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