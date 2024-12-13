package Backend;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userid;
    private String username;
    private String email;
    private String password; // Hashed password
    private String bio;
    private String profilePhoto;
    private String coverPhoto;
    private String status; // online or offline
    private List<Friend> friends;
    private List<Post> posts;
    private String dateOfBirth;
    private List<Notification> notifications;
    // Lists to track friend requests
    private List<FriendRequest> sentRequests;
    private List<FriendRequest> receivedRequests;

    // Constructor
    public User(String userid, String email, String username, String password, String dateOfBirth, String status) {
        this.userid = userid;
        this.email = email;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.status = status;
        this.sentRequests = new ArrayList<>();
        this.receivedRequests = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.posts = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }

    // Constructor for full initialization
    public User(String userid, String username, String email, String password, String bio, String profilePhoto, 
                String coverPhoto, String status, List<Friend> friends, List<Post> posts, String dateOfBirth) {
        this.userid = userid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.profilePhoto = profilePhoto;
        this.coverPhoto = coverPhoto;
        this.status = status;
        this.friends = friends;
        this.posts = posts;
        this.dateOfBirth = dateOfBirth;
        this.sentRequests = new ArrayList<>();
        this.receivedRequests = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    // Getters and Setters for the new lists and fields
    public List<FriendRequest> getSentRequests() {
        return sentRequests;
    }

    public void setSentRequests(List<FriendRequest> sentRequests) {
        this.sentRequests = sentRequests;
    }

    public List<FriendRequest> getReceivedRequests() {
        return receivedRequests;
    }

    public void setReceivedRequests(List<FriendRequest> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

    // Other getters and setters
    public String getUserId() {
        return userid;
    }

    public void setUserId(String userId) {
        this.userid = userId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String hashedPassword) {
        this.password = hashedPassword;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    // Methods for handling friend requests
    public void sendFriendRequest(User recipient) {
        FriendRequest request = new FriendRequest(this, recipient);
        this.sentRequests.add(request);
        recipient.getReceivedRequests().add(request);
    }

    public void acceptFriendRequest(FriendRequest request) {
        if ("pending".equals(request.getStatus())) {
            request.setStatus("accepted");
            addFriend(new Friend(request.getSender().getUsername(), request.getSender().getProfilePhoto()));
            request.getSender().addFriend(new Friend(this.getUsername(), this.getProfilePhoto()));
        }
    }

    public void rejectFriendRequest(FriendRequest request) {
        if ("pending".equals(request.getStatus())) {
            request.setStatus("rejected");
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userid + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", bio='" + bio + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
