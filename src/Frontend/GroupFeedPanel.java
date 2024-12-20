package Frontend;

import Backend.Group;
import Backend.Gpost;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GroupFeedPanel extends JPanel {
    private Group currentGroup;

    public GroupFeedPanel(Group currentGroup) {
        this.currentGroup = currentGroup;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        displayPosts();
    }

    // Display all posts in the group
    public void displayPosts() {
        List<Gpost> posts = currentGroup.getPosts();
        for (Gpost post : posts) {
            JTextArea postArea = new JTextArea();
            postArea.setEditable(false);
            postArea.setText("Post ID: " + post.getPostId() + "\n"
                    + "Content: " + post.getContent() + "\n"
                    + "Author: " + post.getAuthorId() + "\n\n");

            JScrollPane scrollPane = new JScrollPane(postArea);
            add(scrollPane);
        }
    }

    // Refresh the feed when new posts are added
    public void refreshFeed() {
        removeAll();
        displayPosts();
        revalidate();
        repaint();
    }
}
