package chat.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class messageWriter extends Thread {
    private final DataOutputStream outputStream;

    public messageWriter(DataOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            String message = scanner.nextLine();
            while (!"/exit".equals(message)) {
                outputStream.writeUTF(message);
                message = scanner.nextLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
