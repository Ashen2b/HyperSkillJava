package chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 5454;

    public static void main(String[] args) {
        System.out.println("Server started!");
        try (
                ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS));
                Socket socket = server.accept();
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())
                ) {
            Thread inputProcessor = new inputProcessor(inputStream);
            Thread outputProcessor = new outputProcessor(outputStream);

            inputProcessor.start();
            outputProcessor.start();

            Thread.sleep(6000);
            System.exit(0);
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }

    }
}
