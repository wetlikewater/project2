import java.io.*;
import java.net.*;

/**
 * This thread is responsible for reading server input
 * and displays it to the console.
 * It runs in an infinite loop until the
 * client disconnects from the server.
 *
 */
public class ReadThread extends Thread {
    public BufferedReader reader;
    public Socket socket;
    public ChatClient client;

    public ReadThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Server Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String response = reader.readLine();
                System.out.println("\n" + response);

                // this line prints the username after displaying the server's message
                if (client.getUserName() != null) {
                    System.out.print("(" + client.getUserName() + "):");
                }
            } catch (IOException ex) {
                System.out.println(" Server Error: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}
