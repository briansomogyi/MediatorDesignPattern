package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server implements IServer {
    // Active online users
    private final Map<Integer, IUser> users = new HashMap<>();

    // Offline message storage: Map<RecipientID, List<Messages>>
    private final Map<Integer, List<String>> offlineMessages = new HashMap<>();

    public Server() {
        System.out.println("Server started...");
    }

    @Override
    public void RegisterUser(IUser user) {
        if (users.containsKey(user.getId())) {
            System.out.println("User " + user.getName() + " is already logged in.");
            return;
        }

        // 1. Notify all CURRENT users that someone new has joined
        // Update: Removed "Server" parameter call
        notifyAllUsers(user.getName() + " has joined the chat.");

        // 2. Add the new user to the active list
        users.put(user.getId(), user);
        System.out.println("Server: Registered " + user.getName());

        // 3. Check for offline messages waiting for this user
        if (offlineMessages.containsKey(user.getId())) {
            List<String> pending = offlineMessages.get(user.getId());
            if (!pending.isEmpty()) {
                user.ReceiveMessage("Server", "You have " + pending.size() + " offline messages:");
                for (String msg : pending) {
                    // Send the stored message
                    user.ReceiveMessage("OfflineBox", msg);
                }
                // Clear messages after delivery
                offlineMessages.remove(user.getId());
            }
        }
    }

    @Override
    public void Unregister(IUser user) {
        if (!users.containsKey(user.getId())) return;

        users.remove(user.getId());
        System.out.println("Server: Unregistered " + user.getName());

        // Notify remaining users that this user left
        // Update: Removed "Server" parameter call
        notifyAllUsers(user.getName() + " has left the chat.");
    }

    @Override
    public void SendMessage(int userIdSender, int userIdReceiver, String message) {
        IUser sender = users.get(userIdSender);
        String senderName = (sender != null) ? sender.getName() : "Unknown";

        // Check if receiver is online
        if (users.containsKey(userIdReceiver)) {
            IUser receiver = users.get(userIdReceiver);

            // Check if receiver is busy
            if (receiver.getIsBusy()) {
                if (sender != null) {
                    sender.ReceiveMessage("Server", receiver.getName() + " is busy. Message ignored.");
                }
                return;
            }

            // Direct delivery
            receiver.ReceiveMessage(senderName, message);
        } else {
            // Receiver is OFFLINE - Store the message
            System.out.println("Server: User " + userIdReceiver + " is offline. Storing message.");

            offlineMessages.putIfAbsent(userIdReceiver, new ArrayList<>());
            String storedMessage = "From " + senderName + " (while you were offline): " + message;
            offlineMessages.get(userIdReceiver).add(storedMessage);
        }
    }

    // Helper method to broadcast status updates
    // Update: Removed 'senderName' parameter as it was always "Server"
    private void notifyAllUsers(String message) {
        for (IUser user : users.values()) {
            user.ReceiveMessage("Server", message);
        }
    }
}
