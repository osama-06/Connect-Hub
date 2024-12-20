package Frontend;

import Backend.ChattingManager;
import Backend.ChattingMessage;
import Backend.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ChattingChatWindow extends JFrame {
    private final User friend; // The user you are chatting with
    private final User currentUser; // The current logged-in user
    private final ChattingManager chattingManager; // Backend for managing chats

    private JPanel chatHistoryPanel; // Panel for dynamically displaying messages
    private JTextField messageInputField; // Input field for new messages
    private JButton sendButton; // Button to send messages

    public ChattingChatWindow(User friend, User currentUser) {
        this.friend = friend;
        this.currentUser = currentUser;
        this.chattingManager = new ChattingManager();

        setTitle("Chat with " + friend.getUsername());
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Chat history panel
        chatHistoryPanel = new JPanel();
        chatHistoryPanel.setLayout(new BoxLayout(chatHistoryPanel, BoxLayout.Y_AXIS));
        chatHistoryPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane chatHistoryScrollPane = new JScrollPane(chatHistoryPanel);
        chatHistoryScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(chatHistoryScrollPane, BorderLayout.CENTER);

        // Message input panel
        JPanel messageInputPanel = new JPanel(new BorderLayout());
        messageInputField = new JTextField();
        sendButton = new JButton("Send");

        sendButton.addActionListener(this::sendMessage);

        messageInputPanel.add(messageInputField, BorderLayout.CENTER);
        messageInputPanel.add(sendButton, BorderLayout.EAST);

        add(messageInputPanel, BorderLayout.SOUTH);

        // Load and display chat history
        loadChatHistory();

        // Set up the automatic refresh every 2 seconds (2000 milliseconds)
        Timer refreshTimer = new Timer(2000, e -> loadChatHistory());
        refreshTimer.start(); // Start the timer

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void loadChatHistory() {
    SwingUtilities.invokeLater(() -> {
        // Clear chat history panel before loading
        chatHistoryPanel.removeAll();

        try {
            // Fetch all the messages from the ChattingManager
            List<ChattingMessage> messages = chattingManager.getMessagesByName(currentUser, friend);

            // If no messages exist, show a placeholder message
            if (messages.isEmpty()) {
                JLabel noMessagesLabel = new JLabel("No messages yet. Start the conversation!");
                noMessagesLabel.setHorizontalAlignment(SwingConstants.CENTER);
                chatHistoryPanel.add(noMessagesLabel);
            } else {
                // Iterate through all messages and add them to the panel
                for (ChattingMessage message : messages) {
                    addMessageToPanel(message);
                }
            }

            // Refresh the chat history panel
            chatHistoryPanel.revalidate();
            chatHistoryPanel.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading chat history: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
}


    private void sendMessage(ActionEvent e) {
        String messageContent = messageInputField.getText().trim();

        if (messageContent.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Message cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        ChattingMessage message = new ChattingMessage(currentUser.getUsername(), friend.getUsername(), messageContent, timestamp);

        try {
            chattingManager.sendMessage(currentUser, friend, messageContent);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving the message: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add the message to the chat history display
        addMessageToPanel(message);

        // Clear the input field
        messageInputField.setText("");

        // Refresh the chat history panel
        chatHistoryPanel.revalidate();
        chatHistoryPanel.repaint();
    }

    private void addMessageToPanel(ChattingMessage message) {
        // Determine alignment based on sender
        boolean isSender = message.getSenderUsername().equals(currentUser.getUsername());

        // Create a panel for the message
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        // Create a label for the message content
        JLabel messageLabel = new JLabel("<html><p style='width: 200px;'>" + message.getMessage() + "</p></html>");
        messageLabel.setOpaque(true);
        messageLabel.setBackground(isSender ? Color.CYAN : Color.LIGHT_GRAY);
        messageLabel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Add timestamp to the message
        JLabel timestampLabel = new JLabel(formatTimestamp(message.getTimestamp()));
        timestampLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        timestampLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // Add components to the message panel
        if (isSender) {
            messagePanel.add(messageLabel, BorderLayout.EAST); // Align sender's message to the right
        } else {
            messagePanel.add(messageLabel, BorderLayout.WEST); // Align receiver's message to the left
        }
        messagePanel.add(timestampLabel, BorderLayout.SOUTH);

        // Add the message panel to the chat history panel
        chatHistoryPanel.add(messagePanel);
    }

    private String formatTimestamp(String timestamp) {
        return timestamp; // Customize if needed
    }
}
