package lab.bruteforce;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * Created by anna on 29.11.14.
 */
public class DictionaryReader implements Runnable {

    private final BlockingQueue<String> passwordsQueue;
    private final BufferedReader fileReader;

    public DictionaryReader(BlockingQueue<String> passwordsQueue, BufferedReader fileReader) {
        this.passwordsQueue = passwordsQueue;
        this.fileReader = fileReader;
    }

    @Override
    public void run() {
        String password;
        try {
            try {
                while((password = fileReader.readLine()) != null) {
                    passwordsQueue.put(password);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < BruteForce.THREADS_COUNT; i++) {
                passwordsQueue.put(BruteForce.STOP_SIGNAL_MESSAGE);
            }
        } catch (InterruptedException e) {
            System.out.println("Stop reading dictionary");
        }
    }
}
