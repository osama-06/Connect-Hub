package Backend;
import java.util.Date;

public class Comment {
    private String commentID; // Unique ID for the comment
    private String text;      // Comment content
    private User author;      // User who authored the comment
    private Date timestamp;   // Time the comment was created

    public Comment(String commentID, String text, User author) {
        this.commentID = commentID;
        this.text = text;
        this.author = author;
        this.timestamp = new Date(); // Set current time
    }

    // Getters and Setters
    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Comment by " + author.getUsername() + " at " + timestamp + ": " + text;
    }
}
