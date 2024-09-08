package testhandler.concurrent;

import org.json.JSONObject;
import org.junit.Test;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrentModifyStudentInfoTest {

    private static final int THREAD_COUNT = 10;
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 8080;

    @Test
    public void testConcurrentModifyStudentInfo() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try (Socket socket = new Socket(HOSTNAME, PORT);
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    JSONObject request = new JSONObject();
                    request.put("requestType", "modifyStudentInfo");
                    JSONObject parameters = new JSONObject();
                    JSONObject studentData = new JSONObject();
                    studentData.put("id", "09022301");
                    studentData.put("name", "Test Name ");
                    studentData.put("gender", "male");
                    studentData.put("origin", "Test Origin" + Thread.currentThread().getId());
                    studentData.put("birthday", "2000-01-01");
                    studentData.put("academy", "Test Academy");
                    parameters.put("data", studentData);
                    request.put("parameters", parameters);

                    out.println(request.toString());

                    String responseString = in.readLine();
                    System.out.println("Server response: " + responseString);

                    JSONObject response = new JSONObject(responseString);
                    assert "success".equals(response.getString("status"));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }
}
