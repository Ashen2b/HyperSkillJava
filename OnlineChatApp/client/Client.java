package chat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 5454;

    public static void main(String[] args) {
        System.out.println("Client started!");
        try (
                Socket socket = new Socket(InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())
                ) {
            Thread fromServerToClient = new inputProcessor(inputStream);
            Thread fromClientToServer = new outputProcessor(outputStream);

            fromServerToClient.start();
            fromClientToServer.start();

            Thread.sleep(4000);
            System.exit(0);
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}
