package lab.bruteforce;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by anna on 29.11.14.
 */
public class BruteForce {

    public static final int THREADS_COUNT = 3;
    public static final int QUEUE_SIZE = 256;

    private final BlockingQueue<String> passwordsQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
    private final DictionaryReader dictionaryReader;
    private final List<PasswordsChecker> checkers = new ArrayList<>(THREADS_COUNT);

    public BruteForce(BufferedReader fileReader, String urlStr, String userName) {
        this.dictionaryReader = new DictionaryReader(passwordsQueue, fileReader);
        for (int i = 0; i < THREADS_COUNT; i++) {
            checkers.add(new PasswordsChecker(passwordsQueue, userName, urlStr));
        }
    }

    public String search() throws ExecutionException, InterruptedException {
        ExecutorService readerExecutor = Executors.newSingleThreadExecutor();
        readerExecutor.submit(dictionaryReader);

        ExecutorService checkersExecutor = Executors.newFixedThreadPool(THREADS_COUNT);
        String result = checkersExecutor.invokeAny(checkers);
        readerExecutor.shutdownNow();
        checkersExecutor.shutdown();
        checkersExecutor.shutdownNow();
        return result;
    }
}
