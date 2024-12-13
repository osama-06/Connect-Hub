package Backend;
import java.util.*;
import java.util.stream.Collectors;
public class NotificationService {

    private final DatabaseManager databaseManager;

    public NotificationService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    // Create a new notification and add it to the user
    public void createNotification(User recipient, String message, String senderUsername, String type) {
        String timestamp = String.valueOf(System.currentTimeMillis());  // Current timestamp
        Notification notification = new Notification(message, senderUsername, type, timestamp);
        recipient.addNotification(notification);
        databaseManager.updateUser(recipient);  // Save updated user with new notification
    }

    // Mark a notification as read
    public void markAsRead(User user, Notification notification) {
        notification.markAsRead();
        databaseManager.updateUser(user);  // Save updated user with read notification
    }

  public List<Notification> getUnreadNotifications(User user) {
    return user.getNotifications().stream()  // Get the notifications list from the user
            .filter(notification -> !notification.isRead())  // Filter out read notifications
            .collect(Collectors.toList());  // Collect unread notifications into a new list
}

}
