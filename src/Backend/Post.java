package Backend;

import java.time.LocalDateTime;

public class Post {
    private String postId;
    private String userId;
    private String content;
    private String imagePath;
    private LocalDateTime timestamp;

    public Post(String postId, String userId, String content, String imagePath, LocalDateTime timestamp) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.imagePath = imagePath;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
