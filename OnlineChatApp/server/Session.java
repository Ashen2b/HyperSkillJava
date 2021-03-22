package chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

class Session extends Thread {
    private final Socket socket;
    private final int clientNumber;
    private String userName;
    private boolean authorized = false;
    public DataOutputStream outputStream;

    public Session(Socket socket) {
        this.socket = socket;
        clientNumber = ++Server.totalClients;
    }

    @Override
    public void run() {
        try (
                DataInputStream inputStream = new DataInputStream(socket.getInputStream())
        ) {
            syncPrint("Client #" + clientNumber + " connected!");
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            setOutputStream(outputStream);
            setUserName(inputStream, outputStream);
            sendLastMessages(outputStream);
            String clientMessage = inputStream.readUTF();
            while (!"/exit".equals(clientMessage)) {
                syncPrint("Client #" + clientNumber + " sent: " + clientMessage);
                new Broadcast(clientMessage, this.userName).start();
                Server.saveMessage(clientMessage, this.userName);
                clientMessage = inputStream.readUTF();
            }
            syncPrint("Client #" + clientNumber + " disconnected!");
            outputStream.close();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void setOutputStream(DataOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    private void setUserName(DataInputStream inputStream, DataOutputStream outputStream) throws IOException {
        outputStream.writeUTF("Server: write your name");
        while (true) {
            String name = inputStream.readUTF();
            if (!Server.isNameTaken(name)) {
                this.userName = name;
                this.authorized = true;
                Server.addNameToList(this.userName);
                break;
            } else {
                outputStream.writeUTF("Server: this name is already taken! Choose another one.");
            }
        }
    }

    public boolean isAuthorized() {
        return this.authorized;
    }

    private synchronized void sendLastMessages(DataOutputStream outputStream) throws IOException {
        List<String> messages = Server.getMessagesList();
        if (!messages.isEmpty()) {
            StringBuilder output = new StringBuilder();
            for (String message : messages) {
                output.append(message).append("\n");
            }
            outputStream.writeUTF(output.toString());
        } else {
            outputStream.writeUTF("");
        }
    }

    private synchronized void syncPrint(String message) {
        System.out.println(message);
    }
}