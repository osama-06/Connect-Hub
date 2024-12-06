package Backend;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FriendRepository friendRepository;
    
    private User currentUser; // Field to hold the current logged-in user

    public UserService(UserRepository userRepository, PostRepository postRepository, FriendRepository friendRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.friendRepository = friendRepository;
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
            this.currentUser = user; // Set current user upon login
            return user;
        }
        return null; // Invalid credentials
    }

    public void logout() {
        if (currentUser != null) {
            currentUser.setStatus("offline");
            userRepository.updateUser(currentUser);
            currentUser = null; // Clear the current user on logout
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // Add method for changing profile photo
    public void changeProfilePhoto(User user, String newPhotoPath) throws IOException {
        user.setProfilePhotoPath(newPhotoPath);
        userRepository.updateUser(user); // Assuming the user repository has an update method
    }

    // Add method for changing cover photo
    public void changeCoverPhoto(User user, String newPhotoPath) throws IOException {
        user.setCoverPhotoPath(newPhotoPath);
        userRepository.updateUser(user); // Assuming the user repository has an update method
    }

    // Update bio method
    public void updateBio(User user, String newBio) {
        user.setBio(newBio);
        userRepository.updateUser(user);
    }

    // Update password method
    public void updatePassword(User user, String newPassword) {
        user.setPassword(HashUtils.hashPassword(newPassword)); // Hashing the new password
        userRepository.updateUser(user);
    }

    // Fetch posts for a user
    public List<Post> getUserPosts(String userId) {
        return postRepository.getUserPosts(userId);
    }

    // Fetch friends list
    public List<Friend> getUserFriends(String userId) {
        return friendRepository.getFriends(userId);
    }
}
