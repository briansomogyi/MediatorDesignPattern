package org.example;

public class User implements IUser {
    private final int id;
    private final String name;
    private final IServer server;
    private boolean isBusy;

    public User(int id, String name, IServer server) {
        this.id = id;
        this.name = name;
        this.server = server;
        this.isBusy = false; // Default status
    }

    @Override
    public void SendMessage(int userId, String message) {
        System.out.println("[" + this.name + "] sending message to " + userId + ": " + message);
        server.SendMessage(id, userId, message);
    }

    @Override
    public void ReceiveMessage(String userName, String message) {
        System.out.println(" -> " + this.name + " received from " + userName + ": " + message);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setIsBusy(boolean isBusy) {
        this.isBusy = isBusy;
        System.out.println("[" + this.name + "] is now " + (isBusy ? "BUSY" : "AVAILABLE"));
    }

    @Override
    public boolean getIsBusy() {
        return isBusy;
    }
}