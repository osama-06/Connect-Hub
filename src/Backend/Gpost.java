package Backend;

import java.util.UUID;

public class Gpost {
    private String postId;
    private String content;
    private String authorId;
    private String timestamp;

    
        public Gpost() {
    }
    // Constructor
    public Gpost(String content, String authorId) {
        this.postId = UUID.randomUUID().toString(); // Unique ID for the post
        this.content = content;
        this.authorId = authorId;
        this.timestamp = java.time.LocalDateTime.now().toString(); // Current timestamp for when the post is created
    }

    // Getters and setters
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Post ID: " + postId + "\n" +
               "Content: " + content + "\n" +
               "Author ID: " + authorId + "\n" +
               "Timestamp: " + timestamp;
    }
}