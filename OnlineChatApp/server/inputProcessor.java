package chat.server;

import java.io.DataInputStream;
import java.io.IOException;

public class inputProcessor extends Thread {
    private final DataInputStream inputStream;

    public inputProcessor(DataInputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = inputStream.readUTF();
                if (message.isEmpty()) {
                    break;
                } else {
                    System.out.println(message);
                }
            } catch (IOException exception) {
                System.exit(0);
            }
        }
    }
}
