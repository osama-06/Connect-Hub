
package Backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final DatabaseManager databaseManager;

    public UserService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
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
            User user = userOptional.get();
            user.setStatus("online");
            databaseManager.updateUser(user);
            return user;
        }
        return null; // Invalid credentials
    }

    public void logout(User user) {
        user.setStatus("offline");
        databaseManager.updateUser(user);
    }
}

