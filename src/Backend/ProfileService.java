package Backend;

import java.io.IOException;

public class ProfileService {
    

      private DatabaseManager databaseManager;

    public ProfileService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    // Change the user's profile photo
    public void changeProfilePhoto(User user, String newPhotoPath) throws IOException {
        if (newPhotoPath == null || newPhotoPath.isEmpty()) {
            throw new IllegalArgumentException("Profile photo path cannot be empty.");
        }
        user.setProfilePhoto(newPhotoPath);
        databaseManager.updateUser(user); // Save the updated user to the file
        System.out.println("Profile photo updated successfully.");
    }

    // Change the user's cover photo
    public void changeCoverPhoto(User user, String newPhotoPath) throws IOException {
        if (newPhotoPath == null || newPhotoPath.isEmpty()) {
            throw new IllegalArgumentException("Cover photo path cannot be empty.");
        }
        user.setCoverPhoto(newPhotoPath);
        databaseManager.updateUser(user);
        System.out.println("Cover photo updated successfully.");
    }

    // Update the user's bio
    public void updateBio(User user, String newBio) throws IOException {
        if (newBio == null || newBio.trim().isEmpty()) {
            throw new IllegalArgumentException("Bio cannot be empty.");
        }
        user.setBio(newBio);
        databaseManager.updateUser(user);
        System.out.println("Bio updated successfully.");
    }

    // Update the user's password
    public void updatePassword(User user, String newPassword) throws IOException {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        // Here, for simplicity, we won't hash the password. In a real-world app, always hash passwords!
        user.setHashedPassword(newPassword);
        databaseManager.updateUser(user);
        System.out.println("Password updated successfully.");}
        // Method to create a post
    public void createPost(User user, String text, String imagePath) throws IOException {
        Post newPost = new Post(text, imagePath);
        user.addPost(newPost);
        databaseManager.updateUser(user);  // Save the updated user (with new post)
    }

    // Method to delete a post
    public void deletePost(User user, Post post) throws IOException {
        user.removePost(post);
        databaseManager.updateUser(user);  // Save the updated user (without deleted post)
    }


}
