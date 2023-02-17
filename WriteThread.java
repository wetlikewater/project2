import java.io.*;
import java.net.*;

/**
 * This thread handles the connection for each connected client,
 * so the server may handle multiple clients at the same time.
 *
 */
public class UserThread extends Thread {
    public Socket socket;
    public ChatServer server;
    public PrintWriter writer;

    public UserThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            printUsers();

            String userName = reader.readLine();
            server.addUserName(userName);

            String serverMessage = "New user connected:" + userName;
            server.broadcast(serverMessage, this);

            String clientMessage;

            do {
                clientMessage = reader.readLine();
                serverMessage = "(" + userName + "):" + clientMessage;
                server.broadcast(serverMessage, this);

            } while (!clientMessage.equals("exit"));

            server.removeUser(userName, this);
            socket.close();

            serverMessage = userName + " has left the chat";
            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            System.out.println("Server error " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * This block is responsible for sending
     * the list of connected online users
     * to the newly connected user.
     */
    void printUsers() {
        if (server.hasUsers()) {
            writer.println("Online users: " + server.getUserNames());
        } else {
            writer.println("Everyone is offline");
        }
    }

    /**
     * this block is responsible for sending
     * a message to the client.
     */
    public void sendMessage(String message) {
        writer.println(message);
    }
}
