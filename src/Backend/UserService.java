package Backend;

import java.util.List;
import java.util.UUID;

public class UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FriendRepository friendRepository;

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
            return user;
        }
        return null; // Invalid credentials
    }

    public void logout(User user) {
        user.setStatus("offline");
        userRepository.updateUser(user);
    }

    // Update profile details
    public boolean updateProfile(String userId, String bio, String profilePhotoPath, String coverPhotoPath) {
        User user = userRepository.findUserByEmail(userId);
        if (user == null) {
            return false;
        }
        if (bio != null) user.setBio(bio);
        if (profilePhotoPath != null) user.setProfilePhotoPath(profilePhotoPath);
        if (coverPhotoPath != null) user.setCoverPhotoPath(coverPhotoPath);
        userRepository.updateUser(user);
        return true;
    }

    // Change password
    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        User user = userRepository.findUserByEmail(userId);
        if (user == null || !HashUtils.verifyPassword(oldPassword, user.getPassword())) {
            return false;
        }
        user.setPassword(HashUtils.hashPassword(newPassword));
        userRepository.updateUser(user);
        return true;
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
