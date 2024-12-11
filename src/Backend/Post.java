package Backend;

public class Post {
    private String text;
    private String imagePath;

    public Post(String text, String imagePath) {
        this.text = text;
        this.imagePath = imagePath;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}

