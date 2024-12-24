package Frontend;

import Backend.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

public class NewsFeed extends JFrame {
    private User currentUser;
    private DatabaseManager databaseManager;
    private ProfileService profileService;
    private JTextArea postTextArea;
    private JLabel imagePreviewLabel;
    private String selectedImagePath = null;
    private JPanel feedPanel;

    public NewsFeed(DatabaseManager databaseManager, User currentUser) {
        this.databaseManager = databaseManager;
        this.profileService = new ProfileService(databaseManager);
        this.currentUser = currentUser;

        setTitle("News Feed");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center the window

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create Post Panel (Modern design)
        JPanel createPostPanel = new JPanel();
        createPostPanel.setLayout(new BorderLayout(10, 10));
        createPostPanel.setBackground(new Color(242, 242, 242));
        createPostPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Post Text Area (larger, modern font)
        postTextArea = new JTextArea(5, 50);
        postTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        postTextArea.setLineWrap(true);
        postTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(postTextArea);
        postTextArea.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        // Image Preview (with some styling)
        imagePreviewLabel = new JLabel("No Image Selected", SwingConstants.CENTER);
        imagePreviewLabel.setPreferredSize(new Dimension(200, 150));
        imagePreviewLabel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        imagePreviewLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        imagePreviewLabel.setForeground(new Color(150, 150, 150));

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        buttonPanel.setBackground(new Color(242, 242, 242));

        JButton uploadImageButton = new JButton("Upload Image");
        styleButton(uploadImageButton, 200, 40);
        JButton postButton = new JButton("Post");
        styleButton(postButton, 130, 40);
        JButton backButton = new JButton("Back to Profile");
        styleButton(backButton, 200, 40);

        uploadImageButton.addActionListener(e -> chooseImage());
        postButton.addActionListener(e -> createPost());
        backButton.addActionListener(e -> {
            ProfileManagement profilePage = new ProfileManagement(databaseManager ,currentUser);
            profilePage.setVisible(true);
            this.dispose(); // Close News Feed
        });

        buttonPanel.add(uploadImageButton);
        buttonPanel.add(postButton);
        buttonPanel.add(backButton);

        // Add components to createPostPanel
        createPostPanel.add(scrollPane, BorderLayout.CENTER);
        createPostPanel.add(imagePreviewLabel, BorderLayout.EAST);
        createPostPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Feed Panel (where posts are displayed)
        feedPanel = new JPanel();
        feedPanel.setLayout(new BoxLayout(feedPanel, BoxLayout.Y_AXIS));
        feedPanel.setBackground(new Color(255, 255, 255));
        JScrollPane feedScrollPane = new JScrollPane(feedPanel);
        loadPosts();

        // Add Panels to Frame
        add(createPostPanel, BorderLayout.NORTH);
        add(feedScrollPane, BorderLayout.CENTER);
    }

    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedImagePath = fileChooser.getSelectedFile().getAbsolutePath();
            imagePreviewLabel.setText("");
            imagePreviewLabel.setIcon(new ImageIcon(new ImageIcon(selectedImagePath)
                    .getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH)));
        }
    }

    private void createPost() {
        String text = postTextArea.getText().trim();
        if (text.isEmpty() && selectedImagePath == null) {
            JOptionPane.showMessageDialog(this, "Cannot create an empty post!");
            return;
        }

        try {
            profileService.createPost(currentUser, text, selectedImagePath);
            addPostToFeed(new Post(text, selectedImagePath));
            clearCreatePostForm();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving post: " + e.getMessage());
        }
    }

    private void addPostToFeed(Post post) {
        JPanel postPanel = new JPanel();
        postPanel.setLayout(new BorderLayout());
        postPanel.setBackground(new Color(255, 255, 255));
        postPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        postPanel.setMaximumSize(new Dimension(getWidth(), 400));

        // Post Header with Profile Picture and Username
        JPanel postHeaderPanel = new JPanel(new BorderLayout(10, 10));
        postHeaderPanel.setBackground(new Color(255, 255, 255));

        // Profile Picture (Small Icon)
        JLabel profilePictureLabel = new JLabel();
        String profilePicturePath = currentUser.getProfilePhoto();
        if (profilePicturePath != null && !profilePicturePath.isEmpty()) {
            ImageIcon profileIcon = new ImageIcon(new ImageIcon(profilePicturePath)
                    .getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
            profilePictureLabel.setIcon(profileIcon);
        }
        profilePictureLabel.setPreferredSize(new Dimension(50, 50));

        // Username Label
        JLabel usernameLabel = new JLabel(currentUser.getUsername());
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        usernameLabel.setForeground(new Color(50, 50, 50));

        // Add profile picture and username to header panel
        postHeaderPanel.add(profilePictureLabel, BorderLayout.WEST);
        postHeaderPanel.add(usernameLabel, BorderLayout.CENTER);

        postPanel.add(postHeaderPanel, BorderLayout.NORTH);

        // Post Text
        if (post.getText() != null && !post.getText().isEmpty()) {
            JLabel textLabel = new JLabel("<html>" + post.getText().replaceAll("\n", "<br>") + "</html>");
            textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textLabel.setForeground(new Color(50, 50, 50));
            textLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            postPanel.add(textLabel, BorderLayout.CENTER);
        }

        // Post Image (if exists)
        if (post.getImagePath() != null) {
            JLabel imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(post.getImagePath())
                    .getImage().getScaledInstance(600, 300, Image.SCALE_SMOOTH));
            imageLabel.setIcon(imageIcon);
            postPanel.add(imageLabel, BorderLayout.SOUTH);
        }

        // Buttons Panel (Like, Comment, Show Comments)
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonsPanel.setBackground(new Color(255, 255, 255));

        JButton likeButton = new JButton("Like");
        styleButton(likeButton, 100, 30);
        likeButton.addActionListener(e -> handleLike(post));

        JButton commentButton = new JButton("Comment");
        styleButton(commentButton, 100, 30);
        commentButton.addActionListener(e -> handleComment(post));

        JButton showCommentsButton = new JButton("Show Comments");
        styleButton(showCommentsButton, 150, 30);
        showCommentsButton.addActionListener(e -> showComments(post));

        buttonsPanel.add(likeButton);
        buttonsPanel.add(commentButton);
        buttonsPanel.add(showCommentsButton);

        postPanel.add(buttonsPanel, BorderLayout.SOUTH);

        feedPanel.add(postPanel);
        feedPanel.revalidate();
        feedPanel.repaint();
    }

    private void loadPosts() {
        List<Post> posts = currentUser.getPosts();
        for (Post post : posts) {
            addPostToFeed(post);
        }
    }

    private void clearCreatePostForm() {
        postTextArea.setText("");
        selectedImagePath = null;
        imagePreviewLabel.setText("No Image Selected");
        imagePreviewLabel.setIcon(null);
    }

    private void styleButton(JButton button, int width, int height) {
        button.setPreferredSize(new Dimension(width, height));
        button.setBackground(new Color(66, 135, 245));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(50, 115, 220));
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(66, 135, 245));
            }
        });
    }

    private void handleLike(Post post) {
        // Increment like count and save to database
       // post.incrementLikes();
      // databaseManager.savePost(post);
        JOptionPane.showMessageDialog(this, "You liked the post!");
    }

    private void handleComment(Post post) {
        // Display a dialog box to add a comment
        String comment = JOptionPane.showInputDialog(this, "Enter your comment:");
        if (comment != null && !comment.trim().isEmpty()) {
         //   post.addComment(currentUser.getUsername(), comment);
          //  databaseManager.savePost(post);
            JOptionPane.showMessageDialog(this, "Comment added!");
        }
    }

    private void showComments(Post post) {
        // Display all comments in a dialog box
      //  List<String> comments = post.getComments();
        StringBuilder commentsText = new StringBuilder("<html><b>Comments:</b><br>");
      //  for (String comment : comments) {
     //       commentsText.append(comment).append("<br>");
      //  }
        commentsText.append("</html>");
        JOptionPane.showMessageDialog(this, new JLabel(commentsText.toString()), "Comments", JOptionPane.INFORMATION_MESSAGE);
    }
}

