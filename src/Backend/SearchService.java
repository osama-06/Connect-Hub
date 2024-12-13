package Backend;

import java.util.List;
import java.util.stream.Collectors;

public class SearchService {

    private final DatabaseManager databaseManager;

    public SearchService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    // Search users by username
    public List<User> searchUsersByUsername(String username) {
        return databaseManager.getUsers().stream()
                .filter(user -> user.getUsername().toLowerCase().contains(username.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Search users by email
    public List<User> searchUsersByEmail(String email) {
        return databaseManager.getUsers().stream()
                .filter(user -> user.getEmail().toLowerCase().contains(email.toLowerCase()))
                .collect(Collectors.toList());
    }
}
