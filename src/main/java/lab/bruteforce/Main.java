package lab.bruteforce;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by anna on 28.11.14.
 */
public class Main {

    private static final String HELP = "Usage: bruteforce URL LOGIN DICTIONARY";
    private static final String COMPLETED_TIME_MESSAGE = "Operation was completed in %d ms \n";
    private static final String PASSWORD_NOT_FOUND = "Unfortunately password was not found. Try to use another dictionary";
    private static final String SUCCESS_RESULT_MESSAGE = "You password is: %s";

    public static void main(String args[]) throws IOException, ExecutionException, InterruptedException {
        if (args.length != 3) {
            help();
        } else {
            find(args);
        }
    }

    private static void help() {
        System.out.println(HELP);
    }

    private static void find(String args[]) throws IOException, ExecutionException, InterruptedException {
        String urlStr = args[0];
        String login = args[1];
        String pathToDictionary = args[2];

        try (BufferedReader reader = new BufferedReader(new FileReader(pathToDictionary))) {
            BruteForce bruteForce = new BruteForce(reader, urlStr, login);

            long startTime = System.currentTimeMillis();
            String password = bruteForce.search();
            long time = System.currentTimeMillis() - startTime;

            System.out.println((password == null) ? PASSWORD_NOT_FOUND : String.format(SUCCESS_RESULT_MESSAGE, password));
            System.out.printf(COMPLETED_TIME_MESSAGE, time);
        }
    }
}
