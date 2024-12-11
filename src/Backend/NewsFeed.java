package Backend;

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
        styleButton(uploadImageButton, 130, 40);
        JButton postButton = new JButton("Post");
        styleButton(postButton, 130, 40);
        JButton backButton = new JButton("Back to Profile");
        styleButton(backButton, 130, 40);

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

        // Post Text
        if (post.getText() != null && !post.getText().isEmpty()) {
            JLabel textLabel = new JLabel("<html>" + post.getText().replaceAll("\n", "<br>") + "</html>");
            textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textLabel.setForeground(new Color(50, 50, 50));
            textLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            postPanel.add(textLabel, BorderLayout.NORTH);
        }

        // Post Image (if exists)
        if (post.getImagePath() != null) {
            JLabel imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(post.getImagePath())
                    .getImage().getScaledInstance(600, 300, Image.SCALE_SMOOTH));
            imageLabel.setIcon(imageIcon);
            postPanel.add(imageLabel, BorderLayout.CENTER);
        }

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
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 115, 220));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(66, 135, 245));
            }
        });
    }

   
}