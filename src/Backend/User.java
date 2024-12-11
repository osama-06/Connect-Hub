package Backend;
//import java.awt.*;
//import java.util.*;

import java.io.File;

import java.util.List;

import javax.print.DocFlavor.STRING;

public class User {
     private String userId;
    private String username;
    private String email;
    private String hashedPassword;
    private String bio;
    private String profilePhoto;
    private String coverPhoto;
    private String status; // "online" or "offline"
    private List<Friend> friends;
    private List<Post> posts;

                

           public User (){}
        public User(String userId, String username, String email, String hashedPassword, 
                    String bio, String profilePhoto, String coverPhoto, String status, 
                    List<Friend> friends, List<Post> posts) {
            this.userId = userId;
            this.username = username;
            this.email = email;
            this.hashedPassword = hashedPassword;
            this.bio = bio;
            this.profilePhoto = profilePhoto;
            this.coverPhoto = coverPhoto;
            this.status = status;
            this.friends = friends;
            this.posts = posts;
        }
    

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
   

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    // Utility Methods
    public void addFriend(Friend friend) {
        if (!friends.contains(friend)) {
            friends.add(friend);
        }
    }

    public void removeFriend(Friend friend) {
        friends.remove(friend);
    }

    public void addPost(Post post) {
        posts.add(post);
    }

    public void removePost(Post post) {
        posts.remove(post);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", bio='" + bio + '\'' +
                ", status='" + status + '\'' +
                '}';
    }


}
