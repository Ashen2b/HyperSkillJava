package chat.client;

import java.io.DataInputStream;
import java.io.IOException;

class messageListener extends Thread {
    private final DataInputStream inputStream;

    public messageListener(DataInputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                syncPrint(inputStream.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void syncPrint(String message) {
        System.out.println(message);
    }
}
