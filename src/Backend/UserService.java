
package Backend;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    // Signup method with email validation, password hashing, and saving the user to users.json
    public boolean signup(String email, String username, String password, String dob) {
        // Check if the email already exists
        if (userRepository.findUserByEmail(email) != null) {
            return false; // Email already exists
        }

        // Create a new User object with hashed password
        String userId = generateUserId(); // You can implement a method to generate a unique user ID
        User newUser = new User(userId, email, username, HashUtils.hashPassword(password), dob, "offline");

        // Add the user to the repository and save to the file
        userRepository.addUser(newUser);

        return true; // Signup successful
    }

    // Generate a unique user ID (could be based on current time or UUID)
    private String generateUserId() {
        // Implement your logic for generating a unique user ID, e.g., using UUID or current timestamp
        return String.valueOf(System.currentTimeMillis());
    }

    // Login method to verify user credentials (email and password)
    public User login(String email, String password) {
        User user = userRepository.findUserByEmail(email);
        if (user != null && HashUtils.verifyPassword(password, user.getPassword())) {
            return user; // Login successful
        }
        return null; // Invalid credentials
    }
}

