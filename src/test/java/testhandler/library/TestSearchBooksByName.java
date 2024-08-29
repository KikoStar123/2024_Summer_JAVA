package testhandler.library;

import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.*;
import java.net.Socket;

public class TestSearchBooksByName {

    @Test
    public void testSearchBooksByName() {
        String hostname = "localhost";
        int port = 8080;

        try (Socket socket = new Socket(hostname, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            JSONObject request = new JSONObject();
            request.put("requestType", "search_books_by_name");
            JSONObject parameters = new JSONObject();
            parameters.put("bookName", "明朝那些事儿");
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
