package Backend;

public class FriendRequest {
    private User sender;
    private User recipient;
    private String status; // "pending", "accepted", "rejected"

    // Constructor
    public FriendRequest(User sender, User recipient) {
        this.sender = sender;
        this.recipient = recipient;
        this.status = "pending"; // Default status is pending
    }

    // Getters and Setters
    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "sender=" + sender.getUsername() +
                ", recipient=" + recipient.getUsername() +
                ", status='" + status + '\'' +
                '}';
    }
}
