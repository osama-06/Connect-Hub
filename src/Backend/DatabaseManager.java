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
import java.util.UUID;

public class DatabaseManager {

    private static final String FILE_PATH = "users.json";
    private List<User> users;

    public DatabaseManager() {
        loadUsers();
    }

    // Load users from JSON file
    private void loadUsers() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type userListType = new TypeToken<ArrayList<User>>() {}.getType();
            users = new Gson().fromJson(reader, userListType);
            if (users == null) users = new ArrayList<>();
        } catch (IOException e) {
            users = new ArrayList<>();
        }
    }

    // Save users to JSON file
    private void saveUsers() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            new Gson().toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add a new user
    public void addUser(User user) {
        users.add(user);
        saveUsers();
    }

    // Find a user by email
    public Optional<User> findUserByEmail(String email) {
        return users.stream().filter(user -> user.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    // Update user information
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
}