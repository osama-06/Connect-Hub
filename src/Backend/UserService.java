package Backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final DatabaseManager databaseManager;
    private User loggedInUser;  // Store the logged-in user

    public UserService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    

    public boolean signup(String email, String username, String password, String dateOfBirth) {
        if (databaseManager.findUserByEmail(email).isPresent()) {
            return false; // Email already exists
        }
        String hashedPassword = HashUtils.hashPassword(password);

        User user = new User(UUID.randomUUID().toString(), username, email, hashedPassword, null,
                null, null, "offline", new ArrayList<>(),
                new ArrayList<>(), dateOfBirth);
        databaseManager.addUser(user);
        return true;
    }

    public User login(String email, String password) {
        Optional<User> userOptional = databaseManager.findUserByEmail(email);
        if (userOptional.isPresent() && HashUtils.verifyPassword(password, userOptional.get().getPassword())) {
            loggedInUser = userOptional.get(); // Store the logged-in user
            loggedInUser.setStatus("online");
            databaseManager.updateUser(loggedInUser);
            return loggedInUser;
        }
        return null; // Invalid credentials
    }

    public void logout(User user) {
        if (user != null) {
            user.setStatus("offline");
            databaseManager.updateUser(user);
        }
    }

    // Getter for the logged-in user
    public User getLoggedInUser() {
        return loggedInUser;
    }
}
