package chat.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class outputProcessor extends Thread {
    private final Scanner scanner = new Scanner(System.in);
    private final DataOutputStream outputStream;

    public outputProcessor(DataOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        while (true) {
            try {
                outputStream.writeUTF(scanner.nextLine());
            } catch (IOException exception) {
                break;
            }
        }
    }
}
