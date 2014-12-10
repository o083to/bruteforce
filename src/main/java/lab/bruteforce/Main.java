package lab.bruteforce;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by anna on 28.11.14.
 */
public class Main {

    public static void main(String args[]) throws IOException, ExecutionException, InterruptedException {
        String urlStr = "http://localhost:9000/login";
        try (BufferedReader reader = new BufferedReader(new FileReader("hotmail.txt"))) {
            BruteForce bruteForce = new BruteForce(reader, urlStr, "test2");
            String password = bruteForce.search();
            System.out.println((password == null) ? "not found" : password);
        }
    }
}
