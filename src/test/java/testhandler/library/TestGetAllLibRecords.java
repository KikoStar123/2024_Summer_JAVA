package testhandler.library;

import static org.junit.Assert.assertEquals;
import java.io.*;
import java.net.Socket;
import org.json.JSONObject;
import org.junit.Test;

public class TestGetAllLibRecords {

    @Test
    public void testGetAllLibRecords() {
        String hostname = "localhost";
        int port = 8080;

        try (Socket socket = new Socket(hostname, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            JSONObject request = new JSONObject();
            request.put("requestType", "getAllLibRecords");
            JSONObject parameters = new JSONObject();
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
