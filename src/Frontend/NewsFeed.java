package Frontend;

import Backend.User;
import Backend.UserService;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NewsFeed extends JFrame {
    private UserService userService;
    private User currentUser;
    private JList<String> postsList;
    
    public NewsFeed(UserService userService, User currentUser) {
        this.userService = userService;
        this.currentUser = currentUser;
        
        // Set up the frame for the news feed
        setTitle("News Feed");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header panel with user details
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout());
        headerPanel.setBackground(Color.LIGHT_GRAY);
        JLabel userNameLabel = new JLabel("Welcome, " + currentUser.getUsername());
        headerPanel.add(userNameLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        // Posts section
        JPanel postsPanel = new JPanel();
        postsPanel.setLayout(new BorderLayout());
        postsPanel.setBackground(Color.WHITE);
        
        // Get posts for the current user (assuming posts are associated with users)
        List<String> posts = getPostsForUser();
        
        // Display posts
        DefaultListModel<String> postsModel = new DefaultListModel<>();
        for (String post : posts) {
            postsModel.addElement(post);
        }
        postsList = new JList<>(postsModel);
        JScrollPane postsScrollPane = new JScrollPane(postsList);
        postsPanel.add(postsScrollPane, BorderLayout.CENTER);
        
        // Add the posts panel to the frame
        add(postsPanel, BorderLayout.CENTER);

        // Add a button to navigate back to the profile page
        JButton backToProfileButton = new JButton("Back to Profile");
        backToProfileButton.addActionListener(e -> {
            ProfileManagement profilePage = new ProfileManagement(userService);
            profilePage.setVisible(true);
            this.dispose(); // Close the news feed page
        });
        add(backToProfileButton, BorderLayout.SOUTH);
    }
    
    private List<String> getPostsForUser() {
        // For demonstration, just return a few dummy posts
        return List.of(
            "User1: Just had lunch!",
            "User2: Anyone up for a coding challenge?",
            "User3: Had a great time at the park today."
        );
    }
}
