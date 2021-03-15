package chat.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 5454;

    public static void main(String[] args) {
        System.out.println("Client started!");
        try (
                Socket socket = new Socket(InetAddress.getByName(SERVER_ADDRESS), PORT);
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println(inputStream.readUTF());
            authorization(inputStream, outputStream);

            Thread messageListener = new messageListener(inputStream);
            Thread messageWriter = new messageWriter(outputStream);

            messageListener.start();
            messageWriter.start();
            messageWriter.join();
            messageListener.interrupt();
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    private static void authorization(DataInputStream inputStream, DataOutputStream outputStream) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {

            while (true) {
                String clientName = bufferedReader.readLine();
                outputStream.writeUTF(clientName);
                String response = inputStream.readUTF();
                if (response.contains("this name is already taken!")) {
                    System.out.println(response);
                } else if (response.isEmpty()) {
                    break;
                } else {
                    System.out.println(response);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
