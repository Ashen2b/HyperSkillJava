package chat.server;

import java.io.IOException;
import java.util.List;

class Broadcast extends Thread {
    private final String message;
    private final String author;
    private final List<Session> clients;

    public Broadcast(String message, String author) {
        this.message = message;
        this.author = author;
        this.clients = Server.getClientsSessions();
    }

    @Override
    public void run() {
        for (Session client : clients) {
            if (client.isAuthorized())
                try {
                    client.outputStream.writeUTF(getFullMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private String getFullMessage() {
        return this.author + ": " + this.message;
    }
}
