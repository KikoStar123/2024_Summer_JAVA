package testhandler.library;

import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.*;
import java.net.Socket;

public class TestGetBookDetailsById {

    @Test
    public void testGetBookDetailsById() {
        String hostname = "localhost";
        int port = 8080;

        try (Socket socket = new Socket(hostname, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            JSONObject request = new JSONObject();
            request.put("requestType", "get_book_by_id");
            JSONObject parameters = new JSONObject();
            parameters.put("bookId", "978-7-121-33462-7");
            request.put("parameters", parameters);

            out.println(request.toString());

            String responseString = in.readLine();
            System.out.println("Server response: " + responseString);

            JSONObject response = new JSONObject(responseString);

            assertEquals("success", response.getString("status"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
