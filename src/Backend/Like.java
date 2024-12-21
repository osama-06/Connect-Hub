package Backend;

import java.util.Date;

public class Like {
    private String likeID;    // Unique ID for the like
    private User user;        // User who liked the post
    private Date timestamp;   // Time the like was created

    public Like(String likeID, User user) {
        this.likeID = likeID;
        this.user = user;
        this.timestamp = new Date(); // Set current time
    }

    // Getters and Setters
    public String getLikeID() {
        return likeID;
    }

    public void setLikeID(String likeID) {
        this.likeID = likeID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Like by " + user.getUsername() + " at " + timestamp;
    }
}
