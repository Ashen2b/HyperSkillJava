package chat.client;

import java.io.DataInputStream;
import java.io.IOException;

class inputProcessor extends Thread {
    private final DataInputStream inputStream;

    public inputProcessor (DataInputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(inputStream.readUTF());
            } catch (IOException exception) {
                break;
            }
        }
    }
}
