package Backend;
public class FriendshipService {

    private final DatabaseManager databaseManager;

    public FriendshipService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    // Send a friend request
    public boolean sendFriendRequest(User loggedInUser, User userToRequest) {
        // Check if a friend request is already pending
        for (FriendRequest request : loggedInUser.getSentRequests()) {
            if (request.getRecipient().equals(userToRequest) && "pending".equals(request.getStatus())) {
                return false; // Already sent a pending request
            }
        }
        // Create and send the friend request
        FriendRequest request = new FriendRequest(loggedInUser, userToRequest);
        loggedInUser.getSentRequests().add(request);
        userToRequest.getReceivedRequests().add(request);
        databaseManager.updateUser(loggedInUser);
        databaseManager.updateUser(userToRequest);
        String message = loggedInUser.getUsername() + " sent you a friend request!";
        NotificationService notificationService = new NotificationService(databaseManager);
        notificationService.createNotification(userToRequest, message, loggedInUser.getUsername(), "friend_request");
    
        return true; // Request sent successfully
    }

    // Accept a friend request
    public boolean acceptFriendRequest(User loggedInUser, FriendRequest request) {
        if (!loggedInUser.getReceivedRequests().contains(request)) {
            return false; // Request not found
        }
        // Change the request status to accepted
        request.setStatus("accepted");
        loggedInUser.addFriend(new Friend(request.getSender().getUsername(), request.getSender().getProfilePhoto()));
        request.getSender().addFriend(new Friend(loggedInUser.getUsername(), loggedInUser.getProfilePhoto()));
        databaseManager.updateUser(loggedInUser);
        databaseManager.updateUser(request.getSender());
        return true; // Request accepted
    }

    // Reject a friend request
    public boolean rejectFriendRequest(User loggedInUser, FriendRequest request) {
        if (!loggedInUser.getReceivedRequests().contains(request)) {
            return false; // Request not found
        }
        // Change the request status to rejected
        request.setStatus("rejected");
        databaseManager.updateUser(loggedInUser);
        return true; // Request rejected
    }

    // Remove friend
    public boolean removeFriend(User loggedInUser, User friendToRemove) {
        Friend friend = new Friend(friendToRemove.getUsername(), friendToRemove.getProfilePhoto());
        loggedInUser.removeFriend(friend);
        friendToRemove.removeFriend(new Friend(loggedInUser.getUsername(), loggedInUser.getProfilePhoto()));
        databaseManager.updateUser(loggedInUser);
        databaseManager.updateUser(friendToRemove);
        return true; // Friend removed successfully
    }

    // Search for a user by username
    public User searchUserByUsername(String username) {
        return databaseManager.getUserByUsername(username);
    }
}
