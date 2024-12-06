
package Backend;

import java.util.List;
import java.util.UUID;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean signup(String email, String username, String password, String dateOfBirth) {
        if (userRepository.findUserByEmail(email) != null) {
            return false; // Email already exists
        }
        String hashedPassword = HashUtils.hashPassword(password);
        User user = new User(UUID.randomUUID().toString(), email, username, hashedPassword, dateOfBirth, "offline");
        userRepository.addUser(user);
        return true;
    }

    public User login(String email, String password) {
        User user = userRepository.findUserByEmail(email);
        if (user != null && HashUtils.verifyPassword(password, user.getPassword())) {
            user.setStatus("online");
            userRepository.updateUser(user);
            return user;
        }
        return null; // Invalid credentials
    }

    public void logout(User user) {
        user.setStatus("offline");
        userRepository.updateUser(user);
    }
}
