package testhandler.library;

import static org.junit.Assert.assertEquals;
import java.io.*;
import java.net.Socket;
import org.json.JSONObject;
import org.junit.Test;

public class TestBookReturn {

    @Test
    public void testBookReturn() {
        String hostname = "localhost";
        int port = 8080;

        try (Socket socket = new Socket(hostname, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            JSONObject request = new JSONObject();
            request.put("requestType", "bookReturn");
            JSONObject parameters = new JSONObject();
            parameters.put("username", "200000001");
            parameters.put("bookId", "978-7-121-33462-7");
            request.put("parameters", parameters);

            out.println(request.toString());

            String responseString = in.readLine();
            System.out.println("Server response: " + responseString);

            JSONObject response = new JSONObject(responseString);

            assertEquals("success", response.getString("status"));
            assertEquals("Book returned successfully.", response.getString("message"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
