/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend;
import Frontend.MainFrame;
import javax.swing.SwingUtilities;
/**
 *
 * @author User
 */
public class UserAccManagement {
  public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserService userService = new UserService(); // Ensure UserService is implemented
            MainFrame frame = new MainFrame(userService);
            frame.setVisible(true);
        });
}
  }
