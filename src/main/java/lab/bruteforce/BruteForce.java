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
    public static final String STOP_SIGNAL_MESSAGE = "STOP";

    private final BlockingQueue<String> passwordsQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
    private final DictionaryReader dictionaryReader;
    private final List<PasswordsChecker> checkers = new ArrayList<>(THREADS_COUNT);
    private final ExecutorService readerExecutor;
    private final ExecutorService checkersExecutor;

    public BruteForce(BufferedReader fileReader, String urlStr, String userName) {
        this.dictionaryReader = new DictionaryReader(passwordsQueue, fileReader);
        for (int i = 0; i < THREADS_COUNT; i++) {
            checkers.add(new PasswordsChecker(passwordsQueue, userName, urlStr));
        }
        readerExecutor = Executors.newSingleThreadExecutor();
        checkersExecutor = Executors.newFixedThreadPool(THREADS_COUNT);
    }

    public String search() throws ExecutionException, InterruptedException {
        readerExecutor.submit(dictionaryReader);
        List<Future<String>> checkingFutures = submitCheckers(checkers, checkersExecutor);
        String result = waitForResult(checkingFutures);
        shutdownWork();
        return result;
    }

    private String waitForResult(List<Future<String>> checkingFutures) throws ExecutionException, InterruptedException {
        Future<String> completed = null;
        while (completed == null) {
            for (Future<String> future : checkingFutures) {
                if (future.isDone()) {
                    completed = future;
                    break;
                }
            }
        }
        String result = completed.get();
        if (result == null) {
            for (Future<String> future : checkingFutures) {
                if (future != completed) {
                    result = future.get();
                    if (result != null) {
                        break;
                    }
                }
            }
        }
        return result;
    }

    private List<Future<String>> submitCheckers(List<PasswordsChecker> checkers, ExecutorService service) {
        List<Future<String>> futures = new ArrayList<>(THREADS_COUNT);
        for (PasswordsChecker checker : checkers) {
            futures.add(service.submit(checker));
        }
        return futures;
    }

    private void shutdownWork() {
        readerExecutor.shutdownNow();
        checkersExecutor.shutdown();
        checkersExecutor.shutdownNow();
    }
}
