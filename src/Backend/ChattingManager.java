package Backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChattingManager {
    private static final String CHAT_FILE = "chats.json";

    private List<ChattingMessage> messages = new ArrayList<>();

    public ChattingManager() {
        // Load messages from the file
        messages = loadMessages();
        System.out.println("Loaded messages: " + messages.size()); // Debugging line
    }

    // Fetch messages between two users based on their usernames
    public List<ChattingMessage> getMessagesByName(User user1, User user2) {
        List<ChattingMessage> userMessages = new ArrayList<>();
        System.out.println("Fetching messages for: " + user1.getUsername() + " and " + user2.getUsername());

        for (ChattingMessage message : messages) {
            System.out.println("Checking message: " + message.getSenderUsername() + " -> " + message.getReceiverUsername());

            if ((message.getSenderUsername().equals(user1.getUsername()) && message.getReceiverUsername().equals(user2.getUsername())) ||
                (message.getSenderUsername().equals(user2.getUsername()) && message.getReceiverUsername().equals(user1.getUsername()))) {
                userMessages.add(message);
                System.out.println("Added message: " + message.getMessage());
            }
        }

        System.out.println("Total messages fetched: " + userMessages.size());
        return userMessages;
    }

    // Fetch recent chats involving the current user (by username)
    public List<ChattingMessage> loadRecentChats(User currentUser) {
        List<ChattingMessage> recentChats = new ArrayList<>();
        for (ChattingMessage message : messages) {
            if (message.getSenderUsername().equals(currentUser.getUsername()) || message.getReceiverUsername().equals(currentUser.getUsername())) {
                recentChats.add(message);
            }
        }
        return recentChats;
    }

    // Send a new message and save to the file
    public void sendMessage(User sender, User receiver, String messageContent) {
        String timestamp = java.time.LocalDateTime.now().toString();
        ChattingMessage newMessage = new ChattingMessage(sender.getUsername(), receiver.getUsername(), messageContent, timestamp);
        messages.add(newMessage);
        saveMessages(); // Save the updated list back to the file
    }

    // Save messages to JSON file
    private void saveMessages() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(CHAT_FILE), messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load messages from JSON file
    private List<ChattingMessage> loadMessages() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(CHAT_FILE);
            if (file.exists()) {
                List<ChattingMessage> loadedMessages = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, ChattingMessage.class));
                System.out.println("Messages loaded from file: " + loadedMessages.size()); // Debugging line
                return loadedMessages;
            } else {
                System.out.println("Chat file not found, returning empty list."); // Debugging line
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
