  package Backend;
public class Notification {
    private String message;
    private String senderUsername;
    private String type;  // e.g., "friend_request", "new_post"
    private String timestamp;
    private boolean isRead;  // Field to track if the notification is read

    // Constructor
    public Notification(String message, String senderUsername, String type, String timestamp) {
        this.message = message;
        this.senderUsername = senderUsername;
        this.type = type;
        this.timestamp = timestamp;
        this.isRead = false; // Notifications are unread by default
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return isRead;  // Return the value of isRead
    }

    public void markAsRead() {
        this.isRead = true;  // Set isRead to true when marked as read
    }

    @Override
    public String toString() {
        return "Notification{" +
                "message='" + message + '\'' +
                ", senderUsername='" + senderUsername + '\'' +
                ", type='" + type + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", isRead=" + isRead +
                '}';
    }
}
