import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This is the main server of the application
 * CTRL + C TERMINATES THE SERVER
 */
public class ChatServer {
    public int port;
    public Set<String> userNames = new HashSet<>();
    public Set<UserThread> userThreads = new HashSet<>();

    public ChatServer(int port) {
        this.port = port;
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Chat Server is running on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");

                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();

            }

        } catch (IOException ex) {
            System.out.println("Server Error" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Instructions");
            System.out.println("1.OPEN FOLDER LOCATION IN CMD or powershell");
            System.out.println("2.TYPE JAVA CHATSERVER <CHOOSE PORTNUMBER>");
            System.out.println("3.TYPE JAVA CHATCLIENT LOCALHOST <CHOOSE PORTNUMBER>");
            System.exit(0);
        }

        int port = Integer.parseInt(args[0]);

        ChatServer server = new ChatServer(port);
        server.execute();
    }

    /**
     * Delivers a message from one user to others in broadcast style
     * This is the broadcast feature
     */
    public void broadcast(String message, UserThread userThread) {
        for (UserThread aUser : userThreads) {
            if (aUser != userThread) {
                aUser.sendMessage(message);
            }
        }
    }

    /**
     * Stores a username for a newly connected client
     */
    public void addUserName(String userName) {
        userNames.add(userName);
    }

    /**
     * When a client disconnects
     * this removes the associated username and UserThread
     */
    public void removeUser(String userName, UserThread aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println("" + userName + " has left the chat");
        }
    }

    public Set<String> getUserNames() {
        return this.userNames;
    }

    /**
     * Returns true if there are other users connected,
     * but it does not count the currently connected user
     */
    public boolean hasUsers() {
        return !this.userNames.isEmpty();
    }
}
