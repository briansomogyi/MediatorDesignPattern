package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Server server = new Server();

        User ionescu = new User(1, "Ionescu", server);
        User andrei = new User(2, "Andrei", server);
        User alin = new User(3, "Alin", server);

        // 1. Ionescu and Andrei login
        server.RegisterUser(ionescu);
        server.RegisterUser(andrei);

        System.out.println("\n--- Testing Online Communication ---");
        // Ionescu sends to Andrei (Online)
        ionescu.SendMessage(2, "Hello Andrei, are you there?");

        System.out.println("\n--- Testing Offline Messaging ---");
        // Ionescu sends to Alin (Who is currently OFFLINE)
        ionescu.SendMessage(3, "Hello Alin, I know you are offline but read this later.");
        ionescu.SendMessage(3, "Also, meeting is at 10 AM.");

        System.out.println("\n--- Alin Comes Online ---");
        // 2. Alin registers. He should get the notifications and his offline messages.
        server.RegisterUser(alin);

        System.out.println("\n--- Testing Status Notifications ---");
        // 3. Ionescu leaves. Andrei and Alin should be notified.
        server.Unregister(ionescu);

        System.out.println("\n--- Testing Busy Status ---");
        alin.setIsBusy(true);
        andrei.SendMessage(3, "Alin, are you free now?");
    }
}
