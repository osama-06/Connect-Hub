

package Backend;
import Frontend.MainFrame;
import javax.swing.SwingUtilities;


public class UserAccManagement {
  public static void main(String[] args) {
        // Step 1: Initialize backend repositories
        UserRepository userRepository = new UserRepository(); 
        PostRepository postRepository = new PostRepository();
        FriendRepository friendRepository = new FriendRepository();

        // Step 2: Create the UserService instance
        UserService userService = new UserService(userRepository, postRepository, friendRepository);

        // Step 3: Launch the MainFrame with the UserService
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(userService);  // Pass userService to MainFrame
            mainFrame.setVisible(true);
        });
    }
}
