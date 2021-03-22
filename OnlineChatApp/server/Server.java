package chat.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 5454;

    private static final List<String> messagesList = new ArrayList<>();
    private static final List<String> namesList = new ArrayList<>();
    private static final List<Session> clientsSessions = new ArrayList<>();

    public static int totalClients = 0;

    public static void main(String[] args) {
        System.out.println("Server started!");
        try (ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS))) {
            serverSocket.setSoTimeout(12000);
            while (true) {
                final Socket socket = serverSocket.accept();
                Session clientSession = new Session(socket);
                clientsSessions.add(clientSession);
                clientSession.start();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static synchronized boolean isNameTaken(String userName) {
        return namesList.contains(userName);
    }

    public static synchronized void addNameToList(String userName) {
        namesList.add(userName);
    }

    public static synchronized List<String> getMessagesList() {
        if (messagesList.size() > 10) {
            return messagesList.subList(messagesList.size() - 10, messagesList.size());
        } else {
            return messagesList;
        }
    }

    public static synchronized List<Session> getClientsSessions() {
        return clientsSessions;
    }

    public static synchronized void saveMessage(String message, String userName) {
        if (!message.isEmpty()) {
            messagesList.add(userName + ": " + message);
        }
    }
}